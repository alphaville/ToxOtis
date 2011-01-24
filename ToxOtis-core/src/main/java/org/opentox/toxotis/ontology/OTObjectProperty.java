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
package org.opentox.toxotis.ontology;

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.Collection;

/**
 * Object properties link individuals to individuals.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface OTObjectProperty extends OTProperty {

    /**
     * For a property one can define (multiple) <code>rdfs:range</code> axioms. Syntactically, <code>rdfs:range</code>
     * is a built-in property that links a property (some instance of the class <code>rdf:Property</code>) to to
     * either a class description or a data range. An <code>rdfs:range</code> axiom asserts that the values of
     * this property must belong to the class extension of the class description or to data values in the specified data range.
     * Multiple range restrictions are interpreted as stating that the range of the property is the intersection of
     * all ranges (i.e., the intersection of the class extension of the class descriptions c.q. the intersection of
     * the data ranges).
     * 
     * @return
     *    The range of an object property specifies the types of objects than
     *    can be set as values to this property.
     *
     * @see http://www.w3.org/TR/owl-ref/#ObjectProperty-def
     */
    Collection<OntologicalClass> getRange();

    /**
     * Setter method for the range of the current object property. The range is defined
     * as a collection of ontological classes.
     * @param range
     *      Set of ontological classes whose union defines the range of the current
     *      object property.
     */
    void setRange(Collection<OntologicalClass> range);

    /**
     * Cast this {@link OTObjectProperty } object as a Jena <a
     * href="http://jena.sourceforge.net/javadoc/com/hp/hpl/jena/ontology/ObjectProperty.html">
     * ObjectProperty</a> object. This property is assigned to the given <a
     * href="http://jena.sourceforge.net/javadoc/com/hp/hpl/jena/ontology/OntModel.html">
     * ontological model</a>.
     *
     * @param model
     *      The ontological model that holds the <code>ObjectProperty</code> definition.
     * @return
     *      The <code>ObjectProperty</code> object that is assigned to the given
     *      ontological model.
     */
    ObjectProperty asObjectProperty(OntModel model);

}