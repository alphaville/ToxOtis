package org.opentox.toxotis.core;

import java.util.HashSet;
import java.util.Set;
import org.opentox.toxotis.core.component.HttpParameter;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.HttpMethods.MethodsEnum;
import org.opentox.toxotis.ontology.collection.OTRestClasses;

/**
 * Interface documentation for a web service. OpenTox web services define the supported
 * operations that are available and at the same time provide machine-readable directives
 * for their consumption which can be used to generate human-readable documentation too.
 * Allowed methods, 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ServiceDefinition {

    /**
     * Dummy constructor for the class {@link ServiceDefinition } that creates an empty
     * instance of it. The methods {@link #getMethodsSupported() } and {@link #getRestClasses() }
     * will return <code>null</code> if invoked right afterwards.
     */
    public ServiceDefinition() {
    }

    

    private Set<OntologicalClass> restClasses;
    private Set<MethodsEnum> methodsSupported;
    private Set<HttpParameter> httpParameters;
    private Set<OntologicalClass> httpStatusCodes;

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

    public ServiceDefinition setRestClasses(Set<OntologicalClass> restClasses) {
        this.restClasses = restClasses;
        return this;
    }

    public ServiceDefinition addRestClasses(OntologicalClass... restClasses) {
        if (this.restClasses == null) {
            this.restClasses = new HashSet<OntologicalClass>();
        }
        for (OntologicalClass oc : restClasses) {
            this.restClasses.add(oc);
        }
        return this;
    }

    public Set<MethodsEnum> getMethodsSupported() {
        return methodsSupported;
    }

    public ServiceDefinition setMethodsSupported(Set<MethodsEnum> methodsSupported) {
        this.methodsSupported = methodsSupported;
        return this;
    }

    public ServiceDefinition addMethodsSupported(MethodsEnum... methods) {
        if (this.methodsSupported == null) {
            methodsSupported = new HashSet<MethodsEnum>();
        }
        for (MethodsEnum me : methods) {
            methodsSupported.add(me);
        }
        return this;
    }

    public Set<HttpParameter> getHttpParameters() {
        return httpParameters;
    }

    public void setHttpParameters(Set<HttpParameter> httpParameters) {
        this.httpParameters = httpParameters;
    }

    public Set<OntologicalClass> getHttpStatusCodes() {
        return httpStatusCodes;
    }

    public void setHttpStatusCodes(Set<OntologicalClass> httpStatusCodes) {
        this.httpStatusCodes = httpStatusCodes;
    }
    
}
