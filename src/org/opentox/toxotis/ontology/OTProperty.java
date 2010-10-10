package org.opentox.toxotis.ontology;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import java.util.Collection;

public interface OTProperty extends OTResource, java.io.Serializable {

    String getNameSpace();

    void setNameSpace(String ns);

    String getName();

    /**
     * Set the local name of the property (namespace not included).
     * @param name
     */
    void setName(String name);

    /**
     * Get the meta information available abou the property.
     * @return
     *      Meta information as an instance of {@link MetaInfo }.
     */
    MetaInfo getMetaInfo();

    void setMetaInfo(MetaInfo metaInfo);

    Collection<OTProperty> getSuperProperties();

    void setSuperProperties(Collection<OTProperty> superProperties);

    Collection<OntologicalClass> getDomain();

    /**
     * Extract the corresponding property (if any) from the given ontological
     * model. If the property is not found in the data model, the method return
     * <code>null</code>. In that case where the property does not exist in the
     * data model and it should be created, consider using {@link OTProperty#asProperty(com.hp.hpl.jena.ontology.OntModel)
     * asProperty(OntModel)}.
     * @param model
     *      Ontological Model
     * @return
     *      Property found in that model of <code>null</code> otherwise.
     */
    Property getProperty(OntModel model);

    /**
     * The following note is copied here from the online OWL reference at http://www.w3.org/TR/owl-ref:
     * For a property one can define (multiple) <code>rdfs:domain</code> axioms. Syntactically,
     * <code>rdfs:domain</code> is a built-in property that links a property (some instance
     * of the class <code>rdf:Property</code>) to a class description. An <code>rdfs:domain
     * </code> axiom asserts that the subjects of such property statements must belong to the
     * class extension of the indicated class description.
     * @param domain
     */
    void setDomain(Collection<OntologicalClass> domain);

    /**
     * The following note is copied here from the online OWL reference at http://www.w3.org/TR/owl-ref:
     * Cast the <code>OTProperty</code> object as a Property object of Jena. If the property
     * already exists in the ontological model, it is not recreated but returned to the
     * user. In that case, this method is equivalent to {@link
     * OTProperty#getProperty(com.hp.hpl.jena.ontology.OntModel) getProperty}.
     * 
     * @param model
     *      The model to which is property is assigned.
     * @return
     *      The created proeprty in the provided Ontological Model.
     */
    Property asProperty(OntModel model);
}
