package org.opentox.toxotis.ontology.impl;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.DC;
import java.util.ArrayList;
import java.util.Collection;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.OTDatatypeProperty;
import org.opentox.toxotis.ontology.OntologicalClass;

public class OTDatatypePropertyImpl
        extends OTPropertyImpl implements OTDatatypeProperty {

    private Collection<XSDDatatype> domain = new ArrayList<XSDDatatype>();

    public OTDatatypePropertyImpl() {
        super();
    }

    public OTDatatypePropertyImpl(String name) {
        super(name);
    }

    @Override
    public Collection<XSDDatatype> getRange() {
        return domain;
    }

    @Override
    public void setRange(Collection<XSDDatatype> range) {
        this.domain = range;
    }


    @Override
    public DatatypeProperty asDatatypeProperty(OntModel model) {
        DatatypeProperty property = null;
        property = model.getDatatypeProperty(getUri());
        if (property != null) {
            return property;
        }

        property = model.createDatatypeProperty(getUri());
        if (getDomain() != null) {
            for (OntologicalClass dom : getDomain()) {
                property.addDomain(dom.inModel(model));
            }
        }

        if (getRange() != null) {
            for (XSDDatatype xsd : getRange()) {
                property.addRange(model.createResource(xsd.getURI()));
            }
        }

        MetaInfo meta = getMetaInfo();
        meta.attachTo(property, model);
        return property;
    }
}
