package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.GetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.util.spiders.DatasetSpider;
import org.opentox.toxotis.util.spiders.TypedValue;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Conformer extends OTOnlineResource<Conformer> {

    public Conformer() {
        super();
    }

    public Conformer(VRI uri) {
        super(uri);
    }

    /**
     *
     * @return
     */
    public VRI getCompoundUri() {
        throw new UnsupportedOperationException();
    }

    public TypedValue getProperty(VRI uri) throws ToxOtisException {
        Feature tempFeat = new Feature(uri);
        return getProperty(tempFeat);
    }

    /**
     *
     * @param feature
     * @return
     * @throws ToxOtisException
     */
    public TypedValue getProperty(Feature feature) throws ToxOtisException {
        /**
         *TODO: Should this request include the uri parameters for the feature?
         */
        VRI dsUri = new VRI(getUri()).addUrlParameter("feature_uris[]", feature.getUri().toString());
        GetClient client = new GetClient();
        client.setUri(dsUri);
        client.setMediaType("application/rdf+xml");
        OntModel model = client.getResponseOntModel();
        RDF.type.inModel(model);
        StmtIterator dsIt = model.listStatements(null, RDF.type, (RDFNode) OTClasses.Dataset().inModel(model));
        Resource baseResource = null;
        if (dsIt.hasNext()) {
            baseResource = dsIt.nextStatement().getSubject();
            DatasetSpider dsSpider = new DatasetSpider(baseResource, model);
            Dataset ds = dsSpider.parse();
            List<DataEntry> data = ds.getDataEntries();
            if (data != null && data.size() >= 1) {
                DataEntry firstEntry = ds.getDataEntries().get(0);
                if (firstEntry != null && firstEntry.getFeatureValues().size() >= 1) {
                    FeatureValue fVal = firstEntry.getFeatureValue(0);
                    if (fVal != null) {
                        return fVal.getValue();
                    }
                }
            }
        }
        return null;
    }

    public Dataset getProperties(VRI... featureUris) throws ToxOtisException {
        VRI dsUri = new VRI(getUri());
        for (VRI featureUri : featureUris) {
            dsUri.addUrlParameter("feature_uris[]", featureUri.toString());
        }
        GetClient client = new GetClient();
        client.setUri(dsUri);
        client.setMediaType("application/rdf+xml");
        OntModel model = client.getResponseOntModel();
        StmtIterator dsIt = model.listStatements(null, RDF.type, (RDFNode) OTClasses.Dataset().inModel(model));
        Resource baseResource = null;
        if (dsIt.hasNext()) {
            baseResource = dsIt.nextStatement().getSubject();
            DatasetSpider dsSpider = new DatasetSpider(baseResource, model);
            return dsSpider.parse();
        }
        return null;
    }

    public Set<VRI> getAvailableFeatures() throws ToxOtisException {
        VRI featuresUri = new VRI(uri).augment("feature");
        GetClient client = new GetClient();
        client.setUri(featuresUri);
        Set<VRI> availableUris = new HashSet<VRI>();
        for (String fUri : client.getResponseUriList()) {
            try {
                availableUris.add(new VRI(fUri));
            } catch (URISyntaxException ex) {
                Logger.getLogger(Conformer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return availableUris;

    }

    @Override
    public Individual asIndividual(OntModel model) {
        String conformerUri = getUri() != null ? getUri().getStringNoQuery() : null;
        Individual indiv = model.createIndividual(conformerUri, OTClasses.Conformer().inModel(model));
        return indiv;
    }

    @Override
    protected Conformer loadFromRemote(VRI uri) throws ToxOtisException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
