package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.Set;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.OntologicalClass;

/**
 * Generic interface for all OpenTox components in ToxOtis that can be represented
 * as individuals in an RDF graph. According to the OpenTox API specifications (since 
 * API 1.1.) should be available in RDF (application/rdf+xml) while other RDF-related
 * representations can be also available optionally (like application/x-turtle).
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface IOTComponent<T extends IOTComponent> {

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
     * The ontological model will contain no other information apart from the representation
     * of the inderlying OpenTox component.
     * @return
     *      An ontological model for the current OpenTox component.
     */
    public OntModel asOntModel() ;

    /**
     * Get the URI of the resource (as an instance of {@link VRI }. Anonymous
     * resources, return <code>null</code>
     * @return
     *      URI of the component or <code>null</code> if not any.
     */
    public VRI getUri();

    /**
     * Obtain meta information about the underlying OpenTox resource/component.
     * These meta information include various fields of the Dublin Core ontology
     * (title, identifier), properties from the OpenTox ontology (see
     * http://opentox.org/data/documents/development/RDF%20Files/OpenToxOntology)
     * and some elements from other ontologies like rdfs:comment
     * @return
     *      Meta information about the resource
     */
    public MetaInfo getMeta();

    /**
     * Set the meta-information of the component
     * @param meta
     *      Meta information for the component
     * @return
     *      The component itself with the updated meta-information
     * @see MetaInfo Meta Information
     */
    public T setMeta(MetaInfo meta);

    public Set<OntologicalClass> getOntologicalClasses();

    public T setOntologicalClasses(Set<OntologicalClass> ontClasses);
    
    public T addOntologicalClasses(OntologicalClass... ontClasses);

    boolean isEnabled();

    T setEnabled(boolean enabled);

}
