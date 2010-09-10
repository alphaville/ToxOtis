package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.ArrayList;
import org.opentox.toxotis.client.VRI;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Model extends OTComponent<Model>{

    private VRI dataset;
    private Algorithm algorithm;
    private Feature predictedFeature;
    private Feature dependentFeature;
    private ArrayList<Feature> independentFeatures;
    private ArrayList<Parameter> parameters;

    public Model(VRI uri) {
        super(uri);
    }

    public Model() {
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public VRI getDataset() {
        return dataset;
    }

    public void setDataset(VRI dataset) {
        this.dataset = dataset;
    }

    public Feature getDependentFeature() {
        return dependentFeature;
    }

    public void setDependentFeature(Feature dependentFeature) {
        this.dependentFeature = dependentFeature;
    }

    public ArrayList<Feature> getIndependentFeatures() {
        return independentFeatures;
    }

    public void setIndependentFeatures(ArrayList<Feature> independentFeatures) {
        this.independentFeatures = independentFeatures;
    }

    public ArrayList<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<Parameter> parameters) {
        this.parameters = parameters;
    }

    public Feature getPredictedFeature() {
        return predictedFeature;
    }

    public void setPredictedFeature(Feature predictedFeature) {
        this.predictedFeature = predictedFeature;
    }

    @Override
    public Model createFrom(OntModel model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Individual asIndividual(OntModel model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}