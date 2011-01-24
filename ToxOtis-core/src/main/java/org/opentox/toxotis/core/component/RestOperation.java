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
package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.IRestOperation;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import java.util.HashSet;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.HttpMethods.MethodsEnum;
import org.opentox.toxotis.ontology.collection.OTRestClasses;
import org.opentox.toxotis.ontology.collection.OTRestObjectProperties;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;

/**
 * An implementation of {@link IRestOperation }
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class RestOperation extends OTComponent<RestOperation> implements IRestOperation {

    /**
     * Dummy constructor for the class {@link RestOperation } that creates an empty
     * instance of it. The methods {@link #getMethod() } and {@link #getRestClasses() }
     * will return <code>null</code> if invoked right afterwards.
     */
    public RestOperation() {
        super();
        addOntologicalClasses(OTRestClasses.RESTOperation());
    }

    public RestOperation(VRI uri) {
        super(uri);
        addOntologicalClasses(OTRestClasses.RESTOperation());
    }
    private Set<OntologicalClass> restClasses;
    private HttpMethod method;
    private Set<HttpParameter> httpParameters;
    private Set<HttpStatus> httpStatusCodes;
    private Set<HttpMediatype> mediaTypes;

    /**
     * Return the set of ontological classes that describe the underlying resource and
     * define its REST interface available operations choosing from the collection
     * {@link OTRestClasses }.
     * 
     * @return
     *      Set of ontological classes
     */
    @Override
    public Set<OntologicalClass> getRestClasses() {
        return restClasses;
    }

    @Override
    public RestOperation setRestClasses(Set<OntologicalClass> restClasses) {
        this.restClasses = restClasses;
        return this;
    }

    @Override
    public RestOperation addRestClasses(OntologicalClass... restClasses) {
        if (this.restClasses == null) {
            this.restClasses = new HashSet<OntologicalClass>();
        }
        for (OntologicalClass oc : restClasses) {
            this.restClasses.add(oc);
        }
        return this;
    }

    /**
     * The HTTP method which are supported by the web service.
     * @return
     *      Http Method as element of the enumeration {@link MethodsEnum }.
     */
    @Override
    public HttpMethod getMethod() {
        return method;
    }

    /**
     *      Update the REST Operation automatic documentation setting the corresponding
     *      method to which the specifications refer.
     * @param httpMethod
     *      HTTP Method object
     * @return
     *      The updated modifiable instance of REST operation documentation.
     */
    @Override
    public RestOperation setMethod(HttpMethod httpMethod) {
        this.method = httpMethod;
        return this;
    }

    @Override
    public RestOperation setMethod(MethodsEnum httpMethod) {
        this.method = new HttpMethod(httpMethod);
        return this;
    }

    /**
     * Set of input parameters which are expected by the client on POST. These are
     * either posted as a {@link Media#APPLICATION_FORM_URL_ENCODED form} or included
     * in the URL as URL parameters.
     * @return
     *      Specifications for the input parameters expected by the client.
     */
    @Override
    public Set<HttpParameter> getHttpParameters() {
        return httpParameters;
    }

    @Override
    public RestOperation setHttpParameters(Set<HttpParameter> httpParameters) {
        this.httpParameters = httpParameters;
        return this;
    }

    @Override
    public RestOperation addHttpParameters(HttpParameter... httpParameters) {
        if (getHttpParameters() == null) {
            setHttpParameters(new HashSet<HttpParameter>());
        }
        for (HttpParameter prm : httpParameters) {
            getHttpParameters().add(prm);
        }
        return this;
    }

    @Override
    public Set<HttpMediatype> getMediaTypes() {
        return mediaTypes;
    }

    @Override
    public IRestOperation setMediaTypes(Set<HttpMediatype> mediaTypes) {
        this.mediaTypes = mediaTypes;
        return this;
    }

    @Override
    public IRestOperation addMediaTypes(HttpMediatype... mediaTypes) {
        if (getMediaTypes() == null) {
            setMediaTypes(new HashSet<HttpMediatype>(mediaTypes.length));
        }
        for (HttpMediatype media : mediaTypes) {
            getMediaTypes().add(media);
        }
        return this;
    }

    /**
     * A set of the status codes that might occur when invoking the service, including
     * all redirection, error and success status codes. As ontological classes, status
     * codes are found in the colleciton {@link OTRestClasses }, and subclass {@link
     * OTRestClasses#HTTPStatus() } such as {@link OTRestClasses#STATUS_200() }.
     *
     * @return
     *      Set of ontological classes that describe the available status codes.
     */
    @Override
    public Set<HttpStatus> getHttpStatusCodes() {
        return httpStatusCodes;
    }

    @Override
    public RestOperation setHttpStatusCodes(Set<HttpStatus> httpStatusCodes) {
        this.httpStatusCodes = httpStatusCodes;
        return this;
    }

    @Override
    public RestOperation addHttpStatusCodes(HttpStatus... httpStatusCodes) {
        if (getHttpStatusCodes() == null) {
            setHttpStatusCodes(new HashSet<HttpStatus>());
        }
        for (HttpStatus status : httpStatusCodes) {
            getHttpStatusCodes().add(status);
        }
        return this;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        String restOperationUri = getUri() != null ? getUri().toString() : null;
        /* Initialization of an otrs:RESTOperation individual */
        Individual indiv = model.createIndividual(restOperationUri, OTRestClasses.RESTOperation().inModel(model));
        /* Ontological Classes that define the type of the REST operation */
        if (getRestClasses() != null) {
            for (OntologicalClass oc : getRestClasses()) {
                indiv.addProperty(RDF.type, oc.inModel(model));
            }
        }
        /* Input Parameters for the Rest Operation*/
        if (getHttpParameters() != null) {
            Property inputParam = OTRestObjectProperties.inputParam().asObjectProperty(model);
            for (HttpParameter prm : getHttpParameters()) {
                indiv.addProperty(inputParam, prm.asIndividual(model));
            }
        }
        /* Corresponding HTTP Method */
        if (getMethod() != null) {
            Resource methodResource = getMethod().asIndividual(model);
            indiv.addProperty(OTRestObjectProperties.hasHTTPMethod().asObjectProperty(model), methodResource);
        }
        /* Status Codes */
        if (getHttpStatusCodes() != null) {
            Property hasHttpStatusProperty = OTRestObjectProperties.hasHTTPStatus().asObjectProperty(model);
            for (HttpStatus status : getHttpStatusCodes()) {
                indiv.addProperty(hasHttpStatusProperty, status.asIndividual(model));
            }
        }
        /* Media Types Supported */
        if (getMediaTypes() != null) {
            Property hasMediaTypeProperty = OTRestObjectProperties.hasMedia().asObjectProperty(model);
            for (HttpMediatype media : getMediaTypes()) {
                indiv.addProperty(hasMediaTypeProperty, media.asIndividual(model));
            }
        }
        /* Attach meta data */
        if (getMeta() != null) {
            getMeta().attachTo(indiv, model);
        }
        return indiv;
    }

    @Override
    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IRestOperation addUrlParameter(String urlParameterName, boolean optional, XSDDatatype type) {
        return addUrlParameter(urlParameterName, optional, type, new MetaInfoImpl());
    }

    @Override
    public IRestOperation addSimpleHeader(String headerName, boolean optional, XSDDatatype xsdType) {
        return addSimpleHeader(headerName, optional, xsdType, new MetaInfoImpl());
    }

    @Override
    public IRestOperation setProtectedResource(boolean protectedResource) {
        if (getOntologicalClasses() == null) {
            setOntologicalClasses(new HashSet<OntologicalClass>());
        }
        if (protectedResource) {
            getOntologicalClasses().add(OTRestClasses.AA());
        } else {
            getOntologicalClasses().remove(OTRestClasses.AA());
        }
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RestOperation other = (RestOperation) obj;
        if (this.restClasses != other.restClasses && (this.restClasses == null || !this.restClasses.equals(other.restClasses))) {
            return false;
        }
        if (this.method != other.method && (this.method == null || !this.method.equals(other.method))) {
            return false;
        }
        if (this.httpParameters != other.httpParameters && (this.httpParameters == null || !this.httpParameters.equals(other.httpParameters))) {
            return false;
        }
        if (this.httpStatusCodes != other.httpStatusCodes && (this.httpStatusCodes == null || !this.httpStatusCodes.equals(other.httpStatusCodes))) {
            return false;
        }
        if (this.mediaTypes != other.mediaTypes && (this.mediaTypes == null || !this.mediaTypes.equals(other.mediaTypes))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.restClasses != null ? this.restClasses.hashCode() : 0);
        hash = 59 * hash + (this.method != null ? this.method.hashCode() : 0);
        hash = 59 * hash + (this.httpParameters != null ? this.httpParameters.hashCode() : 0);
        hash = 59 * hash + (this.httpStatusCodes != null ? this.httpStatusCodes.hashCode() : 0);
        hash = 59 * hash + (this.mediaTypes != null ? this.mediaTypes.hashCode() : 0);
        return hash;
    }

    @Override
    public IRestOperation addUrlParameter(String urlParameterName, boolean optional, XSDDatatype type, MetaInfo meta) {
        if (getHttpParameters() == null) {
            setHttpParameters(new HashSet<HttpParameter>());
        }
        HttpParameter httpParam = new HttpParameter().setOntologicalClasses(new HashSet<OntologicalClass>()).
                addOntologicalClasses(OTRestClasses.InputParameterSimple(), OTRestClasses.URLParameter());
        httpParam.setParamName(urlParameterName);
        httpParam.setOpentoxParameter(false);
        httpParam.setParamOptional(optional);
        httpParam.setMeta(meta);
        getHttpParameters().add(httpParam);
        return this;
    }

    @Override
    public IRestOperation addSimpleHeader(String headerName, boolean optional, XSDDatatype xsdType, MetaInfo meta) {
        if (getHttpParameters() == null) {
            setHttpParameters(new HashSet<HttpParameter>());
        }
        HttpParameter httpParam = new HttpParameter().setOntologicalClasses(new HashSet<OntologicalClass>()).
                addOntologicalClasses(OTRestClasses.InputParameterSimple(), OTRestClasses.Header());
        httpParam.setParamName(headerName);
        httpParam.setOpentoxParameter(false);
        httpParam.setParamOptional(optional);
        httpParam.setMeta(meta);
        getHttpParameters().add(httpParam);
        return this;
    }
}
