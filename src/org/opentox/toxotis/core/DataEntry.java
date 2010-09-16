package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.List;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DataEntry extends OTComponent<DataEntry> {

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
    public Individual asIndividual(OntModel model) {
        String dataEntryUri = getUri() != null ? getUri().getStringNoQuery() : null;
        Individual indiv = model.createIndividual(dataEntryUri, OTClasses.DataEntry().inModel(model));
        if (meta != null) {
            meta.attachTo(indiv, model);
        }        
        // @chung: just testing
        //TODO: We might need to delete one of the following lines
        indiv.addProperty(OTObjectProperties.conformer().asObjectProperty(model), conformer.asIndividual(model));
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
}
