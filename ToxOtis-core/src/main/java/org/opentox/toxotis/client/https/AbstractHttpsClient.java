package org.opentox.toxotis.client.https;

import com.hp.hpl.jena.ontology.OntModel;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.IClient;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.http.PostHttpClient;
import org.opentox.toxotis.client.RequestHeaders;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.impl.SimpleOntModelImpl;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.aa.SSLConfiguration;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class AbstractHttpsClient implements Closeable, IClient {

    /** Target secure URI */
    protected VRI vri = null;
    /** Connection to the above URI */
    protected javax.net.ssl.HttpsURLConnection con = null;
    /** Size of a buffer used to download the data from the remote server */
    protected static final int bufferSize = 4194304;
    /** Accepted mediatype  */
    protected String acceptMediaType = null;
    /** A mapping from parameter names to their corresponding values */
    protected Map<String, String> headerValues = new HashMap<String, String>();
    private ReentrantReadWriteLock.ReadLock readLock = new ReentrantReadWriteLock().readLock();
    private ReentrantReadWriteLock.WriteLock connectionLock = new ReentrantReadWriteLock().writeLock();

    public AbstractHttpsClient() {
        SSLConfiguration.initializeSSLConnection();
    }

    public AbstractHttpsClient(VRI vri) {
        this();
        this.vri = vri;
    }

    /**
     * Get the targetted URI
     * @return The target URI
     */
    @Override
    public VRI getUri() {
        return vri;
    }

    @Override
    public ReentrantReadWriteLock.WriteLock getConnectionLock() {
        return connectionLock;
    }

    @Override
    public ReentrantReadWriteLock.ReadLock getReadLock() {
        return readLock;
    }

    @Override
    public String getMediaType() {
        return acceptMediaType;
    }

    /**
     * Note: if the parameter name (paramName) is either 'Accept' or 'Content-type', this
     * method will override {@link PostHttpClient#setMediaType(java.lang.String) setMediaType} and
     * {@link PostHttpClient#setContentType(java.lang.String) setContentType} respectively. In general
     * it is not advisable that you choose this method for setting values to these headers. Once the
     * parameter name and its value are submitted to the client, they are encoded using the
     * standard UTF-8 encoding.
     * @param paramName Name of the parameter which will be posted in the header
     * @param paramValue Parameter value
     * @return This object
     * @throws NullPointerException
     *          If any of the arguments is null
     */
    @Override
    public AbstractHttpsClient addHeaderParameter(String paramName, String paramValue) throws NullPointerException, IllegalArgumentException {
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
    @Override
    public AbstractHttpsClient setMediaType(String mediaType) {
        this.acceptMediaType = mediaType;
        return this;
    }

    /**
     * Set the URI on which the GET method is applied.
     * @param vri
     * @throws ToxOtisException
     *      If the provided URI is not secure (https)
     */
    @Override
    public AbstractHttpsClient setUri(VRI vri) throws ToxOtisException {
        if (vri != null) {
            if (!vri.getProtocol().equals("https")) {
                throw new ToxOtisException("The provided URI's protocol is not HTTPS. Better use ClientFactory to create your clients.");
            }
        }
        this.vri = vri;
        return this;
    }

    protected abstract javax.net.ssl.HttpsURLConnection initializeConnection(final java.net.URI uri) throws ToxOtisException;

    protected javax.net.ssl.HttpsURLConnection connect(final java.net.URI uri) throws ToxOtisException {
        connectionLock.lock();
        try {
            return initializeConnection(uri);
        } finally {
            connectionLock.unlock();
        }
    }

    /** Get the normal stream of the response (body) */
    @Override
    public java.io.InputStream getRemoteStream() throws ToxOtisException, java.io.IOException {
        readLock.lock();
        try {
            if (con == null) {
                con = connect(vri.toURI());
            }
            if (con == null) {
                throw new ToxOtisException(ErrorCause.CommunicationError, "Comminucation Error with the remote");
            }
            if (con.getResponseCode() == 200 || con.getResponseCode() == 202 || con.getResponseCode() == 201) {
                return new java.io.BufferedInputStream(con.getInputStream(), bufferSize);
            } else {
                return new java.io.BufferedInputStream(con.getErrorStream(), bufferSize);
            }
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Get the response body in plain text format.
     * @return
     *      String consisting of the response body.
     * @throws ToxOtisException
     *      In case some communication, server or request error occurs.
     */
    @Override
    public String getResponseText() throws ToxOtisException {
        readLock.lock();
        InputStream is = null;
        BufferedReader reader = null;
        try {
            if (con == null) {
                con = connect(vri.toURI());
            }
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
            IOException closeException = null;
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    closeException = ex;
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    closeException = ex;
                }
            }
            readLock.unlock();
            if (closeException != null) {
                throw new ToxOtisException(closeException);
            }
        }
    }

    /** Get the response status */
    @Override
    public int getResponseCode() throws ToxOtisException, java.io.IOException {
        readLock.lock();
        int responseCode = 0;
        try {
            if (con == null) {
                con = connect(vri.toURI());
            }
            responseCode = con.getResponseCode();
        } finally {
            readLock.unlock();
        }
        return responseCode;
    }

    @Override
    public void close() throws IOException {
        if (con != null) {
            con.disconnect();
        }
    }

    @Override
    public AbstractHttpsClient authorize(AuthenticationToken token) {
        return token != null ? addHeaderParameter(RequestHeaders.SSO_AUTHORIZATION, token.getTokenUrlEncoded()) : this;
    }

    @Override
    public com.hp.hpl.jena.ontology.OntModel getResponseOntModel() throws ToxOtisException {
        return getResponseOntModel("RDF/XML");
    }

    @Override
    public com.hp.hpl.jena.ontology.OntModel getResponseOntModel(String specification) throws ToxOtisException {
        readLock.lock();
        if (specification == null) {
            specification = "RDF/XML";
        }
        try {
            com.hp.hpl.jena.ontology.OntModel om = new SimpleOntModelImpl();
            InputStream is = getRemoteStream();
            om.read(is, null, specification);
            if (is != null) {
                is.close();
            }
            return om;
        } catch (final Exception ex) {
            throw new ToxOtisException(ErrorCause.CommunicationError,
                    "Cannot read OntModel from " + vri.toString() + "due to communication"
                    + "error with the remote service.", ex);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public IClient setMediaType(Media mediaType) {
        this.acceptMediaType = mediaType.getMime();
        return this;
    }

    @Override
    public IClient setUri(String uri) throws URISyntaxException, ToxOtisException {
        this.vri = new VRI(uri);
        return this;
    }

    /**
     * Get the response of the remote service as a Set of URIs. The media type of
     * the request, as specified by the <code>Accept</code> header is set to
     * <code>text/uri-list</code>.
     * @return
     *      Set of URIs returned by the remote service.
     * @throws ToxOtisException
     *      In case some I/O communication error inhibits the transimittance of
     *      data between the client and the server or a some stream cannot close.
     */
    @Override
    public java.util.Set<VRI> getResponseUriList() throws ToxOtisException {
        readLock.lock();
        setMediaType(Media.TEXT_URI_LIST);// Set the mediatype to text/uri-list
        java.util.Set<VRI> setOfUris = new java.util.HashSet<VRI>();
        java.io.InputStreamReader isr = null;
        java.io.InputStream is = null;
        java.io.BufferedReader reader = null;
        try {
            if (con == null) {
                con = connect(vri.toURI());
            }
            is = getRemoteStream();
            isr = new java.io.InputStreamReader(is);
            reader = new java.io.BufferedReader(isr);
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    setOfUris.add(new VRI(line));
                } catch (URISyntaxException ex) {
                    throw new ToxOtisException(ErrorCause.InvalidUriReturnedFromRemote,
                            "The server returned an invalid URI : '" + line + "'", ex);
                }
            }
        } catch (final ToxOtisException cl) {
            throw cl;
        } catch (IOException io) {
            throw new ToxOtisException(ErrorCause.CommunicationError, io);
        } finally {
            ToxOtisException toxotisException = null;
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    toxotisException = new ToxOtisException(ErrorCause.StreamCouldNotClose, ex);
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException ex) {
                    toxotisException = new ToxOtisException(ErrorCause.StreamCouldNotClose, ex);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    toxotisException = new ToxOtisException(ErrorCause.StreamCouldNotClose, ex);
                }
            }
            readLock.unlock();
            if (toxotisException != null) {
                throw toxotisException;
            }
        }
        return setOfUris;
    }
}
