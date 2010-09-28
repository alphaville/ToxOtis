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
 * WARNING: This is utterly under developement!!!!!
 * WARNING: This is utterly under developement!!!!!
 * WARNING: This is utterly under developement!!!!!
 * WARNING: This is utterly under developement!!!!!
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Conformer extends Compound {

    private String conformerId;

    public Conformer() {
        super();
    }

    public Conformer(VRI uri) throws ToxOtisException {
        super();
        setUri(uri);
    }

    /**
     *
     * @return
     */
    public VRI getCompoundUri() {
        throw new UnsupportedOperationException();
    }

    public String getConformerId(){
        throw new UnsupportedOperationException();
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
