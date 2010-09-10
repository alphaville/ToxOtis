package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.GetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.Conformer;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class ConformerSpider extends Tarantula<Conformer>{

    VRI uri;

    public ConformerSpider(VRI uri) throws ToxOtisException {
        super();
        this.uri = uri;
        GetClient client = new GetClient();
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
            Logger.getLogger(FeatureSpider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ConformerSpider(OntModel model, String uri) {
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
    public Conformer parse() throws ToxOtisException {
        Conformer conformer = new Conformer();
        StmtIterator it = model.listStatements(
                new SimpleSelector(null,
                OTObjectProperties.dataEntry().asObjectProperty(model), (RDFNode) null));
        StmtIterator it2 = null, it3 = null, itCompound = null;

        itCompound = model.listStatements(
                new SimpleSelector(null,
                RDF.type,
                OTClasses.Compound().inModel(model)));

        if (itCompound.hasNext()) {
            Statement stmt = itCompound.nextStatement();
            MetaInfoSpider metaSpider = new MetaInfoSpider(stmt.getSubject(), model);
            conformer.setMeta(metaSpider.parse());
        }

        return conformer;
    }

}
