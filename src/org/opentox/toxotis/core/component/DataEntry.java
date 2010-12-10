package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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
        int hash = 7;
        hash = 97 * hash + (this.conformer != null ? this.conformer.hashCode() : 0);
        hash = 97 * hash + (this.featureValues != null ? this.featureValues.hashCode() : 0);
        return hash;
    }
}
