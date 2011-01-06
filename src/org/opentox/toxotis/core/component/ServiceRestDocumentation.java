package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.IOTComponent;
import org.opentox.toxotis.core.IRestOperation;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTRestObjectProperties;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ServiceRestDocumentation extends OTComponent<ServiceRestDocumentation> {

    private final IOTComponent component;
    private Set<IRestOperation> restOperations;

    public ServiceRestDocumentation(IOTComponent component) {
        super();
        if (component == null) {
            throw new NullPointerException("Null component is not allowed in this constructor");
        }
        if (component.getUri() == null) {
            throw new IllegalArgumentException("Component with null URI was provided");
        }
        this.component = component;
    }

    private ServiceRestDocumentation(VRI uri) {
        throw new AssertionError("Should not be invoked!");
    }

    private ServiceRestDocumentation() {
        throw new AssertionError("Should not be invoked!");
    }

    public Set<IRestOperation> getRestOperations() {
        return restOperations;
    }

    public void setRestOperations(Set<IRestOperation> restOperations) {
        this.restOperations = restOperations;
    }

    @Override
    public MetaInfo getMeta() {
        return component.getMeta();
    }

    public Individual asIndividual(OntModel model) {
        Set<OntologicalClass> oc = component.getOntologicalClasses();
        OntologicalClass classForCoreComponent = null;
        if (oc != null && !oc.isEmpty()) {
            classForCoreComponent = oc.iterator().next();
        }
        Individual indiv = model.createIndividual(getUri().toString(), classForCoreComponent.inModel(model));
        if (getMeta() != null) {
            getMeta().attachTo(indiv, model);
        }
        if (getRestOperations() != null && !getRestOperations().isEmpty()) {
            ObjectProperty hasRESTOperationProperty = OTRestObjectProperties.hasRESTOperation().asObjectProperty(model);
            for (IRestOperation ro : getRestOperations()) {
                indiv.addProperty(hasRESTOperationProperty, ro.asIndividual(model));
            }
        }
        return indiv;
    }

    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
