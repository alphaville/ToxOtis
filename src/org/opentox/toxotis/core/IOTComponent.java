package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;

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

}
