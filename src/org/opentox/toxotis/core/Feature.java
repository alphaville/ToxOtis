package org.opentox.toxotis.core;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.PostClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.spiders.FeatureSpider;
import org.opentox.toxotis.util.spiders.TypedValue;

/**
 * A Feature is an object,representing any kind of property, assigned to a
 * Compound. The feature types are determined via their links to ontologies
 * (Feature ontologies, Decsriptor ontologies, Endpoints ontologies). OpenTox
 * has established an ontology for biological/toxicological and chemical features
 * that is <a href="http://opentox.org/dev/apis/api-1.1/feature_ontology">
 * available online</a>.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Feature extends OTPublishable<Feature> {

    private Set<OntologicalClass> ontologies = new HashSet<OntologicalClass>();
    private String units;
    private Set<TypedValue> admissibleValue = new HashSet<TypedValue>();

    public Feature() {
        super();
    }

    public Feature(VRI uri) {
        super(uri);
    }

    public Set<TypedValue> getAdmissibleValue() {
        return admissibleValue;
    }

    public void setAdmissibleValue(Set<TypedValue> admissibleValue) {
        this.admissibleValue = admissibleValue;
    }

    public Set<OntologicalClass> getOntologies() {
        return ontologies;
    }

    public void setOntologies(Set<OntologicalClass> ontologies) {
        this.ontologies = ontologies;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        String featureUri = getUri() != null ? getUri().getStringNoQuery() : null;
        Resource mainType = null;
        /* Check if the feature is either Numeric or String */
        if (ontologies != null && !ontologies.isEmpty()) {
            if (ontologies.contains(OTClasses.StringFeature())) {
                mainType = (OTClasses.StringFeature().inModel(model));
            } else if (ontologies.contains(OTClasses.NumericFeature())) {// << Assuming cannot be StringFeature and NumericFeature at the same time
                mainType = (OTClasses.NumericFeature().inModel(model));
            }
        }
        /* If the feature is not Numeric nor String, might be Nominal... */
        if (mainType == null && (ontologies != null && !ontologies.isEmpty())) {
            if (ontologies.contains(OTClasses.NominalFeature())) {
                mainType = (OTClasses.NominalFeature().inModel(model));
            }
        }
        Individual indiv = model.createIndividual(featureUri, mainType != null ? mainType : OTClasses.Feature().inModel(model));
        /* Check again if the feature is additionaly nominal */
        if (ontologies != null && !ontologies.isEmpty()) {
            if (ontologies.contains(OTClasses.NominalFeature())) {
                indiv.addRDFType(OTClasses.NominalFeature().inModel(model));
            }
        }
        /* Add admissible values in the RDF graph */
        if (admissibleValue != null && !admissibleValue.isEmpty()) {
            DatatypeProperty accepts = OTDatatypeProperties.acceptValue().asDatatypeProperty(model);
            for (TypedValue tv : admissibleValue) {
                if (tv != null) {
                    indiv.addProperty(accepts, model.createTypedLiteral(tv.getValue(), tv.getType()));
                }
            }
        }
        /* Add units */
        if (units != null) {
            DatatypeProperty unitsProp = OTDatatypeProperties.units().asDatatypeProperty(model);
            indiv.addProperty(unitsProp, model.createTypedLiteral(units, XSDDatatype.XSDstring));
        }
        /* Add meta data */
        if (meta != null) {
            meta.attachTo(indiv, model);
        }
        return indiv;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (uri != null) {
            builder.append("URI...\n");
            builder.append(uri);
            builder.append("\n\n");
        }
        if (meta != null) {
            builder.append("Meta information....\n");
            builder.append(meta);
            builder.append("\n");
        }
        if (!ontologies.isEmpty()) {
            builder.append("Ontological Classes....\n");
            Iterator<OntologicalClass> i = getOntologies().iterator();
            while (i.hasNext()) {
                builder.append(i.next().getUri());
                builder.append("\n");
            }
        }
        if (units != null && !units.isEmpty()) {
            builder.append("Units : " + units);
        }

        return new String(builder);
    }

    protected Feature loadFromRemote(VRI uri) throws ToxOtisException {
        FeatureSpider fSpider = new FeatureSpider(uri);
        Feature f = fSpider.parse();
        setMeta(f.getMeta());
        setOntologies(f.getOntologies());
        setUnits(f.getUnits());
        return this;
    }

    @Override
    public Task publishOnline(VRI vri, AuthenticationToken token) throws ToxOtisException {
        /** Handle provided token */
        if (token != null) {
            // Replace existing token with the new one
            vri.removeUrlParameter("tokenid").addUrlParameter("tokenid", token.stringValue());
        }
        PostClient client = new PostClient(vri);
        client.setContentType("application/rdf+xml");
        client.setPostable(asOntModel());
        client.setMediaType("text/uri-list");
        client.post();
        int status;
        try {
            status = client.getResponseCode();
            if (status == 200) {
                Task readyTask = new Task();
                readyTask.setPercentageCompleted(100);
                readyTask.setHasStatus(Task.Status.COMPLETED);
                try {
                    readyTask.setResultUri(new VRI(client.getResponseText()));
                    return readyTask;
                } catch (URISyntaxException ex) {
                    throw new ToxOtisException("Unexpected behaviour from the remote server at :'" + vri.getStringNoQuery()
                            + "'. Received status code 200 and messaage:");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Feature.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Task publishOnline(AuthenticationToken token) throws ToxOtisException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
