package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import java.io.IOException;
import java.net.URISyntaxException;
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.GetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.Algorithm;
import org.opentox.toxotis.core.AuthenticationToken;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class AlgorithmSpider extends Tarantula<Algorithm> {

    private VRI uri;
    private AuthenticationToken token = null;

    public AlgorithmSpider(VRI uri, AuthenticationToken token) throws ToxOtisException {
        super();
        //TODO: Implement this one!
    }

    /**
     * @param uri
     * @throws ToxOtisException
     */
    public AlgorithmSpider(VRI uri) throws ToxOtisException {
        super();
        this.uri = uri;        
        GetClient client = new GetClient();
        client.setMediaType("application/rdf+xml");
        client.setUri(uri);
        try {
            final int status = client.getResponseCode();
            if (status == 403) {
                throw new ToxOtisException(ErrorCause.AuthenticationFailed, "Access denied to : '" + uri+"'");
            }
            if (status != 200) {
                throw new ToxOtisException(ErrorCause.CommunicationError, "Communication Error with : '" + uri+"'");
            }
        } catch (IOException ex) {
            throw new ToxOtisException("Communication Error with the remote service at :" + uri, ex);
        }
        model = client.getResponseOntModel();
        resource = model.getResource(uri.getStringNoQuery());
    }

    @Override
    public Algorithm parse() {
        Algorithm algorithm = null;
        try {
            algorithm = new Algorithm(uri.getStringNoQuery());
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
        algorithm.setOntologies(getOTATypes(resource));
        MetaInfoSpider metaSpider = new MetaInfoSpider(model, uri.getStringNoQuery());
        algorithm.setMeta(metaSpider.parse());
        StmtIterator itParam = model.listStatements(
                new SimpleSelector(resource,
                OTObjectProperties.parameters().asObjectProperty(model),
                (RDFNode) null));
        while (itParam.hasNext()) {
            ParameterSpider paramSpider = new ParameterSpider(model, itParam.nextStatement().getObject().as(Resource.class));
            algorithm.getParameters().add(paramSpider.parse());
        }
        return algorithm;

    }
              
}
