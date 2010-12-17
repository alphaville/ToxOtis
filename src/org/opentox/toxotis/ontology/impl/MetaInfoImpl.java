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
    private static final String DUBLIN_CORE_DOC = "http://dublincore.org/documents/usageguide/elements.shtml#%s";
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

    public Set<LiteralValue> getComments() {
        return comments;
    }

    public MetaInfo setComments(Set<LiteralValue> comments) {
        this.comments = comments;
        return this;
    }

    public MetaInfo addComment(String... comment) {
        for (String s : comment) {
            addComment(new LiteralValue<String>(s));
        }
        return this;
    }

    public MetaInfo addComment(LiteralValue... comment) {
        if (this.comments != null) {
            for (LiteralValue lvc : comment) {
                this.comments.add(lvc);
            }
        } else {
            Set<LiteralValue> values = new HashSet<LiteralValue>();
            for (LiteralValue lvc : comment) {
                values.add(lvc);
            }
            setComments(values);
        }
        return this;
    }

    public Set<LiteralValue> getDescriptions() {
        return this.descriptions;
    }

    public MetaInfo setDescriptions(Set<LiteralValue> descriptions) {
        this.descriptions = descriptions;
        return this;
    }

    public MetaInfo addDescription(LiteralValue... description) {
        if (getDescriptions() != null) {
            for (LiteralValue lv : description) {
                getDescriptions().add(lv);
            }

        } else {
            Set<LiteralValue> values = new HashSet<LiteralValue>();
            for (LiteralValue lv : description) {
                values.add(lv);
            }
            setDescriptions(values);
        }
        return this;
    }

    public MetaInfo addDescription(String... description) {
        for (String s : description) {
            addDescription(new LiteralValue<String>(s));
        }
        return this;
    }

    public Set<LiteralValue> getIdentifiers() {
        return identifiers;
    }

    public MetaInfo setIdentifiers(Set<LiteralValue> identifiers) {
        this.identifiers = identifiers;
        return this;
    }

    public MetaInfo addIdentifier(LiteralValue... identifier) {
        if (getIdentifiers() != null) {
            for (LiteralValue lv : identifier) {
                getIdentifiers().add(lv);
            }
        } else {
            Set<LiteralValue> values = new HashSet<LiteralValue>();
            for (LiteralValue lv : identifier) {
                values.add(lv);
            }
            setIdentifiers(values);
        }
        return this;
    }

    public MetaInfo addIdentifier(String... identifier) {
        for (String s : identifier) {
            addIdentifier(new LiteralValue<String>(s));
        }
        return this;
    }

    public Set<ResourceValue> getSameAs() {
        return sameAs;
    }

    public MetaInfo setSameAs(Set<ResourceValue> values) {
        this.sameAs = values;
        return this;
    }

    public MetaInfo addSameAs(ResourceValue value) {
        if (getSameAs() != null) {
            getSameAs().add(value);
        } else {
            Set<ResourceValue> values = new HashSet<ResourceValue>();
            values.add(value);
            setSameAs(values);
        }
        return this;
    }

    public Set<ResourceValue> getSeeAlso() {
        return seeAlso;
    }

    public MetaInfo setSeeAlso(Set<ResourceValue> values) {
        this.seeAlso = values;
        return this;
    }

    public MetaInfo addSeeAlso(ResourceValue value) {
        if (getSeeAlso() != null) {
            getSeeAlso().add(value);
        } else {
            Set<ResourceValue> values = new HashSet<ResourceValue>();
            values.add(value);
            setSeeAlso(values);
        }
        return this;
    }

    public Set<LiteralValue> getTitles() {
        return titles;
    }

    public MetaInfo setTitles(Set<LiteralValue> values) {
        this.titles = values;
        return this;
    }

    public MetaInfo addTitle(String value) {
        addTitle(new LiteralValue<String>(value, XSDDatatype.XSDstring));
        return this;
    }

    public MetaInfo addTitle(LiteralValue... value) {
        if (getTitles() != null) {
            for (LiteralValue lv : value) {
                getTitles().add(lv);
            }
        } else {
            Set<LiteralValue> values = new HashSet<LiteralValue>();
            for (LiteralValue lv : value) {
                values.add(lv);
            }
            setTitles(values);
        }
        return this;
    }

    public MetaInfo addTitle(String... value) {
        for (String s : value) {
            addTitle(new LiteralValue<String>(s));
        }
        return this;
    }

    public Set<LiteralValue> getSubjects() {
        return subjects;
    }

    public MetaInfo setSubjects(Set<LiteralValue> subjects) {
        this.subjects = subjects;
        return this;
    }

    public MetaInfo addSubject(LiteralValue... subject) {
        if (getSubjects() != null) {
            for (LiteralValue lv : subject) {
                getSubjects().add(lv);
            }
        } else {
            Set<LiteralValue> values = new HashSet<LiteralValue>();
            for (LiteralValue lv : subject) {
                values.add(lv);
            }
            setSubjects(values);
        }
        return this;
    }

    public MetaInfo addSubject(String... subject) {
        for (String x : subject) {
            addSubject(new LiteralValue<String>(x));
        }
        return this;
    }

    public Set<LiteralValue> getPublishers() {
        return publishers;
    }

    public MetaInfo setPublishers(Set<LiteralValue> publishers) {
        this.publishers = publishers;
        return this;
    }

    public MetaInfo addPublisher(LiteralValue... publisher) {
        if (getPublishers() != null) {
            for (LiteralValue lv : publisher) {
                getPublishers().add(lv);
            }
        } else {
            Set<LiteralValue> values = new HashSet<LiteralValue>();
            for (LiteralValue lv : publisher) {
                values.add(lv);
            }
            setPublishers(values);
        }
        return this;
    }

    public MetaInfo addPublisher(String... publisher) {
        for (String x : publisher) {
            addPublisher(new LiteralValue<String>(x));
        }
        return this;
    }

    public Set<LiteralValue> getCreators() {
        return creators;
    }

    public MetaInfo setCreators(Set<LiteralValue> creators) {
        this.creators = creators;
        return this;
    }

    public MetaInfo addCreator(LiteralValue... creator) {
        if (getCreators() != null) {
            for (LiteralValue lv : creator) {
                getCreators().add(lv);
            }
        } else {
            Set<LiteralValue> values = new HashSet<LiteralValue>();
            for (LiteralValue lv : creator) {
                values.add(lv);
            }
            setCreators(values);
        }
        return this;
    }

    public MetaInfo addCreator(String... creator) {
        for (String x : creator) {
            addCreator(new LiteralValue<String>(x));
        }
        return this;
    }

    public Set<ResourceValue> getHasSources() {
        return hasSources;
    }

    public MetaInfo setHasSources(Set<ResourceValue> hasSources) {
        this.hasSources = hasSources;
        return this;
    }

    public MetaInfo addHasSource(ResourceValue hasSource) {
        if (getHasSources() != null) {
            getHasSources().add(hasSource);
        } else {
            Set<ResourceValue> values = new HashSet<ResourceValue>();
            values.add(hasSource);
            setHasSources(values);
        }
        return this;
    }

    public Set<LiteralValue> getContributors() {
        return contributors;
    }

    public MetaInfo setContributors(Set<LiteralValue> contributors) {
        this.contributors = contributors;
        return this;
    }

    public MetaInfo addContributor(LiteralValue... contributor) {
        if (getContributors() != null) {
            for (LiteralValue lv : contributor) {
                getContributors().add(lv);
            }
        } else {
            Set<LiteralValue> values = new HashSet<LiteralValue>();
            for (LiteralValue lv : contributor) {
                values.add(lv);
            }
            setContributors(values);
        }
        return this;
    }

    public MetaInfo addContributor(String... contributor) {
        for (String x : contributor) {
            addContributor(new LiteralValue<String>(x));
        }
        return this;
    }

    public Set<LiteralValue> getAudiences() {
        return audiences;
    }

    public MetaInfo setAudiences(Set<LiteralValue> audiences) {
        this.audiences = audiences;
        return this;
    }

    public MetaInfo addAudience(LiteralValue... audience) {
        if (getAudiences() != null) {
            for (LiteralValue lv : audience) {
                getAudiences().add(lv);
            }
        } else {
            Set<LiteralValue> values = new HashSet<LiteralValue>();
            for (LiteralValue lv : audience) {
                values.add(lv);
            }
            setAudiences(values);
        }
        return this;
    }

    public MetaInfo addAudience(String... audience) {
        for (String x : audience) {
            addAudience(new LiteralValue<String>(x));
        }
        return this;
    }

    public LiteralValue getDate() {
        return date;
    }

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
                    setTextAtCursor(createHtmlList(creators));
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

    private static String createHtmlList(Set<LiteralValue> values) {
        if (values.size() == 0) {
            return "";
        } else if (values.size() == 1) {
            return HTMLUtils.linkUrlsInText(values.iterator().next().getValueAsString());
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append("<ol>\n");
            for (LiteralValue lv : values) {
                builder.append("<li>");
                builder.append(HTMLUtils.linkUrlsInText(lv.getValueAsString()));
                builder.append("</li>");
            }
            builder.append("</ol>");
            return builder.toString();
        }
    }

    private static String createHtmlList2(Set<ResourceValue> values) {
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

    public boolean isEmpty() {
        if (getAudiences()!=null && !getAudiences().isEmpty()){
            return false;
        }
        if (getComments()!=null && !getComments().isEmpty()){
            return false;
        }
        if (getContributors()!=null && !getContributors().isEmpty()){
            return false;
        }
        if (getCreators()!=null && !getCreators().isEmpty()){
            return false;
        }
        if (getDescriptions()!=null && !getDescriptions().isEmpty()){
            return false;
        }
        if (getHasSources()!=null && !getHasSources().isEmpty()){
            return false;
        }
        if (getIdentifiers()!=null && !getIdentifiers().isEmpty()){
            return false;
        }
        if (getPublishers()!=null && !getPublishers().isEmpty()){
            return false;
        }
        if (getSameAs()!=null && !getSameAs().isEmpty()){
            return false;
        }
        if (getSeeAlso()!=null && !getSeeAlso().isEmpty()){
            return false;
        }
        if (getSubjects()!=null && !getSubjects().isEmpty()){
            return false;
        }
        if (getTitles()!=null && !getTitles().isEmpty()){
            return false;
        }
        if (getDate()!=null){
            return false;
        }
        return true;
    }
}
