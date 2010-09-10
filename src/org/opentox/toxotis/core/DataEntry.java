package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.List;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DataEntry extends OTComponent<DataEntry>{

    private Compound compound;
    private List<FeatureValue> featureValues;

    public DataEntry() {
    }

    public DataEntry(Compound compound, List<FeatureValue> featureValues) {
        this.compound = compound;
        this.featureValues = featureValues;
    }

    public Compound getCompound() {
        return compound;
    }

    public void setCompound(Compound compound) {
        this.compound = compound;
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
    public DataEntry createFrom(OntModel model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Individual asIndividual(OntModel model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }





}