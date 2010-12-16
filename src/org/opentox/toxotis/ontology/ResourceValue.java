package org.opentox.toxotis.ontology;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import java.io.Serializable;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.collection.OTClasses;

/**
 * A wrapper for a Resource assigned to some other resource through an Object
 * property acting as a value for that property. A resource value is characterized
 * by its URI and its type which in that case is an Ontological Class.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ResourceValue implements Serializable {

    private VRI uri;
    private OntologicalClass ontologicalClass;

    private ResourceValue() {
    }

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

    public Resource inModel(OntModel model) {
        return model.createResource(getUri() != null ? getUri().toString() : null,
                getOntologicalClass() != null ? getOntologicalClass().inModel(model) : OTClasses.Thing().inModel(model));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (ResourceValue.class != obj.getClass()) {
            return false;
        }
        final ResourceValue other = (ResourceValue) obj;
        boolean isEq = getHash() == other.getHash();
        return isEq;
    }

    @Override
    public int hashCode() {
        return (int) getHash();
    }

    public long getHash() {
        long hash = (this.uri != null ? uri.toString().trim().hashCode() : 0);
        hash += 7 * (ontologicalClass != null ? ontologicalClass.getUri().hashCode() : 0);
        return hash;
    }

    public void setHash(long hashCode) {/* Do nothing! */ }
}
