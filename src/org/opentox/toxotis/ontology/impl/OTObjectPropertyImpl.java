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
            String title = meta.getTitle()!=null?meta.getTitle().getValue():null;
            if (title != null) {
                property.addLiteral(model.createAnnotationProperty(DC.title.getURI()),
                        model.createTypedLiteral(title, XSDDatatype.XSDstring));
            }

            String description = meta.getDescription()!=null?meta.getDescription().getValue():null;
            if (description != null) {
                property.addLiteral(model.createAnnotationProperty(DC.description.getURI()),
                        model.createTypedLiteral(description, XSDDatatype.XSDstring));
            }

            String comment = meta.getComment()!=null?meta.getComment().getValue():null;
            if (comment != null) {
                property.addComment(model.createTypedLiteral(comment, XSDDatatype.XSDstring));
            }

            String identifier = meta.getIdentifier()!=null?meta.getIdentifier().getValue():null;
            if (identifier != null) {
                property.addLiteral(model.createAnnotationProperty(DC.identifier.getURI()),
                        model.createTypedLiteral(identifier, XSDDatatype.XSDanyURI));
            }
        }
        return property;
    }
}
