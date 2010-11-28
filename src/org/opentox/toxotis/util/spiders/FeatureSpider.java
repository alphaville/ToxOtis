package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.http.GetHttpClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.core.component.Feature;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class FeatureSpider extends Tarantula<Feature> {

    VRI uri;

    public FeatureSpider(VRI uri) throws ToxOtisException {
        super();
        this.uri = uri;
        GetHttpClient client = new GetHttpClient();
        try {
            client.setMediaType(Media.APPLICATION_RDF_XML.getMime());
            client.setUri(uri);
            int status = client.getResponseCode();
            assessHttpStatus(status, uri);
            model = client.getResponseOntModel();
            resource = model.getResource(uri.toString());
        } catch (final IOException ex) {
            throw new ToxOtisException("Communication Error with the remote service at :" + uri, ex);
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException ex) {
                    throw new ToxOtisException(ErrorCause.StreamCouldNotClose,
                            "Error while trying to close the stream "
                            + "with the remote location at :'" + ((uri != null) ? uri.clearToken().toString() : null) + "'", ex);
                }
            }
        }
    }

    public FeatureSpider(Resource resource, OntModel model) {
        super(resource, model);
        try {
            uri = new VRI(resource.getURI());
        } catch (URISyntaxException ex) {
            Logger.getLogger(FeatureSpider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public FeatureSpider(OntModel model, String uri) {
        super();
        this.model = model;
        try {
            this.uri = new VRI(uri);
        } catch (URISyntaxException ex) {
            Logger.getLogger(FeatureSpider.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.resource = model.getResource(uri);
    }

    @Override
    public Feature parse() {
        Feature feature = new Feature();
        feature.setMeta(new MetaInfoSpider(resource, model).parse()); // Parse meta-info
        feature.setUri(uri);
        feature.setOntologies(getOntologicalTypes(resource));
        Statement unitsStatement = resource.getProperty(OTDatatypeProperties.units().asDatatypeProperty(model));
        if (unitsStatement != null) {
            feature.setUnits(unitsStatement.getString());
        }

        if (feature.getOntologies() != null && feature.getOntologies().contains(OTClasses.NominalFeature())) {
            // Gather 'accept' values from the RDF and add them to the feature
            Set<LiteralValue> admissibleValues = new HashSet<LiteralValue>();
            StmtIterator acceptIt = resource.listProperties(OTDatatypeProperties.acceptValue().asDatatypeProperty(model));
            while (acceptIt.hasNext()) {
                Literal acceptValueLiteral = acceptIt.nextStatement().getObject().as(Literal.class);
                LiteralValue acceptValue = new LiteralValue(acceptValueLiteral.getValue().toString(),
                        (XSDDatatype) acceptValueLiteral.getDatatype());
                admissibleValues.add(acceptValue);
            }
            feature.setAdmissibleValues(admissibleValues);
        }

        return feature;
    }
}
