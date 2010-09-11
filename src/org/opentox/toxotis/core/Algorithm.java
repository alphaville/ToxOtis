package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.util.spiders.AlgorithmSpider;

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
public class Algorithm extends OTOnlineResource<Algorithm> {

    /** Set of parameters of the algorithm. Specify the way the algorithm is parametrized */
    private Set<Parameter> parameters = new HashSet<Parameter>();
    /** Set of ontological classes that characterize the algorithm*/
    private Collection<OntologicalClass> ontologies;

    /**
     * Create a new instance of Algorithm providing its identifier as a {@link VRI }.
     * @param uri
     *      The URI of the algorithm as a {@link VRI }.
     */
    public Algorithm(VRI uri) {
        super(uri);
    }

    public Algorithm(String uri) throws URISyntaxException {
        super(new VRI(uri));
    }

    /**
     * Get the ontological classes of the algorithm
     * @return
     *      The collection of ontological classes for this algorithm.
     */
    public Collection<OntologicalClass> getOntologies() {
        return ontologies;
    }

    /**
     * Specify the ontologies for this algorithm.
     * @param ontologies
     *      A collection of ontological classes that characterize this algorithm.
     */
    public void setOntologies(Collection<OntologicalClass> ontologies) {
        this.ontologies = ontologies;
    }

    /**
     * Retrieve the set of parameters for this algorithm.
     * @return
     *      Set of parameters
     */
    public Set<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(Set<Parameter> parameters) {
        this.parameters = parameters;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Algorithm loadFromRemote() throws ToxOtisException {
        AlgorithmSpider spider = new AlgorithmSpider(getUri());
        Algorithm algorithm = spider.parse();
        setMeta(algorithm.getMeta());
        setOntologies(algorithm.getOntologies());
        setParameters(algorithm.getParameters());
        return this;
    }
}
