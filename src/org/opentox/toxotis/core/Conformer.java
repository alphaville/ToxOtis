package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.ArrayList;
import java.util.List;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.MetaInfo;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Conformer extends OTComponent<Conformer>{

    public Dataset getProperty(Feature feature) throws ToxOtisException{
        return null;
    }
    
    public Dataset getProperties(Feature... features){
        return null;
    }

    public Dataset getAllProperties(){
        return null;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public Conformer createFrom(OntModel model) {
        throw new UnsupportedOperationException("");
    }

}