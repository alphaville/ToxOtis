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
