package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.opentox.toxotis.client.VRI;

/**
 * Provides different representations for chemical compounds with a unique
 * and defined chemical structure. This class is abstract for it is extended by
 * {@link Conformer } that conceptually corresponds to an exact chemical structure
 * while Compound is a proxy for a conformer.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Compound extends OTComponent<Compound> {

    private List<Conformer> conformers;

    public Compound(VRI uri) {
        super(uri);
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
    public Compound createFrom(OntModel model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Individual asIndividual(OntModel model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
