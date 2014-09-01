/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opentox.toxotis.core.component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.opentox.toxotis.client.VRI;
import Jama.Matrix;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author philip
 */
public class ActualModel implements IActualModel{
    
    private byte[] pmml;
    private Serializable serializableActualModel;
    private static final long serialUUID = 124129765424125712L;
    private Object statistics;   //variable for statistics in pmml
    
    //variables for scaling
    private Boolean hasScaling=false;
    private HashMap<VRI, Double> scalingMinVals = new HashMap<VRI, Double>();
    private HashMap<VRI, Double> scalingMaxVals = new HashMap<VRI, Double>();
    private double scalingMin = 0;
    private double scalingMax = 1;
    
    //variables for normalization
    private Boolean hasNormalization=false;
    private HashMap<VRI, Double> normalizationMinVals = new HashMap<VRI, Double>();
    private HashMap<VRI, Double> normedVals = new HashMap<VRI, Double>();
    
    //variables for leverage DoA
    private Boolean hasDoA=false;
    private Matrix dataMatrix = null;
    private double gamma = 0;//3k/n
    private List<String> excludeAttributesDoA = new ArrayList();

    public ActualModel(Serializable serializableActualModel) {
        this.serializableActualModel = serializableActualModel;
    }

    public ActualModel(byte[] pmml, Serializable serializableActualModel) {
        this.pmml = pmml;
        this.serializableActualModel = serializableActualModel;
    }
    
    public ActualModel(){
    }
    
    @Override
    public byte[] getPmml() {
        return pmml;
    }

    @Override
    public void setPmml(byte[] pmml) {
        this.pmml = pmml;
    }

    @Override
    public Serializable getSerializableActualModel() {
        return serializableActualModel;
    }

    @Override
    public void setSerializableActualModel(Serializable serializableActualModel) {
        this.serializableActualModel = serializableActualModel;
    }
    
    /**
     * Gets statistics from weka for the model
     */
    @Override
    public Object getStatistics() {
        return statistics;
    }

    
    /**
     * Sets statistics from weka for the model
     * @param statistics The object which can be used to store statistics.
     */
    public void setStatistics(Object statistics) {
        this.statistics = statistics;
    }


    @Override
    public void setScalingMin(double scalingMin) {
        this.scalingMin = scalingMin;
    }

    @Override
    public void setScalingMax(double scalingMax) {
        this.scalingMax = scalingMax;
    }

    @Override
    public double getScalingMin() {
        return scalingMin;
    }
 
    @Override   
    public double getScalingMax() {
        return scalingMax;
    }

    public HashMap<VRI, Double> getScalingMinVals() {
        return scalingMinVals;
    }

    public HashMap<VRI, Double> getScalingMaxVals() {
        return scalingMaxVals;
    }

    @Override
    public Map<String, Double> getScalingMaxVals2() {
        return getVRIkeyMapToDoublekeyMap(scalingMaxVals);
    }

    @Override
    public Map<String, Double> getScalingMinVals2() {
        return getVRIkeyMapToDoublekeyMap(scalingMinVals);
    }

    private static Map<String, Double> getVRIkeyMapToDoublekeyMap(Map<VRI, Double> scalingVals) {
        if (scalingVals == null) {
            return null;
        }
        Map<String, Double> simpleMap = new HashMap<String, Double>();
        if (!scalingVals.isEmpty()) {
            Iterator<Map.Entry<VRI, Double>> iterator = scalingVals.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<VRI, Double> entry = iterator.next();
                simpleMap.put(entry.getKey().toString(), entry.getValue());
            }
        }
        return simpleMap;
    }

    @Override
    public void setScalingMinVals(HashMap<VRI, Double> scalingMinVals) {
        this.scalingMinVals = scalingMinVals;
    }

    @Override
    public void setScalingMaxVals(HashMap<VRI, Double> scalingMaxVals) {
        this.scalingMaxVals = scalingMaxVals;
    }

    @Override
    public void setHasScaling(Boolean hasScaling) {
        this.hasScaling = hasScaling;
    }   

    @Override
    public Boolean hasScaling() {
        return hasScaling;
    }

    @Override
    public Boolean hasNormalization() {
        return hasNormalization;
    }

    @Override
    public void setHasNormalization(Boolean hasNormalization) {
        this.hasNormalization = hasNormalization;
    }
    
    @Override
    public void setNormalizationMinVals(HashMap<VRI, Double> normalizationMinVals) {
        this.normalizationMinVals = normalizationMinVals;
    }

    @Override
    public void setNormedVals(HashMap<VRI, Double> normedVals) {
        this.normedVals = normedVals;
    }
    
    public HashMap<VRI, Double> getNormedVals() {
        return normedVals;
    }
    
    @Override
    public Map<String, Double> getNormedVals2() {
        return getVRIkeyMapToDoublekeyMap(normedVals);
    }
    
    @Override
    public Map<String, Double> getNormalizationMinVals2() {
        return getVRIkeyMapToDoublekeyMap(normalizationMinVals);
    }

    @Override
    public Boolean hasDoA() {
        return hasDoA;
    }

    @Override
    public void setHasDoA(Boolean hasDoA) {
        this.hasDoA = hasDoA;
    }
    
    @Override
    public Matrix getDataMatrix() {
        return dataMatrix;
    }

    @Override
    public void setDataMatrix(Matrix dataMatrix) {
        this.dataMatrix = dataMatrix;
    }

    @Override
    public double getGamma() {
        return gamma;
    }

    @Override
    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    // where k is the number of attributes 
    // and n is the number of instances
    @Override    
    public void setGamma(int k, int n) {
        this.gamma = 3.0 * k / n;
    }

    @Override
    public List<String> getExcludeAttributesDoA() {
        return excludeAttributesDoA;
    }

    @Override
    public void setExcludeAttributesDoA(List<String> excludeFeatures) {
        this.excludeAttributesDoA = excludeFeatures;
    }
    
    
}
