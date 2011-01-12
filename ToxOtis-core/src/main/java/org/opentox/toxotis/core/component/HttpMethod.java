package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.ontology.collection.HttpMethods.MethodsEnum;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class HttpMethod extends OTComponent<HttpMethod> {

    private final MethodsEnum methodEnumElement;

    public HttpMethod(MethodsEnum method) {
        this.methodEnumElement = method;
    }

    public Individual asIndividual(OntModel model) {
        String httpMehtodUri = getUri() != null ? getUri().toString() : null;
        Individual indiv = model.createIndividual(httpMehtodUri, methodEnumElement.getResourceValue().inModel(model));
        if (getMeta()!=null){
            getMeta().attachTo(indiv, model);
        }
        return indiv;
    }

    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
