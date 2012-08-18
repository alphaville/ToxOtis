/*
 *
 * ToxOtis
 *
 * ToxOtis is the Greek word for Sagittarius, that actually means ‘archer’. ToxOtis
 * is a Java interface to the predictive toxicology services of OpenTox. ToxOtis is
 * being developed to help both those who need a painless way to consume OpenTox
 * services and for ambitious service providers that don’t want to spend half of
 * their time in RDF parsing and creation.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 *
 */
package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.Set;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.OntologicalClass;

/**
 * Generic interface for all OpenTox components in ToxOtis that can be represented
 * as individuals in an RDF graph. According to the OpenTox API specifications (since 
 * API 1.1.) should be available in RDF (application/rdf+xml) while other RDF-related
 * representations can be also available optionally (like application/x-turtle).
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface IOTComponent<T extends IOTComponent> {

    /**
     * The OpenTox component as an individual.
     * @param model
     *      The ontological model to which the individual belongs.
     * @return
     *      The OpenTox component as an individual of a data model.
     */
    Individual asIndividual(OntModel model);

    /**
     * Creates a new Ontological Model (uses an instance of {@link SimpleOntModelImpl })
     * and assigns to it the Individual from the method
     * {@link OTComponent#asIndividual(com.hp.hpl.jena.ontology.OntModel) asIndividual(OntModel)}.
     * The ontological model will contain no other information apart from the representation
     * of the inderlying OpenTox component.
     * @return
     *      An ontological model for the current OpenTox component.
     */
    OntModel asOntModel() ;

    /**
     * Get the URI of the resource (as an instance of {@link VRI }. Anonymous
     * resources, return <code>null</code>
     * @return
     *      URI of the component or <code>null</code> if not any.
     */
    VRI getUri();

    /**
     * Obtain meta information about the underlying OpenTox resource/component.
     * These meta information include various fields of the Dublin Core ontology
     * (title, identifier), properties from the OpenTox ontology (see
     * http://opentox.org/data/documents/development/RDF%20Files/OpenToxOntology)
     * and some elements from other ontologies like rdfs:comment
     * @return
     *      Meta information about the resource
     */
    MetaInfo getMeta();

    /**
     * Set the meta-information of the component
     * @param meta
     *      Meta information for the component
     * @return
     *      The component itself with the updated meta-information
     * @see MetaInfo Meta Information
     */
    T setMeta(MetaInfo meta);

    /**
     * The list of ontological classes for the current entity.
     * @return 
     *      Set of {@link OntologicalClass ontological classes}.
     */
    Set<OntologicalClass> getOntologicalClasses();

    /**
     * A simple setter for the ontological classes of the current OT-component.
     * @param ontClasses
     *      The Set of ontological classes to be prescribed for the current 
     *      component.
     * @return 
     *      The current component with the updated set of ontological classes.
     */
    T setOntologicalClasses(Set<OntologicalClass> ontClasses);
    
    /**
     * Adds a list of ontological classes to the current ones.
     * @param ontClasses
     *      An array of ontological classes.
     * @return 
     *      The current modifiable object updated with the new
     *      ontological classes.
     */
    T addOntologicalClasses(OntologicalClass... ontClasses);

    /**
     * Whether the current entity is enabled. Only enabled entities are presented to
     * end-users. An entity can be virtually deleted it is set to be disabled. 
     * Further action of course is need to handle enabled and disabled resources.
     * Mainly, this serves as an auxiliary property of all OT-components for easier
     * DB management.
     * 
     * @return 
     *      <code>true</code> if the object is enabled and <code>false</code>
     *      otherwise.
     */
    boolean isEnabled();

    /**
     * Sets the status of the current modifiable entity. 
     * @param enabled
     *      Whether to enable or to disable the current instance.
     * @return 
     *      The current modifiable object updated with the new value of 
     *      the <code>enabled</code> status.
     */
    T setEnabled(boolean enabled);

}
