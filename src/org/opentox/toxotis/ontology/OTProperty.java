package org.opentox.toxotis.ontology;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import java.util.Collection;

public interface OTProperty extends OTResource, java.io.Serializable {

    String getNameSpace();

    void setNameSpace(String ns);

    String getName();

    void setName(String name);

    MetaInfo getMetaInfo();

    void setMetaInfo(MetaInfo metaInfo);

    Collection<OTProperty> getSuperProperties();

    void setSuperProperties(Collection<OTProperty> superProperties);

    Collection<OntologicalClass> getDomain();

    Property getProperty(OntModel model);

    void setDomain(Collection<OntologicalClass> domain);

    Property asProperty(OntModel model);
}
