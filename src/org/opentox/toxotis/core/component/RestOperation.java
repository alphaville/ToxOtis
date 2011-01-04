package org.opentox.toxotis.core.component;

import java.util.HashSet;
import java.util.Set;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.HttpMethods.MethodsEnum;
import org.opentox.toxotis.ontology.collection.OTRestClasses;

/**
 * Interface documentation for a web service. OpenTox web services define the supported
 * operations that are available and at the same time provide machine-readable directives
 * for their consumption which can be used to generate human-readable documentation too.
 * Supported HTTP methods, allowed (mandatory and optional) input parameters for the
 * service and information about them as well as possible status codes that might be thrown
 * by the service are included in the definition of the web service.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class RestOperation {

    /**
     * Dummy constructor for the class {@link RestOperation } that creates an empty
     * instance of it. The methods {@link #getMethodsSupported() } and {@link #getRestClasses() }
     * will return <code>null</code> if invoked right afterwards.
     */
    public RestOperation() {
    }
    private Set<OntologicalClass> restClasses;
    private Set<MethodsEnum> methodsSupported;
    private Set<HttpParameter> httpParameters;
    private Set<HttpStatus> httpStatusCodes;

    /**
     * Return the set of ontological classes that describe the underlying resource and
     * define its REST interface available operations choosing from the collection
     * {@link OTRestClasses }.
     * 
     * @return
     *      Set of ontological classes
     */
    public Set<OntologicalClass> getRestClasses() {
        return restClasses;
    }

    public RestOperation setRestClasses(Set<OntologicalClass> restClasses) {
        this.restClasses = restClasses;
        return this;
    }

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
     * The set of HTTP methods which are supported by the web service.
     * @return
     *      A set of Http Methods as elements of the enumeration {@link MethodsEnum }.
     */
    public Set<MethodsEnum> getMethodsSupported() {
        return methodsSupported;
    }

    public RestOperation setMethodsSupported(Set<MethodsEnum> methodsSupported) {
        this.methodsSupported = methodsSupported;
        return this;
    }

    public RestOperation addMethodsSupported(MethodsEnum... methods) {
        if (getMethodsSupported() == null) {
            setMethodsSupported(new HashSet<MethodsEnum>());
        }
        for (MethodsEnum me : methods) {
            getMethodsSupported().add(me);
        }
        return this;
    }

    /**
     * Set of input parameters which are expected by the client on POST. These are
     * either posted as a {@link Media#APPLICATION_FORM_URL_ENCODED form} or included
     * in the URL as URL parameters.
     * @return
     *      Specifications for the input parameters expected by the client.
     */
    public Set<HttpParameter> getHttpParameters() {
        return httpParameters;
    }

    public RestOperation setHttpParameters(Set<HttpParameter> httpParameters) {
        this.httpParameters = httpParameters;
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
    public Set<HttpStatus> getHttpStatusCodes() {
        return httpStatusCodes;
    }

    public RestOperation setHttpStatusCodes(Set<HttpStatus> httpStatusCodes) {
        this.httpStatusCodes = httpStatusCodes;
        return this;
    }

    public RestOperation addHttpStatusCodes(HttpStatus... httpStatusCodes) {
        if (getHttpStatusCodes() == null) {
            setHttpStatusCodes(new HashSet<HttpStatus>());
        }
        for (HttpStatus status : httpStatusCodes) {
            getHttpStatusCodes().add(status);
        }
        return this;
    }
}
