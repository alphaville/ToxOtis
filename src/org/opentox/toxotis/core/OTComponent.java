package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;
import org.opentox.toxotis.ontology.impl.SimpleOntModelImpl;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class OTComponent<T extends OTComponent> {

    protected VRI uri;
    protected MetaInfo meta = new MetaInfoImpl();

    /**
     * Constructor for an empty OpenTox Component
     */
    public OTComponent() {
    }

    public OTComponent(VRI uri) {
        this.uri = uri;
    }

    /**
     * Obtain meta information about the underlying OpenTox resource/component.
     * These meta information include various fields of the Dublin Core ontology
     * (title, identifier), properties from the OpenTox ontology (see
     * http://opentox.org/data/documents/development/RDF%20Files/OpenToxOntology)
     * and some elements from other ontologies like rdfs:comment
     * @return
     *      Meta information about the resource
     */
    public MetaInfo getMeta() {
        return meta;
    }

    public T setMeta(MetaInfo meta) {
        this.meta = meta;
        return (T) this;
    }

    /**
     * The OpenTox component as an individual.
     * @param model
     *      The ontological model to which the individual belongs.
     * @return
     *      The OpenTox component as an individual of a data model.
     */
    public abstract Individual asIndividual(OntModel model);

    /**
     * Creates a new Ontological Model (uses an instance of {@link SimpleOntModelImpl })
     * and assigns to it the Individual from the method
     * {@link OTComponent#asIndividual(com.hp.hpl.jena.ontology.OntModel) asIndividual(OntModel)}.
     * @return
     *      An ontological model for the current OpenTox component.
     */
    public OntModel asOntModel() {
        OntModel om = new SimpleOntModelImpl();
        asIndividual(om).inModel(om);
        return om;
    }

    /**
     * Get the URI of the resource (as an instance of {@link VRI }. Anonymous
     * resources, return <code>null</code>
     * @return
     *      URI of the component or <code>null</code> if not any.
     */
    public VRI getUri() {
        return uri;
    }

    public T setUri(VRI uri) {
        this.uri = uri;
        return (T) this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OTComponent<T> other = (OTComponent<T>) obj;
        if (this.uri != other.uri && (this.uri == null || !this.uri.equals(other.uri))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.uri != null ? this.uri.hashCode() : 0);
        return hash;
    }
}
