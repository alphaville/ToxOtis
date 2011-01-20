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

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import java.util.Collection;

/**
 * An interface for ontological properties used in OpenTox web services.
 * 
 * @author Pantlelis Sopasakis
 */
public interface OTProperty extends OTResource, java.io.Serializable {

    /**
     * Returns the namespace of the ontological property or <code>null</code>
     * if not defined.
     *
     * @return
     *      Namespace of property
     */
    String getNameSpace();

    /**
     * Specify a namespace for the ontological property
     *
     * @param ns
     *      Namespace.
     */
    void setNameSpace(String ns);

    /**
     * Returns the local name of the ontological property omiiting the namespace
     * string.
     *
     * @return
     *      local name of the property as a string.
     */
    String getName();

    /**
     * Set the local name of the property (namespace not included).
     * @param name
     *      local name.
     */
    void setName(String name);

    /**
     * Get the meta information available abou the property.
     * @return
     *      Meta information as an instance of {@link MetaInfo }.
     */
    MetaInfo getMetaInfo();

    /**
     * Provide meta-information about the property
     * @param metaInfo
     *      Meta information about the property.
     */
    void setMetaInfo(MetaInfo metaInfo);

    /**
     * Get a collection of the super-properties of this ontological property
     * declared using <code>rdfs:subPropertyOf</code>.
     * @return
     *      Collection of super-properties.
     */
    Collection<OTProperty> getSuperProperties();

    /**
     *
     * @param superProperties
     *      Collection of super-properties.
     * @see OTProperty#getSuperProperties() getSuperProperties
     */
    void setSuperProperties(Collection<OTProperty> superProperties);

    /**
     * We quote an excperpt of the documentation about the property <code>rdfs:domain</code>
     * found at <a href="http://www.w3.org/TR/2004/REC-owl-features-20040210/#domain">
     * W3C</a> online reference:
     * 
     * <blockquote align="justify">
     * A domain of a property limits the individuals to which the property can be applied.
     * If a property relates an individual to another individual, and the property has a
     * class as one of its domains, then the individual must belong to the class. For example,
     * the property hasChild may be stated to have the domain of Mammal. From this a
     * reasoner can deduce that if Frank hasChild Anna, then Frank must be a Mammal.
     * Note that rdfs:domain is called a global restriction since the restriction is stated on the
     * property and not just on the property when it is associated with a particular class.
     * See the discussion below on property restrictions for more information.
     * </blockquote>
     *
     * @return
     *      A collection of the ontological classes that constitute the domain of the
     *      ontological property.
     */
    Collection<OntologicalClass> getDomain();

    /**
     * Extract the corresponding property (if any) from the given ontological
     * model. If the property is not found in the data model, the method return
     * <code>null</code>. In that case where the property does not exist in the
     * data model and it should be created, consider using {@link OTProperty#asProperty(com.hp.hpl.jena.ontology.OntModel)
     * asProperty(OntModel)}.
     * @param model
     *      Ontological Model
     * @return
     *      Property found in that model of <code>null</code> otherwise.
     */
    Property getProperty(OntModel model);

    /**
     * The following note is copied here from the online OWL reference at http://www.w3.org/TR/owl-ref:
     * For a property one can define (multiple) <code>rdfs:domain</code> axioms. Syntactically,
     * <code>rdfs:domain</code> is a built-in property that links a property (some instance
     * of the class <code>rdf:Property</code>) to a class description. An <code>rdfs:domain
     * </code> axiom asserts that the subjects of such property statements must belong to the
     * class extension of the indicated class description.
     * @param domain
     *      A collection of the ontological classes that constitute the domain of the
     *      ontological property.
     */
    void setDomain(Collection<OntologicalClass> domain);

    /**
     * The following note is copied here from the online OWL reference at http://www.w3.org/TR/owl-ref:
     * Cast the <code>OTProperty</code> object as a Property object of Jena. If the property
     * already exists in the ontological model, it is not recreated but returned to the
     * user. In that case, this method is equivalent to {@link
     * OTProperty#getProperty(com.hp.hpl.jena.ontology.OntModel) getProperty}.
     * 
     * @param model
     *      The model to which is property is assigned.
     * @return
     *      The created proeprty in the provided Ontological Model.
     */
    Property asProperty(OntModel model);
}
