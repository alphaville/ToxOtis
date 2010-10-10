package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.collection.OTClasses;

/**
 * <p>
 * WARNING: This is utterly under developement!!!!!<br/>
 * WARNING: This is utterly under developement!!!!!<br/>
 * WARNING: This is utterly under developement!!!!!<br/>
 * WARNING: This is utterly under developement!!!!!<br/>
 * <br/>
 * </p>
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
