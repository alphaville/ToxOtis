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
    private static OTObjectProperty ms_aa;
    private static OTObjectProperty ms_hasHTTPMethod;
    private static OTObjectProperty ms_hasHTTPStatus;
    private static OTObjectProperty ms_hasMedia;
    private static OTObjectProperty ms_inputParam;
    private static OTObjectProperty ms_paramContent;
    private static OTObjectProperty ms_paramContentOpenTox;
    private static OTObjectProperty ms_paramContentSimple;
    private static OTObjectProperty ms_resource;
    private static OTObjectProperty ms_hasRESTOperation;
    private static OTObjectProperty ms_result;
    private static OTObjectProperty ms_status;
    private static OTObjectProperty ms_uri;

    public static OTObjectProperty hasHTTPMethod() {
        if (ms_hasHTTPMethod == null) {
            OTObjectProperty clazz = new OTObjectPropertyImpl("hasHTTPMethod", NS);
            clazz.getDomain().add(OTRestClasses.RESTOperation());
            clazz.getRange().add(OTRestClasses.HTTPMethod());
            ms_hasHTTPMethod = clazz;
        }
        return ms_hasHTTPMethod;
    }

    public static OTObjectProperty hasHTTPStatus() {
        if (ms_hasHTTPStatus == null) {
            OTObjectProperty clazz = new OTObjectPropertyImpl("hasHTTPStatus", NS);
            clazz.getDomain().add(OTRestClasses.RESTOperation());
            clazz.getRange().add(OTRestClasses.HTTPStatus());
            ms_hasHTTPStatus = clazz;
        }
        return ms_hasHTTPStatus;
    }

    public static OTObjectProperty hasMedia() {
        if (ms_hasMedia == null) {
            OTObjectProperty clazz = new OTObjectPropertyImpl("hasMedia", NS);
            clazz.getDomain().add(OTRestClasses.RESTOperation());
            clazz.getRange().add(OTRestClasses.MediaType());
            ms_hasMedia = clazz;
        }
        return ms_hasMedia;
    }

    public static OTObjectProperty aa() {
        if (ms_aa == null) {
            OTObjectProperty clazz = new OTObjectPropertyImpl("aa", NS);
            clazz.getDomain().add(OTRestClasses.RESTOperation());
            clazz.getRange().add(OTRestClasses.AA());
            ms_aa = clazz;
        }
        return ms_aa;
    }

    public static OTObjectProperty inputParam() {
        if (ms_inputParam == null) {
            OTObjectProperty clazz = new OTObjectPropertyImpl("inputParam", NS);
            clazz.getDomain().add(OTRestClasses.RESTOperation());
            clazz.getDomain().add(OTRestClasses.AA());
            clazz.getRange().add(OTRestClasses.InputParameter());
            ms_inputParam = clazz;
        }
        return ms_inputParam;
    }

    public static OTObjectProperty result() {
        if (ms_result == null) {
            OTObjectProperty clazz = new OTObjectPropertyImpl("result", NS);
            clazz.getDomain().add(OTRestClasses.RESTOperation());
            clazz.getRange().add(OTClasses.OpenToxResource());
            ms_result = clazz;
        }
        return ms_result;
    }

    public static OTObjectProperty paramContent() {
        if (ms_paramContent == null) {
            OTObjectProperty clazz = new OTObjectPropertyImpl("paramContent", NS);
            clazz.getDomain().add(OTRestClasses.InputParameter());
            ms_paramContent = clazz;
        }
        return ms_paramContent;
    }

    public static OTObjectProperty paramContentOpenTox() {
        if (ms_paramContentOpenTox == null) {
            OTObjectProperty clazz = new OTObjectPropertyImpl("paramContentOpenTox", NS);
            clazz.getDomain().add(OTRestClasses.InputParameter());
            clazz.getDomain().add(OTClasses.OpenToxResource());
            ms_paramContentOpenTox = clazz;
        }
        return ms_paramContentOpenTox;
    }

    public static OTObjectProperty paramContentSimple() {
        if (ms_paramContentSimple == null) {
            OTObjectProperty clazz = new OTObjectPropertyImpl("paramContentSimple", NS);
            clazz.getDomain().add(OTRestClasses.InputParameter());
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
            ms_paramContentSimple = clazz;
        }
        return ms_paramContentSimple;
    }

    public static OTObjectProperty resource() {
        if (ms_resource == null) {
            ms_resource = new OTObjectPropertyImpl();
            OTObjectProperty clazz = new OTObjectPropertyImpl("resource", NS) {

                @Override
                public ObjectProperty asObjectProperty(OntModel model) {
                    ObjectProperty op = super.asObjectProperty(model);
                    if (ms_hasRESTOperation == null) {// to avoid StackOverflow
                        op.setInverseOf(hasRESTOperation().asObjectProperty(model));
                        hasRESTOperation().asObjectProperty(model).setInverseOf(op);
                    }
                    return op;
                }
            };
            clazz.getDomain().add(OTRestClasses.RESTOperation());
            clazz.getRange().add(OTClasses.OpenToxResource());
            ms_resource = clazz;
        }
        return ms_resource;
    }

    public static OTObjectProperty hasRESTOperation() {
        if (ms_hasRESTOperation == null) {
            ms_hasRESTOperation = new OTObjectPropertyImpl();
            OTObjectProperty clazz = new OTObjectPropertyImpl("hasRESTOperation", NS) {

                @Override
                public ObjectProperty asObjectProperty(OntModel model) {
                    ObjectProperty op = super.asObjectProperty(model);
                    if (ms_resource == null) {// to avoid StackOverflow
                        op.setInverseOf(resource().asObjectProperty(model));
                        resource().asObjectProperty(model).setInverseOf(op);
                    }
                    return op;
                }
            };
            clazz.getDomain().add(OTClasses.OpenToxResource());
            clazz.getRange().add(OTRestClasses.RESTOperation());
            ms_hasRESTOperation = clazz;
        }
        return ms_hasRESTOperation;
    }

    public static OTObjectProperty status() {
        if (ms_status == null) {
            OTObjectProperty clazz = new OTObjectPropertyImpl("status", NS);
            clazz.getDomain().add(OTRestClasses.RESTOperation());
            clazz.getRange().add(OTRestClasses.HTTPStatus());
            ms_status = clazz;
        }
        return ms_status;
    }

    public static OTObjectProperty uri() {
        if (ms_uri == null) {
            OTObjectProperty clazz = new OTObjectPropertyImpl("uri", NS);
            clazz.getDomain().add(OTRestClasses.RESTOperation());
            clazz.getRange().add(OTRestClasses.RESTTemplate());
            ms_uri = clazz;
        }
        return ms_uri;
    }
}
