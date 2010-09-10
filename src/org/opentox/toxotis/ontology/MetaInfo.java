package org.opentox.toxotis.ontology;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import java.util.Collection;

/**
 * <p align=justify width=80%>
 * MetaInfo provides access to a set of meta-information about
 * a resource. Such are the identifier, the title and other related meta. Formally
 * these are included in the Dublin Core ontology for modeling meta-data and partially
 * in the OpenTox ontology (e.g. hasSource) for modeling some OT-specific properties.
 * </p>
 * @author Sopasakis Pantelis
 */
public interface MetaInfo extends java.io.Serializable {
//TODO: add bibtex
    String getComment();

    String getDescription();

    String getIdentifier();

    String getSameAs();

    String getSeeAlso();

    String getTitle();

    String getVersionInfo();

    String getPublisher();

    String getCreator();

    String getHasSource();

    Collection<String> getContributors();

    Collection<String> getAudiences();

    void setComment(String comment);

    void setDescription(String description);

    void setIdentifier(String identifier);

    void setSameAs(String sameAs);

    void setSeeAlso(String seeAlso);

    void setTitle(String title);

    void setVersionInfo(String versionInfo);

    void setPublisher(String publisher);

    void setCreator(String creator);

    void setHasSource(String hasSource);

    void addContributor(String contributor);

    void addAudience(String audience);

    /**
     * Attaches the meta data to a given resource in an ontological model
     * returning the updated resource. The provided ontological model is also updated
     * with the meta data assigned to the given resource.
     * 
     * @param resource
     *      A resource from an ontological model to which meta data are to
     *      be assigned. The resource provided as input to this method is updated
     *      according to the metadata and various statements are assigned to it
     *      concerning the non-null fields of the metadata object to which the method
     *      is applied.<br/>
     * @param model
     *      The ontological model holding the individual provided in this method.
     * @return
     *      The updated resource with the metadata.
     */
    Resource attachTo(Resource resource, OntModel model);
}
