package org.opentox.toxotis.client;

import com.hp.hpl.jena.ontology.OntModel;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Set;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface IClient extends Closeable {

    /** Standard UTF-8 Encoding */
    final String URL_ENCODING = "UTF-8";

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
     *      If any of the arguments is <code>null</code>.
     */
    IClient addHeaderParameter(String paramName, String paramValue) throws NullPointerException, IllegalArgumentException;

    /**
     * Provide an authentication token to the client. This token is given to the
     * remote server to verify the client's identity. The remote service in turn
     * asks the openSSO service whether the underlying request is allowed for the
     * client with the given token. Services have also access to the client's data
     * such as username, name and email which might be stored on the server side
     * to provide accounting facilities.
     *
     * @param token
     * Authentication token which will be provided in the request's header.
     * Authentication/Authorization follow RFC's guidelines according to which
     * the token is provided using the Header {@link RequestHeaders#AUTHORIZATION
     * Authorization}.
     * @return
     * This object with an updated header.
     */
    IClient authorize(AuthenticationToken token);

    /**
     * Closes this stream and releases any system resources associated
     * with it. If the stream is already closed then invoking this
     * method has no effect.
     *
     * @throws IOException if an I/O error occurs
     */
    void close() throws IOException;

    /**
     * Retrieve the specified media type which is the value for the <code>Accept</code>
     * HTTP Header.
     * @return
     * The accepted media type.
     */
    String getMediaType();

    /**
     * Get the body of the HTTP response as InputStream.
     * @return
     * InputStream for the remote HTTP response
     * @throws ToxOtisException
     * In case an error status code is received from the remote location.
     * @throws java.io.IOException
     * In case some communication error occurs during the transmission
     * of the data.
     */
    InputStream getRemoteStream() throws ToxOtisException, IOException;

    /**
     * Get the HTTP status of the response
     * @return
     * Response status code.
     * @throws ToxOtisException
     * In case the connection cannot be established because a {@link ToxOtisException }
     * is thrown from the method {@link AbstractClient#initializeConnection(java.net.URI)
     * initializeConnection(URI)}.
     * @throws java.io.IOException
     * In case some communication error with the remote location occurs during
     * the transaction of data.
     */
    int getResponseCode() throws ToxOtisException, IOException;

    /**
     * If possible, get the ontological model provided in the response. This assumes
     * that the {@link RequestHeaders#ACCEPT Accept} Header of the request has value
     * <code>application/rdf+xml</code> or at least some other rdf-related MIME (like for
     * example <code>application/x-turtle</code>.
     *
     * @return
     * The ontological model from the response body.
     * @throws ToxOtisException
     * A ToxOtisException is thrown in case the server did not provide a valid
     * (syntactically correct) ontological model, or in case some communication
     * error will arise.
     */
    OntModel getResponseOntModel() throws ToxOtisException;

    /**
     * If possible, get the ontological model provided in the response. This assumes
     * that the {@link RequestHeaders#ACCEPT Accept} Header of the request has value
     * <code>application/rdf+xml</code> or at least some other rdf-related MIME (like for
     * example <code>application/x-turtle</code>.
     * @param specification
     * the langauge of the serialization; <code>null</code> selects the default, that
     * is "RDF/XML". Also available: "TTL", "N-TRIPLE" and "N3".
     * @return
     * The ontological model from the response body.
     * @throws ToxOtisException
     * A ToxOtisException is thrown in case the server did not provide a valid
     * (syntactically correct) ontological model, or in case some communication
     * error will arise.
     */
    OntModel getResponseOntModel(String specification) throws ToxOtisException;

    /**
     * Get the response body as a String in the format specified in the Accept header
     * of the request.
     *
     * @return
     * String consisting of the response body (in a MediaType which results
     * from content negotiation, taking into account the Accept header of the
     * request)
     * @throws ToxOtisException
     * In case some communication, server or request error occurs.
     */
    String getResponseText() throws ToxOtisException;

    /**
     * Get the targetted URI
     * @return The target URI
     */
    VRI getUri();

    /**
     * Specify the mediatype to be used in the <tt>Accept</tt> header.
     * @param mediaType
     * Accepted mediatype
     *
     * @see RequestHeaders#ACCEPT
     */
    IClient setMediaType(String mediaType);

    /**
     * Specify the mediatype to be used in the <tt>Accept</tt> header providing
     * an instance of {@link Media }.
     * @param mediaType
     * Accepted mediatype
     * @see RequestHeaders#ACCEPT
     */
    IClient setMediaType(Media mediaType);

    /**
     * Set the URI on which the GET method is applied.
     * @param vri
     * The URI that will be used by the client to perform the remote connection.
     */
    IClient setUri(VRI vri) throws ToxOtisException;

    /**
     * Provide the target URI as a String
     * @param uri The target URI as a String.
     * @throws java.net.URISyntaxException In case the provided URI is syntactically
     * incorrect.
     */
    IClient setUri(String uri) throws URISyntaxException, ToxOtisException;

    /**
     * Get the response of the remote service as a Set of URIs. The media type of
     * the request, as specified by the <code>Accept</code> header is set to
     * <code>text/uri-list</code>.
     * @return
     * Set of URIs returned by the remote service.
     * @throws ToxOtisException
     * In case some I/O communication error inhibits the transimittance of
     * data between the client and the server or a some stream cannot close.
     */
    Set<VRI> getResponseUriList() throws ToxOtisException;

}
