package org.opentox.toxotis.ontology.impl;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.DC;
import java.util.ArrayList;
import java.util.Collection;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.OTObjectProperty;
import org.opentox.toxotis.ontology.OntologicalClass;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class OTObjectPropertyImpl extends OTPropertyImpl implements OTObjectProperty {

    private Collection<OntologicalClass> range = new ArrayList<OntologicalClass>();

    public OTObjectPropertyImpl() {
        super();
    }

    public OTObjectPropertyImpl(String name) {
        super(name);
    }

    public OTObjectPropertyImpl(String name, String nameSpace) {
        super(name, nameSpace);
    }
    

    @Override
    public Collection<OntologicalClass> getRange() {
        return range;
    }

    @Override
    public void setRange(Collection<OntologicalClass> range) {
        this.range = range;
    }

    @Override
    public ObjectProperty asObjectProperty(OntModel model) {
        ObjectProperty property = null;
        property = model.getObjectProperty(getUri());
        if (property != null) {
            return property;
        }
        property = model.createObjectProperty(getUri());

        if (getDomain() != null) {
            for (OntologicalClass dom : getDomain()) {
                property.addDomain(dom.inModel(model));
            }
        }

        if (getRange() != null) {
            for (OntologicalClass xsd : getRange()) {
                property.addRange(xsd.inModel(model));
            }
        }

        MetaInfo meta = getMetaInfo();
        if (meta != null) {
            meta.attachTo(property, model);
        }
        return property;
    }
}
