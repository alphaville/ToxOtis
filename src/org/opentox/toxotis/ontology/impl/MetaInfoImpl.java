package org.opentox.toxotis.ontology.impl;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;
import org.opentox.toxotis.util.spiders.TypedValue;

public class MetaInfoImpl implements MetaInfo {

    private TypedValue<String> title;
    private TypedValue<String> description;
    private TypedValue<String> identifier;
    private TypedValue<String> comment;
    private TypedValue<String> sameAs;
    private TypedValue<String> seeAlso;
    private TypedValue<String> versionInfo;
    private TypedValue<String> creator;
    private TypedValue<String> publisher;
    private TypedValue<String> hasSource;
    private TypedValue<String> subject;
    private TypedValue<Date> date;
    private Collection<TypedValue<String>> contributors = new ArrayList<TypedValue<String>>();
    private Collection<TypedValue<String>> audiences = new ArrayList<TypedValue<String>>();

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
        if (comment != null) {
            builder.append("comment     : " + comment + "\n");
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
            resource.addLiteral(DC.identifier.inModel(model).as(Property.class), model.createTypedLiteral(identifier.getValue(), XSDDatatype.XSDanyURI));
        }
        if (title != null) {
            resource.addLiteral(DC.title.inModel(model).as(Property.class), model.createTypedLiteral(title.getValue()));
        }
        if (description != null) {
            resource.addLiteral(DC.description.inModel(model).as(Property.class), model.createTypedLiteral(description.getValue()));
        }
        if (sameAs != null) {
            resource.addLiteral(OWL.sameAs.inModel(model).as(Property.class), model.createTypedLiteral(sameAs.getValue(), XSDDatatype.XSDanyURI));
        }
        if (seeAlso != null) {
            resource.addLiteral(RDFS.seeAlso.inModel(model).as(Property.class), model.createTypedLiteral(seeAlso.getValue(), XSDDatatype.XSDanyURI));
        }
        if (publisher != null) {
            resource.addLiteral(DC.publisher.inModel(model).as(Property.class), model.createTypedLiteral(publisher.getValue()));
        }
        if (creator != null) {
            resource.addLiteral(DC.creator.inModel(model).as(Property.class), model.createTypedLiteral(creator.getValue()));
        }
        if (subject != null) {
            resource.addLiteral(DC.subject.inModel(model).as(Property.class), model.createTypedLiteral(subject.getValue()));
        }
        if (hasSource != null) {
            /*
             * Note: hasSource is an Object property that according to the OpenTox ontology
             * is a mapping from ot:Feature ot ot:Dataset or ot:Dataset or ot:Model.
             */
            resource.addProperty(OTObjectProperties.hasSource().asObjectProperty(model), model.createResource(hasSource.getValue()));
        }
        if (comment != null) {
            resource.addLiteral(RDFS.comment.inModel(model).as(Property.class), model.createTypedLiteral(comment.getValue(), XSDDatatype.XSDstring));
        }
        if (versionInfo != null) {
            resource.addLiteral(OWL.versionInfo.inModel(model).as(Property.class), model.createTypedLiteral(versionInfo.getValue()));
        }

        Property contributorProperty = model.createProperty(DC.contributor.getURI());
        if (contributors != null && !contributors.isEmpty()) {
            for (TypedValue<String> contr : contributors) {
                resource.addLiteral(contributorProperty, model.createTypedLiteral(contr != null ? contr.getValue() : null));
            }
        }
        return resource;
    }

    public TypedValue<String> getComment() {
        return comment;
    }

    public TypedValue<String> getDescription() {
        return description;
    }

    public TypedValue<String> getIdentifier() {
        return identifier;
    }

    public TypedValue<String> getSameAs() {
        return sameAs;
    }

    public TypedValue<String> getSeeAlso() {
        return seeAlso;
    }

    public TypedValue<String> getTitle() {
        return title;
    }

    public TypedValue<String> getVersionInfo() {
        return versionInfo;
    }

    public TypedValue<String> getPublisher() {
        return publisher;
    }

    public TypedValue<String> getCreator() {
        return creator;
    }

    public TypedValue<String> getHasSource() {
        return hasSource;
    }

    public Collection<TypedValue<String>> getContributors() {
        return contributors;
    }

    public Collection<TypedValue<String>> getAudiences() {
        return audiences;
    }

    public MetaInfo setComment(String comment) {
        this.comment = comment != null ? new TypedValue<String>(comment) : null;
        return this;
    }

    public MetaInfo setComment(TypedValue<String> comment) {
        this.comment = comment;
        return this;
    }

    public MetaInfo setDescription(String description) {
        this.description = description != null ? new TypedValue<String>(description) : null;
        return this;
    }

    public MetaInfo setDescription(TypedValue<String> description) {
        this.description = description;
        return this;
    }

    public MetaInfo setIdentifier(String identifier) {
        this.identifier = identifier != null ? new TypedValue<String>(identifier) : null;
        return this;
    }

    public MetaInfo setIdentifier(TypedValue<String> identifier) {
        this.identifier = identifier;
        return this;
    }

    public MetaInfo setSameAs(String sameAs) {
        this.sameAs = sameAs != null ? new TypedValue<String>(sameAs) : null;
        return this;
    }

    public MetaInfo setSameAs(TypedValue<String> sameAs) {
        this.sameAs = sameAs;
        return this;
    }

    public MetaInfo setSeeAlso(String seeAlso) {
        this.seeAlso = seeAlso != null ? new TypedValue<String>(seeAlso) : null;
        return this;
    }

    public MetaInfo setSeeAlso(TypedValue<String> seeAlso) {
        this.seeAlso = seeAlso;
        return this;
    }

    public MetaInfo setTitle(String title) {
        this.title = title != null ? new TypedValue<String>(title) : null;
        return this;
    }

    public MetaInfo setTitle(TypedValue<String> title) {
        this.title = title;
        return this;
    }

    public MetaInfo setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo != null ? new TypedValue<String>(versionInfo) : null;
        return this;
    }

    public MetaInfo setVersionInfo(TypedValue<String> versionInfo) {
        this.versionInfo = versionInfo;
        return this;
    }

    public MetaInfo setPublisher(String publisher) {
        this.comment = publisher != null ? new TypedValue<String>(publisher) : null;
        return this;
    }

    public MetaInfo setPublisher(TypedValue<String> publisher) {
        this.publisher = publisher;
        return this;
    }

    public MetaInfo setCreator(String creator) {
        this.creator = creator != null ? new TypedValue<String>(creator) : null;
        return this;
    }

    public MetaInfo setCreator(TypedValue<String> creator) {
        this.creator = creator;
        return this;
    }

    public MetaInfo setHasSource(String hasSource) {
        this.hasSource = hasSource != null ? new TypedValue<String>(hasSource) : null;
        return this;
    }

    public MetaInfo setHasSource(TypedValue<String> hasSource) {
        this.hasSource = hasSource;
        return this;
    }

    public MetaInfo addContributor(String contributor) {
        if (contributor != null) {
            this.contributors.add(new TypedValue<String>(contributor));
        }
        return this;
    }

    public MetaInfo addContributor(TypedValue<String> contributor) {
        if (contributor != null) {
            this.contributors.add(contributor);
        }
        return this;
    }

    public MetaInfo addAudience(String audience) {
        if (audience != null) {
            this.audiences.add(new TypedValue<String>(audience));
        }
        return this;
    }

    public MetaInfo addAudience(TypedValue<String> audience) {
        if (audience != null) {
            this.audiences.add(audience);
        }

        return this;
    }

    public TypedValue<Date> getDate() {
        return date;
    }

    public MetaInfo setDate(TypedValue<Date> date) {
        this.date = date;
        return this;
    }

    public MetaInfo setDate(Date date) {
        this.date = date != null ? new TypedValue<Date>(date) : null;
        return this;
    }

    public TypedValue<String> getSubject() {
        return subject;
    }

    public MetaInfo setSubject(String subject) {
        this.subject = subject != null ? new TypedValue<String>(subject) : null;
        return this;
    }

    public MetaInfo setSubject(TypedValue<String> subject) {
        this.subject = subject;
        return this;
    }
}
