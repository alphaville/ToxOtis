package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import java.util.HashSet;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTRestClasses;
import org.opentox.toxotis.ontology.collection.OTRestObjectProperties;

/**
 * Definition of the actions that the client should take, in term of parameters that
 * should be provided for Authentication and/or Authorization. Optional parameter that
 * specify the user's preferences regarding possible created policies are also listed
 * in the specifications in here.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
@Deprecated
public class AASpecifications extends OTComponent<AASpecifications> {

    private Set<HttpParameter> authParameters;

    public AASpecifications(VRI uri) {
        super(uri);
    }

    public AASpecifications() {
    }

    public Set<HttpParameter> getAuthParameters() {
        return authParameters;
    }

    public AASpecifications addSimpleHeaderParameter(String headerName, boolean optional, XSDDatatype xsdType) {
        if (getAuthParameters() == null) {
            setAuthParameters(new HashSet<HttpParameter>());
        }
        HttpParameter httpParam = new HttpParameter().setOntologicalClasses(new HashSet<OntologicalClass>()).
                addOntologicalClasses(OTRestClasses.InputParameterSimple(), OTRestClasses.Header());
        httpParam.setOpentoxParameter(false);
        httpParam.setParamOptional(optional);
        getAuthParameters().add(httpParam);
        return this;
    }

    public AASpecifications setAuthParameters(Set<HttpParameter> authParameters) {
        this.authParameters = authParameters;
        return this;
    }

    public AASpecifications addAuthParameters(HttpParameter... authParameters) {
        if (getAuthParameters() == null) {
            setAuthParameters(new HashSet<HttpParameter>(authParameters.length));
        }
        for (HttpParameter p : authParameters) {
            getAuthParameters().add(p);
        }
        return this;
    }

    public Individual asIndividual(OntModel model) {
        String aaSpecUri = getUri() != null ? getUri().toString() : null;
        Individual indiv = model.createIndividual(aaSpecUri, OTRestClasses.AA().inModel(model));
        if (getOntologicalClasses() != null && !getOntologicalClasses().isEmpty()) {
            for (OntologicalClass oc : getOntologicalClasses()) {
                indiv.addRDFType(oc.inModel(model));
            }
        }
        if (getAuthParameters() != null && !getAuthParameters().isEmpty()) {
            Property aaProperty = OTRestObjectProperties.aa().asObjectProperty(model);
            for (HttpParameter param : getAuthParameters()) {
                indiv.addProperty(aaProperty, param.asIndividual(model));
            }
        }
        if (getMeta() != null) {
            getMeta().attachTo(indiv, model);
        }
        return indiv;
    }

    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
