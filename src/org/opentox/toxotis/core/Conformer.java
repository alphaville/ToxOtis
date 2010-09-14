package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.collection.OTClasses;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Conformer extends OTOnlineResource<Conformer> {

    public Conformer() {
        super();
    }

    public Conformer(VRI uri) {
        super(uri);
    }


    public Dataset getProperty(Feature feature) throws ToxOtisException {
        return null;
    }

    public Dataset getProperties(Feature... features) {
        return null;
    }

    public Dataset getAllProperties() {
        return null;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        String conformerUri = getUri() != null ? getUri().getStringNoQuery() : null;
        Individual indiv = model.createIndividual(conformerUri, OTClasses.Conformer().inModel(model));
        return indiv;
    }

    @Override
    protected  Conformer loadFromRemote(VRI uri) throws ToxOtisException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
