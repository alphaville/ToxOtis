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
import org.opentox.toxotis.ontology.collection.OTRestClasses;

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

    /**
     * Add HTTP parameters to the current rest operation.
     * 
     * @param httpParameters
     *      Array of HTTP parameters.
     * @return 
     *      The current modifiable instance of IRestOperation with
     *      updated parameters.
     */
    RestOperation addHttpParameters(HttpParameter... httpParameters);

    /**
     * Add supported HTTP status codes to the current rest operation.
     * 
     * @param httpParameters
     *      Array of HTTP status codes.
     * @return 
     *      The current modifiable instance of IRestOperation with
     *      updated HTTP status codes.
     */
    RestOperation addHttpStatusCodes(HttpStatus... httpStatusCodes);

    /**
     * Add REST-related ontological classes.
     * 
     * @param restClasses
     *      Array of ontological classes.
     * 
     * @return 
     *      The current modifiable instance of IRestOperation with
     *      updated ontological classes.
     */
    RestOperation addRestClasses(OntologicalClass... restClasses);

    /**
     * Set of input parameters which are expected by the client on POST. These are
     * either posted as a {@link Media#APPLICATION_FORM_URL_ENCODED form} or included
     * in the URL as URL parameters.
     * 
     * @return
     *      Specifications for the input parameters expected by the client.
     */
    Set<HttpParameter> getHttpParameters();

    /**
     * A set of the status codes that might occur when invoking the service, including
     * all redirection, error and success status codes. As ontological classes, status
     * codes are found in the colleciton {@link OTRestClasses }, and subclass {@link
     * OTRestClasses#HTTPStatus() } such as {@link OTRestClasses#STATUS_200() }.
     *
     * @return
     *      Set of ontological classes that describe the available status codes.
     */
    Set<HttpStatus> getHttpStatusCodes();

    /**
     * The HTTP method which are supported by the web service.
     * @return
     *      Http Method as element of the enumeration {@link MethodsEnum }.
     */
    HttpMethod getMethod();

    /**
     * Return the set of ontological classes that describe the underlying resource and
     * define its REST interface available operations choosing from the collection
     * {@link OTRestClasses }.
     *
     * @return
     *      Set of ontological classes
     */
    Set<OntologicalClass> getRestClasses();

    /**
     * Setter method for the Set of HTTP parameters.
     * @param httpParameters
     *      A set of HTTP parameters.
     * @return 
     *      The current modifiable instance of IRestOperation with
     *      updated set of HTTP parameters.
     */
    RestOperation setHttpParameters(Set<HttpParameter> httpParameters);

    /**
     * Setter method for the possible HTTP status codes returned by
     * the current REST method.
     * 
     * @param httpStatusCodes
     *      Set of HTTP status codes.
     * 
     * @return 
     *      The current modifiable instance of IRestOperation with
     *      updated set of HTTP status codes.
     */
    RestOperation setHttpStatusCodes(Set<HttpStatus> httpStatusCodes);

    /**
     * Setter method for the HTTP method definition for this REST operation.
     * 
     * @param httpMethod
     *      A HTTP method.
     * @return 
     *      The current modifiable instance of IRestOperation with
     *      updated HTTP method definition.
     */
    RestOperation setMethod(HttpMethod httpMethod);

    /**
     * 
     * @param httpMethod
     * @return 
     *      The current modifiable instance of IRestOperation with
     *      updated HTTP method.
     * @see #setMethod(org.opentox.toxotis.core.component.HttpMethod) #setMethod(HttpMethod) 
     */
    RestOperation setMethod(MethodsEnum httpMethod);

    /**
     * Setter method for the set of Ontological Classes describing this
     * REST operation.
     * 
     * @param restClasses
     *      Set of ontological classes.
     * 
     * @return 
     *      The current modifiable instance of IRestOperation with
     *      updated set of REST classes.
     * 
     * @see OTRestClasses
     */
    RestOperation setRestClasses(Set<OntologicalClass> restClasses);

    /**
     * Get the set of supported MIMEs by the current REST operation.
     * 
     * @return 
     *      Set of supported media types.
     */
    Set<HttpMediatype> getMediaTypes();

    /**
     * Setter method for the Set of supported media-types.
     * 
     * @param mediaTypes
     *      Set of HttpMediaType objects.
     * 
     * @return 
     *      The current modifiable instance of IRestOperation with
     *      updated set of MIMEs.
     */
    IRestOperation setMediaTypes(Set<HttpMediatype> mediaTypes);

    /**
     * Add MIME definitions to the current REST operation.
     * 
     * @param mediaTypes
     *      Array of HttpMediaType objects.
     * 
     * @return 
     *      The current modifiable instance of IRestOperation with
     *      updated set of MIMEs.
     */
    IRestOperation addMediaTypes(HttpMediatype... mediaTypes);

    /**
     * Add a URL parameter to this REST operation.
     * @param urlParameterName
     *      Name of the URL parameter.
     * @param optional
     *      Whether the URL parameter is optional (<code>true</code>) 
     *      or mandatory (<code>false</code>).
     * @param type
     *      The XSD data-type of the URL parameter.
     * @return 
     *      The current modifiable instance of IRestOperation with
     *      updated URL parameter definitions.
     */
    IRestOperation addUrlParameter(String urlParameterName, boolean optional, XSDDatatype type);

    /**
     * Add a URL parameter to this REST operation.
     * @param urlParameterName
     *      Name of the URL parameter.
     * @param optional
     *      Whether the URL parameter is optional (<code>true</code>) 
     *      or mandatory (<code>false</code>).
     * @param type
     *      The XSD data-type of the URL parameter.
     * @param meta 
     *      Meta-information about the URL parameter being added.
     * @return 
     *      The current modifiable instance of IRestOperation with
     *      updated URL parameter definitions.
     */
    IRestOperation addUrlParameter(String urlParameterName, boolean optional, XSDDatatype type, MetaInfo meta);

    /**
     * Add a header to the current REST operation.
     * 
     * @param headerName
     *      Name of the header.
     * @param optional
     *      Whether the HTTP header is optional (<code>true</code>) 
     *      or mandatory (<code>false</code>).
     * @param xsdType
     *      The XSD data-type definition of the current header being added.
     * 
     * @return 
     *      The current modifiable instance of IRestOperation with
     *      updated list of Headers.
     */
    IRestOperation addSimpleHeader(String headerName, boolean optional, XSDDatatype xsdType);

    /**
     * Add a header to the current REST operation.
     * 
     * @param headerName
     *      Name of the header.
     * @param optional
     *      Whether the HTTP header is optional (<code>true</code>) 
     *      or mandatory (<code>false</code>).
     * @param xsdType
     *      The XSD data-type definition of the current header being added.
     * @param meta
     *      Meta-information about the header.
     * 
     * @return 
     *      The current modifiable instance of IRestOperation with
     *      updated list of Headers.
     */
    IRestOperation addSimpleHeader(String headerName, boolean optional, XSDDatatype xsdType, MetaInfo meta);

    /**
     * Specify whether the resource is protected by the OpenTox SSO 
     * infrastructure.
     * 
     * @param protectedResource
     *      Whether the resource is protected.
     * @return 
     *      The current modifiable instance of IRestOperation with
     *      updated information about whether the resource is protected.
     */
    IRestOperation setProtectedResource(boolean protectedResource);
}
