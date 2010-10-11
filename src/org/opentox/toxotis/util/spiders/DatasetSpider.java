package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.GetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.core.component.DataEntry;
import org.opentox.toxotis.core.component.Dataset;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 * A parser for RDF representations of OpenTox datasets.
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class DatasetSpider extends Tarantula<Dataset> {

    VRI datasetUri;

    public DatasetSpider(VRI uri) throws ToxOtisException {
        super();
        long timeFlag = System.currentTimeMillis();
        this.datasetUri = uri;
        GetClient client = new GetClient();
        try {
            client.setMediaType(Media.APPLICATION_RDF_XML);
            client.setUri(uri);
            int status = client.getResponseCode();
            assessHttpStatus(status, uri);
            model = client.getResponseOntModel();
            resource = model.getResource(uri.getStringNoQuery());
            readRemoteTime = System.currentTimeMillis() - timeFlag;
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
            throw new RuntimeException(ex);
        }
        this.resource = model.getResource(uri);
    }

    @Override
    public Dataset parse() throws ToxOtisException {
        long timeFlag = System.currentTimeMillis();
        Dataset dataset = new Dataset();
        dataset.setUri(datasetUri);
        /**
         * START **
         * The following lines is a workaround will all RDF representations from 
         * remote servers are well formed. This will force the data model of a dataset
         * to have explicit declarations of ot:Feature, ot:NominalFeautre, ot:NumericFeature 
         * and ot:StringFeature
         */
        OTClasses.Feature().inModel(model);
        OTClasses.NominalFeature().inModel(model);
        OTClasses.NumericFeature().inModel(model);
        OTClasses.StringFeature().inModel(model);
        /** END **
         */
        dataset.setMeta(new MetaInfoSpider(resource, model).parse());
        StmtIterator entryIt = model.listStatements(
                new SimpleSelector(resource, OTObjectProperties.dataEntry().asObjectProperty(model),
                (RDFNode) null));
        ArrayList<DataEntry> dataEntries = new ArrayList<DataEntry>();
        while (entryIt.hasNext()) {
            Resource entryResource = entryIt.nextStatement().getObject().as(Resource.class);
            DataEntrySpider dataEntrySpider = new DataEntrySpider(entryResource, model);
            dataEntries.add(dataEntrySpider.parse());
        }
        dataset.setDataEntries(dataEntries);
        parseTime = System.currentTimeMillis() - timeFlag;
        return dataset;
    }
}
