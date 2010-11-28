package org.opentox.toxotis.ontology.impl;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.AnnotationProperty;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.ResourceValue;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

public class MetaInfoImpl implements MetaInfo {

    public MetaInfoImpl() {
    }
    private Set<LiteralValue> identifiers;
    private Set<LiteralValue> comments;
    private Set<LiteralValue> descriptions;
    private Set<LiteralValue> titles;
    private Set<LiteralValue> subjects;
    private Set<LiteralValue> publishers;
    private Set<LiteralValue> creators;
    private Set<LiteralValue> contributors;
    private Set<LiteralValue> audiences;
    private LiteralValue date;
    private Set<ResourceValue> sameAs;
    private Set<ResourceValue> seeAlso;
    private Set<ResourceValue> hasSources;

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
//        if (title != null) {
//            builder.append("title       : " + title + "\n");
//        }
//        if (description != null) {
//            builder.append("description : " + description + "\n");
//        }
//        if (date != null) {
//            builder.append("date        : " + date + "\n");
//        }
//        if (sameAs != null) {
//            builder.append("same as     : " + sameAs + "\n");
//        }
//        if (seeAlso != null) {
//            builder.append("see Also    : " + seeAlso + "\n");
//        }
//        if (publisher != null) {
//            builder.append("publisher   : " + publisher + "\n");
//        }
//        if (creator != null) {
//            builder.append("creator     : " + creator + "\n");
//        }
//        if (hasSource != null) {
//            builder.append("has source  : " + hasSource + "\n");
//        }
//        if (comment != null && !comment.isEmpty()) {
//            Iterator<AnyValue<String>> commentIterator = comment.iterator();
//            while (commentIterator.hasNext()) {
//                builder.append("comment     : " + commentIterator.next() + "\n");
//            }
//        }
//        if (versionInfo != null) {
//            builder.append("version info: " + versionInfo + "\n");
//        }
//        if (subject != null) {
//            builder.append("subject     : " + subject + "\n");
//        }
        return new String(builder);
    }

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
                resource.addProperty(hasSourceProp, source.inModel(model));
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
//        if (versionInfo != null) {
//            resource.addLiteral(OWL.versionInfo.inModel(model).as(Property.class),
//                    model.createTypedLiteral(versionInfo.getValue()));
//        }
        if (date != null) {
            AnnotationProperty dateProperty = model.createAnnotationProperty(DC.date.getURI());
            resource.addLiteral(dateProperty, date.inModel(model));
        }

//        AnnotationProperty contributorProperty = model.createAnnotationProperty(DC.contributor.getURI());
//        if (contributors != null && !contributors.isEmpty()) {
//
//        }
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

    public void writeToStAX(javax.xml.stream.XMLStreamWriter writer) throws javax.xml.stream.XMLStreamException {
//        writeMetaDatumToStAX("dc", "identifier", identifier, writer);
//        if (comment != null && !comment.isEmpty()) {
//            Iterator<AnyValue<String>> commentIterator = comment.iterator();
//            while (commentIterator.hasNext()) {
//                writeMetaDatumToStAX("rdfs", "comment", commentIterator.next(), writer);
//            }
//        }
//
//        writeMetaDatumToStAX("rdfs", "creator", creator, writer);
//        if (contributors != null) {
//            for (AnyValue contrib : contributors) {
//                writeMetaDatumToStAX("dc", "contributor", contrib, writer);
//            }
//        }
//
//        writeMetaDatumToStAX("dc", "date", date, writer);
//        writeMetaDatumToStAX("dc", "description", description, writer);
//        writeMetaDatumResourceToStAX("ot", "hasSource", hasSource, writer);
//        writeMetaDatumToStAX("dc", "publisher", publisher, writer);
//        writeMetaDatumResourceToStAX("owl", "sameAs", sameAs, writer);
//        writeMetaDatumToStAX("rdfs", "seeAlso", seeAlso, writer);
//        writeMetaDatumToStAX("dc", "subject", subject, writer);
//        writeMetaDatumToStAX("dc", "title", title, writer);
//        writeMetaDatumToStAX("owl", "versionInfo", versionInfo, writer);
    }

    public Set<LiteralValue> getComments() {
        return comments;
    }

    public void setComments(Set<LiteralValue> comments) {
        this.comments = comments;
    }

    public void addComment(String comment) {
        addComment(new LiteralValue<String>(comment));
    }

    public void addComment(LiteralValue comment) {
        if (this.comments != null) {
            this.comments.add(comment);
        } else {
            Set<LiteralValue> values = new HashSet<LiteralValue>();
            values.add(comment);
            setComments(values);
        }
    }

    public Set<LiteralValue> getDescriptions() {
        return this.descriptions;
    }

    public void setDescriptions(Set<LiteralValue> descriptions) {
        this.descriptions = descriptions;
    }

    public MetaInfo addDescription(LiteralValue description) {
        if (getDescriptions() != null) {
            getDescriptions().add(description);
        } else {
            Set<LiteralValue> values = new HashSet<LiteralValue>();
            values.add(description);
            setDescriptions(values);
        }
        return this;
    }

    public MetaInfo addDescription(String description) {
        addDescription(new LiteralValue<String>(description));
        return this;
    }

    public Set<LiteralValue> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(Set<LiteralValue> identifiers) {
        this.identifiers = identifiers;
    }

    public void addIdentifier(LiteralValue identifier) {
        if (getIdentifiers() != null) {
            getIdentifiers().add(identifier);
        }
    }

    public Set<ResourceValue> getSameAs() {
        return sameAs;
    }

    public void setSameAs(Set<ResourceValue> values) {
        this.sameAs = values;
    }

    public void addSameAs(ResourceValue value) {
        if (getSameAs() != null) {
            getSameAs().add(value);
        } else {
            Set<ResourceValue> values = new HashSet<ResourceValue>();
            values.add(value);
            setSameAs(values);
        }
    }

    public Set<ResourceValue> getSeeAlso() {
        return seeAlso;
    }

    public void setSeeAlso(Set<ResourceValue> values) {
        this.seeAlso = values;
    }

    public void addSeeAlso(ResourceValue value) {
        if (getSeeAlso() != null) {
            getSeeAlso().add(value);
        } else {
            Set<ResourceValue> values = new HashSet<ResourceValue>();
            values.add(value);
            setSeeAlso(values);
        }
    }

    public Set<LiteralValue> getTitles() {
        return titles;
    }

    public void setTitles(Set<LiteralValue> values) {
        this.titles = values;
    }

    public void addTitle(String value) {
        addTitle(new LiteralValue<String>(value, XSDDatatype.XSDstring));
    }

    public void addTitle(LiteralValue value) {
        if (getTitles() != null) {
            getTitles().add(value);
        } else {
            Set<LiteralValue> values = new HashSet<LiteralValue>();
            values.add(value);
            setTitles(values);
        }
    }

    public Set<LiteralValue> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<LiteralValue> subjects) {
        this.subjects = subjects;
    }

    public void addSubject(LiteralValue subject) {
        if (getSubjects() != null) {
            getSubjects().add(subject);
        } else {
            Set<LiteralValue> values = new HashSet<LiteralValue>();
            values.add(subject);
            setSubjects(values);
        }
    }

    public Set<LiteralValue> getPublishers() {
        return publishers;
    }

    public void setPublishers(Set<LiteralValue> publishers) {
        this.publishers = publishers;
    }

    public void addPublishers(LiteralValue publisher) {
        if (getPublishers() != null) {
            getPublishers().add(publisher);
        } else {
            Set<LiteralValue> values = new HashSet<LiteralValue>();
            values.add(publisher);
            setPublishers(values);
        }
    }

    public Set<LiteralValue> getCreators() {
        return creators;
    }

    public void setCreators(Set<LiteralValue> creators) {
        this.creators = creators;
    }

    public void addCreator(LiteralValue creator) {
        if (getCreators() != null) {
            getCreators().add(creator);
        } else {
            Set<LiteralValue> values = new HashSet<LiteralValue>();
            values.add(creator);
            setCreators(values);
        }
    }

    public Set<ResourceValue> getHasSources() {
        return hasSources;
    }

    public void setHasSources(Set<ResourceValue> hasSources) {
        this.hasSources = hasSources;
    }

    public void addHasSource(ResourceValue hasSource) {
        if (getHasSources() != null) {
            getHasSources().add(hasSource);
        } else {
            Set<ResourceValue> values = new HashSet<ResourceValue>();
            values.add(hasSource);
            setHasSources(hasSources);
        }
    }

    public Set<LiteralValue> getContributors() {
        return contributors;
    }

    public void setContributors(Set<LiteralValue> contributors) {
        this.contributors = contributors;
    }

    public void addContributor(LiteralValue contributor) {
        if (getContributors() != null) {
            getContributors().add(contributor);
        } else {
            Set<LiteralValue> values = new HashSet<LiteralValue>();
            values.add(contributor);
            setContributors(values);
        }
    }

    public Set<LiteralValue> getAudiences() {
        return audiences;
    }

    public void setAudiences(Set<LiteralValue> audiences) {
        this.audiences = audiences;
    }

    public void addAudience(LiteralValue audience) {
        if (getAudiences() != null) {
            getAudiences().add(audience);
        } else {
            Set<LiteralValue> values = new HashSet<LiteralValue>();
            values.add(audience);
            setAudiences(values);
        }
    }

    public LiteralValue getDate() {
        return date;
    }

    public void setDate(LiteralValue date) {
        this.date = date;
    }

    public void setDateCurrent() {
        Date currentDate = new Date(System.currentTimeMillis());
        setDate(new LiteralValue<Date>(currentDate, XSDDatatype.XSDdate));
    }

    public void addIdentifier(String identifier) {
        addIdentifier(new LiteralValue<String>(identifier, XSDDatatype.XSDstring));
    }

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

    public void setHash(long hash) {
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
        if (this.identifiers != other.identifiers && (this.identifiers == null || !this.identifiers.equals(other.identifiers))) {
            return false;
        }
        if (this.comments != other.comments && (this.comments == null || !this.comments.equals(other.comments))) {
            return false;
        }
        if (this.descriptions != other.descriptions && (this.descriptions == null || !this.descriptions.equals(other.descriptions))) {
            return false;
        }
        if (this.titles != other.titles && (this.titles == null || !this.titles.equals(other.titles))) {
            return false;
        }
        if (this.subjects != other.subjects && (this.subjects == null || !this.subjects.equals(other.subjects))) {
            return false;
        }
        if (this.publishers != other.publishers && (this.publishers == null || !this.publishers.equals(other.publishers))) {
            return false;
        }
        if (this.creators != other.creators && (this.creators == null || !this.creators.equals(other.creators))) {
            return false;
        }
        if (this.contributors != other.contributors && (this.contributors == null || !this.contributors.equals(other.contributors))) {
            return false;
        }
        if (this.audiences != other.audiences && (this.audiences == null || !this.audiences.equals(other.audiences))) {
            return false;
        }
        if (this.date != other.date && (this.date == null || !this.date.equals(other.date))) {
            return false;
        }
        if (this.sameAs != other.sameAs && (this.sameAs == null || !this.sameAs.equals(other.sameAs))) {
            return false;
        }
        if (this.seeAlso != other.seeAlso && (this.seeAlso == null || !this.seeAlso.equals(other.seeAlso))) {
            return false;
        }
        if (this.hasSources != other.hasSources && (this.hasSources == null || !this.hasSources.equals(other.hasSources))) {
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
}
