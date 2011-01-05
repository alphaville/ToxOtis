package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import org.opentox.toxotis.client.VRI;

/**
 * Generic interface for all OpenTox components in ToxOtis that can be represented
 * as individuals in an RDF graph. According to the OpenTox API specifications (since 
 * API 1.1.) should be available in RDF (application/rdf+xml) while other RDF-related
 * representations can be also available optionally (like application/x-turtle).
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface IOTComponent {

    /**
     * The OpenTox component as an individual.
     * @param model
     *      The ontological model to which the individual belongs.
     * @return
     *      The OpenTox component as an individual of a data model.
     */
    public abstract Individual asIndividual(OntModel model);

    /**
     * Creates a new Ontological Model (uses an instance of {@link SimpleOntModelImpl })
     * and assigns to it the Individual from the method
     * {@link OTComponent#asIndividual(com.hp.hpl.jena.ontology.OntModel) asIndividual(OntModel)}.
     * The ontological model will contain no other information apart from the representation
     * of the inderlying OpenTox component.
     * @return
     *      An ontological model for the current OpenTox component.
     */
    public OntModel asOntModel() ;

    /**
     * Get the URI of the resource (as an instance of {@link VRI }. Anonymous
     * resources, return <code>null</code>
     * @return
     *      URI of the component or <code>null</code> if not any.
     */
    public VRI getUri();

}
