package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import org.opentox.toxotis.util.spiders.TypedValue;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class FeatureValue extends OTComponent<FeatureValue> {

    private Feature feature;
    private TypedValue value;

    public FeatureValue() {
    }

    public FeatureValue(Feature feature, TypedValue value) {
        this.feature = feature;
        this.value = value;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public TypedValue getValue() {
        return value;
    }

    public void setValue(TypedValue value) {
        this.value = value;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
