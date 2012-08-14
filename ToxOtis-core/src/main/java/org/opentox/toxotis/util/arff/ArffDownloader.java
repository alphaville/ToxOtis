package org.opentox.toxotis.util.arff;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.IGetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.core.component.Dataset;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ArffDownloader {

    private final VRI datasetUri;

    public ArffDownloader(final VRI datasetUri) {
        this.datasetUri = datasetUri;
    }

    public Instances getInstances() {
        IGetClient client = ClientFactory.createGetClient(datasetUri);
        client.setMediaType(Media.WEKA_ARFF);
        try {
            InputStream is = client.getRemoteStream();
            InputStreamReader reader = new InputStreamReader(is);
            ArffReader arff = new ArffReader(reader);
            return arff.getData();
        } catch (IOException ex) {
            Logger.getLogger(ArffDownloader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServiceInvocationException ex) {
            Logger.getLogger(ArffDownloader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                client.close();
            } catch (IOException ex) {
                Logger.getLogger(ArffDownloader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
}
