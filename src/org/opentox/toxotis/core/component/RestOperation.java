package org.opentox.toxotis.core.component;

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
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.HttpMethods.MethodsEnum;
import org.opentox.toxotis.ontology.collection.OTRestClasses;
import org.opentox.toxotis.ontology.collection.OTRestObjectProperties;

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
     * The HTTP method which are supported by the web service.
     * @return
     *      Http Method as element of the enumeration {@link MethodsEnum }.
     */
    public HttpMethod getMethod() {
        return method;
    }

    public RestOperation setMethod(HttpMethod httpMethod) {
        this.method = httpMethod;
        return this;
    }

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
    public Set<HttpParameter> getHttpParameters() {
        return httpParameters;
    }

    public RestOperation setHttpParameters(Set<HttpParameter> httpParameters) {
        this.httpParameters = httpParameters;
        return this;
    }

    public RestOperation addHttpParameters(HttpParameter... httpParameters) {
        if (getHttpParameters() == null) {
            setHttpParameters(new HashSet<HttpParameter>());
        }
        for (HttpParameter prm : httpParameters) {
            getHttpParameters().add(prm);
        }
        return this;
    }

    public Set<HttpMediatype> getMediaTypes() {
        return mediaTypes;
    }

    public IRestOperation setMediaTypes(Set<HttpMediatype> mediaTypes) {
        this.mediaTypes = mediaTypes;
        return this;
    }

    public IRestOperation addMediaTypes(HttpMediatype... mediaTypes) {
        if (getMediaTypes()==null){
            setMediaTypes(new HashSet<HttpMediatype>(mediaTypes.length));
        }
        for (HttpMediatype media : mediaTypes){
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
        if (getMediaTypes()!=null){
            Property hasMediaTypeProperty = OTRestObjectProperties.hasMedia().asObjectProperty(model);
            for (HttpMediatype media : getMediaTypes()){
                indiv.addProperty(hasMediaTypeProperty, media.asIndividual(model));
            }
        }
        /* Attach meta data */
        if (getMeta() != null) {
            getMeta().attachTo(indiv, model);
        }
        return indiv;
    }

    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
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

    
}
