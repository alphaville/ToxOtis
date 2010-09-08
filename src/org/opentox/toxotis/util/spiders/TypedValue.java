package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;

/**
 * A simple wrapper for a value (String) and its datatype according to the XSD
 * specidcation (find online documentation at <a href="http://infohost.nmt.edu/tcc/help/pubs/rnc/xsd.html">RNC</a>
 * and the complete specification at the <a href="http://www.w3.org/TR/xmlschema11-2/">
 * W3C web page</a>.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class TypedValue {

    /** The value */
    private final String value;
    /** XSD datatype for the value */
    private final XSDDatatype type;

    /**
     * Create a new typed value
     * @param value
     *      The value
     * @param type
     *      Datatype for the value
     */
    public TypedValue(final String value, final XSDDatatype type) {
        this.value = value;
        this.type = type;
    }

    /**
     * Create a Typed value with no explicit type declaration. The type will be
     * set to {@link XSDDatatype#XSDstring xsd:string}.
     * @param value
     *      The value
     */
    public TypedValue(final String value) {
        this.value = value;
        this.type = XSDDatatype.XSDstring;
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
    public String getValue() {
        return value;
    }

    


}