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
package org.opentox.toxotis.client;

import java.net.HttpURLConnection;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.exceptions.impl.BadRequestException;
import org.opentox.toxotis.exceptions.impl.ConnectionException;
import org.opentox.toxotis.exceptions.impl.RemoteServiceException;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.ontology.impl.SimpleOntModelImpl;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 * An abstract class providing necessary methods for the implementation of a
 * HTTP client. 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class AbstractClient implements IClient {

    /** Target URI */
    protected VRI vri = null;
    /** Connection to the above URI */
    protected java.net.HttpURLConnection con = null;
    /** Size of a buffer used to download the data from the remote server */
    protected static final int bufferSize = 4194304;
    /** Accepted media-type  */
    protected String acceptMediaType = null;
    /** A mapping from parameter names to their corresponding values */
    protected Map<String, String> headerValues = new HashMap<String, String>();
    private ReentrantReadWriteLock.ReadLock readLock = new ReentrantReadWriteLock().readLock();
    private ReentrantReadWriteLock.WriteLock connectionLock = new ReentrantReadWriteLock().writeLock();
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AbstractClient.class);
    private static final String NO_TARGET_MSG = "No target specified",
            IO_ERROR_MSG = "Input-Output error occured while connecting to the server at %s";

    public AbstractClient() {
    }

    public AbstractClient(VRI vri) {
        this.vri = vri;
    }

    protected HttpURLConnection getConnection() {
        return con;
    }

    public void setConnection(HttpURLConnection con) {
        this.con = con;
    }

    @Override
    public WriteLock getConnectionLock() {
        return connectionLock;
    }

    @Override
    public ReadLock getReadLock() {
        return readLock;
    }

    /**
     * Get the target URI.
     * @return 
     *      The target URI
     */
    @Override
    public VRI getUri() {
        return vri;
    }

    /**
     * Retrieve the specified media type which is the value for the <code>Accept</code>
     * HTTP Header.
     * @return
     *      The accepted media type.
     */
    @Override
    public String getMediaType() {
        return acceptMediaType;
    }

    /**
     * Note: if the parameter name (paramName) is either 'Accept' or 'Content-type', this
     * method will override {@link IClient#setMediaType(java.lang.String) setMediaType} and
     * {@link IPostClient#setContentType(java.lang.String) setContentType} respectively. In general
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
    public AbstractClient addHeaderParameter(String paramName, String paramValue) throws NullPointerException {
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
     * Provide an authentication token to the client. This token is given to the
     * remote server to verify the client's identity. The remote service in turn
     * asks the openSSO service whether the underlying request is allowed for the
     * client with the given token. Services have also access to the client's data
     * such as username, name and email which might be stored on the server side
     * to provide accounting facilities.
     *
     * @param token
     *      Authentication token which will be provided in the request's header.
     *      Authentication/Authorization follow RFC's guidelines according to which
     *      the token is provided using the Header {@link RequestHeaders#AUTHORIZATION
     *      Authorization}.
     * @return
     *      This object with an updated header.
     */
    @Override
    public AbstractClient authorize(AuthenticationToken token) {
        return token != null ? addHeaderParameter(RequestHeaders.SSO_AUTHORIZATION, token.stringValue()) : this;
    }

    /**
     * Initiate a connection to the remote location identified by the provided URI and
     * using the already specified header parameters.
     * 
     * @param uri
     *      The location to which the HTTP connection should be made.
     * @return
     *      An instance of HttpURLConnection that is used to perform the remote
     *      HTTP request.
     * @throws ToxOtisException
     *      In case an error status code is received from the remote service or
     *      an I/O exception is thrown due to communication problems with the remote
     *      server.
     */
    protected abstract java.net.HttpURLConnection initializeConnection(final java.net.URI uri) throws ServiceInvocationException;

    protected java.net.HttpURLConnection connect(final java.net.URI uri) throws ServiceInvocationException {
        connectionLock.lock();
        try {
            return initializeConnection(uri);
        } finally {
            connectionLock.unlock();
        }
    }

    protected InputStream getConnectionInputStream() throws ConnectionException {
        getReadLock().lock();
        try {
            if (getConnection() == null) {
                throw new NullPointerException("No connection established");
            }
            InputStream is = null;
            try {
                is = getConnection().getInputStream();
            } catch (IOException ex) {
                ConnectionException connectionExc = new ConnectionException(String.format(IO_ERROR_MSG, getUri()), ex);
                connectionExc.setActor(getUri() != null ? getUri().toString() : NO_TARGET_MSG);
                throw connectionExc;
            }
            return is;
        } finally {
            getReadLock().unlock();
        }

    }

    /**
     * Get the body of the HTTP response as InputStream.
     * @return
     *      InputStream for the remote HTTP response
     * @throws ServiceInvocationException
     *      In case an error status code is received from the remote location.
     * @throws ConnectionException
     *      In case no connection is feasible to the remote resource. Stream cannot
     *      open and the generated connection is <code>null</code>
     * @throws java.io.IOException
     *      In case some communication error occurs during the transmission
     *      of the data.
     */
    @Override
    public java.io.InputStream getRemoteStream() throws ServiceInvocationException {
        getReadLock().lock();
        try {
            if (getConnection() == null) {
                setConnection(connect(getUri().toURI()));
            }
            if (getConnection() == null) {
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
                return new java.io.BufferedInputStream(getConnection().getErrorStream(), bufferSize);
            }
        } finally {
            getReadLock().unlock();
        }
    }

    /**
     * Get the response body as a String in the format specified in the Accept header
     * of the request.
     *
     * @return
     *      String consisting of the response body (in a MediaType which results
     *      from content negotiation, taking into account the Accept header of the
     *      request)
     * @throws ToxOtisException
     *      In case some communication, server or request error occurs.
     */
    @Override
    public String getResponseText() throws ServiceInvocationException {
        InputStream is = null;
        BufferedReader reader = null;
        try {
            if (getConnection() == null) {
                setConnection(connect(vri.toURI()));
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
            ConnectionException connectionExc = new ConnectionException(String.format(IO_ERROR_MSG, getUri()), io);
            connectionExc.setActor(getUri() != null ? getUri().toString() : NO_TARGET_MSG);
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
            if (closeException != null) {
                ConnectionException connExc = new ConnectionException("Stream could not close", closeException);
                connExc.setActor(getUri() != null ? getUri().toString() : "N/A");
                throw connExc;
            }
        }
    }

    /**
     * If possible, get the ontological model provided in the response. This assumes
     * that the {@link RequestHeaders#ACCEPT Accept} Header of the request has value
     * <code>application/rdf+xml</code> or at least some other rdf-related MIME (like for
     * example <code>application/x-turtle</code>.
     *
     * @return
     *      The ontological model from the response body.
     * @throws ToxOtisException
     *      A ToxOtisException is thrown in case the server did not provide a valid
     *      (syntactically correct) ontological model, or in case some communication
     *      error will arise.
     */
    @Override
    public com.hp.hpl.jena.ontology.OntModel getResponseOntModel() throws ServiceInvocationException {
        return getResponseOntModel("RDF/XML");
    }

    /**
     * If possible, get the ontological model provided in the response. This assumes
     * that the {@link RequestHeaders#ACCEPT Accept} Header of the request has value
     * <code>application/rdf+xml</code> or at least some other rdf-related MIME (like for
     * example <code>application/x-turtle</code>.
     * @param specification
     *      the langauge of the serialization; <code>null</code> selects the default, that
     *      is "RDF/XML". Also available: "TTL", "N-TRIPLE" and "N3".
     * @return
     *      The ontological model from the response body.
     * @throws ServiceInvocationException
     *      A ServiceInvocationException is thrown in case the server did not provide a valid
     *      (syntactically correct) ontological model, or in case some communication
     *      error will arise.
     */
    @Override
    public com.hp.hpl.jena.ontology.OntModel getResponseOntModel(String specification) throws ServiceInvocationException {
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
        } catch (final NullPointerException npe) {// Exception to become something more specific
            logger.trace("Cannot parse RDF representation!", npe);
            BadRequestException badRequest = new BadRequestException("Remote service at '" + getUri() + "' did not provide a valid "
                    + "RDF representation. The returned representation cannot be parsed. Please check the validity of the "
                    + "RDF representations you provide", npe);
            badRequest.setDetails("The remote resource at " + getUri()
                    + " returned a representation that cannot be parsed (Accept-Header : "
                    + getMediaType() + ").");
            throw badRequest;
        } catch (IOException io) {
            throw new ConnectionException("Cannot close input stream after connected to '" + getUri() + "'.", io);
        } catch (Exception ex) {// Exception to become something more specific
            logger.warn("Unhandled exception in AbstractHttpClient#getResponseOntModel(String)::OntModel. Throwing \"Bad Request\"... "
                    + "!!!TO BE HANDLED BY OTHER MORE SPECIFIC EXCEPTION!!!", ex);
            throw new BadRequestException("Remote service at '" + getUri() + "' did not provide a valid "
                    + "RDF representation. The returned representation cannot be parsed", ex);
        }
    }

    /**
     * Get the HTTP status of the response
     * @return
     *      Response status code.
     * @throws ToxOtisException
     *      In case the connection cannot be established because a {@link ToxOtisException }
     *      is thrown from the method {@link AbstractHttpClient#initializeConnection(java.net.URI)
     *      initializeConnection(URI)}.
     * @throws java.io.IOException
     *      In case some communication error with the remote location occurs during
     *      the transaction of data.
     */
    @Override
    public int getResponseCode() throws ServiceInvocationException {
        int responseCode = 0;
        try {
            if (getConnection() == null) {
                connect(getUri().toURI());
            }
            responseCode = getConnection().getResponseCode();
        } catch (IOException ex) {
            ConnectionException connectionExc = new ConnectionException(String.format(IO_ERROR_MSG, getUri()), ex);
            connectionExc.setActor(getUri() != null ? getUri().toString() : NO_TARGET_MSG);
            throw connectionExc;
        }
        return responseCode;
    }

    /**
     * Specify the mediatype to be used in the <tt>Accept</tt> header.
     * @param mediaType 
     *      Accepted mediatype
     *
     * @see RequestHeaders#ACCEPT
     */
    @Override
    public AbstractClient setMediaType(String mediaType) {
        this.acceptMediaType = mediaType;
        return this;
    }

    /**
     * Specify the mediatype to be used in the <tt>Accept</tt> header providing
     * an instance of {@link Media }.
     * @param mediaType
     *      Accepted mediatype
     * @see RequestHeaders#ACCEPT
     */
    @Override
    public AbstractClient setMediaType(Media mediaType) {
        this.acceptMediaType = mediaType.getMime();
        return this;
    }

    /**
     * Set the URI on which the GET method is applied.
     * @param vri
     *      The URI that will be used by the client to perform the remote connection.
     */
    @Override
    public AbstractClient setUri(VRI vri) {
        this.vri = vri;
        return this;
    }

    /**
     * Provide the target URI as a String
     * @param uri The target URI as a String.
     * @throws java.net.URISyntaxException In case the provided URI is syntactically
     * incorrect.
     */
    @Override
    public AbstractClient setUri(String uri) throws java.net.URISyntaxException {
        this.vri = new VRI(uri);
        return this;
    }

    /**
     * Closes this stream and releases any system resources associated
     * with it. If the stream is already closed then invoking this
     * method has no effect.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        if (getConnection() != null) {
            getConnection().disconnect();
        }
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
        setMediaType(Media.TEXT_URI_LIST);// Set the mediatype to text/uri-list
        java.util.Set<VRI> setOfUris = new java.util.HashSet<VRI>();
        java.io.InputStreamReader isr = null;
        java.io.InputStream is = null;
        java.io.BufferedReader reader = null;
        try {
            if (getConnection() == null) {
                setConnection(connect(vri.toURI()));
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
            ConnectionException connectionExc = new ConnectionException(String.format(IO_ERROR_MSG, getUri()), io);
            connectionExc.setActor(getUri() != null ? getUri().toString() : NO_TARGET_MSG);
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
        if (getConnection() == null) {
            initializeConnection(vri.toURI());
        }
        for (int i = 0;; i++) {
            String headerName = getConnection().getHeaderFieldKey(i);
            String headerValue = getConnection().getHeaderField(i);
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
