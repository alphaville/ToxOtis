package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;

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
    /** Typed value for the parameter */
    private LiteralValue<T> typedValue;
    /** The scope of the parameter (mandatory/optional)*/
    private ParameterScope scope;

    public Parameter() {
        super();
    }

    public Parameter(String name, LiteralValue value){
        super();
        setName(name);
        setTypedValue(value);
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

    public void setTypedValue(LiteralValue<T> value) {
        this.typedValue = value;
    }

    public LiteralValue<T> getTypedValue() {
        return typedValue;
    }

    public LiteralValue getName() {
        if (getMeta() != null) {
            if (getMeta().getTitles() != null && !getMeta().getTitles().isEmpty()) {
                LiteralValue val = getMeta().getTitles().iterator().next();
                return val;
            }
        }
        return null;
    }

    public Parameter setName(String name) {
        if (getMeta() == null) {
            setMeta(new MetaInfoImpl());
        }
        getMeta().addTitle(name);
        return this;
    }

// </editor-fold>
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
        builder.append(getName());
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Parameter<T> other = (Parameter<T>) obj;
        if (this.typedValue != other.typedValue && (this.typedValue == null || !this.typedValue.equals(other.typedValue))) {
            return false;
        }
        if (this.scope != other.scope && (this.scope == null || !this.scope.equals(other.scope))) {
            return false;
        }
        if (getName() != other.getName() && (getName() == null || !getName().equals(other.getName()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.getName() != null ? this.getName().toString().hashCode() : 0);
        hash = 37 * hash + (this.typedValue != null ? this.typedValue.toString().hashCode() : 0);
        hash = 37 * hash + (this.scope != null ? this.scope.toString().hashCode() : 0);
        return hash;
    }
}
