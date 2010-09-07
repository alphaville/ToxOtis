package org.opentox.toxotis.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.ontology.impl.SimpleOntModelImpl;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class PostClient {

    /** Accepted mediatype  */
    private String mediaType = null;
    /** Type of the posted content*/
    private String contentType = null;
    /** Target URI */
    private VRI vri = null;
    /** Connection to the above URI */
    private java.net.HttpURLConnection con = null;
    /** Parameters to be posted as application/x-www-form-urlencoded (if any) */
    private Map<String, String> postParameters = new HashMap<String, String>();
    private static final String MEDIATYPE_FORM_URL_ENCODED = "application/x-www-form-urlencoded";
    /** Standard UTF-8 Encoding */
    private static final String URL_ENCODING = "UTF-8";
    /** Size of a buffer used to download the data from the remote server */
    private static final int bufferSize = 4194304;

    public PostClient() {
    }

    public PostClient(VRI vri) {
        this();
        this.vri = vri;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getContentType() {
        return contentType;
    }

    public PostClient setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * Add a parameter which will be posted to the target URI. Once the parameter is
     * submitted to the PostClient, it is stored as URL-encoded using the UTF-8 encoding.
     * @param paramName Parameter name
     * @param paramValue Parameter value
     * @return This object
     * @throws NullPointerException If paramName is <code>null</code>.
     */
    public PostClient addParameter(String paramName, String paramValue) throws NullPointerException {
        if (paramName == null) {
            throw new NullPointerException("paramName must be not null");
        }
        try {
            postParameters.put(URLEncoder.encode(paramName, URL_ENCODING), paramValue != null ? URLEncoder.encode(paramValue, URL_ENCODING) : "");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(PostClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }

    public VRI getUri() {
        return vri;
    }

    public PostClient setUri(VRI uri) {
        this.vri = uri;
        return this;
    }

    private String getParametersAsQuery() {
        if (postParameters.isEmpty()) {
            return "";
        }
        StringBuilder string = new StringBuilder();
        final int nParams = postParameters.size();
        if (nParams > 0) {
            int counter = 0;
            for (Map.Entry<String, String> e : postParameters.entrySet()) {
                string.append(e.getKey());
                string.append("=");
                if (e.getValue() != null) {
                    string.append(e.getValue());
                }
                if (counter != nParams - 1) {
                    string.append("&");
                }
                counter++;
            }
        }
        return new String(string);
    }

    /** Initialize a connection to the target URI */
    private java.net.HttpURLConnection initializeConnection(final java.net.URI uri) throws ToxOtisException {
        try {
            java.net.HttpURLConnection.setFollowRedirects(true);
            java.net.URL dataset_url = uri.toURL();
            con = (java.net.HttpURLConnection) dataset_url.openConnection();
            con.setAllowUserInteraction(false);
            con.setDoInput(true);
            con.setDoOutput(true); // allow data to be posted
            con.setUseCaches(false);
            if (contentType != null) {
                con.setRequestProperty("Content-type", contentType);
            }
            /* If there are some parameters to be posted, then the POST will
             * declare the posted data as application/x-form-urlencoded.
             */
            if (!postParameters.isEmpty()) {
                setContentType(MEDIATYPE_FORM_URL_ENCODED);
                con.setRequestProperty("Content-type", contentType);
                con.setRequestProperty("Content-Length", ""
                        + Integer.toString(getParametersAsQuery().getBytes().length));
            }
            return con;
        } catch (final Exception ex) {
            throw new ToxOtisException(ex);
        }
    }

    public void postParameters() throws ToxOtisException {
        initializeConnection(vri.toURI());
        DataOutputStream wr;
        try {
            wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(getParametersAsQuery());// POST the parameters
            wr.flush();
            wr.close();
        } catch (final IOException ex) {
            throw new ToxOtisException("I/O Exception caught while posting the parameters", ex);
        }
    }

    /** Get the normal stream of the response (body) */
    public java.io.InputStream getRemoteStream() throws ToxOtisException, java.io.IOException {
        if (con == null) {
            con = initializeConnection(vri.toURI());
        }
        if (con == null) {
            throw new ToxOtisException("Comminucation Error with the remote");
        }
        if (con.getResponseCode() == 200 || con.getResponseCode() == 202) {
            return new java.io.BufferedInputStream(con.getInputStream(), bufferSize);
        } else {
            return new java.io.BufferedInputStream(con.getErrorStream(), bufferSize);
        }
    }

    public String getResponseText() throws ToxOtisException {
        if (con == null) {
            con = initializeConnection(vri.toURI());
        }
        InputStream is = null;
        BufferedReader reader = null;
        try {
            is = getRemoteStream();
            reader = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return new String(sb);
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
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    throw new ToxOtisException(ex);
                }
            }
        }
    }

      public com.hp.hpl.jena.ontology.OntModel getResponseOntModel() throws ToxOtisException {
        try {
            com.hp.hpl.jena.ontology.OntModel om = new SimpleOntModelImpl();
            om.read(getRemoteStream(), null);
            return om;
        } catch (final Exception ex) {
            throw new ToxOtisException("Cannot read OntModel from " + vri.toString(), ex);
        }
    }
}
