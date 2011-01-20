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

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.Set;

/**
 * ToxOtis interface for ontological classes. A class is a collection of entities,
 * sets or other entities that is unambiguously defined by a property that all its
 * members share. In order to establish a communicatable description with disambiguation,
 * ontological classes here are endowed with a set of meta-information.
 *   Ontological classes are Resources (in the RDF sense) characterized by their
 * URI, meta information about them and a set of relations with other ontological
 * classes like subclassing/inheritance and distinctions.
 * 
 * @author Pantelis Sopasakis
 * @author Hampos Chomenides
 */
public interface OntologicalClass extends OTResource, java.io.Serializable {

    String getNameSpace();

    OntologicalClass setNameSpace(String ns);

    String getName();

    OntologicalClass setName(String name);

    MetaInfo getMetaInfo();

    OntologicalClass setMetaInfo(MetaInfo metaInfo);

    Set<OntologicalClass> getSuperClasses();

    OntologicalClass setSuperClasses(Set<OntologicalClass> superClasses);

    Set<OntologicalClass> getDisjointWith();

    String getUri();

    OntologicalClass setUri(String uri);

    OntologicalClass setDisjointWith(Set<OntologicalClass> disjointWith);

    OntClass inModel(OntModel model);

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();
}
