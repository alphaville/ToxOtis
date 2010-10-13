package org.opentox.toxotis.client.secure;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class SecureDeleteClient extends SecureClient {

    /** The method that this client applies */
    public static final String METHOD = "DELETE";

    public SecureDeleteClient(VRI vri) {
        super(vri);
    }

    public SecureDeleteClient() {
    }

    @Override
    protected HttpsURLConnection initializeConnection(URI uri) throws ToxOtisException {
        try {
            java.net.URL target_url = uri.toURL();
            con = (javax.net.ssl.HttpsURLConnection) target_url.openConnection();
            con.setRequestMethod(METHOD);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
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

    public void doDelete() throws ToxOtisException {
        if (con == null) {
            con = initializeConnection(vri.toURI());
            try {
                int code = con.getResponseCode();
                if (code != 200) {
                    throw new ToxOtisException("DELETE failed on '" + vri.clearToken() + "'. The remote service responded "
                            + "with status code " + code);
                }
            } catch (IOException ex) {
                throw new ToxOtisException(ErrorCause.ConnectionException, "Connection Exception while trying to reach "
                        + "the remote service at '" + vri.clearToken() + "' to apply a DELETE method.\n"
                        + "Exception occured while attempting to read the status code from the response header!", ex);
            }
        }
    }
}
