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
package org.opentox.toxotis.ontology.impl;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.AnnotationProperty;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.ibm.icu.impl.CollectionUtilities;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import org.opentox.toxotis.core.html.HTMLContainer;
import org.opentox.toxotis.core.html.HTMLDivBuilder;
import org.opentox.toxotis.core.html.HTMLTable;
import org.opentox.toxotis.core.html.HTMLUtils;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.ResourceValue;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

public class MetaInfoImpl implements MetaInfo {

    public MetaInfoImpl() {
    }
    private static final transient String DUBLIN_CORE_DOC = "http://dublincore.org/documents/usageguide/elements.shtml#%s";
    private HashSet<LiteralValue> identifiers;
    private HashSet<LiteralValue> comments;
    private HashSet<LiteralValue> descriptions;
    private HashSet<LiteralValue> titles;
    private HashSet<LiteralValue> subjects;
    private HashSet<LiteralValue> publishers;
    private HashSet<LiteralValue> creators;
    private HashSet<LiteralValue> contributors;
    private HashSet<LiteralValue> audiences;
    private HashSet<LiteralValue> rights;
    private LiteralValue date;
    private HashSet<ResourceValue> sameAs;
    private HashSet<ResourceValue> seeAlso;
    private HashSet<ResourceValue> hasSources;
    private static final long serialVersionUID = 258712452874812L;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (identifiers != null) {
            builder.append("identifiers  : ");
            for (LiteralValue id : identifiers) {
                builder.append(id.toString());
                builder.append(" ");
            }
            builder.append("\n");
        }
        return new String(builder);
    }

    @Override
    public Resource attachTo(Resource resource, OntModel model) {
        if (identifiers != null && !identifiers.isEmpty()) {
            AnnotationProperty identProp = model.getAnnotationProperty(DC.identifier.getURI());
            if (identProp == null) {
                identProp = model.createAnnotationProperty(DC.identifier.getURI());
            }
            for (LiteralValue identifier : identifiers) {
                resource.addLiteral(identProp, model.createTypedLiteral(identifier.getValue(), XSDDatatype.XSDanyURI));
            }
        }

        if (titles != null && !titles.isEmpty()) {
            AnnotationProperty titleProp = model.getAnnotationProperty(DC.title.getURI());
            if (titleProp == null) {
                titleProp = model.createAnnotationProperty(DC.title.getURI());
            }
            for (LiteralValue title : titles) {
                resource.addLiteral(titleProp, model.createTypedLiteral(title.getValue()));
            }
        }
        if (descriptions != null && !descriptions.isEmpty()) {
            AnnotationProperty descriptionProp = model.getAnnotationProperty(DC.description.getURI());
            if (descriptionProp == null) {
                descriptionProp = model.createAnnotationProperty(DC.description.getURI());
            }
            for (LiteralValue description : descriptions) {
                resource.addLiteral(descriptionProp, model.createTypedLiteral(description.getValue()));
            }
        }
        if (sameAs != null && !sameAs.isEmpty()) {
            Property sameAsProp = model.getProperty(OWL.sameAs.getURI());
            if (sameAsProp == null) {
                sameAsProp = model.createObjectProperty(OWL.sameAs.getURI());
            }
            for (ResourceValue sa : sameAs) {
                resource.addProperty(sameAsProp, sa.inModel(model));
            }
        }
        if (seeAlso != null && !seeAlso.isEmpty()) {
            ObjectProperty seeAlsoProp = model.getObjectProperty(RDFS.seeAlso.getURI());
            if (seeAlsoProp == null) {
                seeAlsoProp = model.createObjectProperty(RDFS.seeAlso.getURI());
            }
            for (ResourceValue see : seeAlso) {
                resource.addProperty(seeAlsoProp, see.inModel(model));
            }
        }
        if (publishers != null && !publishers.isEmpty()) {
            AnnotationProperty publisherProp = model.getAnnotationProperty(DC.publisher.getURI());
            if (publisherProp == null) {
                publisherProp = model.createAnnotationProperty(DC.publisher.getURI());
            }
            for (LiteralValue publisher : publishers) {
                resource.addLiteral(publisherProp, model.createTypedLiteral(publisher.getValue()));
            }
        }
        if (creators != null && !creators.isEmpty()) {
            AnnotationProperty creatorProp = model.getAnnotationProperty(DC.creator.getURI());
            if (creatorProp == null) {
                creatorProp = model.createAnnotationProperty(DC.creator.getURI());
            }
            for (LiteralValue creator : creators) {
                resource.addLiteral(creatorProp, creator.inModel(model));
            }
        }
        if (subjects != null && !subjects.isEmpty()) {
            AnnotationProperty subjectProp = model.getAnnotationProperty(DC.subject.getURI());
            if (subjectProp == null) {
                subjectProp = model.createAnnotationProperty(DC.subject.getURI());
            }
            for (LiteralValue subject : subjects) {
                resource.addLiteral(DC.subject.inModel(model).as(Property.class), subject.inModel(model));
            }
        }
        if (hasSources != null && !hasSources.isEmpty()) {
            /*
             * Note: hasSource is an Object property that according to the OpenTox ontology
             * is a mapping from ot:Feature ot ot:Dataset or ot:Dataset or ot:Model.
             */
            ObjectProperty hasSourceProp = OTObjectProperties.hasSource().asObjectProperty(model);
            for (ResourceValue source : hasSources) {
                Resource sourceResource = source.inModel(model);
                resource.addProperty(hasSourceProp, sourceResource);
                if (source.getOntologicalClass() != null) {
                    source.inModel(model).addProperty(RDF.type, source.getOntologicalClass().inModel(model));
                }
            }

        }
        if (comments != null && !comments.isEmpty()) {
            AnnotationProperty commentsProp = model.createAnnotationProperty(RDFS.comment.getURI());
            for (LiteralValue comment : comments) {
                resource.addLiteral(commentsProp, comment.inModel(model));
            }

        }
        if (contributors != null && !contributors.isEmpty()) {
            AnnotationProperty commentsProp = model.createAnnotationProperty(DC.contributor.getURI());
            for (LiteralValue contributor : contributors) {
                resource.addLiteral(commentsProp, contributor.inModel(model));
            }

        }
        if (rights != null && !rights.isEmpty()) {
            AnnotationProperty commentsProp = model.createAnnotationProperty(DC.rights.getURI());
            for (LiteralValue right : rights) {
                resource.addLiteral(commentsProp, right.inModel(model));
            }

        }

        if (date != null) {
            AnnotationProperty dateProperty = model.createAnnotationProperty(DC.date.getURI());
            resource.addLiteral(dateProperty, date.inModel(model));
        }

        return resource;
    }

    private void writeMetaDatumResourceToStAX(String metaDatumNS, String metaDatumName, ResourceValue value, javax.xml.stream.XMLStreamWriter writer) throws XMLStreamException {
        if (value != null && value.getUri() != null && value.getUri().toString() != null && !(value.getUri().toString().isEmpty())) {
            String stringVal = value.getUri().toString();
            writer.writeEmptyElement(metaDatumNS + ":" + metaDatumName);
            writer.writeAttribute("rdf:resource", stringVal);
        }
    }

    private void writeMetaDatumToStAX(String metaDatumNS, String metaDatumName, LiteralValue<?> value, javax.xml.stream.XMLStreamWriter writer) throws XMLStreamException {
        if (value != null) {
            String propertyTag = metaDatumNS + ":" + metaDatumName;
            if (value.getValue().toString().isEmpty()) {
                writer.writeEmptyElement(propertyTag);
            }
            writer.writeStartElement(propertyTag);
            writer.writeCharacters(value.getValue().toString());
            writer.writeEndElement();
        }
    }

    @Override
    public void writeToStAX(javax.xml.stream.XMLStreamWriter writer) throws javax.xml.stream.XMLStreamException {
        if (identifiers != null) {
            for (LiteralValue lv : identifiers) {
                writeMetaDatumToStAX("dc", "identifier", lv, writer);
            }
        }
        if (comments != null) {
            for (LiteralValue lv : comments) {
                writeMetaDatumToStAX("rdfs", "comment", lv, writer);
            }
        }
        if (creators != null) {
            for (LiteralValue lv : creators) {
                writeMetaDatumToStAX("dc", "creator", lv, writer);
            }
        }
        if (contributors != null) {
            for (LiteralValue lv : contributors) {
                writeMetaDatumToStAX("dc", "contributor", lv, writer);
            }
        }
        if (descriptions != null) {
            writeMetaDatumToStAX("dc", "date", date, writer);
            for (LiteralValue lv : descriptions) {
                writeMetaDatumToStAX("dc", "description", lv, writer);
            }
        }
        if (publishers != null) {
            for (LiteralValue lv : publishers) {
                writeMetaDatumToStAX("dc", "publisher", lv, writer);
            }
        }
        if (audiences != null) {
            for (LiteralValue lv : audiences) {
                writeMetaDatumToStAX("dc", "audience", lv, writer);
            }
        }
        if (subjects != null) {
            for (LiteralValue lv : subjects) {
                writeMetaDatumToStAX("dc", "subject", lv, writer);
            }
        }
        if (titles != null) {
            for (LiteralValue lv : titles) {
                writeMetaDatumToStAX("dc", "title", lv, writer);
            }
        }
        if (hasSources != null) {
            for (ResourceValue rv : hasSources) {
                writeMetaDatumResourceToStAX("ot", "hasSource", rv, writer);
            }
        }
        if (sameAs != null) {
            for (ResourceValue rv : sameAs) {
                writeMetaDatumResourceToStAX("owl", "sameAs", rv, writer);
            }
        }
        if (seeAlso != null) {
            for (ResourceValue rv : seeAlso) {
                writeMetaDatumResourceToStAX("rdfs", "seeAlso", rv, writer);
            }
        }

    }

    @Override
    public HashSet<LiteralValue> getComments() {
        return comments;
    }

    @Override
    public MetaInfo setComments(Set<LiteralValue> comments) {
        this.comments = new HashSet<LiteralValue>(comments);
        return this;
    }

    @Override
    public MetaInfo addComment(String... comment) {
        if (getComments() == null) {
            setComments(new HashSet<LiteralValue>());
        }
        for (String s : comment) {
            addComment(new LiteralValue<String>(s));
        }
        return this;
    }

    @Override
    public MetaInfo addComment(LiteralValue... comment) {
        if (this.comments != null) {
            for (LiteralValue lvc : comment) {
                this.comments.add(lvc);
            }
        } else {
            HashSet<LiteralValue> values = new HashSet<LiteralValue>();
            for (LiteralValue lvc : comment) {
                values.add(lvc);
            }
            setComments(values);
        }
        return this;
    }

    @Override
    public HashSet<LiteralValue> getDescriptions() {
        return this.descriptions;
    }

    @Override
    public MetaInfo setDescriptions(Set<LiteralValue> descriptions) {
        this.descriptions = new HashSet<LiteralValue>(descriptions);
        return this;
    }

    @Override
    public MetaInfo addDescription(LiteralValue... description) {
        if (getDescriptions() != null) {
            for (LiteralValue lv : description) {
                getDescriptions().add(lv);
            }

        } else {
            HashSet<LiteralValue> values = new HashSet<LiteralValue>();
            for (LiteralValue lv : description) {
                values.add(lv);
            }
            setDescriptions(values);
        }
        return this;
    }

    @Override
    public MetaInfo addDescription(String... description) {
        for (String s : description) {
            addDescription(new LiteralValue<String>(s));
        }
        return this;
    }

    @Override
    public HashSet<LiteralValue> getIdentifiers() {
        return identifiers;
    }

    @Override
    public MetaInfo setIdentifiers(Set<LiteralValue> identifiers) {
        this.identifiers = new HashSet<LiteralValue>(identifiers);
        return this;
    }

    @Override
    public MetaInfo addIdentifier(LiteralValue... identifier) {
        if (getIdentifiers() != null) {
            for (LiteralValue lv : identifier) {
                getIdentifiers().add(lv);
            }
        } else {
            HashSet<LiteralValue> values = new HashSet<LiteralValue>();
            for (LiteralValue lv : identifier) {
                values.add(lv);
            }
            setIdentifiers(values);
        }
        return this;
    }

    @Override
    public MetaInfo addIdentifier(String... identifier) {
        for (String s : identifier) {
            addIdentifier(new LiteralValue<String>(s));
        }
        return this;
    }

    @Override
    public HashSet<ResourceValue> getSameAs() {
        return sameAs;
    }

    @Override
    public MetaInfo setSameAs(Set<ResourceValue> values) {
        this.sameAs = new HashSet<ResourceValue>(values);
        return this;
    }

    @Override
    public MetaInfo addSameAs(ResourceValue value) {
        if (getSameAs() != null) {
            getSameAs().add(value);
        } else {
            HashSet<ResourceValue> values = new HashSet<ResourceValue>();
            values.add(value);
            setSameAs(values);
        }
        return this;
    }

    @Override
    public HashSet<ResourceValue> getSeeAlso() {
        return seeAlso;
    }

    @Override
    public MetaInfo setSeeAlso(Set<ResourceValue> values) {
        this.seeAlso = new HashSet<ResourceValue>(values);
        return this;
    }

    @Override
    public MetaInfo addSeeAlso(ResourceValue value) {
        if (getSeeAlso() != null) {
            getSeeAlso().add(value);
        } else {
            HashSet<ResourceValue> values = new HashSet<ResourceValue>();
            values.add(value);
            setSeeAlso(values);
        }
        return this;
    }

    @Override
    public HashSet<LiteralValue> getTitles() {
        return titles;
    }

    @Override
    public MetaInfo setTitles(Set<LiteralValue> values) {
        this.titles = new HashSet<LiteralValue>(values);
        return this;
    }

    public MetaInfo addTitle(String value) {
        addTitle(new LiteralValue<String>(value, XSDDatatype.XSDstring));
        return this;
    }

    @Override
    public MetaInfo addTitle(LiteralValue... value) {
        if (getTitles() != null) {
            for (LiteralValue lv : value) {
                getTitles().add(lv);
            }
        } else {
            HashSet<LiteralValue> values = new HashSet<LiteralValue>();
            for (LiteralValue lv : value) {
                values.add(lv);
            }
            setTitles(values);
        }
        return this;
    }

    @Override
    public MetaInfo addTitle(String... value) {
        for (String s : value) {
            addTitle(new LiteralValue<String>(s));
        }
        return this;
    }

    @Override
    public HashSet<LiteralValue> getSubjects() {
        return subjects;
    }

    @Override
    public MetaInfo setSubjects(Set<LiteralValue> subjects) {
        this.subjects = new HashSet<LiteralValue>(subjects);
        return this;
    }

    @Override
    public MetaInfo addSubject(LiteralValue... subject) {
        if (getSubjects() != null) {
            for (LiteralValue lv : subject) {
                getSubjects().add(lv);
            }
        } else {
            HashSet<LiteralValue> values = new HashSet<LiteralValue>();
            for (LiteralValue lv : subject) {
                values.add(lv);
            }
            setSubjects(values);
        }
        return this;
    }

    @Override
    public MetaInfo addSubject(String... subject) {
        for (String x : subject) {
            addSubject(new LiteralValue<String>(x));
        }
        return this;
    }

    @Override
    public HashSet<LiteralValue> getPublishers() {
        return publishers;
    }

    @Override
    public MetaInfo setPublishers(Set<LiteralValue> publishers) {
        this.publishers = new HashSet<LiteralValue>(publishers);
        return this;
    }

    @Override
    public MetaInfo addPublisher(LiteralValue... publisher) {
        if (getPublishers() != null) {
            for (LiteralValue lv : publisher) {
                getPublishers().add(lv);
            }
        } else {
            HashSet<LiteralValue> values = new HashSet<LiteralValue>();
            for (LiteralValue lv : publisher) {
                values.add(lv);
            }
            setPublishers(values);
        }
        return this;
    }

    @Override
    public MetaInfo addPublisher(String... publisher) {
        for (String x : publisher) {
            addPublisher(new LiteralValue<String>(x));
        }
        return this;
    }

    @Override
    public HashSet<LiteralValue> getCreators() {
        return creators;
    }

    @Override
    public MetaInfo setCreators(Set<LiteralValue> creators) {
        this.creators = new HashSet<LiteralValue>(creators);
        return this;
    }

    @Override
    public MetaInfo addCreator(LiteralValue... creator) {
        if (getCreators() != null) {
            for (LiteralValue lv : creator) {
                getCreators().add(lv);
            }
        } else {
            HashSet<LiteralValue> values = new HashSet<LiteralValue>();
            for (LiteralValue lv : creator) {
                values.add(lv);
            }
            setCreators(values);
        }
        return this;
    }

    @Override
    public MetaInfo addCreator(String... creator) {
        for (String x : creator) {
            addCreator(new LiteralValue<String>(x));
        }
        return this;
    }

    @Override
    public HashSet<ResourceValue> getHasSources() {
        return hasSources;
    }

    @Override
    public MetaInfo setHasSources(Set<ResourceValue> hasSources) {
        this.hasSources = new HashSet<ResourceValue>(hasSources);
        return this;
    }

    @Override
    public MetaInfo addHasSource(ResourceValue hasSource) {
        if (getHasSources() != null) {
            getHasSources().add(hasSource);
        } else {
            HashSet<ResourceValue> values = new HashSet<ResourceValue>();
            values.add(hasSource);
            setHasSources(values);
        }
        return this;
    }

    @Override
    public HashSet<LiteralValue> getContributors() {
        return contributors;
    }

    @Override
    public MetaInfo setContributors(Set<LiteralValue> contributors) {
        this.contributors = new HashSet<LiteralValue>(contributors);
        return this;
    }

    @Override
    public MetaInfo addContributor(LiteralValue... contributor) {
        if (getContributors() != null) {
            for (LiteralValue lv : contributor) {
                getContributors().add(lv);
            }
        } else {
            HashSet<LiteralValue> values = new HashSet<LiteralValue>();
            for (LiteralValue lv : contributor) {
                values.add(lv);
            }
            setContributors(values);
        }
        return this;
    }

    @Override
    public MetaInfo addContributor(String... contributor) {
        for (String x : contributor) {
            addContributor(new LiteralValue<String>(x));
        }
        return this;
    }

    @Override
    public HashSet<LiteralValue> getAudiences() {
        return audiences;
    }

    @Override
    public MetaInfo setAudiences(Set<LiteralValue> audiences) {
        this.audiences = new HashSet<LiteralValue>(audiences);
        return this;
    }

    @Override
    public MetaInfo addAudience(LiteralValue... audience) {
        if (getAudiences() != null) {
            for (LiteralValue lv : audience) {
                getAudiences().add(lv);
            }
        } else {
            HashSet<LiteralValue> values = new HashSet<LiteralValue>();
            for (LiteralValue lv : audience) {
                values.add(lv);
            }
            setAudiences(values);
        }
        return this;
    }

    @Override
    public MetaInfo addAudience(String... audience) {
        for (String x : audience) {
            addAudience(new LiteralValue<String>(x));
        }
        return this;
    }

    @Override
    public LiteralValue getDate() {
        return date;
    }

    @Override
    public MetaInfo setDate(LiteralValue date) {
        this.date = date;
        return this;
    }

    public MetaInfo setDateCurrent() {
        Date currentDate = new Date(System.currentTimeMillis());
        setDate(new LiteralValue<Date>(currentDate, XSDDatatype.XSDdate));
        return this;
    }

    public MetaInfo addIdentifier(String identifier) {
        addIdentifier(new LiteralValue<String>(identifier, XSDDatatype.XSDstring));
        return this;
    }

    @Override
    public long getHash() {
        int hash = 3;
        hash = 89 * hash + (this.identifiers != null ? this.identifiers.hashCode() : 0);
        hash = 89 * hash + (this.comments != null ? this.comments.hashCode() : 0);
        hash = 89 * hash + (this.descriptions != null ? this.descriptions.hashCode() : 0);
        hash = 89 * hash + (this.titles != null ? this.titles.hashCode() : 0);
        hash = 89 * hash + (this.subjects != null ? this.subjects.hashCode() : 0);
        hash = 89 * hash + (this.publishers != null ? this.publishers.hashCode() : 0);
        hash = 89 * hash + (this.creators != null ? this.creators.hashCode() : 0);
        hash = 89 * hash + (this.contributors != null ? this.contributors.hashCode() : 0);
        hash = 89 * hash + (this.audiences != null ? this.audiences.hashCode() : 0);
        hash = 89 * hash + (this.date != null ? this.date.hashCode() : 0);
        hash = 89 * hash + (this.sameAs != null ? this.sameAs.hashCode() : 0);
        hash = 89 * hash + (this.seeAlso != null ? this.seeAlso.hashCode() : 0);
        hash = 89 * hash + (this.hasSources != null ? this.hasSources.hashCode() : 0);
        return hash;
    }

    @Override
    public void setHash(long hash) {
    }

    private boolean areSetEquals(Set set1, Set set2) {
        if (set1 == null ^ set2 == null) {
            return false;
        } else if (set1 == null && set2 == null) {
            return true;
        }

        int s1 = set1.size();
        int s2 = set2.size();
        if (s1 != s2) {
            return false;
        }
        for (Object o : set1) {
            boolean foundInSet2 = false;
            for (Object o2 : set2) {
                if (o.equals(o2)) {
                    foundInSet2 = true;
                    break;
                }
            }
            if (!foundInSet2) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MetaInfoImpl other = (MetaInfoImpl) obj;
        if (!areSetEquals(this.identifiers, other.identifiers)) {
            return false;
        }

        if (!areSetEquals(this.comments, other.comments)) {
            return false;
        }

        if (!areSetEquals(this.descriptions, other.descriptions)) {
            return false;
        }

        if (!areSetEquals(this.titles, other.titles)) {
            return false;
        }

        if (!areSetEquals(this.subjects, other.subjects)) {
            return false;
        }

        if (!areSetEquals(this.publishers, other.publishers)) {
            return false;
        }
        if (!areSetEquals(this.creators, other.creators)) {
            return false;
        }
        if (!areSetEquals(this.contributors, other.contributors)) {
            return false;
        }
        if (!areSetEquals(this.audiences, other.audiences)) {
            return false;
        }
        if (this.date != other.date && (this.date == null || !this.date.equals(other.date))) {
            return false;
        }
        if (!areSetEquals(this.sameAs, other.sameAs)) {
            return false;
        }
        if (!areSetEquals(this.seeAlso, other.seeAlso)) {
            return false;
        }
        if (!areSetEquals(this.hasSources, other.hasSources)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.identifiers != null ? this.identifiers.hashCode() : 0);
        hash = 89 * hash + (this.comments != null ? this.comments.hashCode() : 0);
        hash = 89 * hash + (this.descriptions != null ? this.descriptions.hashCode() : 0);
        hash = 89 * hash + (this.titles != null ? this.titles.hashCode() : 0);
        hash = 89 * hash + (this.subjects != null ? this.subjects.hashCode() : 0);
        hash = 89 * hash + (this.publishers != null ? this.publishers.hashCode() : 0);
        hash = 89 * hash + (this.creators != null ? this.creators.hashCode() : 0);
        hash = 89 * hash + (this.contributors != null ? this.contributors.hashCode() : 0);
        hash = 89 * hash + (this.audiences != null ? this.audiences.hashCode() : 0);
        hash = 89 * hash + (this.date != null ? this.date.hashCode() : 0);
        hash = 89 * hash + (this.sameAs != null ? this.sameAs.hashCode() : 0);
        hash = 89 * hash + (this.seeAlso != null ? this.seeAlso.hashCode() : 0);
        hash = 89 * hash + (this.hasSources != null ? this.hasSources.hashCode() : 0);
        return hash;
    }

    private String listOfCreators() {
        StringBuilder builder = new StringBuilder();
        String creatorId = null;
        for (LiteralValue creator : creators) {
            creatorId = creator.getValueAsString();
            builder.append("<a href=\"");
            builder.append("/user/");
            builder.append(creatorId);
            builder.append("\">");
            builder.append(creatorId);
            builder.append("</a>\n");
        }
        return builder.toString();
    }

    @Override
    public HTMLContainer inHtml() {
        HTMLDivBuilder builder = new HTMLDivBuilder("metainfo");
        HTMLTable table = builder.addTable(2);
        if (identifiers != null && !identifiers.isEmpty()) {
            table.setTextAtCursor("<a href=\"" + String.format(DUBLIN_CORE_DOC, "identifier") + "\">Identifier" + (identifiers.size() > 1 ? "s" : "") + "</a>").
                    setTextAtCursor(createHtmlList(identifiers));
        }
        if (titles != null && !titles.isEmpty()) {
            table.setTextAtCursor("<a href=\"" + String.format(DUBLIN_CORE_DOC, "title") + "\">Title" + (titles.size() > 1 ? "s" : "") + "</a>").
                    setTextAtCursor(createHtmlList(titles));
        }
        if (descriptions != null && !descriptions.isEmpty()) {
            table.setTextAtCursor("<a href=\"" + String.format(DUBLIN_CORE_DOC, "description") + "\">Description" + (descriptions.size() > 1 ? "s" : "") + "</a>").
                    setTextAtCursor(createHtmlList(descriptions));
        }
        if (subjects != null && !subjects.isEmpty()) {
            table.setTextAtCursor("<a href=\"" + String.format(DUBLIN_CORE_DOC, "subject") + "\">Subject" + (subjects.size() > 1 ? "s" : "") + "</a>").
                    setTextAtCursor(createHtmlList(subjects));
        }
        if (creators != null && !creators.isEmpty()) {
            table.setTextAtCursor("<a href=\"" + String.format(DUBLIN_CORE_DOC, "creator") + "\">Creator" + (creators.size() > 1 ? "s" : "") + "</a>").
                    setTextAtCursor(listOfCreators());
        }
        if (publishers != null && !publishers.isEmpty()) {
            table.setTextAtCursor("<a href=\"" + String.format(DUBLIN_CORE_DOC, "publisher") + "\">Publisher" + (publishers.size() > 1 ? "s" : "") + "</a>").
                    setTextAtCursor(createHtmlList(publishers));
        }
        if (contributors != null && !contributors.isEmpty()) {
            table.setTextAtCursor("<a href=\"" + String.format(DUBLIN_CORE_DOC, "contributor") + "\">Contributor" + (contributors.size() > 1 ? "s" : "") + "</a>").
                    setTextAtCursor(createHtmlList(contributors));
        }
        if (audiences != null && !audiences.isEmpty()) {
            table.setTextAtCursor("<a href=\"" + String.format(DUBLIN_CORE_DOC, "audience") + "\">Audience" + (audiences.size() > 1 ? "s" : "") + "</a>").
                    setTextAtCursor(createHtmlList(audiences));
        }

        if (seeAlso != null && !seeAlso.isEmpty()) {
            table.setTextAtCursor("<a href=\"http://www.w3.org/TR/2000/CR-rdf-schema-20000327/#s2.3.4\">See Also</a>").
                    setTextAtCursor(createHtmlList2(seeAlso));
        }
        if (sameAs != null && !sameAs.isEmpty()) {
            table.setTextAtCursor("<a href=\"http://www.w3.org/TR/owl-ref/#sameAs-def\">Same As</a>").
                    setTextAtCursor(createHtmlList2(sameAs));
        }
        if (hasSources != null && !hasSources.isEmpty()) {
            table.setTextAtCursor("<a>Source" + (hasSources.size() > 1 ? "s" : "") + "</a>").
                    setTextAtCursor(createHtmlList2(hasSources));
        }
        if (comments != null && !comments.isEmpty()) {
            table.setTextAtCursor("<a href=\"http://www.w3.org/TR/rdf-schema/#ch_comment\">Comment" + (comments.size() > 1 ? "s" : "") + "</a>").
                    setTextAtCursor(createHtmlList(comments));
        }
        if (rights != null && !rights.isEmpty()) {
            table.setTextAtCursor("<a href=\"" + String.format(DUBLIN_CORE_DOC, "rights") + "\">Copyright Note" + (rights.size() > 1 ? "s" : "") + "</a>").
                    setTextAtCursor(createHtmlList(rights));
        }
        if (date != null) {
            table.setTextAtCursor("<a href=\"" + String.format(DUBLIN_CORE_DOC, "date") + "\">Date</a>").
                    setTextAtCursor(date.getValueAsString());
        }
        table.setCellPadding(5).
                setCellSpacing(2).
                setTableBorder(1).
                setColWidth(1, 150).
                setColWidth(2, 600);

        return builder.getDiv();
    }

    private static String createHtmlList(HashSet<LiteralValue> values) {
        if (values.size() == 0) {
            return "";
        } else if (values.size() == 1) {
            return HTMLUtils.linkUrlsInText(HTMLUtils.normalizeTextForHtml(values.iterator().next().getValueAsString()));
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append("<ol>\n");
            for (LiteralValue lv : values) {
                builder.append("<li>");
                builder.append(HTMLUtils.linkUrlsInText(HTMLUtils.normalizeTextForHtml(lv.getValueAsString())));
                builder.append("</li>");
            }
            builder.append("</ol>");
            return builder.toString();
        }
    }

    private static String createHtmlList2(HashSet<ResourceValue> values) {
        if (values.size() == 0) {
            return "";
        } else if (values.size() == 1) {
            return values.iterator().next().getUri().toString();
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append("<ol>\n");
            for (ResourceValue lv : values) {
                builder.append("<li>");
                builder.append("<a href=\"" + lv.getUri().toString() + "\">" + lv.getUri().toString() + "</a>");
                builder.append("</li>");
            }
            builder.append("</ol>");
            return builder.toString();
        }
    }

    @Override
    public boolean isEmpty() {
        if (getAudiences() != null && !getAudiences().isEmpty()) {
            return false;
        }
        if (getComments() != null && !getComments().isEmpty()) {
            return false;
        }
        if (getContributors() != null && !getContributors().isEmpty()) {
            return false;
        }
        if (getCreators() != null && !getCreators().isEmpty()) {
            return false;
        }
        if (getDescriptions() != null && !getDescriptions().isEmpty()) {
            return false;
        }
        if (getHasSources() != null && !getHasSources().isEmpty()) {
            return false;
        }
        if (getIdentifiers() != null && !getIdentifiers().isEmpty()) {
            return false;
        }
        if (getPublishers() != null && !getPublishers().isEmpty()) {
            return false;
        }
        if (getSameAs() != null && !getSameAs().isEmpty()) {
            return false;
        }
        if (getSeeAlso() != null && !getSeeAlso().isEmpty()) {
            return false;
        }
        if (getSubjects() != null && !getSubjects().isEmpty()) {
            return false;
        }
        if (getTitles() != null && !getTitles().isEmpty()) {
            return false;
        }
        if (getRights() != null && !getRights().isEmpty()) {
            return false;
        }
        if (getDate() != null) {
            return false;
        }
        return true;
    }

    @Override
    public HashSet<LiteralValue> getRights() {
        return this.rights;
    }

    @Override
    public MetaInfo setRights(Set<LiteralValue> rights) {
        this.rights = new HashSet<LiteralValue>(rights);
        return this;
    }

    @Override
    public MetaInfo addRights(LiteralValue... rights) {
        if (this.rights == null) {
            this.rights = new HashSet<LiteralValue>();
        }
        for (LiteralValue lv : rights) {
            this.rights.add(lv);
        }
        return this;
    }

    @Override
    public MetaInfo addRights(String... rights) {
        if (this.rights == null) {
            this.rights = new HashSet<LiteralValue>();
        }
        for (String s : rights) {
            this.rights.add(new LiteralValue<String>(s));
        }
        return this;
    }
}
