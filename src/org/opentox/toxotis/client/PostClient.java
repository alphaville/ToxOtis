package org.opentox.toxotis.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.opentox.toxotis.ToxOtisException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class PostClient extends AbstractClient {

    /** Type of the posted content*/
    private String contentType = null;
    /** Parameters to be posted as application/x-www-form-urlencoded (if any) */
    private Map<String, String> postParameters = new HashMap<String, String>();
    private Map<String, String> headerValues = new HashMap<String, String>();
    /** The method that this client applies */
    public static final String METHOD = "POST";

    public PostClient() {
        super();
    }

    public PostClient(VRI vri) {
        super();
        this.vri = vri;
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
            throw new RuntimeException(ex);
        }
        return this;
    }

    /**
     * Note: if the parameter name (paramName) is either 'Accept' or 'Content-type', this
     * method will override {@link PostClient#setMediaType(java.lang.String) setMediaType} and
     * {@link PostClient#setContentType(java.lang.String) setContentType} respectively. In general
     * it is not advisable that you choose this method for setting values to these headers. Once the
     * parameter name and its value are submitted to the client, they are encoded using the
     * standard UTF-8 encoding.
     * @param paramName Name of the parameter which will be posted in the header
     * @param paramValue Parameter value
     * @return This object
     * @throws NullPointerException
     *          If any of the arguments is null
     */
    public PostClient addHeaderParameter(String paramName, String paramValue) throws NullPointerException, IllegalArgumentException {
        if (paramName == null) {
            throw new NullPointerException("ParamName is null");
        }
        if (paramValue == null) {
            throw new NullPointerException("ParamValue is null");
        }
        if (RequestHeaders.ACCEPT.equalsIgnoreCase(paramName)) {
            setMediaType(paramValue);
            return this;
        }
        if (RequestHeaders.CONTENT_TYPE.equalsIgnoreCase(paramName)) {
            setContentType(paramValue);
            return this;
        }
        headerValues.put(paramName, paramValue);
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
    protected java.net.HttpURLConnection initializeConnection(final java.net.URI uri) throws ToxOtisException {
        try {
            java.net.HttpURLConnection.setFollowRedirects(true);
            java.net.URL dataset_url = uri.toURL();
            con = (java.net.HttpURLConnection) dataset_url.openConnection();
            con.setRequestMethod(METHOD);
            con.setAllowUserInteraction(false);
            con.setDoInput(true);
            con.setDoOutput(true); // allow data to be posted
            con.setUseCaches(false);
            if (contentType != null) {
                con.setRequestProperty(RequestHeaders.CONTENT_TYPE, contentType);
            }
            if (acceptMediaType != null) {
                con.setRequestProperty(RequestHeaders.ACCEPT, acceptMediaType);
            }
            if (!headerValues.isEmpty()) {
                for (Map.Entry<String, String> e : headerValues.entrySet()) {
                    con.setRequestProperty(e.getKey(), e.getValue());// These are already URI-encoded!
                }
            }
            /* If there are some parameters to be posted, then the POST will
             * declare the posted data as application/x-form-urlencoded.
             */
            if (!postParameters.isEmpty()) {
                setContentType(MEDIATYPE_FORM_URL_ENCODED);
                con.setRequestProperty(RequestHeaders.CONTENT_TYPE, contentType);
                con.setRequestProperty(RequestHeaders.CONTENT_LENGTH,
                        Integer.toString(getParametersAsQuery().getBytes().length));
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
}
