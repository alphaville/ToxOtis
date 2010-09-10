package org.opentox.toxotis.ontology;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import java.util.Collection;
import java.util.Date;
import org.opentox.toxotis.util.spiders.TypedValue;

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

    TypedValue<String> getComment();

    TypedValue<String> getDescription();

    TypedValue<String> getIdentifier();

    TypedValue<String> getSameAs();

    TypedValue<String> getSeeAlso();

    TypedValue<String> getTitle();

    TypedValue<String> getVersionInfo();

    TypedValue<String> getPublisher();

    TypedValue<String> getCreator();

    TypedValue<String> getHasSource();

    Collection<TypedValue<String>> getContributors();

    Collection<TypedValue<String>> getAudiences();

    MetaInfo setComment(String comment);

    MetaInfo setComment(TypedValue<String> comment);

    MetaInfo setDescription(String description);

    MetaInfo setDescription(TypedValue<String> description);

    MetaInfo setIdentifier(String identifier);

    MetaInfo setIdentifier(TypedValue<String> identifier);

    MetaInfo setSameAs(String sameAs);

    MetaInfo setSameAs(TypedValue<String> sameAs);

    MetaInfo setSeeAlso(String seeAlso);

    MetaInfo setSeeAlso(TypedValue<String> seeAlso);

    MetaInfo setTitle(String title);

    MetaInfo setTitle(TypedValue<String> title);

    MetaInfo setVersionInfo(String versionInfo);

    MetaInfo setVersionInfo(TypedValue<String> versionInfo);

    MetaInfo setPublisher(String publisher);

    MetaInfo setPublisher(TypedValue<String> publisher);

    MetaInfo setCreator(String creator);

    MetaInfo setCreator(TypedValue<String> creator);

    MetaInfo setHasSource(String hasSource);

    MetaInfo setHasSource(TypedValue<String> hasSource);

    MetaInfo addContributor(String contributor);

    MetaInfo addContributor(TypedValue<String> contributor);

    MetaInfo addAudience(String audience);

    MetaInfo addAudience(TypedValue<String> audience);

    TypedValue<Date> getDate();

    MetaInfo setDate(TypedValue<Date> date);

    MetaInfo setDate(Date date);

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
