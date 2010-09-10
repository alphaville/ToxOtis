package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.GetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.DataEntry;
import org.opentox.toxotis.core.Dataset;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class DatasetSpider extends Tarantula<Dataset>{

    VRI datasetUri;

    public DatasetSpider(VRI uri) throws ToxOtisException {
        super();
        this.datasetUri = uri;
        GetClient client = new GetClient();
        client.setMediaType("application/rdf+xml");
        client.setUri(uri);
        model = client.getResponseOntModel();
        resource = model.getResource(uri.toString());
    }

    public DatasetSpider(Resource resource, OntModel model) {
        super(resource, model);
        try {
            datasetUri = new VRI(resource.getURI());
        } catch (URISyntaxException ex) {
            Logger.getLogger(FeatureSpider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public DatasetSpider(OntModel model, String uri) {
        super();
        this.model = model;
        try {
            this.datasetUri = new VRI(uri);
        } catch (URISyntaxException ex) {
            Logger.getLogger(FeatureSpider.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.resource = model.getResource(uri);
    }

    @Override
    public Dataset parse() throws ToxOtisException {
        Dataset dataset = new Dataset();
        dataset.setUri(datasetUri);
        dataset.setMeta(new MetaInfoSpider(resource, model).parse());

        StmtIterator entryIt = model.listStatements(
                new SimpleSelector(resource, OTObjectProperties.dataEntry().asObjectProperty(model),
                (RDFNode)null));

        ArrayList<DataEntry> dataEntries = new ArrayList<DataEntry>();
        while(entryIt.hasNext()){
            Resource entryResource = entryIt.nextStatement().getObject().as(Resource.class);
            DataEntrySpider dataEntrySpider = new DataEntrySpider(entryResource, model);
            dataEntries.add(dataEntrySpider.parse());
        }
        dataset.setDataEntries(dataEntries);
        return dataset;
    }

}
