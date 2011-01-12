package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import java.net.URISyntaxException;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.IGetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Conformer;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class ConformerSpider extends Tarantula<Conformer> {

    VRI uri;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ConformerSpider.class);

    public ConformerSpider(VRI uri) throws ToxOtisException {
        super();
        this.uri = uri;
        IGetClient client = ClientFactory.createGetClient(uri);
        client.setMediaType("application/rdf+xml");
        client.setUri(uri);
        model = client.getResponseOntModel();
        resource = model.getResource(uri.toString());
    }

    public ConformerSpider(Resource resource, OntModel model) {
        super(resource, model);
        try {
            uri = new VRI(resource.getURI());
        } catch (URISyntaxException ex) {
            logger.debug(null, ex);
        }
    }

    public ConformerSpider(OntModel model, String uri) {
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
    public Conformer parse() throws ToxOtisException {
        Conformer conformer = new Conformer(uri);
        StmtIterator it = model.listStatements(
                new SimpleSelector(null,
                OTObjectProperties.dataEntry().asObjectProperty(model), (RDFNode) null));
        StmtIterator it2 = null, it3 = null, itCompound = null;

        itCompound = model.listStatements(
                new SimpleSelector(null,
                RDF.type,
                OTClasses.Conformer().inModel(model)));

        if (itCompound.hasNext()) {
            Statement stmt = itCompound.nextStatement();
            MetaInfoSpider metaSpider = new MetaInfoSpider(stmt.getSubject(), model);
            conformer.setMeta(metaSpider.parse());
        }

        return conformer;
    }
}