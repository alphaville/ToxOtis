package org.opentox.toxotis.core;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import org.opentox.toxotis.ontology.MetaInfo;

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
    private MetaInfo meta;

    public Parameter() {
    }

    public Parameter(XSDDatatype type, T value, ParameterScope scope) {
        this.type = type;
        this.value = value;
        this.scope = scope;
    }

    public MetaInfo getMeta() {
        return meta;
    }

    public void setMeta(MetaInfo meta) {
        this.meta = meta;
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
