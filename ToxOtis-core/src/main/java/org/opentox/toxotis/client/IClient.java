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

import com.hp.hpl.jena.ontology.OntModel;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import org.opentox.toxotis.exceptions.impl.*;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 * Generic interface for a client in ToxOtis. 
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface IClient extends Closeable {

    String httpProtocol = "HTTP";
    String httpSecureProtocol = "HTTPS";
    
    /** Standard UTF-8 Encoding */
    String URL_ENCODING = "UTF-8";

    /**
     * Note: if the parameter name (paramName) is either 'Accept' or 'Content-type', this
     * method will override {@link IClient#setMediaType(java.lang.String) setMediaType} and
     * {@link IPostClient#setContentType(java.lang.String) setContentType} respectively. In general
     * it is not advisable that you choose this method for setting values to these headers. Once the
     * parameter name and its value are submitted to the client, they are encoded using the
     * standard UTF-8 encoding.
     * @param paramName 
     *      Name of the parameter which will be posted in the header
     * @param paramValue 
     *      Parameter value
     * @return 
     *      The current instance of client with updated headers.
     * @throws NullPointerException
     *      If any of the arguments is <code>null</code>.
     */
    IClient addHeaderParameter(String paramName, String paramValue);

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
     * 
     * @return
     *      This object with an updated header.
     */
    IClient authorize(AuthenticationToken token);

    /**
     * Closes this stream and releases any system resources associated
     * with it. If the stream is already closed then invoking this
     * method has no effect.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
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
     */
    InputStream getRemoteStream() throws ServiceInvocationException;

    /**
     * Get the HTTP status of the response
     * @return
     * Response status code.
     * @throws ToxOtisException
     * In case the connection cannot be established because a {@link ToxOtisException }
     * is thrown while a connection is attempted to the remote service.
     */
    int getResponseCode() throws ServiceInvocationException;

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
    OntModel getResponseOntModel() throws ServiceInvocationException;

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
    OntModel getResponseOntModel(String specification) throws ServiceInvocationException;

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
    String getResponseText() throws ServiceInvocationException;

    /**
     * Get the targetted URI
     * @return
     *      The target URI
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
     * Specify the media-type to be used in the <tt>Accept</tt> header providing
     * an instance of {@link Media }.
     * 
     * @param mediaType
     *      Accepted media-type
     * 
     * 
     * @see RequestHeaders#ACCEPT
     */
    IClient setMediaType(Media mediaType);

    /**
     * Set the URI on which the GET method is applied.
     * 
     * @param vri
     *      The URI that will be used by the client to perform the 
     *      remote connection.
     */
    IClient setUri(VRI vri) throws ToxOtisException;

    /**
     * Provide the target URI as a String
     * 
     * @param uri 
     *      The target URI as a String.
     * @throws java.net.URISyntaxException 
     *      In case the provided URI is syntactically incorrect.
     */
    IClient setUri(String uri) throws URISyntaxException, ToxOtisException;

    /**
     * Get the response of the remote service as a Set of URIs. The media type of
     * the request, as specified by the <code>Accept</code> header is set to
     * <code>text/uri-list</code>.
     * 
     * @return
     *      Set of URIs returned by the remote service.
     * @throws ToxOtisException
     *      In case some I/O communication error inhibits the transmittance of
     *      data between the client and the server or a some stream cannot close.
     */
    Set<VRI> getResponseUriList() throws ServiceInvocationException;

    /**
     * Returns the connection lock. While a connection is being established
     * this lock is held by the client. You can monitor it to know exactly when the
     * connection has been established.
     * 
     * @return 
     *      The connection Write Lock.
     */
    WriteLock getConnectionLock();

    /**
     * Returns the read-lock of the client. As long as the client reads the
     * response that is returned from the remote location, this lock is being
     * held by the client. Use this lock to know exactly when the client starts
     * and stops reading from the response stream.
     * 
     * @return 
     *      The read-lock.
     */
    ReadLock getReadLock();

    /**
     * The header of the response.
     * 
     * @param header
     *      Name of the header.
     * @return
     *      The value of the header as it is specified in the response.
     * 
     * @throws ServiceInvocationException 
     *      A ServiceInvocationException  is thrown is the response headers are
     *      not accessible for any reason including, but not limited to, 
     *      connectivity issues.
     */
    String getResponseHeader(String header) throws ServiceInvocationException;

    /**
     * Returns the content-type of the response. This is a short-cut for
     * {@link #getResponseHeader(java.lang.String) #getResponseHeader("Content-type")}.
     * 
     * @return
     *      The content-type of the response.
     * @throws ServiceInvocationException 
     *      A ServiceInvocationException is thrown in case this header value 
     *      is not accessible as for example if the remote server is not
     *      reachable.
     */
    String getResponseContentType() throws ServiceInvocationException;
}
