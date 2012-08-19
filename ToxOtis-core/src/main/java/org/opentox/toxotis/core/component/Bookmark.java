package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.core.IHTMLSupport;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.core.html.HTMLContainer;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Bookmark extends OTComponent<Bookmark> implements IHTMLSupport {

    @Override
    public Individual asIndividual(OntModel model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public HTMLContainer inHtml() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
