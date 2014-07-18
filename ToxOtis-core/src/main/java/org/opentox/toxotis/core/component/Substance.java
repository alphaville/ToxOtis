/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 *
 * @author lab
 */
public class Substance extends Compound{

    public Substance() {
        super();
        setMeta(null);
    }

    public Substance(VRI uri) throws ToxOtisException {
        super();
        setUri(uri);
        setMeta(null);
    }
    
     public VRI getSubstanceUri() {
        throw new UnsupportedOperationException();
    }

    public String getSubstanceId(){
        throw new UnsupportedOperationException();
    }        

    @Override
    public Individual asIndividual(OntModel model) {
        String substanceUri = getUri() != null ? getUri().getStringNoQuery() : null;
        Individual indiv = model.createIndividual(substanceUri, OTClasses.substance().inModel(model));
        return indiv;
    }

    @Override
    protected Substance loadFromRemote(VRI uri, AuthenticationToken token) throws ServiceInvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
