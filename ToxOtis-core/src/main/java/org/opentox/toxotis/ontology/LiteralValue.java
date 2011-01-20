/*
 *
 * ToxOtis
 *
 * ToxOtis is the Greek word for Sagittarius, that actually means ‘archer’. ToxOtis
 * is a Java interface to the predictive toxicology services of OpenTox. ToxOtis is
 * being developed to help both those who need a painless way to consume OpenTox
 * services and for ambitious service providers that don’t want to spend half of
 * their time in RDF parsing and creation.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 *
 */
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
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LiteralValue.class);

    /**
     * Dummy constructor for the class {@link LiteralValue } which initializes a new
     * instance of it with <code>null</code> value and default class <code>java.lang.String</code>,
     * or in terms of xsd datatypes, {@link XSDDatatype#XSDstring }.
     */
    public LiteralValue() {
    }

    /**
     * Create a new typed value with given value and XSD datatype.
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

    /**
     * Delegates the method {@link #getHash() } herein. It is advisable that you use
     * the method {@link #getHash() } instead where needed.
     * @return
     *      The hashCode value returned by {@link #getHash() } cast as integer. In case
     *      the result returned by {@link #getHash() } is too big to be cast as an integer
     *      , then it is adapted within the limits or minimum and maximum integers.
     */
    @Override
    public int hashCode() {
//        if (getHash() <= Integer.MAX_VALUE && getHash() >= Integer.MIN_VALUE) {
            return (int) getHash();
//        } else if (getHash() > Integer.MAX_VALUE) {
//            return (int) (Integer.MAX_VALUE - 2 * getHash());
//        } else {
//            return (int) (Integer.MIN_VALUE + 2 * getHash());
//        }
    }

    /**
     * Creates a typed literal in an ontological data model.
     * @param model
     *      Ontological model to be used for the construction of the typed literal.
     *      This parameter must be <code>not null</code>.
     * @return
     *      Returns the typed literal as a Jena object.
     */
    public Literal inModel(OntModel model) {
        return model.createTypedLiteral(getValue(), getType());
    }

    /**
     * Getter fot the hash code of the current LiteralValue object. Notice that a
     * difference from the ordinary method {@link #hashCode() } is that the latter
     * returns the result as an integer while this custom method returns a long hash.
     * @return
     *      Long hash code.
     */
    public long getHash() {
        long hash = (value != null ? value.toString().trim().hashCode() : 0)
                + 7 * (type != null ? type.toString().trim().hashCode() : 0);
        return hash;
    }

    /**
     * Required so that the class can be persistent using Hibernate. In fact this
     * method has empty body and does nothing whatsoever.
     * @param hashCode
     *      hash code which will not be used anywhere.
     */
    public void setHash(long hashCode) {/* Do nothing! */ }
}
