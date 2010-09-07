package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import java.net.URISyntaxException;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.GetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.Algorithm;
import org.opentox.toxotis.core.AuthenticationToken;
import org.opentox.toxotis.ontology.OntologicalClass;
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
        this.token = token;
        this.uri = uri;
        GetClient client = new GetClient();
        client.setMediaType("application/rdf+xml");
        client.setUri(uri);
        model = client.getResponseOntModel();
        resource = model.getResource(uri.toString());
    }

    public AlgorithmSpider(VRI uri) throws ToxOtisException {
        super();
        this.uri = uri;
        GetClient client = new GetClient();
        client.setMediaType("application/rdf+xml");
        client.setUri(uri);
        model = client.getResponseOntModel();
        resource = model.getResource(uri.toString());
    }

    @Override
    public Algorithm parse() {
        Algorithm algorithm = new Algorithm(uri);
        algorithm.setOntologies(getOTATypes(resource));

        MetaInfoSpider metaSpider = new MetaInfoSpider(model, uri.toString());
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

    public static void main(String... args) throws ToxOtisException, URISyntaxException {
        AlgorithmSpider spider = null;
        spider = new AlgorithmSpider(new VRI("http://opentox.ntua.gr:3000/algorithm/filter"));
        Algorithm a = spider.parse();
        System.out.println(a.getMeta().getTitle());        
        for (OntologicalClass oc : a.getOntologies()) {
            System.out.println(oc.getName());
        }

    }

    
}
