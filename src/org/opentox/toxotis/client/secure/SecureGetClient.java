package org.opentox.toxotis.client.secure;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.RequestHeaders;
import org.opentox.toxotis.client.VRI;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class SecureGetClient extends SecureClient {

    
    /** The method that this client applies */
    public static final String METHOD = "GET";

    public SecureGetClient(VRI vri) {
        super(vri);
    }

    public SecureGetClient() {
    }

    @Override
    protected HttpsURLConnection initializeConnection(URI uri) throws ToxOtisException {
        try {
            java.net.URL target_url = uri.toURL();
            con = (javax.net.ssl.HttpsURLConnection) target_url.openConnection();
            con.setRequestMethod(METHOD);
            con.setDoInput(true);
            con.setUseCaches(false);
            if (acceptMediaType != null) {
                con.setRequestProperty(RequestHeaders.ACCEPT, acceptMediaType);
            }
            if (!headerValues.isEmpty()) {
                for (Map.Entry<String, String> e : headerValues.entrySet()) {
                    con.setRequestProperty(e.getKey(), e.getValue());// These are already URI-encoded!
                }
            }
            return con;
        } catch (IOException ex) {
            throw new ToxOtisException(ex);
        }
    }


    /** Get the result as a URI list */
    public java.util.List<String> getResponseUriList() throws ToxOtisException {
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
            throw new ToxOtisException(io);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    throw new ToxOtisException(ex);
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException ex) {
                    throw new ToxOtisException(ex);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    throw new ToxOtisException(ex);
                }
            }
        }
        return list;
    }

}