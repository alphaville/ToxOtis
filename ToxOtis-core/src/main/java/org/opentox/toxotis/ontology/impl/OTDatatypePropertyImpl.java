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
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.DC;
import java.util.ArrayList;
import java.util.Collection;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.OTDatatypeProperty;
import org.opentox.toxotis.ontology.OntologicalClass;

public class OTDatatypePropertyImpl
        extends OTPropertyImpl implements OTDatatypeProperty {

    private Collection<XSDDatatype> domain = new ArrayList<XSDDatatype>();

    public OTDatatypePropertyImpl() {
        super();
    }

    public OTDatatypePropertyImpl(String name) {
        super(name);
    }

    @Override
    public Collection<XSDDatatype> getRange() {
        return domain;
    }

    @Override
    public void setRange(Collection<XSDDatatype> range) {
        this.domain = range;
    }


    @Override
    public DatatypeProperty asDatatypeProperty(OntModel model) {
        DatatypeProperty property = null;
        property = model.getDatatypeProperty(getUri());
        if (property != null) {
            return property;
        }

        property = model.createDatatypeProperty(getUri());
        if (getDomain() != null) {
            for (OntologicalClass dom : getDomain()) {
                property.addDomain(dom.inModel(model));
            }
        }

        if (getRange() != null) {
            for (XSDDatatype xsd : getRange()) {
                property.addRange(model.createResource(xsd.getURI()));
            }
        }

        MetaInfo meta = getMetaInfo();
        meta.attachTo(property, model);
        return property;
    }
}
