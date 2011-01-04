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
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTRestClasses;
import org.opentox.toxotis.ontology.collection.OTRestDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTRestObjectProperties;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class HttpParameter extends OTComponent<HttpParameter> {

    private Set<OntologicalClass> paramContent;
    private Set<OntologicalClass> inputParamClass;
    private boolean paramOptional;
    private String paramName;

    public HttpParameter() {
    }

    public Set<OntologicalClass> getParamContent() {
        return paramContent;
    }

    public void setParamContent(Set<OntologicalClass> paramContent) {
        this.paramContent = paramContent;
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

    public void setInputParamClass(Set<OntologicalClass> inputParamClass) {
        this.inputParamClass = inputParamClass;
    }

    public boolean isParamOptional() {
        return paramOptional;
    }

    public void setParamOptional(boolean paramOptional) {
        this.paramOptional = paramOptional;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }
    

    public Individual asIndividual(OntModel model) {
        String httpParameterUri = getUri() != null ? getUri().toString() : null;

        Individual indiv = model.createIndividual(httpParameterUri, OTRestClasses.InputParameter().inModel(model));
        /* Define Types */
        if (getInputParamClass() != null) {
            for (OntologicalClass oc : getInputParamClass()) {
                indiv.addProperty(RDF.type, oc.inModel(model));
            }
        }
        /* Define the REST parameter content */
        if (getParamContent() != null) {
            Property paramContentProperty = OTRestObjectProperties.paramContent().asObjectProperty(model);
            for (OntologicalClass oc : getParamContent()) {
                indiv.addProperty(paramContentProperty, oc.inModel(model));
            }
        }
        /* Define whether the parameter is optional or not */
        indiv.addLiteral(OTRestDatatypeProperties.paramOptional().asDatatypeProperty(model),
                model.createTypedLiteral(isParamOptional(), XSDDatatype.XSDboolean));
        /* Name of the parameter */
        if (getParamName()!=null){
            indiv.addLiteral(OTRestDatatypeProperties.paramName().asDatatypeProperty(model), model.createTypedLiteral(getParamName(), XSDDatatype.XSDstring));
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
