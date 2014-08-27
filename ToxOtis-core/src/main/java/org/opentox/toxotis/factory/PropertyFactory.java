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

package org.opentox.toxotis.factory;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.json.JSONObject;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Feature;
import org.opentox.toxotis.core.component.Substance;
import org.opentox.toxotis.core.component.SubstanceDataset;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.json.DatasetJsonDownloader;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 * @author Nikolaos Lampovas
 */
public class PropertyFactory {

    private PropertyFactory(){
        // Hidden Constructor - FeatureFactory is a utility class.
    }
    
    public static Feature createAndPublishProperty(String title,
            String units,SubstanceDataset ds, VRI featureService, AuthenticationToken token) 
            throws ServiceInvocationException {
        Feature brandNewProperty = new Feature();
        MetaInfo mi = new MetaInfoImpl();
        mi.addTitle(title);
        brandNewProperty.setMeta(mi);
        brandNewProperty.setUnits(units);
        
        
        //TODO custom enanomapper
        // Publish the property. Inside the substanceDataset there is a csv containing 
        // the header info of a property and a dummy substance
        
        Future<VRI> predictedFeatureUri = ds.publish(featureService, token);
        /* Wait for remote server to respond */
        try {
            while (!predictedFeatureUri.isDone()) {
                Thread.sleep(1000);
            }
            // Publishing a property is not available. Thus we post to /substance, which returns a substance
            // From that substance we get the substanceOwner and then get the dataset (created only for publishing properties)
         
            VRI resultUri = predictedFeatureUri.get();
            
            String host = SubstanceDataset.getHostFromVRI(ds.getUri().toString());
            String ownerUUID = Substance.getSubstanceKey(token,resultUri.getUri(), "ownerUUID");
            
            //TODO custom enanomapper
            // Get the dataset of the substance
            String datasetUri = SubstanceDataset.getDatasetFromUUIDOwner(ownerUUID, host);
            
            VRI input = new VRI(datasetUri);
            DatasetJsonDownloader jsn = new DatasetJsonDownloader(input);
            JSONObject obj = jsn.getJSON(token);

            // Get the info from the first (and only one) property of the dataset 
            // There the property's URI is returned.
            
            String property = jsn.getFirstProperty(obj,host);
            
            brandNewProperty.setUri(new VRI(property));
            return brandNewProperty;
        } catch (InterruptedException ex) {
            throw new IllegalArgumentException("Interrupted", ex);
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Invalid URI", ex);
        }catch (ExecutionException ex) {
            if (ex.getCause() != null && ex.getCause() instanceof ServiceInvocationException) {
                throw (ServiceInvocationException) ex.getCause();
            }
            throw new ServiceInvocationException(ex);
        }
    }
}
