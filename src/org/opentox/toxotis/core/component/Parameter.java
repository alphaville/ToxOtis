package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.util.spiders.AnyValue;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Parameter<T> extends OTComponent<Parameter<T>> {

    @Override
    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public enum ParameterScope {

        OPTIONAL,
        MANDATORY;
    };
    /** The name of the parameter*/
    private String name;
    /** Typed value for the parameter */
    private AnyValue<T> typedValue;
    /** The scope of the parameter (mandatory/optional)*/
    private ParameterScope scope;

    public Parameter() {
        super();
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public ParameterScope getScope() {
        return scope;
    }

    public void setScope(ParameterScope scope) {
        this.scope = scope;
    }

    public XSDDatatype getType() {
        return this.typedValue.getType();
    }

    public T getValue() {
        return typedValue != null ? typedValue.getValue() : null;
    }

    public void setTypedValue(AnyValue<T> value) {
        this.typedValue = value;
    }

    public AnyValue<T> getTypedValue() {
        return typedValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.meta.addComment(name);
    }// </editor-fold>

    @Override
    public Individual asIndividual(OntModel model) {

        Individual indiv = model.createIndividual(getUri() != null ? getUri().toString() : null, OTClasses.Parameter().inModel(model));
        MetaInfo metaInfo = getMeta();
        if (metaInfo != null) {
            metaInfo.attachTo(indiv, model);
        }

        // scope
        if (getScope() != null) {
            indiv.addLiteral(OTDatatypeProperties.paramScope().asDatatypeProperty(model),
                    model.createTypedLiteral(getScope().toString(), XSDDatatype.XSDstring));
        }

        // value
        if (getValue() != null) {
            XSDDatatype xsdType = getType();
            if (xsdType == null) {
                xsdType = XSDDatatype.XSDstring;
            }
            indiv.addLiteral(OTDatatypeProperties.paramValue().asDatatypeProperty(model),
                    model.createTypedLiteral(getValue(), xsdType));
        }

        return indiv;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Name  : ");
        builder.append(name);
        builder.append("\n");
        builder.append("Value : ");
        builder.append(typedValue.getValue());
        builder.append("\n");
        builder.append("Scope : ");
        builder.append(scope);
        builder.append("\n");
        builder.append("Type  : ");
        builder.append(typedValue.getType());
        return new String(builder);
    }
}
