package org.opentox.toxotis.ontology;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.Collection;

public interface OntologicalClass extends OTResource, java.io.Serializable{

    String getNameSpace();

    void setNameSpace(String ns);

    String getName();

    void setName(String name);

    MetaInfo getMetaInfo();

    void setMetaInfo(MetaInfo metaInfo);

    Collection<OntologicalClass> getSuperClasses();

    void setSuperClasses(Collection<OntologicalClass> superClasses);

    Collection<OntologicalClass> getDisjointWith();

    void setDisjointWith(Collection<OntologicalClass> disjointWith);

    OntClass inModel(OntModel model);


}
