package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTClasses;

/**
 * A minimal implementaiton of a component with just a URI and a set of ontological
 * classes and not much functionality therein.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DummyComponent extends OTComponent<DummyComponent>{

    /**
     * Dummy constructor for this component with no arguments. Calls the contructor
     * in the <code>super</code> class and initializes the ontological classes for it
     * with {@link OTClasses#OpenToxResource() }
     *
     */
    public DummyComponent() {
        super();
        addOntologicalClasses(OTClasses.OpenToxResource());
    }

    public DummyComponent(VRI uri) {
        super(uri);
        addOntologicalClasses(OTClasses.OpenToxResource());
    }

    public Individual asIndividual(OntModel om) {
        OntologicalClass ontologicalClass = null;
        if (getOntologicalClasses()!=null && !getOntologicalClasses().isEmpty()){
            ontologicalClass = getOntologicalClasses().iterator().next();
        }
        if (ontologicalClass == null){
            ontologicalClass = OTClasses.OpenToxResource();
        }
        String componentUri = getUri()!=null?getUri().toString():null;
        Individual indiv = om.createIndividual(componentUri, ontologicalClass.inModel(om));
        return indiv;
    }

    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }




}