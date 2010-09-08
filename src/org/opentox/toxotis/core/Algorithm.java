package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.Collection;
import java.util.Set;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.OntologicalClass;

/**
 * Provides access to different types of algorithms. An algorithm object contains
 * very general metadata about an algorithm and in fact describes its input and
 * output requirements and gives information about its parametrization. The different
 * types of algorithms used in <a href="http://opentox.org">OpenTox</a> and the
 * connection to each other is formally established throught the 
 * <a href="http://opentox.org/dev/apis/api-1.1/Algorithms">OpenTox Algorithm Ontology</a>.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Algorithm extends OTComponent<Algorithm> {

    private Set<Parameter> parameters;
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
