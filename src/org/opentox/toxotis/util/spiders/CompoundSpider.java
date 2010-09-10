package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.GetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.Compound;
import org.opentox.toxotis.core.Conformer;
import org.opentox.toxotis.core.DataEntry;
import org.opentox.toxotis.core.Dataset;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class CompoundSpider extends Tarantula<Compound>{

    VRI uri;

    public CompoundSpider(VRI uri) throws ToxOtisException {
        super();
        this.uri = uri;
        GetClient client = new GetClient();
        client.setMediaType("application/rdf+xml");
        client.setUri(uri);
        model = client.getResponseOntModel();
        resource = model.getResource(uri.toString());
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
        Compound compound = new Compound();

        compound.setMeta(new MetaInfoSpider(resource, model).parse());

        String conformersUri = uri.toString() + "/conformer";
        GetClient getConformers = new GetClient();
        try {
            getConformers.setUri(conformersUri);
        } catch (URISyntaxException ex) {
            Logger.getLogger(CompoundSpider.class.getName()).log(Level.SEVERE, null, ex);
        }

        model = getConformers.getResponseOntModel();
        resource = model.getResource(conformersUri);

        ArrayList<Conformer> conformers = new ArrayList<Conformer>();
        Dataset conformersDS = new DatasetSpider(resource, model).parse();
        for(DataEntry dataEntry : conformersDS.getDataEntries()){
            conformers.add(dataEntry.getConformer());
        }
        compound.setConformers(conformers);

        return compound;
    }

}
