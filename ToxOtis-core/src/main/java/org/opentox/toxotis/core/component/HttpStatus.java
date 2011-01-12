package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.RDF;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTRestClasses;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class HttpStatus extends OTComponent<HttpStatus> {

    private OntologicalClass httpStatusClass;

    public HttpStatus(VRI uri) {
        super(uri);
    }

    public HttpStatus() {
    }

    public HttpStatus(OntologicalClass clazz) {
        this.httpStatusClass = clazz;
    }

    public OntologicalClass getHttpStatusClass() {
        return httpStatusClass;
    }

    public void setHttpStatusClass(OntologicalClass httpStatusClass) {
        this.httpStatusClass = httpStatusClass;
    }

    public Individual asIndividual(OntModel model) {
        String httpStatusUri = getUri() != null ? getUri().toString() : null;
        Individual indiv = model.createIndividual(httpStatusUri, OTRestClasses.HTTPStatus().inModel(model));
        if (getHttpStatusClass()!=null){
            indiv.addProperty(RDF.type, getHttpStatusClass().inModel(model));
        }
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
        final HttpStatus other = (HttpStatus) obj;
        if (this.httpStatusClass != other.httpStatusClass && (this.httpStatusClass == null || !this.httpStatusClass.equals(other.httpStatusClass))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.httpStatusClass != null ? this.httpStatusClass.hashCode() : 0);
        return hash;
    }

    
}
