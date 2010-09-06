package org.opentox.toxotis.ontology.impl;

import java.util.ArrayList;
import java.util.Collection;
import org.opentox.toxotis.ontology.MetaInfo;

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


}
