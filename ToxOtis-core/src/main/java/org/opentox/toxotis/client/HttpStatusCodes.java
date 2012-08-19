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

/**
 * Standard HTTP status codes.
 * @author chung
 */
public enum HttpStatusCodes {

    /**
     * The request has succeeded. 
     * 
     * <p>
     * The information returned with the 
     * response is dependent on the method used in the request, for example:
     * GET an entity corresponding to the requested resource is sent in the response;
     * HEAD the entity-header fields corresponding to the requested resource are 
     * sent in the response without any message-body;
     * POST an entity describing or containing the result of the action;
     * TRACE an entity containing the request message as received by the end server.
     * </p>
     */
    Success(200, "The request has succeeded."),
    /**
     * The request has been fulfilled and resulted in a new resource being created. 
     * 
     * <p>
     * The newly created resource can be referenced by the URI(s) returned in the 
     * entity of the response, with the most specific URI for the resource given 
     * by a Location header field. The response SHOULD include an entity containing
     * a list of resource characteristics and location(s) from which the user or 
     * user agent can choose the one most appropriate. The entity format is 
     * specified by the media type given in the Content-Type header field. The origin 
     * server MUST create the resource before returning the 201 status code. If the 
     * action cannot be carried out immediately, the server SHOULD respond with 202 
     * (Accepted) response instead.
     * </p>
     */
    Created(201, "The request has been fulfilled and resulted in a new resource being created."),
    /**
     * The request has been accepted for processing, but the processing has not
     * been completed. 
     * 
     * <p>The request might or might not eventually be acted upon, 
     * as it might be disallowed when processing actually takes place. There is 
     * no facility for re-sending a status code from an asynchronous operation 
     * such as this.</p>
     * 
     * <p>The 202 response is intentionally non-committal. Its purpose is to allow a 
     * server to accept a request for some other process (perhaps a batch-oriented 
     * process that is only run once per day) without requiring that the user 
     * agent's connection to the server persist until the process is completed. 
     * The entity returned with this response SHOULD include an indication of the 
     * request's current status and either a pointer to a status monitor or some 
     * estimate of when the user can expect the request to be fulfilled.</p>
     */
    Accepted(202, "The request has been accepted for processing, but the processing has not been completed."),
    /**
     * The server has fulfilled the request but does not need to return an 
     * entity-body, and might want to return updated meta-information. 
     * 
     * <p>The response MAY include new or updated meta-information in the form of 
     * entity-headers, which if present SHOULD be associated with the requested 
     * variant.</p>
     * 
     * <p>If the client is a user agent, it SHOULD NOT change its document 
     * view from that which caused the request to be sent. This response is
     * primarily intended to allow input for actions to take place without 
     * causing a change to the user agent's active document view, although any 
     * new or updated meta-information SHOULD be applied to the document currently 
     * in the user agent's active view.</p>
     * 
     * <p>The 204 response MUST NOT include a message-body, and thus is always 
     * terminated by the first empty line after the header fields.</p>
     */
    NoContent(204, "The server has fulfilled the request but does not need to return "
    + "an entity-body, and might want to return updated metainformation."),
    /**
     * The requested resource has been assigned a new permanent URI and any 
     * future references to this resource SHOULD use one of the returned URIs. 
     * 
     * 
     * <p>Clients with link editing capabilities ought to automatically re-link 
     * references to the Request-URI to one or more of the new references returned 
     * by the server, where possible. This response is cacheable unless indicated otherwise.</p>
     * 
     * <p>The new permanent URI SHOULD be given by the Location field in the response. 
     * Unless the request method was HEAD, the entity of the response SHOULD contain 
     * a short hypertext note with a hyperlink to the new URI(s).</p>
     * 
     * <p>If the 301 status code is received in response to a request other than 
     * GET or HEAD, the user agent MUST NOT automatically redirect the request 
     * unless it can be confirmed by the user, since this might change the 
     * conditions under which the request was issued.</p>
     * 
     * <p>
     * <pre>
     * Note: When automatically redirecting a POST request after
     * receiving a 301 status code, some existing HTTP/1.0 user agents
     * will erroneously change it into a GET request.</pre>
     * </p>
     */
    MovedPermanently(301, "The requested resource has been assigned a new permanent URI "
    + "and any future references to this resource SHOULD use one of the returned URIs."),
    /**
     * The requested resource resides temporarily under a different URI. 
     * 
     * <p>Since the redirection might be altered on occasion, the client 
     * SHOULD continue to use the Request-URI for future requests. This 
     * response is only cacheable if indicated by a Cache-Control or Expires 
     * header field.</p>
     * 
     * <p>The temporary URI SHOULD be given by the Location field in the response. 
     * Unless the request method was HEAD, the entity of the response SHOULD 
     * contain a short hypertext note with a hyperlink to the new URI(s).</p>
     * 
     * <p>If the 302 status code is received in response to a request other than 
     * GET or HEAD, the user agent MUST NOT automatically redirect the request 
     * unless it can be confirmed by the user, since this might change the
     * conditions under which the request was issued.</p>
     * 
     * <p>Note: RFC 1945 and RFC 2068 specify that the client is not allowed
     * to change the method on the redirected request.  However, most
     * existing user agent implementations treat 302 as if it were a 303
     * response, performing a GET on the Location field-value regardless
     * of the original request method. The status codes 303 and 307 have
     * been added for servers that wish to make unambiguously clear which
     * kind of reaction is expected of the client.</p>
     */
    Found(302, "The requested resource resides temporarily under a different URI."),
    /**
     * The response to the request can be found under a different URI and 
     * SHOULD be retrieved using a GET method on that resource. 
     * 
     * <p>This method exists primarily to allow the output of a POST-activated 
     * script to redirect the user agent to a selected resource. The new URI is 
     * not a substitute reference for the originally requested resource. 
     * The 303 response MUST NOT be cached, but the response to the second 
     * (redirected) request might be cacheable.</p>
     * 
     * <p>The different URI SHOULD be given by the Location field in the response. 
     * Unless the request method was HEAD, the entity of the response SHOULD 
     * contain a short hypertext note with a hyperlink to the new URI(s).</p>
     * 
     * <p>Note: Many pre-HTTP/1.1 user agents do not understand the 303
     * status. When interoperability with such clients is a concern, the
     * 302 status code may be used instead, since most user agents react
     * to a 302 response as described here for 303.</p>
     */
    SeeOther(303, "The response to the request can be found under a different URI "
    + "and SHOULD be retrieved using a GET method on that resource."),
    /**
     * If the client has performed a conditional GET request and access is allowed, 
     * but the document has not been modified, the server SHOULD respond with this 
     * status code. The 304 response MUST NOT contain a message-body, and thus is 
     * always terminated by the first empty line after the header fields.
     */
    NotModified(304, "If the client has performed a conditional GET request "
    + "and access is allowed, but the document has not been modified, "
    + "the server SHOULD respond with this status code."),
    /**
     * The requested resource resides temporarily under a different URI. 
     * 
     * <p>Since the redirection MAY be altered on occasion, the client SHOULD 
     * continue to use the Request-URI for future requests. This response is only 
     * cacheable if indicated by a Cache-Control or Expires header field.</p>
     * 
     * <p>The temporary URI SHOULD be given by the Location field in the response. 
     * Unless the request method was HEAD, the entity of the response SHOULD contain a 
     * short hypertext note with a hyperlink to the new URI(s) , since many pre-HTTP/1.1 
     * user agents do not understand the 307 status. Therefore, the note SHOULD 
     * contain the information necessary for a user to repeat the original 
     * request on the new URI.</p>
     * 
     * <p>If the 307 status code is received in response to a request other than GET 
     * or HEAD, the user agent MUST NOT automatically redirect the request unless 
     * it can be confirmed by the user, since this might change the conditions 
     * under which the request was issued.</p>
     */
    TemporaryRedirect(307, "The requested resource resides temporarily under a different URI."),
    /**
     * The request could not be understood by the server due to malformed syntax.
     * The client SHOULD NOT repeat the request without modifications.
     */
    BadRequest(400, "The request could not be understood by the server due to malformed syntax."),
    /**
     * The request requires user authentication. 
     * 
     * <p>The response MUST include a 
     * WWW-Authenticate header field (section 14.47) containing a challenge 
     * applicable to the requested resource. The client MAY repeat the request 
     * with a suitable Authorization header field (section 14.8). If the request 
     * already included Authorization credentials, then the 401 response indicates 
     * that authorization has been refused for those credentials. If the 401 
     * response contains the same challenge as the prior response, and the user 
     * agent has already attempted authentication at least once, then the user 
     * SHOULD be presented the entity that was given in the response, since that 
     * entity might include relevant diagnostic information.</p>
     */
    Unauthorized(401, "The request requires user authentication."),
    /**
     * The server understood the request, but is refusing to fulfill it.
     * 
     * <p>Authorization will not help and the request SHOULD NOT be repeated. 
     * If the request method was not HEAD and the server wishes to make public why 
     * the request has not been fulfilled, it SHOULD describe the reason for the 
     * refusal in the entity. If the server does not wish to make this information 
     * available to the client, the status code 404 (Not Found) can be used instead.</p>
     */
    Forbidden(403, "The server understood the request, but is refusing to fulfill it."),
    /**
     * The server has not found anything matching the Request-URI. 
     * 
     * <p>No indication is given of whether the condition is temporary or permanent. 
     * The 410 (Gone) status code SHOULD be used if the server knows, through some 
     * internally configurable mechanism, that an old resource is permanently 
     * unavailable and has no forwarding address. This status code is commonly 
     * used when the server does not wish to reveal exactly why the request has 
     * been refused, or when no other response is applicable.</p>
     */
    NotFound(404, "The server has not found anything matching the Request-URI."),
    /**
     * The method specified in the Request-Line is not allowed for the resource identified 
     * by the Request-URI. 
     * 
     * <p>The response MUST include an Allow header containing a list of valid
     * methods for the requested resource.</p>
     */
    MethodNotAllowed(405, "The method specified in the Request-Line is not allowed "
    + "for the resource identified by the Request-URI."),
    /**
     * The server encountered an unexpected condition which prevented it 
     * from fulfilling the request.
     */
    InternalServerError(500, "The server encountered an unexpected condition which "
    + "prevented it from fulfilling the request."),
    /**
     * The server, while acting as a gateway or proxy, received an invalid 
     * response from the upstream server it accessed in attempting to fulfill the request.
     */
    BadGateway(502, "The server, while acting as a gateway or proxy, received an "
    + "invalid response from the upstream server it accessed in attempting "
    + "to fulfill the request."),
    /**
     * The server is currently unable to handle the request due to a 
     * temporary overloading or maintenance of the server. 
     * 
     * <p>The implication is that this is a temporary condition which will 
     * be alleviated after some delay. If known, the length of the delay MAY be 
     * indicated in a Retry-After header. If no Retry-After is given, the client 
     * SHOULD handle the response as it would for a 500 response.</p>
     * 
     * <p>Note: The existence of the 503 status code does not imply that a
     * server must use it when becoming overloaded. Some servers may wish
     * to simply refuse the connection.</p>
     */
    ServiceUnavailable(503, "The server is currently unable to handle the request "
    + "due to a temporary overloading or maintenance of the server.");
    /**
     * HTTP Status as integer
     */
    private int status;
    /**
     * Short RFC documentation.
     */
    private String documentation;

    private HttpStatusCodes() {
    }

    private HttpStatusCodes(int status, String documentation) {
        this.status = status;
        this.documentation = documentation;
    }

    private HttpStatusCodes(int status) {
        this.status = status;
    }

    /**
     * Get some short documentation about the status code.
     * The documentation is copied from the RFC-2616 specification.
     * @return 
     *      Documentation as String.
     */
    public String getDocumentation() {
        return documentation;
    }

    /**
     * Return the status code as an integer.
     * @return 
     *      Status code.
     */
    public int getStatus() {
        return status;
    }
}
