/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opentox.toxotis.core.component;

import java.io.Serializable;

/**
 *
 * @author philip
 */
public interface IActualModel extends Serializable {
         
    public byte[] getPmml();

    public void setPmml(byte[] pmml);
    
    public Serializable getSerializableActualModel();
    
    public void setSerializableActualModel(Serializable serializableActualModel);
}
