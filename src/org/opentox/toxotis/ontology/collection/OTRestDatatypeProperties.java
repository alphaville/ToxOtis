package org.opentox.toxotis.ontology.collection;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import org.opentox.toxotis.ontology.OTDatatypeProperty;
import org.opentox.toxotis.ontology.impl.OTDatatypePropertyImpl;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class OTRestDatatypeProperties {

    private static OTDatatypeProperty ms_hasAttribute;
    private static OTDatatypeProperty ms_hasURI;
    private static OTDatatypeProperty ms_paramName;
    private static OTDatatypeProperty ms_paramOptional;

    public static OTDatatypeProperty hasAttribute() {
        if (ms_hasAttribute == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasAttribute");
            property.getDomain().add(OTRestClasses.RESTTemplateAttribute());
            property.getRange().add(XSDDatatype.XSDstring);
            ms_hasAttribute = property;
        }
        return ms_hasAttribute;
    }

    public static OTDatatypeProperty hasURI() {
        if (ms_hasURI == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasURI");
            property.getDomain().add(OTRestClasses.RESTTemplate());
            property.getRange().add(XSDDatatype.XSDstring);
            ms_hasURI = property;
        }
        return ms_hasURI;
    }

    public static OTDatatypeProperty paramName() {
        if (ms_paramName == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("paramName");
            property.getDomain().add(OTRestClasses.InputParameter());
            property.getRange().add(XSDDatatype.XSDstring);
            ms_paramName = property;
        }
        return ms_paramName;
    }

    public static OTDatatypeProperty paramOptional() {
        if (ms_paramOptional == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("paramOptional");
            property.getDomain().add(OTRestClasses.InputParameter());
            property.getRange().add(XSDDatatype.XSDboolean);
            ms_paramOptional = property;
        }
        return ms_paramOptional;
    }



}
