package org.opentox.toxotis.ontology;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.Collection;

/**
 * <p align=justify width=80%>
 * According to the OWL specification provided by W3C, datatype properties link
 * individuals to data values.
 * </p>
 *
 * @author Sopasakis Pantelis
 */
public interface OTDatatypeProperty extends OTProperty{

    /**
     * <p align=justify width=80%>
     * For a property one can define (multiple) <code>rdfs:range</code> axioms. Syntactically, <code>rdfs:range</code>
     * is a built-in property that links a property (some instance of the class <code>rdf:Property</code>) to to
     * either a class description or a data range. An <code>rdfs:range</code> axiom asserts that the values of
     * this property must belong to the class extension of the class description or to data values in the specified data range.
     * Multiple range restrictions are interpreted as stating that the range of the property is the intersection of
     * all ranges (i.e., the intersection of the class extension of the class descriptions c.q. the intersection of
     * the data ranges).
     * </p>
     *
     *
     * @return
     *    The range of an object property specifies the admissible XSD datatypes
     *    for this property.
     *
     * @see http://www.w3.org/TR/owl-ref/#ObjectProperty-def
     */
    Collection<XSDDatatype> getRange();

    /**
     * Set the range for this datatype property
     * @param range
     *      A collection of XSD datatypes
     */
    void setRange(Collection<XSDDatatype> range);

    /**
     * Cast this {@link OTDatatypeProperty } object as a Jena <a
     * href="http://jena.sourceforge.net/javadoc/com/hp/hpl/jena/ontology/DatatypeProperty.html">DatatypeProperty</a>
     * object. This property is assigned to the given <a
     * href="http://jena.sourceforge.net/javadoc/com/hp/hpl/jena/ontology/OntModel.html">ontological model</a>.
     * @param model
     *      The ontological model that holds the <code>
     *      DatatypeProperty</code> definition.
     * @return
     *      The <code>DatatypeProperty</code> object that is assigned to the given
     *      ontological model.
     */
    DatatypeProperty asDatatypeProperty(OntModel model);
}