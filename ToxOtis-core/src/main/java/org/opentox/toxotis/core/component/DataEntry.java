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
package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 * A DataEntry is a set a multiplicity consisting of a compounds and a set of
 * feature-value pairs assigned to it.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DataEntry extends OTComponent<DataEntry> {

    private Compound conformer;
    private List<FeatureValue> featureValues;
    private static final String DISCRIMINATOR = "dataEntry";
    private static final int HASH_OFFSET = 7, HASH_MOD = 97;

    @Override
    public VRI getUri() {
        if (uri == null) {
            int hash = 91;
            for (FeatureValue fv : featureValues) {
                hash += 3 * (fv!=null?(fv.getUri()!=null?fv.getUri().hashCode():0):0);
            }
            uri = Services.anonymous().augment(DISCRIMINATOR,
                    hash, conformer!=null?conformer.getUri().toString().hashCode():"x");
        }
        return uri;
    }

    public DataEntry() {
        featureValues = new ArrayList<FeatureValue>();
        conformer = new Compound();
        this.meta = null;
    }

    public DataEntry(Compound compound, List<FeatureValue> featureValues) {
        this.conformer = compound;
        this.featureValues = featureValues;
    }

    public Compound getConformer() {
        return conformer;
    }

    public void setConformer(Compound compound) {
        this.conformer = compound;
    }

    public List<FeatureValue> getFeatureValues() {
        return featureValues;
    }

    public void setFeatureValues(List<FeatureValue> featureValues) {
        this.featureValues = featureValues;
    }

    public void addFeatureValue(int index, FeatureValue element) {
        featureValues.add(index, element);
    }

    public boolean addFeatureValue(FeatureValue e) {
        return featureValues.add(e);
    }

    public int featureValuesSize() {
        return featureValues.size();
    }

    public FeatureValue removeFeatureValue(int index) {
        return featureValues.remove(index);
    }

    public boolean hasFeatureValues() {
        return !featureValues.isEmpty();
    }

    public FeatureValue getFeatureValue(int index) {
        return featureValues.get(index);
    }

    @Override
    public Individual asIndividual(OntModel model) {
        String dataEntryUri = getUri() != null ? getUri().getStringNoQuery() : null;
        Individual indiv = model.createIndividual(dataEntryUri, OTClasses.DataEntry().inModel(model));
        if (meta != null) {
            meta.attachTo(indiv, model);
        }
        indiv.addProperty(OTObjectProperties.compound().asObjectProperty(model), conformer.asIndividual(model));

        if (featureValues != null && !featureValues.isEmpty()) {
            ObjectProperty valuesProp = OTObjectProperties.values().asObjectProperty(model);
            for (FeatureValue fv : featureValues) {
                if (fv != null) {
                    indiv.addProperty(valuesProp, fv.asIndividual(model));
                }
            }
        }
        return indiv;
    }

    @Override
    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DataEntry other = (DataEntry) obj;
        if (this.conformer != other.conformer && (this.conformer == null || !this.conformer.equals(other.conformer))) {
            return false;
        }
        if (this.featureValues != other.featureValues && (this.featureValues == null || !this.featureValues.equals(other.featureValues))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = HASH_OFFSET;
        hash = HASH_MOD * hash + (this.conformer != null ? this.conformer.hashCode() : 0);
        hash = HASH_MOD * hash + (this.featureValues != null ? this.featureValues.hashCode() : 0);
        return hash;
    }
}
