package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 * Conformers are compounds with 3D characteristics. Conformers don't play any
 * significant role in ToxOtis till now since all chemical entities are cast
 * as Compounds.
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
    protected Conformer loadFromRemote(VRI uri, AuthenticationToken token) throws ToxOtisException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
