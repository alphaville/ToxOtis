package org.opentox.toxotis.ontology;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.Set;

public interface OntologicalClass extends OTResource, java.io.Serializable {

    String getNameSpace();

    void setNameSpace(String ns);

    String getName();

    void setName(String name);

    MetaInfo getMetaInfo();

    void setMetaInfo(MetaInfo metaInfo);

    Set<OntologicalClass> getSuperClasses();

    void setSuperClasses(Set<OntologicalClass> superClasses);

    Set<OntologicalClass> getDisjointWith();

    String getUri();

    void setUri(String uri);

    void setDisjointWith(Set<OntologicalClass> disjointWith);

    OntClass inModel(OntModel model);

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();
}
