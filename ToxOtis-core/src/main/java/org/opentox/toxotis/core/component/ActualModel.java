/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opentox.toxotis.core.component;

import org.opentox.toxotis.core.component.IActualModel;
import java.io.Serializable;

/**
 *
 * @author philip
 */
public class ActualModel implements IActualModel{
    
    private byte[] pmml;
    private Serializable serializableActualModel;
    private static final long serialUUID = 124129765424125712L;

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
    
}
