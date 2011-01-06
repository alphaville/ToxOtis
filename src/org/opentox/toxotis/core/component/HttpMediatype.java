package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.IOTComponent;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.ontology.OntologicalClass;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class HttpMediatype extends OTComponent<HttpMediatype> {

    public HttpMediatype() {
        super();
    }

    public HttpMediatype(VRI uri) {
        super(uri);
    }

    public Individual asIndividual(OntModel model) {
        String mediaUri = getUri() != null ? getUri().toString() : null;
        OntologicalClass ontClass = null;
        if (getOntologicalClasses() != null && !getOntologicalClasses().isEmpty()) {
            ontClass = getOntologicalClasses().iterator().next();
        }
        Individual indiv = model.createIndividual(mediaUri, ontClass.inModel(model));
        if (getOntologicalClasses() != null && !getOntologicalClasses().isEmpty()) {
            for (OntologicalClass oc : getOntologicalClasses()) {
                indiv.addRDFType(oc.inModel(model));
            }
        }
        return indiv;
    }

    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
