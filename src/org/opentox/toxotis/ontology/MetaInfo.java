package org.opentox.toxotis.ontology;

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
}
