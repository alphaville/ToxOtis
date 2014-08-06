/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opentox.toxotis.core.component;

import org.opentox.toxotis.core.component.IActualModel;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.opentox.toxotis.client.VRI;

/**
 *
 * @author philip
 */
public class ActualModel implements IActualModel{
    
    private byte[] pmml;
    private Serializable serializableActualModel;
    private static final long serialUUID = 124129765424125712L;
    private Object statistics;
    private HashMap<VRI, Double> scalingMinVals = new HashMap<VRI, Double>();
    private HashMap<VRI, Double> scalingMaxVals = new HashMap<VRI, Double>();
    private double scalingMin = 0;
    private double scalingMax = 1;
    private Boolean hasScaling=false;

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
    public Map<String, Double> getMaxVals2() {
        if (scalingMaxVals == null) {
            return null;
        }
        Map<String, Double> simpleMap = new HashMap<String, Double>();
        if (!scalingMaxVals.isEmpty()) {
            Iterator<Map.Entry<VRI, Double>> iterator = scalingMaxVals.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<VRI, Double> entry = iterator.next();
                simpleMap.put(entry.getKey().toString(), entry.getValue());
            }
        }
        return simpleMap;
    }

    @Override
    public Map<String, Double> getMinVals2() {
        if (scalingMinVals == null) {
            return null;
        }
        Map<String, Double> simpleMap = new HashMap<String, Double>();
        if (!scalingMinVals.isEmpty()) {
            Iterator<Map.Entry<VRI, Double>> iterator = scalingMinVals.entrySet().iterator();
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
    
}
