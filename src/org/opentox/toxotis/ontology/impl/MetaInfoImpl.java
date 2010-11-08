package org.opentox.toxotis.ontology.impl;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.iri.IRIException;
import com.hp.hpl.jena.iri.IRIFactory;
import com.hp.hpl.jena.ontology.AnnotationProperty;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import javax.xml.stream.XMLStreamException;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;
import org.opentox.toxotis.util.spiders.AnyValue;

public class MetaInfoImpl implements MetaInfo {

    private AnyValue<String> title;
    private AnyValue<String> description;
    private AnyValue<String> identifier;
    private Collection<AnyValue<String>> comment = new ArrayList<AnyValue<String>>();
    private AnyValue<String> sameAs;
    private AnyValue<String> seeAlso;
    private AnyValue<String> versionInfo;
    private AnyValue<String> creator;
    private AnyValue<String> publisher;
    private AnyValue<String> hasSource;
    private AnyValue<String> subject;
    private AnyValue<Date> date;
    private Collection<AnyValue<String>> contributors = new ArrayList<AnyValue<String>>();
    private Collection<AnyValue<String>> audiences = new ArrayList<AnyValue<String>>();

    public MetaInfoImpl() {
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (identifier != null) {
            builder.append("identifier  : " + identifier + "\n");
        }
        if (title != null) {
            builder.append("title       : " + title + "\n");
        }
        if (description != null) {
            builder.append("description : " + description + "\n");
        }
        if (date != null) {
            builder.append("date        : " + date + "\n");
        }
        if (sameAs != null) {
            builder.append("same as     : " + sameAs + "\n");
        }
        if (seeAlso != null) {
            builder.append("see Also    : " + seeAlso + "\n");
        }
        if (publisher != null) {
            builder.append("publisher   : " + publisher + "\n");
        }
        if (creator != null) {
            builder.append("creator     : " + creator + "\n");
        }
        if (hasSource != null) {
            builder.append("has source  : " + hasSource + "\n");
        }
        if (comment != null && !comment.isEmpty()) {
            Iterator<AnyValue<String>> commentIterator = comment.iterator();
            while (commentIterator.hasNext()) {
                builder.append("comment     : " + commentIterator.next() + "\n");
            }
        }
        if (versionInfo != null) {
            builder.append("version info: " + versionInfo + "\n");
        }
        if (subject != null) {
            builder.append("subject     : " + subject + "\n");
        }
        return new String(builder);
    }

    public Resource attachTo(Resource resource, OntModel model) {
        if (identifier != null) {
            AnnotationProperty identProp = model.getAnnotationProperty(DC.identifier.getURI());
            if (identProp == null) {
                identProp = model.createAnnotationProperty(DC.identifier.getURI());
            }
            resource.addLiteral(identProp, model.createTypedLiteral(identifier.getValue(), XSDDatatype.XSDanyURI));
        }
        if (title != null) {
            AnnotationProperty titleProp = model.getAnnotationProperty(DC.title.getURI());
            if (titleProp == null) {
                titleProp = model.createAnnotationProperty(DC.title.getURI());
            }
            resource.addLiteral(titleProp, model.createTypedLiteral(title.getValue()));
        }
        if (description != null) {
            AnnotationProperty descriptionProp = model.getAnnotationProperty(DC.description.getURI());
            if (descriptionProp == null) {
                descriptionProp = model.createAnnotationProperty(DC.description.getURI());
            }
            resource.addLiteral(descriptionProp, model.createTypedLiteral(description.getValue()));
        }
        if (sameAs != null) {
            Property sameAsProp = model.getProperty(OWL.sameAs.getURI());
            if (sameAsProp == null) {
                sameAsProp = model.createProperty(OWL.sameAs.getURI());
            }
            Resource sameAsResource = sameAs.isLiteral() ? OWL.Thing : sameAs.getClassType().inModel(model);
            resource.addProperty(sameAsProp, model.createResource(sameAs.getValue(),
                    sameAsResource));
        }
        if (seeAlso != null) {
            AnnotationProperty seeAlsoProp = model.getAnnotationProperty(RDFS.seeAlso.getURI());
            if (seeAlsoProp == null) {
                seeAlsoProp = model.createAnnotationProperty(RDFS.seeAlso.getURI());
            }
            resource.addLiteral(seeAlsoProp, model.createTypedLiteral(seeAlso.getValue(), XSDDatatype.XSDanyURI));
        }
        if (publisher != null) {
            AnnotationProperty publisherProp = model.getAnnotationProperty(DC.publisher.getURI());
            if (publisherProp == null) {
                publisherProp = model.createAnnotationProperty(DC.publisher.getURI());
            }
            resource.addLiteral(publisherProp, model.createTypedLiteral(publisher.getValue()));
        }
        if (creator != null) {
            AnnotationProperty creatorProp = model.getAnnotationProperty(DC.creator.getURI());
            if (creatorProp == null) {
                creatorProp = model.createAnnotationProperty(DC.creator.getURI());
            }
            resource.addLiteral(creatorProp, model.createTypedLiteral(creator.getValue()));
        }
        if (subject != null) {
            resource.addLiteral(DC.subject.inModel(model).as(Property.class), model.createTypedLiteral(subject.getValue()));
        }
        if (hasSource != null) {
            /*
             * Note: hasSource is an Object property that according to the OpenTox ontology
             * is a mapping from ot:Feature ot ot:Dataset or ot:Dataset or ot:Model.
             */
            ObjectProperty sameProp = model.createObjectProperty(OTObjectProperties.hasSource().getUri());
            try {
                IRIFactory.semanticWebImplementation().construct(hasSource.getValue());
                /*
                 * TODO: In the following line change 'OTClasses.OpenToxResource().inModel(model))' with
                 * the right Ontological Class in every case according to the value of hasSource...
                 */
                resource.addProperty(sameProp, model.createResource(hasSource.getValue(),
                        hasSource.isLiteral() ? OTClasses.OpenToxResource().inModel(model) : hasSource.getClassType().inModel(model)));
            } catch (final IRIException ex) {
                // In this case the hasSource property is not assigned to the
                // corresponding node because it cannot be cast as an IRI.
                System.err.println("[WARNING] Cannot create a resource with identifier : '" + hasSource.getValue() + "'. Not a valid IRI!");
            }
        }
        if (comment != null && !comment.isEmpty()) {
            Iterator<AnyValue<String>> commentIterator = comment.iterator();
            while (commentIterator.hasNext()) {
                resource.addLiteral(RDFS.comment.inModel(model).as(Property.class),
                        model.createTypedLiteral(commentIterator.next().getValue(), XSDDatatype.XSDstring));
            }
        }
        if (versionInfo != null) {
            resource.addLiteral(OWL.versionInfo.inModel(model).as(Property.class),
                    model.createTypedLiteral(versionInfo.getValue()));
        }
        if (date != null) {
            AnnotationProperty dateProperty = model.createAnnotationProperty(DC.date.getURI());
            resource.addLiteral(dateProperty,
                    model.createTypedLiteral(date.getValue(), XSDDatatype.XSDstring));
        }
        AnnotationProperty contributorProperty = model.createAnnotationProperty(DC.contributor.getURI());
        if (contributors != null && !contributors.isEmpty()) {
            Iterator<AnyValue<String>> contrIt = contributors.iterator();
            while (contrIt.hasNext()) {
                resource.addLiteral(contributorProperty, contrIt.next());
            }
        }
        return resource;
    }

    public Collection<AnyValue<String>> getComment() {
        return comment;
    }

    public AnyValue<String> getDescription() {
        return description;
    }

    public AnyValue<String> getIdentifier() {
        return identifier;
    }

    public AnyValue<String> getSameAs() {
        return sameAs;
    }

    public AnyValue<String> getSeeAlso() {
        return seeAlso;
    }

    public AnyValue<String> getTitle() {
        return title;
    }

    public AnyValue<String> getVersionInfo() {
        return versionInfo;
    }

    public AnyValue<String> getPublisher() {
        return publisher;
    }

    public AnyValue<String> getCreator() {
        return creator;
    }

    public AnyValue<String> getHasSource() {
        return hasSource;
    }

    public Collection<AnyValue<String>> getContributors() {
        return contributors;
    }

    public Collection<AnyValue<String>> getAudiences() {
        return audiences;
    }

    public MetaInfo addComment(String comment) {
        this.comment.add(comment != null ? new AnyValue<String>(comment) : null);
        return this;
    }

    public MetaInfo addComment(AnyValue<String> comment) {
        this.comment.add(comment);
        return this;
    }

    public MetaInfo setDescription(String description) {
        this.description = description != null ? new AnyValue<String>(description) : null;
        return this;
    }

    public MetaInfo setDescription(AnyValue<String> description) {
        this.description = description;
        return this;
    }

    public MetaInfo setIdentifier(String identifier) {
        this.identifier = identifier != null ? new AnyValue<String>(identifier) : null;
        return this;
    }

    public MetaInfo setIdentifier(AnyValue<String> identifier) {
        this.identifier = identifier;
        return this;
    }

    public MetaInfo setSameAs(String sameAs) {
        this.sameAs = sameAs != null ? new AnyValue<String>(sameAs) : null;
        return this;
    }

    public MetaInfo setSameAs(AnyValue<String> sameAs) {
        this.sameAs = sameAs;
        return this;
    }

    public MetaInfo setSeeAlso(String seeAlso) {
        this.seeAlso = seeAlso != null ? new AnyValue<String>(seeAlso) : null;
        return this;
    }

    public MetaInfo setSeeAlso(AnyValue<String> seeAlso) {
        this.seeAlso = seeAlso;
        return this;
    }

    public MetaInfo setTitle(String title) {
        this.title = title != null ? new AnyValue<String>(title) : null;
        return this;
    }

    public MetaInfo setTitle(AnyValue<String> title) {
        this.title = title;
        return this;
    }

    public MetaInfo setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo != null ? new AnyValue<String>(versionInfo) : null;
        return this;
    }

    public MetaInfo setVersionInfo(AnyValue<String> versionInfo) {
        this.versionInfo = versionInfo;
        return this;
    }

    public MetaInfo setPublisher(String publisher) {
        this.publisher = publisher != null ? new AnyValue<String>(publisher) : null;
        return this;
    }

    public MetaInfo setPublisher(AnyValue<String> publisher) {
        this.publisher = publisher;
        return this;
    }

    public MetaInfo setCreator(String creator) {
        this.creator = creator != null ? new AnyValue<String>(creator) : null;
        return this;
    }

    public MetaInfo setCreator(AnyValue<String> creator) {
        this.creator = creator;
        return this;
    }

    public MetaInfo setHasSource(String hasSource) {
        this.hasSource = hasSource != null ? new AnyValue<String>(hasSource) : null;
        return this;
    }

    public MetaInfo setHasSource(AnyValue<String> hasSource) {
        this.hasSource = hasSource;
        return this;
    }

    public MetaInfo addContributor(String contributor) {
        if (contributor != null) {
            this.contributors.add(new AnyValue<String>(contributor));
        }
        return this;
    }

    public MetaInfo addContributor(AnyValue<String> contributor) {
        if (contributor != null) {
            this.contributors.add(contributor);
        }
        return this;
    }

    public MetaInfo addAudience(String audience) {
        if (audience != null) {
            this.audiences.add(new AnyValue<String>(audience));
        }
        return this;
    }

    public MetaInfo addAudience(AnyValue<String> audience) {
        if (audience != null) {
            this.audiences.add(audience);
        }

        return this;
    }

    public AnyValue<Date> getDate() {
        return date;
    }

    public MetaInfo setDate(AnyValue<Date> date) {
        this.date = date;
        return this;
    }

    public MetaInfo setDate(Date date) {
        this.date = date != null ? new AnyValue<Date>(date) : null;
        return this;
    }

    public AnyValue<String> getSubject() {
        return subject;
    }

    public MetaInfo setSubject(String subject) {
        this.subject = subject != null ? new AnyValue<String>(subject) : null;
        return this;
    }

    public MetaInfo setSubject(AnyValue<String> subject) {
        this.subject = subject;
        return this;
    }

    private void writeMetaDatumResourceToStAX(String metaDatumNS, String metaDatumName, AnyValue<?> value, javax.xml.stream.XMLStreamWriter writer) throws XMLStreamException {
        if (value != null && value.getValue() != null && !value.getValue().toString().isEmpty()) {
            String stringVal = value.getValue().toString();
            try {
                IRIFactory.semanticWebImplementation().construct(stringVal);
            } catch (IRIException iriEx) {
                stringVal = OTClasses.NS + stringVal;
            }
            writer.writeEmptyElement(metaDatumNS + ":" + metaDatumName);
            writer.writeAttribute("rdf:resource", stringVal);
        }
    }

    private void writeMetaDatumToStAX(String metaDatumNS, String metaDatumName, AnyValue<?> value, javax.xml.stream.XMLStreamWriter writer) throws XMLStreamException {
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
        writeMetaDatumToStAX("dc", "identifier", identifier, writer);
        if (comment != null && !comment.isEmpty()) {
            Iterator<AnyValue<String>> commentIterator = comment.iterator();
            while (commentIterator.hasNext()) {
                writeMetaDatumToStAX("rdfs", "comment", commentIterator.next(), writer);
            }
        }

        writeMetaDatumToStAX("rdfs", "creator", creator, writer);
        if (contributors != null) {
            for (AnyValue contrib : contributors) {
                writeMetaDatumToStAX("dc", "contributor", contrib, writer);
            }
        }

        writeMetaDatumToStAX("dc", "date", date, writer);
        writeMetaDatumToStAX("dc", "description", description, writer);
        writeMetaDatumResourceToStAX("ot", "hasSource", hasSource, writer);
        writeMetaDatumToStAX("dc", "publisher", publisher, writer);
        writeMetaDatumResourceToStAX("owl", "sameAs", sameAs, writer);
        writeMetaDatumToStAX("rdfs", "seeAlso", seeAlso, writer);
        writeMetaDatumToStAX("dc", "subject", subject, writer);
        writeMetaDatumToStAX("dc", "title", title, writer);
        writeMetaDatumToStAX("owl", "versionInfo", versionInfo, writer);
    }
}
