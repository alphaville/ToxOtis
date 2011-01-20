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

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import java.util.ArrayList;
import java.util.Collection;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.OTProperty;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTClasses;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class OTPropertyImpl implements OTProperty {

    private MetaInfo metaInfo = new MetaInfoImpl();
    private Collection<OntologicalClass> domain = new ArrayList<OntologicalClass>();
    private Collection<OTProperty> superProperties = new ArrayList<OTProperty>();
    private String ns = OTClasses.NS;
    private String name;

    public OTPropertyImpl() {
    }

    public OTPropertyImpl(String name) {
        this.name = name;
    }
    public OTPropertyImpl(String name, String nameSpace) {
        this.name = name;
        this.ns = nameSpace;
    }

    @Override
    public MetaInfo getMetaInfo() {
        return metaInfo;
    }

    @Override
    public void setMetaInfo(MetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    @Override
    public Collection<OntologicalClass> getDomain() {
        return domain;
    }

    @Override
    public void setDomain(Collection<OntologicalClass> domain) {
        this.domain = domain;
    }

    @Override
    public Collection<OTProperty> getSuperProperties() {
        return superProperties;
    }

    @Override
    public void setSuperProperties(Collection<OTProperty> superProperties) {
        this.superProperties = superProperties;
    }

    @Override
    public String getNameSpace() {
        return ns;
    }

    @Override
    public void setNameSpace(String ns) {
        this.ns = ns;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUri() {
        return getNameSpace() + getName();
    }

    @Override
    public Property getProperty(OntModel model) {
        return model.getProperty(getUri());
    }

    public Property asProperty(OntModel model) {
        Property property = null;
        property = model.getProperty(getUri());
        if (property != null) {
            return property;
        }
        property = model.createProperty(getUri());


        MetaInfo meta = getMetaInfo();
        if (meta != null) {
            meta.attachTo(property, model);
        }
        return property;
    }
}
