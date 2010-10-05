package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
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
import org.opentox.toxotis.core.Compound;
import org.opentox.toxotis.core.Conformer;
import org.opentox.toxotis.core.DataEntry;
import org.opentox.toxotis.core.Dataset;
import org.opentox.toxotis.core.ErrorReport;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class CompoundSpider extends Tarantula<Compound> {

    VRI uri;

    public CompoundSpider(VRI uri) throws ToxOtisException {
        super();
        this.uri = uri;
        GetClient client = new GetClient();
        try {
            client.setMediaType(Media.APPLICATION_RDF_XML.getMime());
            client.setUri(uri);
            final int status = client.getResponseCode();
            assessHttpStatus(status, uri);
            model = client.getResponseOntModel();
            resource = model.getResource(uri.toString());
        } catch (IOException ex) {
            throw new ToxOtisException("Communication Error with the remote service at :" + uri, ex);
        } finally { // Have to close the client (disconnect)
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

    public CompoundSpider(Resource resource, OntModel model) {
        super(resource, model);
        try {
            uri = new VRI(resource.getURI());
        } catch (URISyntaxException ex) {
            Logger.getLogger(FeatureSpider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CompoundSpider(OntModel model, String uri) {
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
    public Compound parse() throws ToxOtisException {
        Compound compound = new Compound(uri);
        compound.setMeta(new MetaInfoSpider(resource, model).parse());
        return compound;
    }
}
