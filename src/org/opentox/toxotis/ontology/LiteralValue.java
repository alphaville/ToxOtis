package org.opentox.toxotis.ontology;

import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import org.opentox.toxotis.ontology.OntologicalClass;

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
public class LiteralValue<T> {

    /** The value */
    private final T value;
    /** XSD datatype for the value */
    private XSDDatatype type;

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
    }

    /**
     * Create a Typed value with no explicit type declaration. The type will be
     * specified by the type mapper according to the Java generic <code>T</code>.
     * @param value
     *      The value
     */
    public LiteralValue(final T value) {
        this.value = value;
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
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LiteralValue<T> other = (LiteralValue<T>) obj;
        if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
            return false;
        }
        if (this.type != other.type && (this.type == null || !this.type.equals(other.type))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.value != null ? this.value.hashCode() : 0);
        hash = 71 * hash + (this.type != null ? this.type.hashCode() : 0);
        return hash;
    }

    public Literal inModel(OntModel model){
        return model.createTypedLiteral(getValue(),getType());
    }
}
