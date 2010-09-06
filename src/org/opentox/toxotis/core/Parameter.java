package org.opentox.toxotis.core;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Parameter<T> {

    public enum ParameterScope {

        OPTIONAL,
        MANDATORY;
    };
    private XSDDatatype type;
    private T value;
    private ParameterScope scope;

    public Parameter(XSDDatatype type, T value, ParameterScope scope) {
        this.type = type;
        this.value = value;
        this.scope = scope;
    }

    public ParameterScope getScope() {
        return scope;
    }

    public void setScope(ParameterScope scope) {
        this.scope = scope;
    }

    public XSDDatatype getType() {
        return type;
    }

    public void setType(XSDDatatype type) {
        this.type = type;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
