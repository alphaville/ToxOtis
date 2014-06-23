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

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.Collection;

/**
 * <p align=justify>
 * According to the OWL specification provided by W3C, datatype properties link
 * individuals to data values.
 * </p>
 *
 * @author Sopasakis Pantelis
 */
public interface OTDatatypeProperty extends OTProperty {

    /**
     * <p align=justify>
     * For a property one can define (multiple) <code>rdfs:range</code> axioms. Syntactically, <code>rdfs:range</code>
     * is a built-in property that links a property (some instance of the class <code>rdf:Property</code>) to to
     * either a class description or a data range. An <code>rdfs:range</code> axiom asserts that the values of
     * this property must belong to the class extension of the class description or to data values in the specified data range.
     * Multiple range restrictions are interpreted as stating that the range of the property is the intersection of
     * all ranges (i.e., the intersection of the class extension of the class descriptions c.q. the intersection of
     * the data ranges).
     * </p>
     *
     *
     * @return
     *    The range of an object property specifies the admissible XSD datatypes
     *    for this property.
     *
     * @see <a href="http://www.w3.org/TR/owl-ref/#ObjectProperty-def">Object Property Definition</a>
     * 
     */
    Collection<XSDDatatype> getRange();

    /**
     * Set the range for this datatype property
     * @param range
     *      A collection of XSD datatypes
     */
    void setRange(Collection<XSDDatatype> range);

    /**
     * Cast this {@link OTDatatypeProperty } object as a Jena <a
     * href="http://jena.sourceforge.net/javadoc/com/hp/hpl/jena/ontology/DatatypeProperty.html">DatatypeProperty</a>
     * object. This property is assigned to the given <a
     * href="http://jena.sourceforge.net/javadoc/com/hp/hpl/jena/ontology/OntModel.html">ontological model</a>.
     * @param model
     *      The ontological model that holds the <code>
     *      DatatypeProperty</code> definition.
     * @return
     *      The <code>DatatypeProperty</code> object that is assigned to the given
     *      ontological model.
     */
    DatatypeProperty asDatatypeProperty(OntModel model);
}