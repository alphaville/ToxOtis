package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.ArrayList;
import org.opentox.toxotis.client.GetClient;

/**
 * Provides different representations for chemical compounds with a unique
 * and defined chemical structure. This class is abstract for it is extended by
 * {@link Conformer } that conceptually corresponds to an exact chemical structure
 * while Compound is a proxy for a conformer.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract  class Compound extends OTComponent<Compound>{

    private ArrayList<Conformer> conformers;

    public ArrayList<Conformer> listConformers(){
        if (conformers==null){
            // GetClient
        }
        return conformers;
    }

    public Conformer getPrimaryConformer(){

        throw new UnsupportedOperationException();
    }

    @Override
    public Compound createFrom(OntModel model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Individual asIndividual(OntModel model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}