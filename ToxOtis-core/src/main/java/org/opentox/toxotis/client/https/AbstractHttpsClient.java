/*
 *
 * ToxOtis
 *
 * ToxOtis is the Greek word for Sagittarius, that actually means ‘archer’. ToxOtis
 * is a Java interface to the predictive toxicology services of OpenTox. ToxOtis is
 * being developed to help both those who need a painless way to consume OpenTox
 * services and for ambitious service providers that don’t want to spend half of
 * their time in RDF parsing and creation.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 *
 */
package org.opentox.toxotis.client.https;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.opentox.toxotis.client.HttpStatusCodes;
import org.opentox.toxotis.client.IClient;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.http.PostHttpClient;
import org.opentox.toxotis.client.RequestHeaders;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.exceptions.impl.ConnectionException;
import org.opentox.toxotis.exceptions.impl.RemoteServiceException;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
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
    public AbstractHttpsClient addHeaderParameter(String paramName, String paramValue) throws NullPointerException {
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

    protected abstract javax.net.ssl.HttpsURLConnection initializeConnection(final java.net.URI uri) throws ServiceInvocationException;

    protected javax.net.ssl.HttpsURLConnection connect(final java.net.URI uri) throws ServiceInvocationException {
        connectionLock.lock();
        try {
            return initializeConnection(uri);
        } finally {
            connectionLock.unlock();
        }
    }

    protected InputStream getConnectionInputStream() throws ConnectionException {
        if (con == null) {
            throw new NullPointerException("No connection established");
        }
        InputStream is = null;
        try {
            is = con.getInputStream();
        } catch (IOException ex) {
            ConnectionException connectionExc = new ConnectionException("Input-Output error occured while connecting to "
                    + "the server. Cannot initialize an InputStream to " + getUri(), ex);
            connectionExc.setActor(getUri() != null ? getUri().toString() : "No target specified");
            throw connectionExc;
        }
        return is;
    }

    /** Get the normal stream of the response (body) */
    @Override
    public java.io.InputStream getRemoteStream() throws ConnectionException, ServiceInvocationException {
//        readLock.lock();       
//        try {
        if (con == null) {
            con = connect(vri.toURI());
        }
        if (con == null) {
            ConnectionException badConnection = new ConnectionException("Cannot establish connection to " + getUri());
            badConnection.setActor(vri != null ? vri.toString() : "N/A");
            badConnection.setDetails("An instance of java.net.HttpURLConnection was attempted but the connection failed. Null "
                    + "connection returned. The remote resource at '" + getUri() + "' seems not to be responding. The stream could "
                    + "not open.");
            throw badConnection;
        }
        int connectionResponseCode = getResponseCode();
        if (connectionResponseCode == HttpStatusCodes.Accepted.getStatus()
                || connectionResponseCode == HttpStatusCodes.Success.getStatus()
                || connectionResponseCode == HttpStatusCodes.Created.getStatus()) {
            return new java.io.BufferedInputStream(getConnectionInputStream(), bufferSize);
        } else {
            return new java.io.BufferedInputStream(con.getErrorStream(), bufferSize);
        }
//        } 
//        finally {
////            readLock.unlock();
//        }
    }

    /**
     * Get the response body in plain text format.
     * @return
     *      String consisting of the response body.
     * @throws ToxOtisException
     *      In case some communication, server or request error occurs.
     */
    @Override
    public String getResponseText() throws ServiceInvocationException {
//        readLock.lock();
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
            ConnectionException connectionExc = new ConnectionException("Input-Output error occured while connecting to "
                    + "the server. Cannot read input stream to " + getUri(), io);
            connectionExc.setActor(getUri() != null ? getUri().toString() : "No target specified");
            throw connectionExc;
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
//            readLock.unlock();
            if (closeException != null) {
                ConnectionException connExc = new ConnectionException("Stream could not close", closeException);
                connExc.setActor(getUri() != null ? getUri().toString() : "N/A");
                throw connExc;
            }
        }
    }

    /** Get the response status */
    @Override
    public int getResponseCode() throws ConnectionException, ServiceInvocationException {
//        readLock.lock();
        int responseCode = 0;
        try {
            if (con == null) {
                connect(getUri().toURI());
            }
            responseCode = con.getResponseCode();
        } catch (IOException ex) {
            ConnectionException connectionExc = new ConnectionException("Input-Output error occured while connecting to "
                    + "the server", ex);
            connectionExc.setActor(getUri() != null ? getUri().toString() : "No target specified");
            throw connectionExc;
        }
//        finally {
////            readLock.unlock();
//        }
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
        return token != null ? addHeaderParameter(RequestHeaders.SSO_AUTHORIZATION, token.stringValue()) : this;
    }

    @Override
    public com.hp.hpl.jena.ontology.OntModel getResponseOntModel() throws ServiceInvocationException {
        return getResponseOntModel("RDF/XML");
    }

    @Override
    public com.hp.hpl.jena.ontology.OntModel getResponseOntModel(String specification) throws ServiceInvocationException {
//        readLock.lock();
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
        } catch (final ServiceInvocationException ex) {
            throw ex;
        } catch (Exception ex) {// Exception to become something more specific
            ex.printStackTrace();
            throw new RemoteServiceException("Remote service at '" + getUri() + "' did not provide a valid "
                    + "RDF representation. The returned representation cannot be parsed", ex);
        }
//        finally {
////            readLock.unlock();
//        }
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
    public java.util.Set<VRI> getResponseUriList() throws ServiceInvocationException {
//        readLock.lock();
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
                    throw new RemoteServiceException(
                            "The server returned an invalid URI : '" + line + "'", ex);
                }
            }
        } catch (IOException io) {
            ConnectionException connectionExc = new ConnectionException("Input-Output error occured while connecting to "
                    + "the server", io);
            connectionExc.setActor(getUri() != null ? getUri().toString() : "No target specified");
            throw connectionExc;
        } finally {
            ServiceInvocationException serviceInvocationException = null;
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    serviceInvocationException = new ConnectionException("The stream reader (SR) over the connection to the "
                            + "remote service at '" + getUri() + "' cannot close gracefully", ex);
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException ex) {
                    serviceInvocationException = new ConnectionException("The input stream reader (ISR) over the connection to the "
                            + "remote service at '" + getUri() + "' cannot close gracefully", ex);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    serviceInvocationException = new ConnectionException("The input stream (IS) over the connection to the "
                            + "remote service at '" + getUri() + "' cannot close gracefully", ex);
                }
            }
//            readLock.unlock();
            if (serviceInvocationException != null) {
                throw serviceInvocationException;
            }
        }
        return setOfUris;
    }

    @Override
    public String getResponseContentType() throws ServiceInvocationException {
        String ct = getResponseHeader(RequestHeaders.CONTENT_TYPE);
        if (ct == null) {
            return null;
        }
        return ct.split(";")[0];

    }

    @Override
    public String getResponseHeader(String header) throws ServiceInvocationException {
        if (con == null) {
            initializeConnection(vri.toURI());
        }
        for (int i = 0;; i++) {
            String headerName = con.getHeaderFieldKey(i);
            String headerValue = con.getHeaderField(i);
            if (headerName == null && headerValue == null) {
                break;
            }
            if (headerName == null) {
                continue;
            } else {
                if (headerName.equalsIgnoreCase(header)) {
                    return headerValue;
                }

            }
        }
        return null;
    }
}
