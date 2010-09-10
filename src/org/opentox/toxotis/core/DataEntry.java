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

    private Conformer conformer;
    private List<FeatureValue> featureValues;

    public DataEntry() {
    }

    public DataEntry(Conformer compound, List<FeatureValue> featureValues) {
        this.conformer = compound;
        this.featureValues = featureValues;
    }

    public Conformer getConformer() {
        return conformer;
    }

    public void setConformer(Conformer compound) {
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
    public DataEntry createFrom(OntModel model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Individual asIndividual(OntModel model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }





}