package org.opentox.toxotis.ontology;

import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import java.io.Serializable;
import java.util.Date;

/**
 * A simple wrapper for a value (usually String) and its datatype according to the XSD
 * specidcation (find online documentation at <a href="http://infohost.nmt.edu/tcc/help/pubs/rnc/xsd.html">RNC</a>
 * and the complete specification at the <a href="http://www.w3.org/TR/xmlschema11-2/">
 * W3C web page</a>. Literal values are atomic values characterized by some datatype,
 * according to the XSD specifications. As we read at <a href="http://www.w3.org/TR/rdf-concepts/#section-Literals">
 * http://www.w3.org/TR/rdf-concepts/#section-Literals</a>:
 *
 * <blockquote>
 * Literals are used to identify values such as numbers and dates by means of a
 * lexical representation. Anything represented by a literal could also be represented by
 * a URI, but it is often more convenient or intuitive to use literals.
 * A literal may be the object of an RDF statement, but not the subject or the predicate.
 * </blockquote>
 *
 * This class aims at providing the Java interface to RDF literal values.
 *
 * @param <T> Generic for the (Java) datatype of the value.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class LiteralValue<T> implements Serializable {

    /** The value */
    private T value;
    private Class<?> clazz = String.class;
    /** XSD datatype for the value */
    private XSDDatatype type = XSDDatatype.XSDstring;

    public LiteralValue() {
    }

    /**
     * Create a new typed value
     * @param value
     *      The value
     * @param type
     *      Datatype for the value
     */
    public LiteralValue(final T value, final XSDDatatype type) {
        this.value = value;
        this.type = type;
        if (value != null) {
            this.clazz = value.getClass();
        }
    }

    /**
     * Create a Typed value with no explicit type declaration. The type will be
     * specified by the type mapper according to the Java generic <code>T</code>.
     * @param value
     *      The value
     */
    public LiteralValue(final T value) {
        this.value = value;
        if (value != null) {
            this.clazz = value.getClass();
        }
        if (value.getClass().equals(java.util.Date.class)) {
            this.type = XSDDatatype.XSDdateTime; // << According to the OpenTox API dc:date should be xsd:datetime
        } else {
            this.type = (XSDDatatype) TypeMapper.getInstance().getTypeByClass(value.getClass());
        }
    }

    /**
     * Retrieve the datatype
     * @return
     *      The type of the value
     */
    public XSDDatatype getType() {
        return type;
    }

    public void setType(XSDDatatype type) {
        this.type = type;
    }

    public void setValue(T value) {
        this.value = value;
    }

    /**
     * Returns a String representation of the underlying value. This is
     * different from the method toString() defined in this class.
     * @return
     *      The value as String or <code>null</code> in case the value is null.
     */
    public String getValueAsString() {
        return value != null ? value.toString() : null;
    }

    public void setValueAsString(String value) {
        if (clazz != null) {
            if (value == null) {
                this.value = null;
            } else {
                if (clazz == Double.class) {
                    this.value = (T) (Double) Double.parseDouble(value);
                } else if (clazz == Integer.class) {
                    this.value = (T) (Integer) Integer.parseInt(value);
                } else if (clazz == Date.class) {
                    this.value = (T) new Date(value);
                } else if (String.class.isAssignableFrom(clazz)) {
                    this.value = (T) value.toString();
                }
            }
        } else {
            this.value = (T) value;
        }
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (LiteralValue.class != obj.getClass()) {
            return false;
        }
        final LiteralValue<T> other = (LiteralValue<T>) obj;
        boolean isEq = getHash() == other.getHash();
        return isEq;
    }

    @Override
    public int hashCode() {
        return (int) getHash();
    }

    public Literal inModel(OntModel model) {
        return model.createTypedLiteral(getValue(), getType());
    }


    public long getHash() {
        long hash =  (value != null ? value.toString().trim().hashCode() : 0)
                + 7 * (type != null ? type.toString().trim().hashCode() : 0);
        return hash;
    }

    public void setHash(long hashCode) {/* Do nothing! */ }
    
}
