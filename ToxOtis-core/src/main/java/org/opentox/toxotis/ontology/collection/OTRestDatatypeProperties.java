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

    private static OTDatatypeProperty hasAttribute;
    private static OTDatatypeProperty hasURI;
    private static OTDatatypeProperty paramName;
    private static OTDatatypeProperty paramOptional;

    public static OTDatatypeProperty hasAttribute() {
        if (hasAttribute == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasAttribute");
            property.getDomain().add(OTRestClasses.RESTTemplateAttribute());
            property.getRange().add(XSDDatatype.XSDstring);
            hasAttribute = property;
        }
        return hasAttribute;
    }

    public static OTDatatypeProperty hasURI() {
        if (hasURI == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("hasURI");
            property.getDomain().add(OTRestClasses.RESTTemplate());
            property.getRange().add(XSDDatatype.XSDstring);
            hasURI = property;
        }
        return hasURI;
    }

    public static OTDatatypeProperty paramName() {
        if (paramName == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("paramName");
            property.getDomain().add(OTRestClasses.InputParameter());
            property.getRange().add(XSDDatatype.XSDstring);
            paramName = property;
        }
        return paramName;
    }

    public static OTDatatypeProperty paramOptional() {
        if (paramOptional == null) {
            OTDatatypeProperty property = new OTDatatypePropertyImpl("paramOptional");
            property.getDomain().add(OTRestClasses.InputParameter());
            property.getRange().add(XSDDatatype.XSDboolean);
            paramOptional = property;
        }
        return paramOptional;
    }



}
