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

import java.net.URISyntaxException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.ResourceValue;

/**
 * Collection of HTTP methods as ontological individuals, instances of {@link OTRestClasses#httpMethod()  }
 * used to describe the available methods by a web service. These instances are related to the
 * REST interface self-definition of a web service which is available on GET from its
 * RDF representation.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class HttpMethods {
    
    private HttpMethods(){
        // Hidden Constructor. 
        // This is a utility class.
    }

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(HttpMethods.class);
    private static final VRI POST_VRI, GET_VRI, PUT_VRI, DELETE_VRI, OPTIONS_VRI, HEAD_VRI;
    public static final ResourceValue GET, POST, PUT, DELETE, HEAD, OPTIONS;

    static {
        try {
            POST_VRI = new VRI(OTRestClasses.NS + "POST");
            GET_VRI = new VRI(OTRestClasses.NS + "GET");
            PUT_VRI = new VRI(OTRestClasses.NS + "PUT");
            DELETE_VRI = new VRI(OTRestClasses.NS + "DELETE");
            HEAD_VRI = new VRI(OTRestClasses.NS + "HEAD");
            OPTIONS_VRI = new VRI(OTRestClasses.NS + "OPTIONS");
            GET = new ResourceValue(GET_VRI, OTRestClasses.httpMethod());
            POST = new ResourceValue(POST_VRI, OTRestClasses.httpMethod());
            PUT = new ResourceValue(PUT_VRI, OTRestClasses.httpMethod());
            DELETE = new ResourceValue(DELETE_VRI, OTRestClasses.httpMethod());
            HEAD = new ResourceValue(HEAD_VRI, OTRestClasses.httpMethod());
            OPTIONS = new ResourceValue(OPTIONS_VRI, OTRestClasses.httpMethod());
        } catch (URISyntaxException ex) {
            logger.error(null, ex);
            throw new IllegalArgumentException("Programming Error!");
        }
    }

    public enum MethodsEnum {

        GET(HttpMethods.GET),
        POST(HttpMethods.POST),
        PUT(HttpMethods.PUT),
        DELETE(HttpMethods.DELETE),
        HEAD(HttpMethods.HEAD),
        OPTIONS(HttpMethods.OPTIONS);
        private ResourceValue rv;

        public ResourceValue getResourceValue() {
            return rv;
        }

        private MethodsEnum(ResourceValue rv) {
            this.rv = rv;
        }
    }
}
