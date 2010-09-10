package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import java.net.URISyntaxException;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.GetClient;
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

    public T setMeta(MetaInfo meta){
        this.meta = meta;
        return (T) this;
    }


    /**
     * Parses a given ontological model and returns an instance of the underlying
     * object identified by the generic datatype <code>T</code>.
     * @param model
     *      Ontological Model that is parsed to generate an object of type
     *      <code>T</code>.
     * @return
     *      An OpenTox Component.
     */
    public abstract T createFrom(OntModel model);

    /**
     * Update the current component according to some remote resource. Load information
     * from the remote location.
     * @param uri
     *      The remote destination where the data will be downloaded from provided
     *      as a String.
     * @return
     *      An OpenTox component as an instance of <code>T</code>, i.e. of the
     *      same type with the object performing the request.
     * @throws URISyntaxException
     *      In case the provided URI (as String) is not syntactically correct.
     * @throws ToxOtisException
     *      In case the Ontological Model cannot be downloaded from the specified
     *      online resource.
     */
    public T loadFromRemote(String uri) throws URISyntaxException, ToxOtisException {
        return loadFromRemote(new VRI(uri));
    }

    /**
     * Update the current component according to some remote resource. Load information
     * from the remote location.
     * @param uri
     *      The remote destination where the data will be downloaded from provided
     *      as an instance of {@link VRI }.
     * @return
     *      An OpenTox component as an instance of <code>T</code>, i.e. of the
     *      same type with the object performing the request.
     * @throws ToxOtisException
     *      In case the Ontological Model cannot be downloaded from the specified
     *      online resource.
     */
    public T loadFromRemote(VRI uri) throws ToxOtisException {
        GetClient client = new GetClient();
        client.setUri(uri);
        return createFrom(client.getResponseOntModel());
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
        if(obj instanceof OTComponent){
            return ((OTComponent)obj).getUri().equals(this.uri);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.uri != null ? this.uri.hashCode() : 0);
        return hash;
    }


}
