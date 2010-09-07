package org.opentox.toxotis.client;

import java.io.IOException;
import org.opentox.toxotis.ToxOtisException;

/**
 * A client that performs GET requests.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class GetClient extends AbstractClient {

            

    /** Create a new instance of GetClient */
    public GetClient() {
    }
        

    /** Initialize a connection to the target URI */
    protected java.net.HttpURLConnection initializeConnection(final java.net.URI uri) throws ToxOtisException {
        try {
            java.net.HttpURLConnection.setFollowRedirects(true);
            java.net.URL dataset_url = uri.toURL();
            con = (java.net.HttpURLConnection) dataset_url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            if (acceptMediaType != null) {
                con.setRequestProperty("Accept", acceptMediaType);
            }
            return con;
        } catch (final Exception ex) {
            throw new ToxOtisException(ex);
        }
    }
        

    /** Get the result as a URI list */
    public java.util.List<String> getResponseUriList() throws ToxOtisException, IOException {
        setMediaType("text/uri-list");// Set the mediatype to text/uri-list
        java.util.List<String> list = new java.util.ArrayList<String>();
        java.io.InputStreamReader isr = null;
        java.io.InputStream is = null;
        java.io.BufferedReader reader = null;
        try {
            if (con == null) {
                con = initializeConnection(vri.toURI());
            }
            is = getRemoteStream();
            isr = new java.io.InputStreamReader(is);
            reader = new java.io.BufferedReader(isr);
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (ToxOtisException cl) {
            throw cl;
        } catch (IOException io) {
            throw io;
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (is != null) {
                is.close();
            }
        }
        return list;
    }
        
}

