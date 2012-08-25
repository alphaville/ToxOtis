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
 * This is a collection of some HTTP Request and Entity Headers gathered from the
 * W3C specifications at http://www.w3.org/Protocols/rfc2616/rfc2616.html.
 * <p align=justify><em>Documentation copied here from the W3C specification for HTTP Headers : </em>
 * HTTP header fields, which include general-header (section 4.5), request-header
 * (section 5.3), response-header (section 6.2), and entity-header (section 7.1) fields,
 * follow the same generic format as that given in Section 3.1 of RFC 822 [9].
 * Each header field consists of a name followed by a colon (":") and the field value.
 * Field names are case-insensitive. The field value MAY be preceded by any amount of
 * LWS, though a single SP is preferred. Header fields can be extended over multiple
 * lines by preceding each extra line with at least one SP or HT. Applications ought to
 * follow "common form", where one is known or indicated, when generating HTTP constructs,
 * since there might exist some implementations that fail to accept anything beyond the common
 * forms.
 * </p>
 * <pre> message-header = field-name ":" [ field-value ]
 *      field-name     = token
 *      field-value    = *( field-content | LWS )
 *      field-content  = <the OCTETs making up the field-value
 *                       and consisting of either *TEXT or combinations
 *                       of token, separators, and quoted-string>
 *</pre>
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 *
 * @see <a href="http://www.cs.tut.fi/~jkorpela/http.html">HTTP Headers Quick Reference</a>
 * @see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616.html">W3C specification</a>
 */
public class RequestHeaders {

    private RequestHeaders(){
        // Hidden Constructor - RequestHeaders is a utility class.
    }
    /**
     * Specifies which Internet media types are acceptable
     * for the response and to assign preferences to them.
     * @see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.1">W3C specification for this header</a>
     */
    public static final String ACCEPT = "Accept";
    /**
     * Specifies which character encodings (confusingly called "charsets") are
     * acceptable for the response and to assign preferences to them.
     * @see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.2">W3C specification for this header</a>
     */
    public static final String ACCEPT_CHARSET = "Accept-Charset";
    /**
     * Specifies which data format tranformations, confusingly called
     * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.5">content
     * (en)codings</a>, such as compression  mechanisms, are acceptable for the
     * response and to assign preferences to them.
     * @see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.3">W3C specification for this header</a>
     */
    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    /**
     * Specifies which natural languages are acceptable for the response 
     * and to assign preferences to them. Useful for 
     * <a href="http://www.cs.tut.fi/~jkorpela/multi/5.html">language negotation.</a>
     * @see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.4">W3C specification for this header</a>
     */
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    /**
     * Consists of credentials containing the authentication information of
     * the client for the realm of the resource being requested
     * @see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.8">W3C specification for this header</a>
     */
    public static final String AUTHORIZATION = "Authorization";

    /**
     * Header used within OpenTox for exchanging authorization tokens (between services and
     * the central SSO server as well)
     */
    public static final String SSO_AUTHORIZATION = "subjectid";

    /**
     * Used as a modifier to the media-type, to indicate what additional data
     * format transformations such as compression have been applied to the entity-body.
     */
    public static final String CONTENT_ENCODING = "Content-Encoding";
    /**
     * Indicates the size (in octets) of the entity-body that is sent or that
     * would have been sent if it has reen requested.
     */
    public static final String CONTENT_LENGTH = "Content-Length";
    /**
     * Specifies the Internet media type  of the entity-body that is sent or
     * would have been sent if requested. Often includes a charset parameter
     * specifying the character encoding.
     */
    public static final String CONTENT_TYPE = "Content-Type";
    /**
     * Indicates that particular server behaviors are required by the client.
     * @see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.20">W3C specification for this header</a>
     */
    public static final String EXPECT = "Expect";
    /**
     * The Internet e-mail address for the human user who controls
     * the requesting browser or other client.
     * @see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.22">W3C specification for this header</a>
     */
    public static final String FROM = "From";
    /**
     * Specifies the Internet host and port number of the resource being
     * requested. Obligatory in all HTTP/1.1 requests.
     * @see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.23">W3C specification for this header</a>
     */
    public static final String HOST = "Host";
    /**
     * Used with a method to make it conditional: a client that has previously
     * obtained entities can verify that one of those entities is current by
     * including a list of their associated
     * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.11">entity tags</a>
     * in the If-Match header field.
     * @see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.24">W3C specification for this header</a>
     */
    public static final String IF_MATCH = "If-Match";
    /**
     * Provides a mechanism with the TRACE and OPTIONS  methods to limit the
     * number of proxies or gateways that can forward the request to the next inbound server.
     * @see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.31">W3C specification for this header</a>
     */
    public static final String MAX_FORWARDS = "Max-Forwards";
    /**
     * Used by a client to identify itself (or its user) to a proxy
     * which requires authentication.
     * @see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.34">W3C specification for this header</a>
     */
    public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
    /**
     * Restricts the request to some part(s), specified as range(s) of octets, in the resource.
     * @see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.35">W3C specification for this header</a>
     */
    public static final String RANGE = "Range";
    /**
     * Used by a client to specify, for the server's benefit, the address (URI)
     * of the resource from which the Request-URI was obtained.
     * @see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.36">W3C specification for this header</a>
     */
    public static final String REFERER = "Referer";
    /**
     * Contains information about the user agent (client) originating the request
     */
    public static final String USER_AGENT = "User-Agent";
}
