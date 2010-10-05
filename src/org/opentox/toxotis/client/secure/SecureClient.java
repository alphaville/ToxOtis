package org.opentox.toxotis.client.secure;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.PostClient;
import org.opentox.toxotis.client.RequestHeaders;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.util.aa.SSLConfiguration;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class SecureClient implements Closeable {

    /** Target secure URI */
    protected VRI vri = null;
    /** Connection to the above URI */
    protected javax.net.ssl.HttpsURLConnection con = null;
    protected static final String MEDIATYPE_FORM_URL_ENCODED = "application/x-www-form-urlencoded";
    /** Standard UTF-8 Encoding */
    protected static final String URL_ENCODING = "UTF-8";
    /** Size of a buffer used to download the data from the remote server */
    protected static final int bufferSize = 4194304;
    /** Accepted mediatype  */
    protected String acceptMediaType = null;
    /** A mapping from parameter names to their corresponding values */
    protected Map<String, String> headerValues = new HashMap<String, String>();

    public SecureClient() {
        SSLConfiguration.initializeSSLConnection();
    }

    public SecureClient(VRI vri) {
        this();
        this.vri = vri;
    }

    /**
     * Get the targetted URI
     * @return The target URI
     */
    public VRI getUri() {
        return vri;
    }

    public String getMediaType() {
        return acceptMediaType;
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
    public SecureClient addHeaderParameter(String paramName, String paramValue) throws NullPointerException, IllegalArgumentException {
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
        headerValues.put(paramName, paramValue);
        return this;
    }

    /**
     * Specify the mediatype to be used in the <tt>Accept</tt> header.
     * @param mediaType
     *      Accepted mediatype
     *
     * @see RequestHeaders#ACCEPT
     */
    public SecureClient setMediaType(String mediaType) {
        this.acceptMediaType = mediaType;
        return this;
    }

    /**
     * Set the URI on which the GET method is applied.
     * @param vri
     * @throws ToxOtisException
     *      If the provided URI is not secure (https)
     */
    public SecureClient setUri(VRI vri) throws ToxOtisException {
        if (vri != null) {
            if (!vri.getProtocol().equals("https")) {
                throw new ToxOtisException();
            }
        }
        this.vri = vri;
        return this;
    }

    protected abstract javax.net.ssl.HttpsURLConnection initializeConnection(final java.net.URI uri) throws ToxOtisException;

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

    /**
     * Get the response body in plain text format.
     * @return
     *      String consisting of the response body.
     * @throws ToxOtisException
     *      In case some communication, server or request error occurs.
     */
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

    /** Get the response status */
    public int getResponseCode() throws ToxOtisException {
        if (con == null) {
            con = initializeConnection(vri.toURI());
        }
        try {
            return con.getResponseCode();
        } catch (IOException ex) {
            throw new ToxOtisException(ErrorCause.CommunicationError, ex);
        }
    }

    public void close() throws IOException {
        if (con != null) {
            con.disconnect();
        }
    }
}
