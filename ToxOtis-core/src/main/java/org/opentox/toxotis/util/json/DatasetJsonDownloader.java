/*
 *
 * Jaqpot - version 3
 *
 * The JAQPOT-3 web services are OpenTox API-1.2 compliant web services. Jaqpot
 * is a web application that supports model training and data preprocessing algorithms
 * such as multiple linear regression, support vector machines, neural networks
 * (an in-house implementation based on an efficient algorithm), an implementation
 * of the leverage algorithm for domain of applicability estimation and various
 * data preprocessing algorithms like PLS and data cleanup.
 *
 * Copyright (C) 2009-2014 Pantelis Sopasakis & Charalampos Chomenides & Lampovas Nikolaos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 *
 */

package org.opentox.toxotis.util.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.IGetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 * @author Nikolaos Lampovas
 */
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opentox.toxotis.util.aa.AuthenticationToken;
public class DatasetJsonDownloader {

    private final VRI datasetUri;
    
    private JSONObject jsonDataset;
    
    public DatasetJsonDownloader(final VRI datasetUri) {
        this.datasetUri = datasetUri;
    }
    
    
    public JSONObject getJSON() {
        IGetClient client = ClientFactory.createGetClient(datasetUri);
        client.setMediaType(Media.APPLICATION_JSON);
        
        try {
            AuthenticationToken token = new AuthenticationToken("AQIC5wM2LY4Sfcxx-5zU69ErY43e5JHXDDgQhSc4WzsYcAE.*AAJTSQACMDE.*");
            client.authorize(token);
            InputStream is = client.getRemoteStream();
            StringWriter writer = new StringWriter();
            IOUtils.copy(is, writer, "UTF-8");
            String theString = writer.toString();
            JSONObject jsonObj = new JSONObject(theString);
            jsonDataset = jsonObj;
            return jsonObj;
        } catch (ServiceInvocationException ex) {
            Logger.getLogger(DatasetJsonDownloader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(DatasetJsonDownloader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DatasetJsonDownloader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                client.close();
            } catch (IOException ex) {
                Logger.getLogger(DatasetJsonDownloader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    public String traverse(List<String> keys,JSONObject val) {
        String res="",out ="";
        JSONArray temp;
        JSONObject temp2;
        int i=0;
        for (;i<keys.size();++i) {
            if(val.get(keys.get(i)) instanceof JSONObject) {
                val = (JSONObject) val.get(keys.get(i));
                
            } else if ( val.get(keys.get(i)) instanceof String) {
                out = (String) val.get(keys.get(i));
            } else if ( val.get(keys.get(i)) instanceof JSONArray) {
                temp = (JSONArray) val.get(keys.get(i));
                for(int j=0;j<temp.length();++j) {
                    temp2 = (JSONObject) temp.get(j);
                    if(temp2.get(keys.get(i+1))!= null) {
                        val = (JSONObject) temp2;
                        break;
                    }
                }
            } else {
                break;
            }
        }
        if(i==keys.size())
           res = out;     
        return res;
    }
    public String traverse(List<String> keys) {
        return traverse(keys,jsonDataset);
    }
    
    //TODO these shouldnt be customURIS
    public Map<String,String> bindUUIDsToNames(JSONObject val,String prefix) {
        Map<String,String> res = new HashMap<String, String>();
        JSONArray temp2;
        JSONObject temp3;
        Object obj;
        String tempName,tempUUID;
        temp2 = (JSONArray) val.get("dataEntry");
        
        for(int i=0;i<temp2.length();++i) {
            temp3 = (JSONObject) temp2.get(i);
            if(temp3!=null) {
                temp3 = (JSONObject) temp3.get("values");
                if(temp3!=null) {
                    try {
                        if(StringUtils.isNotEmpty((String) temp3.get(prefix+"identifier/uuid")) &&
                            StringUtils.isNotEmpty((String) temp3.get(prefix+"identifier/name")) ) {
                            tempUUID = (String) temp3.get(prefix+"identifier/uuid");
                            tempName = (String) temp3.get(prefix+"identifier/name");
                            res.put(prefix+"substance/"+tempUUID, tempName);

                        }
                    } catch(JSONException ex) {
                        
                    }
                }
            }
           
        }
        return res;
    }
}
