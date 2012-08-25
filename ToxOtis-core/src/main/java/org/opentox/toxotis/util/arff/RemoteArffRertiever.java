package org.opentox.toxotis.util.arff;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.HttpStatusCodes;
import org.opentox.toxotis.client.IGetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.exceptions.impl.ConnectionException;
import org.opentox.toxotis.exceptions.impl.RemoteServiceException;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class RemoteArffRertiever implements Closeable {

    private IGetClient client;
    private InputStream is;
    private Instances instances;
    private BufferedReader br;
    private int nAttr;
    private double[] currentValues;

    public RemoteArffRertiever(VRI datasetUri) {
        client = ClientFactory.createGetClient(datasetUri);
        client.setMediaType(Media.WEKA_ARFF);
    }

    public boolean arffSupport() throws ServiceInvocationException {
        String contentType = client.getResponseContentType();
        if (contentType == null) {
            return false;
        }
        return (contentType.equalsIgnoreCase("text/x-arff"));
    }

    public Instances nextData() throws ServiceInvocationException {
        if (instances == null) {
            initializeInstances();
        }
        if (instances.numInstances() > 0) {
            instances.delete(0);
        }
        Instance tempInstance = new Instance(nAttr);
        String currentLine = null;

        try {
            currentLine = br.readLine();
            if (currentLine == null) {
                return null;
            }
            while (currentLine.isEmpty()) {
                continue;
            }
            String[] segments = currentLine.split(",");
            String currentValueString;
            for (int i = 0; i < nAttr; i++) {
                currentValueString = segments[i];
                if (instances.attribute(i).isNumeric()) {
                    if ("?".equals(currentValueString)) {
                        currentValues[i] = Instance.missingValue();
                    } else if (instances.attribute(i).isString()) {
                        currentValues[i] = Double.parseDouble(currentValueString);
                    }
                } else {
                    currentValues[i] = instances.attribute(i).addStringValue(currentValueString);
                }
            }
        } catch (IOException ex) {
            throw new ConnectionException(ex);
        }

        tempInstance = new Instance(1.0, currentValues);
        instances.add(tempInstance);
        return instances;
    }

    private void initializeInstances() throws ServiceInvocationException {
        if (!arffSupport()) {
            throw new RemoteServiceException("The remote service does not support TEXT/X-ARFF");
        }
        int status = client.getResponseCode();
        if (status != HttpStatusCodes.Success.getStatus()) {
            throw new ServiceInvocationException("The remote service returned a status code " + status);
        }
        is = client.getRemoteStream();
        InputStreamReader isr = new InputStreamReader(is);
        br = new BufferedReader(isr);
        String line = null;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                line = line.trim();
                sb.append(line);
                sb.append("\n");
                if (line.equals("@data")) {
                    break;
                }
            }
        } catch (IOException ex) {
            throw new ConnectionException(ex);
        }
        // The buffered reader should not close!!!
        StringReader reader = new StringReader(sb.toString());
        try {
            ArffReader arffReader = new ArffReader(reader);
            instances = arffReader.getData(); // ONLY the header is in there!!!
        } catch (IOException ex) {
            Logger.getLogger(RemoteArffRertiever.class.getName()).log(Level.SEVERE, null, ex);
        }
        nAttr = instances.numAttributes();
        currentValues = new double[nAttr];
    }

    @Override
    public void close() throws IOException {
        br.close();
        is.close();
        client.close();
    }
    
}
