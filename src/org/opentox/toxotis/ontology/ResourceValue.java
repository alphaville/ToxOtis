package org.opentox.toxotis.ontology;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.collection.OTClasses;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ResourceValue {

    private VRI uri;
    private OntologicalClass ontologicalClass;

    public ResourceValue(VRI uri, OntologicalClass ontologicalClass) {
        this.uri = uri;
        this.ontologicalClass = ontologicalClass;
    }

    public OntologicalClass getOntologicalClass() {
        return ontologicalClass;
    }

    public void setOntologicalClass(OntologicalClass ontologicalClass) {
        this.ontologicalClass = ontologicalClass;
    }

    public VRI getUri() {
        return uri;
    }

    public void setUri(VRI uri) {
        this.uri = uri;
    }

    public Resource inModel(OntModel model){
        return model.createResource(getUri()!=null?getUri().toString():null,
                getOntologicalClass()!=null?getOntologicalClass().inModel(model):OTClasses.Thing().inModel(model));
    }

}