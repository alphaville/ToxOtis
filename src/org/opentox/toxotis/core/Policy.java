package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Policy extends OTOnlineResource<Policy>{

    @Override
    public Individual asIndividual(OntModel model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Policy loadFromRemote() throws ToxOtisException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}