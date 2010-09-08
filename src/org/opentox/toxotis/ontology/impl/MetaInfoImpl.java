package org.opentox.toxotis.ontology.impl;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.DC_11;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;
import java.util.ArrayList;
import java.util.Collection;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

public class MetaInfoImpl implements MetaInfo {

    private String title;
    private String description;
    private String identifier;
    private String comment;
    private String sameAs;
    private String seeAlso;
    private String versionInfo;
    private String creator;
    private String publisher;
    private String hasSource;
    private Collection<String> contributors = new ArrayList<String>();
    private Collection<String> audiences = new ArrayList<String>();

    public MetaInfoImpl() {
    }

    @Override
    public String getVersionInfo() {
        return versionInfo;
    }

    @Override
    public void setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getSameAs() {
        return sameAs;
    }

    @Override
    public void setSameAs(String sameAs) {
        this.sameAs = sameAs;
    }

    @Override
    public String getSeeAlso() {
        return seeAlso;
    }

    @Override
    public void setSeeAlso(String seeAlso) {
        this.seeAlso = seeAlso;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Collection<String> getAudiences() {
        return audiences;
    }

    @Override
    public void addAudience(String audience) {
        if (audiences == null) {
            audiences = new ArrayList<String>();
        }
        audiences.add(audience);
    }

    @Override
    public String getPublisher() {
        return publisher;
    }

    @Override
    public String getCreator() {
        return creator;
    }

    @Override
    public Collection<String> getContributors() {
        return contributors;
    }

    @Override
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Override
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public void addContributor(String contributor) {
        if (contributors == null) {
            contributors = new ArrayList<String>();
        }
        contributors.add(contributor);
    }

    @Override
    public String getHasSource() {
        return hasSource;
    }

    @Override
    public void setHasSource(String hasSource) {
        this.hasSource = hasSource;
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
        return new String(builder);
    }

    public Resource attachTo(Resource resource, OntModel model) {
        if (identifier != null) {
            resource.addLiteral(DC.identifier.inModel(model).as(Property.class), model.createTypedLiteral(identifier, XSDDatatype.XSDanyURI));
        }
        if (title != null) {
            resource.addLiteral(DC.title.inModel(model).as(Property.class), model.createTypedLiteral(title));
        }
        if (description != null) {
            resource.addLiteral(DC.description.inModel(model).as(Property.class), model.createTypedLiteral(description));
        }
        if (sameAs != null) {
            resource.addLiteral(OWL.sameAs.inModel(model).as(Property.class), model.createTypedLiteral(sameAs, XSDDatatype.XSDanyURI));
        }
        if (seeAlso != null) {
            resource.addLiteral(RDFS.seeAlso.inModel(model).as(Property.class), model.createTypedLiteral(seeAlso, XSDDatatype.XSDanyURI));
        }
        if (publisher != null) {
            resource.addLiteral(DC.publisher.inModel(model).as(Property.class), model.createTypedLiteral(publisher));
        }
        if (creator != null) {
            resource.addLiteral(DC.creator.inModel(model).as(Property.class), model.createTypedLiteral(creator));
        }
        if (hasSource != null) {
            /*
             * Note: hasSource is an Object property that according to the OpenTox ontology
             * is a mapping from ot:Feature ot ot:Dataset or ot:Dataset or ot:Model.
             */
            resource.addProperty(OTObjectProperties.hasSource().asObjectProperty(model), model.createResource(hasSource));
        }
        if (comment != null) {
            resource.addLiteral(RDFS.comment.inModel(model).as(Property.class), model.createTypedLiteral(comment, XSDDatatype.XSDstring));
        }
        if (versionInfo != null) {
            resource.addLiteral(OWL.versionInfo.inModel(model).as(Property.class), model.createTypedLiteral(versionInfo));
        }

        Property contributorProperty = model.createProperty(DC.contributor.getURI());
        if (contributors != null && !contributors.isEmpty()) {
            for (String contr : contributors) {
                resource.addLiteral(contributorProperty, model.createTypedLiteral(contr));
            }
        }
        return resource;
    }

    // <editor-fold defaultstate="collapsed" desc="Equals and hashCode">
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MetaInfoImpl other = (MetaInfoImpl) obj;
        if ((this.title == null) ? (other.title != null) : !this.title.equals(other.title)) {
            return false;
        }
        if ((this.description == null) ? (other.description != null) : !this.description.equals(other.description)) {
            return false;
        }
        if ((this.identifier == null) ? (other.identifier != null) : !this.identifier.equals(other.identifier)) {
            return false;
        }
        if ((this.comment == null) ? (other.comment != null) : !this.comment.equals(other.comment)) {
            return false;
        }
        if ((this.sameAs == null) ? (other.sameAs != null) : !this.sameAs.equals(other.sameAs)) {
            return false;
        }
        if ((this.seeAlso == null) ? (other.seeAlso != null) : !this.seeAlso.equals(other.seeAlso)) {
            return false;
        }
        if ((this.versionInfo == null) ? (other.versionInfo != null) : !this.versionInfo.equals(other.versionInfo)) {
            return false;
        }
        if ((this.creator == null) ? (other.creator != null) : !this.creator.equals(other.creator)) {
            return false;
        }
        if ((this.publisher == null) ? (other.publisher != null) : !this.publisher.equals(other.publisher)) {
            return false;
        }
        if ((this.hasSource == null) ? (other.hasSource != null) : !this.hasSource.equals(other.hasSource)) {
            return false;
        }
        if (this.contributors != other.contributors && (this.contributors == null || !this.contributors.equals(other.contributors))) {
            return false;
        }
        if (this.audiences != other.audiences && (this.audiences == null || !this.audiences.equals(other.audiences))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.title != null ? this.title.hashCode() : 0);
        hash = 29 * hash + (this.description != null ? this.description.hashCode() : 0);
        hash = 29 * hash + (this.identifier != null ? this.identifier.hashCode() : 0);
        hash = 29 * hash + (this.comment != null ? this.comment.hashCode() : 0);
        hash = 29 * hash + (this.sameAs != null ? this.sameAs.hashCode() : 0);
        hash = 29 * hash + (this.seeAlso != null ? this.seeAlso.hashCode() : 0);
        hash = 29 * hash + (this.versionInfo != null ? this.versionInfo.hashCode() : 0);
        hash = 29 * hash + (this.creator != null ? this.creator.hashCode() : 0);
        hash = 29 * hash + (this.publisher != null ? this.publisher.hashCode() : 0);
        hash = 29 * hash + (this.hasSource != null ? this.hasSource.hashCode() : 0);
        hash = 29 * hash + (this.contributors != null ? this.contributors.hashCode() : 0);
        hash = 29 * hash + (this.audiences != null ? this.audiences.hashCode() : 0);
        return hash;
    }// </editor-fold>

    
}
