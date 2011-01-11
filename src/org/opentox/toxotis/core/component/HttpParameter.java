package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.vocabulary.RDF;
import java.util.HashSet;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTRestClasses;
import org.opentox.toxotis.ontology.collection.OTRestDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTRestObjectProperties;

/**
 *
 * An HTTP parameter is a component that defines the parameters expected from the client
 * on a POST request. Every web service through its RDF representation, defined which
 * parameters it expects, whether these are optional or mandatory and provide some
 * information about them.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class HttpParameter extends OTComponent<HttpParameter> {

    private Set<OntologicalClass> paramContent;
    private Set<OntologicalClass> inputParamClass;
    private boolean paramOptional;
    private String paramName;
    private boolean opentoxParameter;

    public HttpParameter() {
        super();
    }

    public HttpParameter(VRI uri) {
        super(uri);
    }

    public boolean isOpentoxParameter() {
        return opentoxParameter;
    }

    public HttpParameter setOpentoxParameter(boolean opentoxParameter) {
        this.opentoxParameter = opentoxParameter;
        return this;
    }

    public Set<OntologicalClass> getParamContent() {
        return paramContent;
    }

    public HttpParameter setParamContent(Set<OntologicalClass> paramContent) {
        this.paramContent = paramContent;
        return this;
    }

    public HttpParameter addParamContent(OntologicalClass... paramContents) {
        if (this.paramContent == null) {
            this.paramContent = new HashSet<OntologicalClass>();
        }
        for (OntologicalClass oc : paramContents) {
            this.paramContent.add(oc);
        }
        return this;
    }

    public Set<OntologicalClass> getInputParamClass() {
        return inputParamClass;
    }

    public HttpParameter setInputParamClass(Set<OntologicalClass> inputParamClass) {
        this.inputParamClass = inputParamClass;
        return this;
    }

    public HttpParameter addInputParamClass(OntologicalClass... inputParamClass) {
        if (getInputParamClass() == null) {
            setInputParamClass(new HashSet<OntologicalClass>());
        }
        for (OntologicalClass oc : inputParamClass) {
            getInputParamClass().add(oc);
        }
        return this;
    }

    public boolean isParamOptional() {
        return paramOptional;
    }

    public HttpParameter setParamOptional(boolean paramOptional) {
        this.paramOptional = paramOptional;
        return this;
    }

    public String getParamName() {
        return paramName;
    }

    public HttpParameter setParamName(String paramName) {
        this.paramName = paramName;
        return this;
    }

    public Individual asIndividual(OntModel model) {
        String httpParameterUri = getUri() != null ? getUri().toString() : null;

        Individual indiv = model.createIndividual(httpParameterUri, OTRestClasses.InputParameter().inModel(model));
        /* Define Input Parameter Types */
        if (getInputParamClass() != null) {
            for (OntologicalClass oc : getInputParamClass()) {
                indiv.addProperty(RDF.type, oc.inModel(model));
            }
        }
        /* Ontological Classes for the Http Parameter */
        if (getOntologicalClasses()!=null){
            for (OntologicalClass oc : getOntologicalClasses()){
                indiv.addRDFType(oc.inModel(model));
            }
        }
        /* Define the REST parameter content */
        if (getParamContent() != null) {

            Property paramContentProperty = null;
            if (isOpentoxParameter()) {
                paramContentProperty = OTRestObjectProperties.paramContentOpenTox().asObjectProperty(model);
            } else {
                paramContentProperty = OTRestObjectProperties.paramContentSimple().asObjectProperty(model);
            }
            for (OntologicalClass oc : getParamContent()) {
                indiv.addProperty(paramContentProperty, oc.inModel(model));
            }
        }
        /* Define whether the parameter is optional or not */
        indiv.addLiteral(OTRestDatatypeProperties.paramOptional().asDatatypeProperty(model),
                model.createTypedLiteral(isParamOptional(), XSDDatatype.XSDboolean));
        /* Name of the parameter */
        if (getParamName() != null) {
            indiv.addLiteral(OTRestDatatypeProperties.paramName().asDatatypeProperty(model),
                    model.createTypedLiteral(getParamName(), XSDDatatype.XSDstring));
        }
        /* Meta information about the HttpParameter */
        if (getMeta() != null) {
            getMeta().attachTo(indiv, model);
        }
        return indiv;
    }

    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
