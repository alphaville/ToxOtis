package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.ArrayList;
import java.util.List;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;

/**
 * Provides different representations for chemical compounds with a unique
 * and defined chemical structure. This class is abstract for it is extended by
 * {@link Conformer } that conceptually corresponds to an exact chemical structure
 * while Compound is a proxy for a conformer.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Compound extends OTOnlineResource<Compound> {

    private List<Conformer> conformers;

    public Compound(VRI uri) throws ToxOtisException {
        super(uri);
        if (uri != null) {
            if (!Compound.class.equals(uri.getOpenToxType())) {
                throw new ToxOtisException("The provided URI : '" + uri.getStringNoQuery()
                        + "' is not a valid Compound uri according to the OpenTox specifications.");
            }
        }
    }

    public Compound() {
    }

    public List<Conformer> getConformers() {
        if (conformers == null) {
            conformers = new ArrayList<Conformer>();
        }
        return conformers;
    }

    public void setConformers(List<Conformer> conformers) {
        this.conformers = conformers;
    }

    public Conformer getPrimaryConformer() {
        return getConformers().get(0);
    }

    @Override
    public Individual asIndividual(OntModel model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Compound loadFromRemote(VRI uri) throws ToxOtisException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
