package org.opentox.toxotis.client.https;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.IGetClient;
import org.opentox.toxotis.client.RequestHeaders;
import org.opentox.toxotis.client.VRI;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class GetHttpsClient extends AbstractHttpsClient implements IGetClient{
   

    public GetHttpsClient(VRI vri) {
        super(vri);
    }

    public GetHttpsClient() {
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
    @Override
    public Set<VRI> getResponseUriList() throws ToxOtisException {
        java.util.Set<VRI> list = new java.util.HashSet<VRI>();
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
                try {
                    list.add(new VRI(line));
                } catch (URISyntaxException ex) {
                    Logger.getLogger(GetHttpsClient.class.getName()).log(Level.SEVERE, null, ex);
                }
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