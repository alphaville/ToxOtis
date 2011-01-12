package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import java.io.IOException;
import java.net.URISyntaxException;
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.IGetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.core.component.Compound;

/**
 * Downloader and parser for a compound resource available in RDF.
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class CompoundSpider extends Tarantula<Compound> {

    VRI uri;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CompoundSpider.class);

    public CompoundSpider(VRI uri) throws ToxOtisException {
        super();
        this.uri = uri;
        IGetClient client = ClientFactory.createGetClient(uri);
        try {
            client.setMediaType(Media.APPLICATION_RDF_XML.getMime());
            client.setUri(uri);
            int status = client.getResponseCode();
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
            logger.debug(null, ex);
        }
    }

    public CompoundSpider(OntModel model, String uri) {
        super();
        this.model = model;
        try {
            this.uri = new VRI(uri);
        } catch (URISyntaxException ex) {
            logger.debug(null, ex);
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
