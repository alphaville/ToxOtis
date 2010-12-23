package org.opentox.toxotis.client.http;

import org.opentox.toxotis.client.IClient;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.IPostClient;
import org.opentox.toxotis.client.RequestHeaders;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.ontology.impl.SimpleOntModelImpl;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 * An abstract class providing necessary methods for the implementation of a
 * HTTP client. 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class AbstractHttpClient implements IClient {

    /** Target URI */
    protected VRI vri = null;
    /** Connection to the above URI */
    protected java.net.HttpURLConnection con = null;
    /** Size of a buffer used to download the data from the remote server */
    protected static final int bufferSize = 4194304;
    /** Accepted mediatype  */
    protected String acceptMediaType = null;
    /** A mapping from parameter names to their corresponding values */
    protected Map<String, String> headerValues = new HashMap<String, String>();

    /**
     * Get the targetted URI
     * @return The target URI
     */
    public VRI getUri() {
        return vri;
    }

    /**
     * Retrieve the specified media type which is the value for the <code>Accept</code>
     * HTTP Header.
     * @return
     *      The accepted media type.
     */
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
    public AbstractHttpClient addHeaderParameter(String paramName, String paramValue) throws NullPointerException, IllegalArgumentException {
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
    public AbstractHttpClient authorize(AuthenticationToken token) {
        return token != null ? addHeaderParameter(RequestHeaders.AUTHORIZATION, token.getTokenUrlEncoded()) : this;
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
    protected abstract java.net.HttpURLConnection initializeConnection(final java.net.URI uri) throws ToxOtisException;

    /**
     * Get the body of the HTTP response as InputStream.
     * @return
     *      InputStream for the remote HTTP response
     * @throws ToxOtisException
     *      In case an error status code is received from the remote location.
     * @throws java.io.IOException
     *      In case some communication error occurs during the transmission
     *      of the data.
     */
    public java.io.InputStream getRemoteStream() throws ToxOtisException, java.io.IOException {
        if (con == null) {
            con = initializeConnection(vri.toURI());
        }
        if (con == null) {
            throw new ToxOtisException(ErrorCause.CommunicationError, "Comminucation Error with the remote");
        }
        if (con.getResponseCode() == 200 || con.getResponseCode() == 202 || con.getResponseCode() == 201) {
            return new java.io.BufferedInputStream(con.getInputStream(), bufferSize);
        } else {
            return new java.io.BufferedInputStream(con.getErrorStream(), bufferSize);
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
    public com.hp.hpl.jena.ontology.OntModel getResponseOntModel() throws ToxOtisException {
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
     * @throws ToxOtisException
     *      A ToxOtisException is thrown in case the server did not provide a valid
     *      (syntactically correct) ontological model, or in case some communication
     *      error will arise.
     */
    public com.hp.hpl.jena.ontology.OntModel getResponseOntModel(String specification) throws ToxOtisException {
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
    public int getResponseCode() throws ToxOtisException, java.io.IOException {
        if (con == null) {
            con = initializeConnection(vri.toURI());
        }
        return con.getResponseCode();
    }

    /**
     * Specify the mediatype to be used in the <tt>Accept</tt> header.
     * @param mediaType 
     *      Accepted mediatype
     *
     * @see RequestHeaders#ACCEPT
     */
    public AbstractHttpClient setMediaType(String mediaType) {
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
    public AbstractHttpClient setMediaType(Media mediaType) {
        this.acceptMediaType = mediaType.getMime();
        return this;
    }

    /**
     * Set the URI on which the GET method is applied.
     * @param vri
     *      The URI that will be used by the client to perform the remote connection.
     */
    public AbstractHttpClient setUri(VRI vri) {
        this.vri = vri;
        return this;
    }

    /**
     * Provide the target URI as a String
     * @param uri The target URI as a String.
     * @throws java.net.URISyntaxException In case the provided URI is syntactically
     * incorrect.
     */
    public AbstractHttpClient setUri(String uri) throws java.net.URISyntaxException {
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
    public void close() throws IOException {
        if (con != null) {
            con.disconnect();
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
    public java.util.Set<VRI> getResponseUriList() throws ToxOtisException {
        setMediaType(Media.TEXT_URI_LIST);// Set the mediatype to text/uri-list
        java.util.Set<VRI> setOfUris = new java.util.HashSet<VRI>();
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
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    throw new ToxOtisException(ErrorCause.StreamCouldNotClose, ex);
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException ex) {
                    throw new ToxOtisException(ErrorCause.StreamCouldNotClose, ex);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    throw new ToxOtisException(ErrorCause.StreamCouldNotClose, ex);
                }
            }
        }
        return setOfUris;
    }

}
