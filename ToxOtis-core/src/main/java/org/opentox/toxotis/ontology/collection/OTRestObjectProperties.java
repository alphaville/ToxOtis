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
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import org.opentox.toxotis.ontology.OTObjectProperty;
import org.opentox.toxotis.ontology.impl.OTObjectPropertyImpl;
import org.opentox.toxotis.ontology.impl.OntologicalClassImpl;

/**
 * OpenTox REST Object Properties.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public final class OTRestObjectProperties {

    /**
     * NameSpace of the OpenTox REST ontology.
     */
    public static final String NS = "http://opentox.org/opentox-rest.owl#";
    private static OTObjectProperty msAa;
    private static OTObjectProperty msHasHTTPMethod;
    private static OTObjectProperty msHasHTTPStatus;
    private static OTObjectProperty msHasMedia;
    private static OTObjectProperty msInputParam;
    private static OTObjectProperty msParamContent;
    private static OTObjectProperty msParamContentOpenTox;
    private static OTObjectProperty msParamContentSimple;
    private static OTObjectProperty msResource;
    private static OTObjectProperty msHasRESTOperation;
    private static OTObjectProperty msResult;
    private static OTObjectProperty msStatus;
    private static OTObjectProperty msUri;

    public static OTObjectProperty hasHTTPMethod() {
        if (msHasHTTPMethod == null) {
            OTObjectProperty clazz = new OTObjectPropertyImpl("hasHTTPMethod", NS);
            clazz.getDomain().add(OTRestClasses.restOperation());
            clazz.getRange().add(OTRestClasses.httpMethod());
            msHasHTTPMethod = clazz;
        }
        return msHasHTTPMethod;
    }

    public static OTObjectProperty hasHTTPStatus() {
        if (msHasHTTPStatus == null) {
            OTObjectProperty clazz = new OTObjectPropertyImpl("hasHTTPStatus", NS);
            clazz.getDomain().add(OTRestClasses.restOperation());
            clazz.getRange().add(OTRestClasses.httpStatus());
            msHasHTTPStatus = clazz;
        }
        return msHasHTTPStatus;
    }

    public static OTObjectProperty hasMedia() {
        if (msHasMedia == null) {
            OTObjectProperty clazz = new OTObjectPropertyImpl("hasMedia", NS);
            clazz.getDomain().add(OTRestClasses.restOperation());
            clazz.getRange().add(OTRestClasses.mediaType());
            msHasMedia = clazz;
        }
        return msHasMedia;
    }

    public static OTObjectProperty aa() {
        if (msAa == null) {
            OTObjectProperty clazz = new OTObjectPropertyImpl("aa", NS);
            clazz.getDomain().add(OTRestClasses.restOperation());
            clazz.getRange().add(OTRestClasses.aa());
            msAa = clazz;
        }
        return msAa;
    }

    public static OTObjectProperty inputParam() {
        if (msInputParam == null) {
            OTObjectProperty clazz = new OTObjectPropertyImpl("inputParam", NS);
            clazz.getDomain().add(OTRestClasses.restOperation());
            clazz.getDomain().add(OTRestClasses.aa());
            clazz.getRange().add(OTRestClasses.inputParameter());
            msInputParam = clazz;
        }
        return msInputParam;
    }

    public static OTObjectProperty result() {
        if (msResult == null) {
            OTObjectProperty clazz = new OTObjectPropertyImpl("result", NS);
            clazz.getDomain().add(OTRestClasses.restOperation());
            clazz.getRange().add(OTClasses.openToxResource());
            msResult = clazz;
        }
        return msResult;
    }

    public static OTObjectProperty paramContent() {
        if (msParamContent == null) {
            OTObjectProperty clazz = new OTObjectPropertyImpl("paramContent", NS);
            clazz.getDomain().add(OTRestClasses.inputParameter());
            msParamContent = clazz;
        }
        return msParamContent;
    }

    public static OTObjectProperty paramContentOpenTox() {
        if (msParamContentOpenTox == null) {
            OTObjectProperty clazz = new OTObjectPropertyImpl("paramContentOpenTox", NS);
            clazz.getDomain().add(OTRestClasses.inputParameter());
            clazz.getDomain().add(OTClasses.openToxResource());
            msParamContentOpenTox = clazz;
        }
        return msParamContentOpenTox;
    }

    public static OTObjectProperty paramContentSimple() {
        if (msParamContentSimple == null) {
            OTObjectProperty clazz = new OTObjectPropertyImpl("paramContentSimple", NS);
            clazz.getDomain().add(OTRestClasses.inputParameter());
            clazz.getRange().add(new OntologicalClassImpl("string", XSDDatatype.XSD));
            clazz.getRange().add(new OntologicalClassImpl("boolean", XSDDatatype.XSD));
            clazz.getRange().add(new OntologicalClassImpl("int", XSDDatatype.XSD));
            clazz.getRange().add(new OntologicalClassImpl("integer", XSDDatatype.XSD));
            clazz.getRange().add(new OntologicalClassImpl("anyURI", XSDDatatype.XSD));
            clazz.getRange().add(new OntologicalClassImpl("double", XSDDatatype.XSD));
            clazz.getRange().add(new OntologicalClassImpl("float", XSDDatatype.XSD));
            clazz.getRange().add(new OntologicalClassImpl("long", XSDDatatype.XSD));
            clazz.getRange().add(new OntologicalClassImpl("byte", XSDDatatype.XSD));
            clazz.getRange().add(new OntologicalClassImpl("date", XSDDatatype.XSD));
            msParamContentSimple = clazz;
        }
        return msParamContentSimple;
    }

    public static OTObjectProperty resource() {
        if (msResource == null) {
            msResource = new OTObjectPropertyImpl();
            OTObjectProperty clazz = new OTObjectPropertyImpl("resource", NS) {

                @Override
                public ObjectProperty asObjectProperty(OntModel model) {
                    ObjectProperty op = super.asObjectProperty(model);
                    if (msHasRESTOperation == null) {// to avoid StackOverflow
                        op.setInverseOf(hasRESTOperation().asObjectProperty(model));
                        hasRESTOperation().asObjectProperty(model).setInverseOf(op);
                    }
                    return op;
                }
            };
            clazz.getDomain().add(OTRestClasses.restOperation());
            clazz.getRange().add(OTClasses.openToxResource());
            msResource = clazz;
        }
        return msResource;
    }

    public static OTObjectProperty hasRESTOperation() {
        if (msHasRESTOperation == null) {
            msHasRESTOperation = new OTObjectPropertyImpl();
            OTObjectProperty clazz = new OTObjectPropertyImpl("hasRESTOperation", NS) {

                @Override
                public ObjectProperty asObjectProperty(OntModel model) {
                    ObjectProperty op = super.asObjectProperty(model);
                    if (msResource == null) {// to avoid StackOverflow
                        op.setInverseOf(resource().asObjectProperty(model));
                        resource().asObjectProperty(model).setInverseOf(op);
                    }
                    return op;
                }
            };
            clazz.getDomain().add(OTClasses.openToxResource());
            clazz.getRange().add(OTRestClasses.restOperation());
            msHasRESTOperation = clazz;
        }
        return msHasRESTOperation;
    }

    public static OTObjectProperty status() {
        if (msStatus == null) {
            OTObjectProperty clazz = new OTObjectPropertyImpl("status", NS);
            clazz.getDomain().add(OTRestClasses.restOperation());
            clazz.getRange().add(OTRestClasses.httpStatus());
            msStatus = clazz;
        }
        return msStatus;
    }

    public static OTObjectProperty uri() {
        if (msUri == null) {
            OTObjectProperty clazz = new OTObjectPropertyImpl("uri", NS);
            clazz.getDomain().add(OTRestClasses.restOperation());
            clazz.getRange().add(OTRestClasses.restTemplate());
            msUri = clazz;
        }
        return msUri;
    }
}
