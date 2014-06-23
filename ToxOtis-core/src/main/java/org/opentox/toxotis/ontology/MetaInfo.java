/*
 *
 * ToxOtis
 *
 * ToxOtis is the Greek word for Sagittarius, that actually means ‘archer’. ToxOtis
 * is a Java interface to the predictive toxicology services of OpenTox. ToxOtis is
 * being developed to help both those who need a painless way to consume OpenTox
 * services and for ambitious service providers that don’t want to spend half of
 * their time in RDF parsing and creation.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 *
 */
package org.opentox.toxotis.ontology;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import java.util.Set;
import org.opentox.toxotis.core.IHTMLSupport;

/**
 *
 * Interface describing meta-information for a resource.
 * 
 * <p align=justify>
 * The term metadata is usually used to describe <em>data about data</em> that include 
 * a definition, description and other auxiliary information about the underlying resource. 
 * The interface MetaInfo provides access to a set of such metadata about
 * a resource. Such are the identifier, the title and other related meta. Formally
 * these are included in the Dublin Core ontology for modeling meta-data and partially
 * in the OpenTox ontology (e.g. hasSource) for modeling some OT-specific properties.
 * The list of attributes that compile the list of generic meta-information in this
 * class inludes:
 * </p>
 * <ul>
 * <li>A set of identifiers each one of which uniquely identifies the resource</li>
 * <li>A set of comments</li>
 * <li>The subject(s) of the resource acting also like a set of keywords</li>
 * <li>Descriptions that are available for the resource</li>
 * <li>A set of resources that are 'owl:sameAs' the underlying resource</li>
 * <li>A set of "sources" for the resource, that is resources that are de facto responsible
 * for the creation/existence of the resource. (The corresponding object property ot:hasSource is
 * defined in the OpenTox ontology)</li>
 * <li>A set of resources provided as reference through the seeAlso property</li>
 * <li>A set of titles for the resource</li>
 * <li>Audiences to which the resource addresses or is suitable for</li>
 * <li>Creators of the resource</li>
 * <li>A collection of publishers</li>
 * <li>The date of creation or some characteristic date for the resource (e.g. date of publication)</li>
 * </ul>
 *
 * @author Sopasakis Pantelis
 *
 * @see <a href="http://dublincore.org/documents/usageguide/elements.shtml">Dublin Core Documentation</a>
 */
public interface MetaInfo extends java.io.Serializable, IHTMLSupport {

    //// 1. comments
    ////
    /**
     * The property <code>rdfs:comment</code> is used to provide a human-readable
     * description of a resource. Users can specify a set of such comments on a
     * resource of interest.
     *
     * @return
     *      A comment on the described entity.
     */
    Set<LiteralValue> getComments();

    /**
     * Setter for comments as a set.
     * @param comments
     *      Comments for the resource
     * @return
     *      The current modifiable MetaInfo object.
     */
    MetaInfo setComments(Set<LiteralValue> comments);

    /**
     * Add comments to the current instance of MetaInfo
     * @param comment
     *      Sequence of comments provided for this MetaInfo object as literal
     *      value objects.
     * @return
     *      The current modifiable MetaInfo object.
     */
    MetaInfo addComment(LiteralValue... comment);

    /**
     * Add comments to the current instance of MetaInfo
     * @param comment
     *      Sequence of comments provided for this MetaInfo object as plain strings.
     * @return
     *      The current modifiable MetaInfo object.
     */
    MetaInfo addComment(String... comment);

    /**
     * Copyright notes for the underlying resource.
     * @return
     *      Set of copyright notes for the underlying resource for which this
     *      meta-info element is composed.
     */
    Set<LiteralValue> getRights();

    /**
     *
     * @param rights
     * @return
     */
    MetaInfo setRights(Set<LiteralValue> rights);

    /**
     *
     * @param rights
     * @return
     */
    MetaInfo addRights(LiteralValue... rights);

    /**
     *
     * @param rights
     * @return
     */
    MetaInfo addRights(String... rights);

    //// 2. descriptions
    ////
    /**
     * An account of the content of the resource. Description may include but is not
     * limited to: an abstract, table of contents, reference to a graphical representation
     * of content or a free-text account of the content.
     * @return
     *      Description as a typed value
     */
    Set<LiteralValue> getDescriptions();

    /**
     * Provide set of descriptions
     * @param descriptions
     *      Set of descriptions as literal values
     * @return
     *
     */
    MetaInfo setDescriptions(Set<LiteralValue> descriptions);

    /**
     *
     * @param description
     * @return
     */
    MetaInfo addDescription(LiteralValue... description);

    /**
     *
     * @param description
     * @return
     */
    MetaInfo addDescription(String... description);

    /**
     * Get the identifier (ID) of the described entity. An identifier is an unambiguous
     * reference to the resource within a given context. Recommended best practice is
     * to identify the resource by means of a string or number conforming to a
     * formal identification system. Examples of formal identification systems include
     * the Uniform Resource Identifier (URI) (including the Uniform Resource Locator (URL),
     * the Digital Object Identifier (DOI) and the International Standard Book Number (ISBN).
     * @return
     *      Identifier as a Typed Value
     * @see <a href="http://dublincore.org/documents/usageguide/elements.shtml">http://dublincore.org/documents/usageguide/elements.shtml</a>
     */
    Set<LiteralValue> getIdentifiers();

    /**
     *
     * @param identifiers
     * @return
     */
    MetaInfo setIdentifiers(Set<LiteralValue> identifiers);

    /**
     *
     * @param identifier
     * @return
     */
    MetaInfo addIdentifier(LiteralValue... identifier);

    /**
     *
     * @param identifier
     * @return
     */
    MetaInfo addIdentifier(String... identifier);

    //// 4. sameAs
    ////
    /**
     * The built-in OWL property <code>owl:sameAs</code> links an individual to an individual
     * (i.e. it is an object property).
     * Such an <code>owl:sameAs</code> statement indicates that two URI references
     * actually refer to the same thing: the individuals have the same "identity".
     * For individuals such as "people" this notion is relatively easy to understand.
     * For example, we could state that the following two URI references actually refer
     * to the same person:
     * <pre>
     * &lt;rdf:Description rdf:about="#William_Jefferson_Clinton"&gt;
     * &lt;owl:sameAs rdf:resource="#BillClinton"/&gt;
     * &lt;/rdf:Description&gt;
     * </pre>
     * The <code>owl:sameAs</code> statements are often used in defining mappings between ontologies.
     * It is unrealistic to assume everybody will use the same name to refer to individuals.
     * That would require some grand design, which is contrary to the spirit of the web.
     * @return
     *      A link to a resource that resembles the described entity
     *
     */
    Set<ResourceValue> getSameAs(); // TODO: Change this into some other class...

    /**
     *
     * @param values
     * @return
     */
    MetaInfo setSameAs(Set<ResourceValue> values);

    /**
     *
     * @param value
     * @return
     */
    MetaInfo addSameAs(ResourceValue value);

    //// 5. seeAlso
    /**
     * Related to the datatype ontological property <code>rdfs:seeAlso</code>. The property
     * <code>rdfs:seeAlso</code> specifies a resource that might provide additional information
     * about the subject resource. This property may be specialized using rdfs:subPropertyOf
     * to more precisely indicate the nature of the information the object resource has about the
     * subject resource. The object and the subject resources are constrained only to be instances
     * of the class rdfs:Resource.
     *
     * @return
     *      A reference to some other entity as a typed value.
     */
    Set<ResourceValue> getSeeAlso(); // TODO: Change this into some other class...

    /**
     *
     * @param values
     * @return
     */
    MetaInfo setSeeAlso(Set<ResourceValue> values);

    /**
     *
     * @param value
     * @return
     */
    MetaInfo addSeeAlso(ResourceValue value);

    //// 6. titles (dc:title)
    ////
    /**
     * Get the name given to the resource.
     * Typically, a Title will be a name by which the resource is formally known.
     * @return
     *      Tile as a Typed Value
     */
    Set<LiteralValue> getTitles();

    /**
     *
     * @param values
     * @return
     */
    MetaInfo setTitles(Set<LiteralValue> values);

    /**
     *
     * @param value
     * @return
     */
    MetaInfo addTitle(LiteralValue... value);

    /**
     *
     * @param value
     * @return
     */
    MetaInfo addTitle(String... value);

    //// 7. subjects (dc:subject)
    ////
    /**
     * Get the topic of the content of the resource. Typically, a Subject will be
     * expressed as keywords or key phrases or classification codes that describe the
     * topic of the resource. Recommended best practice is to select a value from
     * a controlled vocabulary or formal classification scheme.
     * @return
     *      Subject as a typed value
     */
    Set<LiteralValue> getSubjects();

    /**
     *
     * @param subjects
     * @return
     */
    MetaInfo setSubjects(Set<LiteralValue> subjects);

    /**
     *
     * @param subject
     * @return
     */
    MetaInfo addSubject(LiteralValue... subject);

    /**
     *
     * @param subject
     * @return
     */
    MetaInfo addSubject(String... subject);

    //// 8. publishers
    ////
    /**
     *
     * @return
     */
    Set<LiteralValue> getPublishers();

    /**
     *
     * @param publishers
     * @return
     */
    MetaInfo setPublishers(Set<LiteralValue> publishers);

    /**
     *
     * @param publisher
     * @return
     */
    MetaInfo addPublisher(LiteralValue... publisher);

    /**
     *
     * @param publisher
     * @return
     */
    MetaInfo addPublisher(String... publisher);

    //// 9. creators
    ////
    /**
     * The <code>creator</code> is an entity primarily responsible for making the content
     * of the resource. Examples of a Creator include a person, an organization, or a service.
     * Typically the name of the Creator should be used to indicate the entity.
     * @return
     *      Creator as a typed value
     */
    Set<LiteralValue> getCreators();

    /**
     *
     * @param creators
     * @return
     */
    MetaInfo setCreators(Set<LiteralValue> creators);

    /**
     *
     * @param creator
     * @return
     */
    MetaInfo addCreator(LiteralValue... creator);

    /**
     *
     * @param creator
     * @return
     */
    MetaInfo addCreator(String... creator);

    //// 10. Sources (ot:hasSource)
    ////
    /**
     *
     * @return
     */
    Set<ResourceValue> getHasSources();

    /**
     *
     * @param hasSources
     * @return
     */
    MetaInfo setHasSources(Set<ResourceValue> hasSources);

    /**
     *
     * @param hasSource
     * @return
     */
    MetaInfo addHasSource(ResourceValue hasSource);

    //// 11. Contributors
    /**
     *
     * @return
     */
    Set<LiteralValue> getContributors();

    /**
     *
     * @param contributors
     * @return
     */
    MetaInfo setContributors(Set<LiteralValue> contributors);

    /**
     *
     * @param contributor
     * @return
     */
    MetaInfo addContributor(LiteralValue... contributor);

    /**
     *
     * @param contributor
     * @return
     */
    MetaInfo addContributor(String... contributor);

    /**
     *
     * @return
     */
    Set<LiteralValue> getAudiences();

    /**
     *
     * @param audiences
     * @return
     */
    MetaInfo setAudiences(Set<LiteralValue> audiences);

    /**
     *
     * @param audience
     * @return
     */
    MetaInfo addAudience(LiteralValue... audience);

    /**
     *
     * @param audience
     * @return
     */
    MetaInfo addAudience(String... audience);

    /**
     *
     * @return
     */
    LiteralValue getDate();// used for versioning, timestamping 

    /**
     *
     * @param date
     * @return
     */
    MetaInfo setDate(LiteralValue date);

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    // --------------------- End of Getters -------------------------
    // --------------------------------------------------------------
    // --------------------------------------------------------------
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
     *      is applied.
     * @param model
     *      The ontological model holding the individual provided in this method.
     * @return
     *      The updated resource with the metadata.
     */
    Resource attachTo(Resource resource, OntModel model);

    /**
     * Write the meta information in RDF/XML format using an XML Stream Writer. The
     * data are written at the current position of the cursor of the writer. The method
     * appends meta information about the underlying resource that is described in the
     * RDF/XML document. Note that the writer should include at least the following
     * namespaces before the invocation of this method:
     *
     * <pre>
     * ot   : http://www.opentox.org/api/1.1#
     * rdfs : http://www.w3.org/2000/01/rdf-schema#
     * rdf  : http://www.w3.org/1999/02/22-rdf-syntax-ns#
     * dc   : http://purl.org/dc/elements/1.1/
     * </pre>
     *
     * The following code can be used to declare the above namespaces:
     *
     * <pre>
     * writer.writeNamespace("ot", OTClasses.NS);
     * writer.writeNamespace("rdfs", RDFS.getURI());
     * writer.writeNamespace("rdf", RDF.getURI());
     * writer.writeNamespace("dc", DC.NS);
     * writer.writeNamespace("owl", OWL.NS);
     * </pre>
     *
     * Additionally you can declare the above as a set of predixes:
     *
     * <pre>
     * writer.setPrefix("ot", OTClasses.NS);
     * writer.setPrefix("rdfs", RDFS.getURI());
     * writer.setPrefix("rdf", RDF.getURI());
     * writer.setPrefix("dc", DC.NS);
     * writer.setPrefix("owl", OWL.NS);
     * </pre>
     * 
     * @param writer
     *      Writer used to write XML data to an Output Stream
     * @throws javax.xml.stream.XMLStreamException
     *      In case there the data that try to be written are not consisent. For example
     *      such an exception is thrown if you forget to close a non-empty element
     *      in the XML.
     */
    void writeToStAX(javax.xml.stream.XMLStreamWriter writer) throws javax.xml.stream.XMLStreamException;

    /**
     *
     * @return
     */
    long getHash();

    /**
     *
     * @param hash
     */
    void setHash(long hash);

    /**
     * Check whether the meta info object is empty.
     * @return
     *      <code>true</code> if the MetaInfo object is empty and <code>false</code>
     *      otherwise.
     */
    boolean isEmpty();
}
