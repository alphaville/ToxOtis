package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import java.util.HashSet;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.IOTComponent;
import org.opentox.toxotis.core.IRestOperation;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTRestClasses;
import org.opentox.toxotis.ontology.collection.OTRestObjectProperties;

/**
 * Provides a machine-readable documentation for the consumption of all available
 * services provided including information about diffferent HTTP methods, possible
 * status codes, input parameters and more. This can be used to generate human-readable
 * guidelines for the invokation of the web service.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ServiceRestDocumentation extends OTComponent<ServiceRestDocumentation> {

    private final IOTComponent component;
    private Set<IRestOperation> restOperations;

    /**
     * A <code>ServiceRestDocumentation</code> object should be initialized with
     * an OpenTox component.
     * @param component
     *      An opentox component or an instance of {@link DummyCompo
     */
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

    public ServiceRestDocumentation setRestOperations(Set<IRestOperation> restOperations) {
        this.restOperations = restOperations;
        return this;
    }

    public ServiceRestDocumentation addRestOperations(IRestOperation... restOperations) {
        if (getRestOperations() == null) {
            setRestOperations(new HashSet<IRestOperation>());
        }
        for (IRestOperation rest : restOperations) {
            getRestOperations().add(rest);
        }
        return this;
    }

    public IOTComponent getComponent() {
        return component;
    }

    @Override
    public MetaInfo getMeta() {
        return getComponent().getMeta();
    }

    @Override
    public VRI getUri() {
        return getComponent().getUri();
    }

    @Override
    public Set<OntologicalClass> getOntologicalClasses() {
        Set<OntologicalClass> classes = component.getOntologicalClasses();
        classes.add(OTRestClasses.RESTOperation());
        return classes;
    }

    public Individual asIndividual(OntModel model) {
        OntologicalClass classForCoreComponent = null;
        if (getOntologicalClasses() != null && !getOntologicalClasses().isEmpty()) {
            classForCoreComponent = getOntologicalClasses().iterator().next();
        }
        Individual indiv = model.createIndividual(getUri().toString(), classForCoreComponent.inModel(model));
        if (getOntologicalClasses() != null && !getOntologicalClasses().isEmpty()) {
            for (OntologicalClass oc : getOntologicalClasses()) {
                indiv.addRDFType(oc.inModel(model));
            }
        }
        if (getMeta() != null) {
            getMeta().attachTo(indiv, model);
        }
        if (getRestOperations() != null && !getRestOperations().isEmpty()) {
            ObjectProperty hasRESTOperationProperty = OTRestObjectProperties.hasRESTOperation().asObjectProperty(model);
            ObjectProperty resourceProperty = OTRestObjectProperties.resource().asObjectProperty(model);
            for (IRestOperation ro : getRestOperations()) {
                Resource restOperationResource = ro.asIndividual(model);
                indiv.addProperty(hasRESTOperationProperty, restOperationResource);
                restOperationResource.addProperty(resourceProperty, indiv);

            }
        }
        return indiv;
    }

    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
