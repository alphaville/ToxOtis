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
package org.opentox.toxotis.core;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import java.util.Set;
import org.opentox.toxotis.core.component.HttpMediatype;
import org.opentox.toxotis.core.component.HttpMethod;
import org.opentox.toxotis.core.component.HttpParameter;
import org.opentox.toxotis.core.component.HttpStatus;
import org.opentox.toxotis.core.component.RestOperation;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.HttpMethods.MethodsEnum;

/**
 * Interface documentation for a web service. OpenTox web services define the supported
 * operations that are available and at the same time provide machine-readable directives
 * for their consumption which can be used to generate human-readable documentation too.
 * Corresponding HTTP method, allowed (mandatory and optional) input parameters for the
 * service and information about them as well as possible status codes that might be thrown
 * by the service are included in the definition of the web service.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface IRestOperation extends IOTComponent {

    RestOperation addHttpParameters(HttpParameter... httpParameters);

    RestOperation addHttpStatusCodes(HttpStatus... httpStatusCodes);

    RestOperation addRestClasses(OntologicalClass... restClasses);

    /**
     * Set of input parameters which are expected by the client on POST. These are
     * either posted as a {@link Media#APPLICATION_FORM_URL_ENCODED form} or included
     * in the URL as URL parameters.
     * @return
     * Specifications for the input parameters expected by the client.
     */
    Set<HttpParameter> getHttpParameters();

    /**
     * A set of the status codes that might occur when invoking the service, including
     * all redirection, error and success status codes. As ontological classes, status
     * codes are found in the colleciton {@link OTRestClasses }, and subclass {@link
     * OTRestClasses#HTTPStatus() } such as {@link OTRestClasses#STATUS_200() }.
     *
     * @return
     * Set of ontological classes that describe the available status codes.
     */
    Set<HttpStatus> getHttpStatusCodes();

    /**
     * The HTTP method which are supported by the web service.
     * @return
     * Http Method as element of the enumeration {@link MethodsEnum }.
     */
    HttpMethod getMethod();

    /**
     * Return the set of ontological classes that describe the underlying resource and
     * define its REST interface available operations choosing from the collection
     * {@link OTRestClasses }.
     *
     * @return
     * Set of ontological classes
     */
    Set<OntologicalClass> getRestClasses();

    RestOperation setHttpParameters(Set<HttpParameter> httpParameters);

    RestOperation setHttpStatusCodes(Set<HttpStatus> httpStatusCodes);

    RestOperation setMethod(HttpMethod httpMethod);

    RestOperation setMethod(MethodsEnum httpMethod);

    RestOperation setRestClasses(Set<OntologicalClass> restClasses);

    Set<HttpMediatype> getMediaTypes();

    IRestOperation setMediaTypes(Set<HttpMediatype> mediaTypes);

    IRestOperation addMediaTypes(HttpMediatype... mediaTypes);

    IRestOperation addUrlParameter(String urlParameterName, boolean optional, XSDDatatype type);

    IRestOperation addUrlParameter(String urlParameterName, boolean optional, XSDDatatype type, MetaInfo meta);

    IRestOperation addSimpleHeader(String headerName, boolean optional, XSDDatatype xsdType);

    IRestOperation addSimpleHeader(String headerName, boolean optional, XSDDatatype xsdType, MetaInfo meta);

    IRestOperation setProtectedResource(boolean protectedResource);
}
