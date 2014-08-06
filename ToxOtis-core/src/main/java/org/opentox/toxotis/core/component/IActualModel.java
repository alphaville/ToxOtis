/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opentox.toxotis.core.component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.opentox.toxotis.client.VRI;
import weka.core.Instances;

/**
 *
 * @author philip
 */
public interface IActualModel extends Serializable {
         
    public byte[] getPmml();

    public void setPmml(byte[] pmml);
    
    public Serializable getSerializableActualModel();
    
    public void setSerializableActualModel(Serializable serializableActualModel);
    
    public void setHasScaling(Boolean hasScaling);
    public Boolean hasScaling();
    
    public void setScalingMinVals(HashMap<VRI, Double> scalingMinVals);
    public void setScalingMaxVals(HashMap<VRI, Double> scalingMaxVals);
    public Map<String, Double> getScalingMinVals2();
    public Map<String, Double> getScalingMaxVals2();
    
    public void setScalingMin(double scalingMin);
    public void setScalingMax(double scalingMax);
    public double getScalingMin(); 
    public double getScalingMax();
    
    public void setHasNormalization(Boolean hasScaling);
    public Boolean hasNormalization();
    
    public void setNormalizationMinVals(HashMap<VRI, Double> normalizationMinVals);
    public Map<String, Double> getNormedVals2();
    public Map<String, Double> getNormalizationMinVals2();
    public void setNormedVals(HashMap<VRI, Double> normedVals);
    
}
