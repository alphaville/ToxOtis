package org.opentox.toxotis.ontology.impl;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDFS;
import java.util.ArrayList;
import java.util.Collection;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.OTProperty;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTClasses;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class OTPropertyImpl implements OTProperty {

    private MetaInfo metaInfo = new MetaInfoImpl();
    private Collection<OntologicalClass> domain = new ArrayList<OntologicalClass>();
    private Collection<OTProperty> superProperties = new ArrayList<OTProperty>();
    private String ns = OTClasses.NS;
    private String name;

    public OTPropertyImpl() {
    }

    public OTPropertyImpl(String name) {
        this.name = name;
    }

    @Override
    public MetaInfo getMetaInfo() {
        return metaInfo;
    }

    @Override
    public void setMetaInfo(MetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    @Override
    public Collection<OntologicalClass> getDomain() {
        return domain;
    }

    @Override
    public void setDomain(Collection<OntologicalClass> domain) {
        this.domain = domain;
    }

    @Override
    public Collection<OTProperty> getSuperProperties() {
        return superProperties;
    }

    @Override
    public void setSuperProperties(Collection<OTProperty> superProperties) {
        this.superProperties = superProperties;
    }

    @Override
    public String getNameSpace() {
        return ns;
    }

    @Override
    public void setNameSpace(String ns) {
        this.ns = ns;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUri() {
        return getNameSpace() + getName();
    }

    @Override
    public Property getProperty(OntModel model) {
        return model.getProperty(getUri());
    }

    public Property asProperty(OntModel model) {
        Property property = null;
        property = model.getProperty(getUri());
        if (property != null) {
            return property;
        }
        property = model.createProperty(getUri());


        MetaInfo meta = getMetaInfo();
        if (meta != null) {
            meta.attachTo(property, model);
        }
        return property;
    }
}
