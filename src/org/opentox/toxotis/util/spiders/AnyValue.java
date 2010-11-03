package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTClasses;

/**
 * A simple wrapper for a value (usually String) and its datatype according to the XSD
 * specidcation (find online documentation at <a href="http://infohost.nmt.edu/tcc/help/pubs/rnc/xsd.html">RNC</a>
 * and the complete specification at the <a href="http://www.w3.org/TR/xmlschema11-2/">
 * W3C web page</a>.
 *
 * @param <T> Generic for the (Java) datatype of the value
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class AnyValue<T> {

    /** The value */
    private final T value;
    /** XSD datatype for the value */
    private XSDDatatype type;
    private boolean literal = true;
    private OntologicalClass classType = null;

    /**
     * Create a new typed value
     * @param value
     *      The value
     * @param type
     *      Datatype for the value
     */
    public AnyValue(final T value, final XSDDatatype type) {
        this.value = value;
        this.type = type;
        this.literal = true;
    }

    /**
     * Create a Typed value with no explicit type declaration. The type will be
     * specified by the type mapper according to the Java generic <code>T</code>.
     * @param value
     *      The value
     */
    public AnyValue(final T value) {
        this.value = value;
        this.literal = true;
        if (value.getClass().equals(java.util.Date.class)) {
            this.type = XSDDatatype.XSDdateTime; // << According to the OpenTox API dc:date should be xsd:datetime
        } else {
            this.type = (XSDDatatype) TypeMapper.getInstance().getTypeByClass(value.getClass());
        }
    }

    public AnyValue(final T uri, OntologicalClass classType) {
        this.value = (T) uri;
        this.classType = classType;
        this.literal = false;
        this.type = null;
    }

    public boolean isLiteral() {
        return literal;
    }

    public OntologicalClass getClassType() {
        return classType;
    }
    

    /**
     * Retrieve the datatype
     * @return
     *      The type of the value
     */
    public XSDDatatype getType() {
        return type;
    }

    /**
     * Retrieve the value
     * @return
     *      The value itself
     */
    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (getType() == null) {
            return getValue().toString();
        }
        return getValue() + "^^" + getType().getURI().replaceAll(XSDDatatype.XSD + "#", "");
    }
}
