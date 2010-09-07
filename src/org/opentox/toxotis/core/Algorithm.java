package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.Collection;
import java.util.Set;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.OntologicalClass;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Algorithm extends OTComponent<Algorithm> {

    private Set<Parameter> parameters;
    private MetaInfo meta;
    private Collection<OntologicalClass> ontologies;

    public Algorithm(VRI uri) {
        super(uri);
    }

    public Collection<OntologicalClass> getOntologies() {
        return ontologies;
    }

    public void setOntologies(Collection<OntologicalClass> ontologies) {
        this.ontologies = ontologies;
    }

    public MetaInfo getMeta() {
        return meta;
    }

    public void setMeta(MetaInfo meta) {
        this.meta = meta;
    }

    public Set<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(Set<Parameter> parameters) {
        this.parameters = parameters;
    }

    

    @Override
    public Algorithm createFrom(OntModel model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Individual asIndividual(OntModel model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
