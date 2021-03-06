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
package org.opentox.toxotis.util.aa;

import org.opentox.toxotis.core.component.User;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Date;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.HttpStatusCodes;
import org.opentox.toxotis.client.IPostClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.https.PostHttpsClient;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.client.collection.Services.*;
import org.opentox.toxotis.exceptions.impl.ConnectionException;
import org.opentox.toxotis.exceptions.impl.ForbiddenRequest;
import org.opentox.toxotis.exceptions.impl.RemoteServiceException;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.exceptions.impl.Unauthorized;

/**
 * <p align=justify>OpenTox specific implementation regarding the token provided by the OpenSSO server.
 * Client authenticates against OpenSSO server and obtains a token (a successful authentication
 * identifier). The user data is drawn from the LDAP backend that also the Plone website uses
 * (http://opentox.org). This token is used to permit or deny a client a specific action.
 * The token encodes conjunction of user and point of time and has a certain lifetime.
 * If a token is authorized for the action according to current server policy,
 * the webservice performs the action.</p>
 *
 * @see <a href="http://opentox.org/data/documents/partner/wp/3/deliverables/Draft%20Report%20WP3-D3.3.">Documentation (Draft)</a>
 * @see <a href="http://opentox.org/dev/apis/api-1.1/AA">Authenticationa and Authorization API (OpenTox)</a>
 * @see <a href="http://en.wikipedia.org/wiki/Single_sign-on">Single Sign-on (SSO) in WikiPedia</a>
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class AuthenticationToken {

    /** Timestamp of token creation */
    private long tokenCreationTimestamp = -1L;
    /** The token as a String (Non-URL encoded!) */
    private String token;
    /** Local lifetime of the token. The token (as an object) will expire after 23 hours.
    Note that the real token expires in 24 hours.*/
    private static final long TOKEN_LOCAL_LIFESPAN = 23 * 3600 * 1000L; // 23hrs
    /** Encoding used to encode tokens received from the SSO server */
    private String encoding = "UTF-8";
    /** Flag used to tell if the token is logged out */
    private boolean logOut = false;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AuthenticationToken.class);

    /**
     * Initialize a new Authentication Token. The constructor also initializes
     * the SSL connection with the openSSO server.
     * @see Services#SSO_SERVER Default SSO Server
     */
    public AuthenticationToken() {
        super();
        SSLConfiguration.initializeSSLConnection();
    }

    /**
     * <p align=justify>Create a new token providing its String representation. The timestamp of
     * the constructor invocation is set as the creation timestamp of the token, but
     * it has to the validated against the openSSO server using the method {@link
     * AuthenticationToken#validate() validate()} to make sure that the token
     * will be acceptable by OpenTox services. End users are adviced to prefer the
     * constructor {@link AuthenticationToken#AuthenticationToken(java.lang.String, java.lang.String) },
     * providing their username and password.</p>
     * @param token
     *      The token as a String.
     */
    public AuthenticationToken(String token) {
        this();
        this.token = token;
        this.tokenCreationTimestamp = System.currentTimeMillis();
    }

    /**
     * <p align=justify>Create a new Authentication token providing your credentials.
     * These credentials are posted to the {@link org.opentox.toxotis.client.collection.Services.SingleSignOn#ssoAuthenticate() SSO AUTH} server
     * which (if they are valid) returns a token. This is used to construct a new
     * Authentication Token object. The timestamp of the method invocation is set as
     * the timestamp for the object construction. All data transactions take place
     * over secure layers (SSL) and using encryption (TLS).
     * </p>
     * @param username
     *      The username of an OpenTox user
     * @param password
     *      The password of an OpenTox user
     * @throws ServiceInvocationException
     *      In case the credentials are not valid, some exceptional event occurs
     *      during the data transaction or in case the SSO service returns an
     *      error code other than expected (e.g. encounters some internal error
     *      and returns a status code 500).
     */
    public AuthenticationToken(String username, String password) throws ServiceInvocationException {
        this();
        IPostClient poster = ClientFactory.createPostClient(Services.SingleSignOn.ssoAuthenticate());
        try {
            poster.addPostParameter("username", username);
            poster.addPostParameter("password", password);
            poster.post();
            int status = poster.getResponseCode();
            if (status >= HttpStatusCodes.BadRequest.getStatus()) {
                if (status == HttpStatusCodes.Unauthorized.getStatus()) {
                    throw new Unauthorized("Unauthorized User using username:"
                            + username + " and password:" + (password != null ? "YES" : "NO"));
                } else if (status == HttpStatusCodes.Forbidden.getStatus()) {
                    throw new ForbiddenRequest("Forbidden");
                }
            }
            String response = poster.getResponseText();
            if (response.contains("token.id=")) {
                response = response.replaceAll("token.id=", "");
            }
            this.token = response;
            this.tokenCreationTimestamp = System.currentTimeMillis();
        } finally {
            if (poster != null) {
                try {
                    poster.close();
                } catch (final IOException ex) {
                    ConnectionException conectionException = new ConnectionException("Cannot close connection to "
                            + Services.SingleSignOn.ssoAuthenticate(), ex);
                    conectionException.setActor(Services.SingleSignOn.ssoAuthenticate().toString());
                    conectionException.setHttpStatus(HttpStatusCodes.InternalServerError.getStatus());
                    throw conectionException;
                }
            }
        }

    }

    /**
     * Construct an authentication token as a clone of an existing one.
     * @param other
     *      An authentication token to be cloned.
     */
    public AuthenticationToken(AuthenticationToken other) {
        this();
        token = other.token;
        encoding = other.encoding;
        tokenCreationTimestamp = other.tokenCreationTimestamp;
    }

    /**
     * Cosntruct an authentication token using a password file. Password files are
     * generated by the {@link PasswordFileManager }. Make sure that you have configured
     * properly the password manager specifying the path of the <code>master key</code>
     * file using the method {@link PasswordFileManager#setMasterPasswordFile(java.lang.String)
     * setMasterPasswordFile}.
     * @param file
     *      Encrypted file containing your credentials.
     * @throws IOException
     *      In case the the file is not found or cannot be read.
     * @throws ToxOtisException
     *      In case the credentials are not valid, some exceptional event occurs
     *      during the data transaction or in case the SSO service returns an
     *      error code other than expected (e.g. encounters some internal error
     *      and returns a status code 500).
     * @throws ServiceInvocationException
     */
    public AuthenticationToken(File file) throws IOException, ToxOtisException, ServiceInvocationException {
        this(PasswordFileManager.CRYPTO.authFromFile(file));
    }

    /**
     * Retrieve the encoding used to encode tokens. The default value used in this
     * implementation is 'UTF-8'.
     * @return
     *      URL encoding standard.
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Specify the standard encoding that should be used for the encoding of the
     * token. This will affect the method {@link AuthenticationToken#getTokenUrlEncoded() }.
     * By default this is set to <code>UTF-8</code>.
     * @param encoding
     *      Standard Encoding.
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * Status of the token.
     */
    public enum TokenStatus {

        /**
         * The token is still active and can be used in
         * an A&amp;A session. However it is advisable to use the method {@link AuthenticationToken#validate() }
         * to validate the token against the SSO server prior to its use.
         */
        ACTIVE,
        /**
         * The token has either expired or it has been invalidated (the user has
         * logged out).
         */
        INACTIVE,
        /**
         * The token is not yet initialized or is in a state
         * that should noway be used for authentication/authorization.
         */
        DEAD;
    }

    /**
     * Get the status of the token. Token status is characterized by the enumeration
     * {@link TokenStatus }.
     * @return
     *      Status of the token as an element of {@link TokenStatus }. Will return the
     *      value {@link TokenStatus#DEAD DEAD} if the token was never successfully initialized,
     *      {@link TokenStatus#INACTIVE INACTIVE} if the token was invalidated (logged out)
     *      of if it has expired, otherwise {@link TokenStatus#ACTIVE ACTIVE}.
     */
    public TokenStatus getStatus() {
        if (logOut) {
            return TokenStatus.INACTIVE;
        }
        if (tokenCreationTimestamp == -1) {
            return TokenStatus.DEAD;
        } else if ((System.currentTimeMillis() - tokenCreationTimestamp) < TOKEN_LOCAL_LIFESPAN) {
            return TokenStatus.ACTIVE;
        } else {
            return TokenStatus.INACTIVE;
        }
    }

    /**
     * Returns the token as a String (not URL encoded). Tokens are retrived by the
     * remote openSSO server either using the constructor {@link AuthenticationToken#AuthenticationToken(java.lang.String, java.lang.String)
     * AuthenticationToken(String, String)} or the {@link PasswordFileManager } (read documentation therein).
     * @return
     *      Token as string; not encoded
     */
    public String stringValue() {
        return token;
    }

    /**
     * Returns the token as a URL encoded String.
     * @return
     *      Token encoded using the UTF-8 encoding.
     */
    public String getTokenUrlEncoded() {
        try {
            return URLEncoder.encode(token, encoding);
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalArgumentException("Bad encoding :" + encoding, ex);
        }
    }

    /**
     * Retrieve the timestamp of the token creation. If the difference between
     * the current timestamp retrieved by <code>System.currentTimeMillis()</code>
     * and the timestamp of the token creation exceeds {@link AuthenticationToken#TOKEN_LOCAL_LIFESPAN }
     * then the token is considered to be {@link TokenStatus#INACTIVE Inactive}.
     * @return
     *      The timestamp of the token creation
     * @see AuthenticationToken#getStatus()
     */
    public long getTokenCreationTimestamp() {
        return tokenCreationTimestamp;
    }

    public Date getTokenCreationDate() {
        return new Date(tokenCreationTimestamp);
    }

    /**
     * Ask the remote SSO server whether this token is valid. This method performs
     * a (secure) connection to the validation service on the SSO server and
     * POSTs the token. The RESTful API concerning A&amp;A can be found online at
     * <a href="http://opentox.org/dev/apis/api-1.1/AA">OpenTox API</a>.
     *
     * @return
     *      <code>true</code> if a positive response was returned from the remote
     *      server and <code>false</code> otherwise.
     * @throws ServiceInvocationException
     *      In case an error status is received from the remote service or there
     *      is some communication problem.
     */
    public boolean validate() throws ServiceInvocationException {
        if (!this.getStatus().equals(TokenStatus.ACTIVE)) {
            return false;
        }
        if (token == null || (token != null && token.isEmpty())) {
            return false;
        }
        IPostClient poster = null;
        try {
            poster = new PostHttpsClient(Services.SingleSignOn.ssoValidate());
            poster.addPostParameter("tokenid", stringValue());
            poster.post();
            int status = poster.getResponseCode();
            String message = (poster.getResponseText()).trim();
            if (status != HttpStatusCodes.Success.getStatus() && status != HttpStatusCodes.Unauthorized.getStatus()) {
                throw new ServiceInvocationException("Status code " + status + " received from " + Services.SingleSignOn.ssoValidate());
            } else if (status == HttpStatusCodes.Unauthorized.getStatus()) {
                if (!message.equals("boolean=false")) {
                    return false;
                } else {
                    throw new ServiceInvocationException("Status code " + status + " received from " + Services.SingleSignOn.ssoValidate());
                }
            }

            if (message.equals("boolean=true")) {
                return true;
            } else {
                return false;
            }
        } finally {
            if (poster != null) {
                try {
                    poster.close();
                } catch (final IOException ex) {
                    throw new ServiceInvocationException(ex);
                }
            }
        }
    }

    /**
     * Sends an invalidation request to the remote server. Once the token is
     * invalidated it can no longer be used in any authentication/authorization
     * sessions.
     * @throws ServiceInvocationException
     *      In case an error status is received from the remote service or there
     *      is some communication problem.
     */
    public void invalidate() throws ServiceInvocationException {
        if (TokenStatus.INACTIVE.equals(getStatus())) {
            return;
        }
        this.logOut = true;
        if (token == null || (token != null && token.isEmpty())) {
            return; // Nothing to invalidate!
        }
        IPostClient poster = null;
        try {
            poster = new PostHttpsClient(Services.SingleSignOn.ssoInvalidate());
            poster.addPostParameter("subjectid", stringValue());
            poster.post();
            int status = poster.getResponseCode();
            if (status != HttpStatusCodes.Success.getStatus()) {
                throw new ServiceInvocationException("Status code " + status + " received from " + Services.SingleSignOn.ssoInvalidate());
            }
        } finally {
            if (poster != null) {
                try {
                    poster.close();
                } catch (final IOException ex) {
                    throw new ServiceInvocationException(ex);
                }
            }
        }
    }

    /**
     * Information about the user that holds the token (for which the token was
     * created).
     * @return
     *      User information as an instance of {@link User }.
     * @throws ServiceInvocationException
     *      In case the remote identity (SSO) service is not reachable/accessible
     *      at the moment or returns a <code>401</code>/<code>403</code> error HTTP
     *      code implying that the token is not valid.
     * @throws ToxOtisException
     *      If the mail is invalid
     *
     */
    public User getUser() throws ServiceInvocationException, ToxOtisException {
        if (!this.getStatus().equals(TokenStatus.ACTIVE)) {
            throw new ForbiddenRequest("This token is not active: " + getStatus());
        }
        InputStream is = null;
        BufferedReader reader = null;
        IPostClient poster = null;
        User u = new User();
        try {
            poster = ClientFactory.createPostClient(Services.SingleSignOn.ssoAttributes());
            poster.addPostParameter("subjectid", stringValue());
            poster.post();
            int status = poster.getResponseCode();
            if (status != HttpStatusCodes.Success.getStatus()) {
                if (status == HttpStatusCodes.Unauthorized.getStatus()) {
                    throw new Unauthorized("User is not authorized to access the resource at : '" + poster.getUri() + "'. Status is " + status);
                }
                if (status == HttpStatusCodes.Forbidden.getStatus()) {
                    throw new ForbiddenRequest("Permission is denied to : '" + poster.getUri() + "'. Status is " + status);
                }
                throw new RemoteServiceException("Service '" + poster.getUri() + "' returned status code " + status);
            }
            final String valueKey = "userdetails.attribute.value=";
            final String nameKey = "userdetails.attribute.name=%s";
            is = poster.getRemoteStream();
            reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.equals(String.format(nameKey, "uid"))) {
                    line = reader.readLine();
                    if (line != null) {
                        line = line.trim();
                        u.setUid(line.replaceAll(valueKey, "") + "@" + Services.SSO_HOST);
                    }
                } else if (line.equals(String.format(nameKey, "mail"))) {
                    line = reader.readLine();
                    if (line != null) {
                        line = line.trim();
                        String mail = line.replaceAll(valueKey, "");
                        if (mail.equals("na")) {
                            mail = "anonymous@anonymous.org";
                        }
                        u.setMail(mail);
                    }
                } else if (line.equals(String.format(nameKey, "sn"))) {
                    line = reader.readLine();
                    if (line != null) {
                        line = line.trim();
                        u.setName(line.replaceAll(valueKey, ""));
                    }
                } else if (line.equals(String.format(nameKey, "userpassword"))) {
                    line = reader.readLine();
                    if (line != null) {
                        line = line.trim();
                        u.setHashedPass(line.replaceAll(valueKey, ""));
                    }
                }
            }

        } catch (IOException ex) {
            throw new ServiceInvocationException(ex);
        } finally {
            IOException exception = null;
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException ex) {
                    logger.info(null, ex);
                    exception = ex;
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (final IOException ex) {
                    logger.info(null, ex);
                    exception = ex;
                }
            }
            if (poster != null) {
                try {
                    poster.close();
                } catch (final IOException ex) {
                    logger.warn("IO Exception caught while closing the connection of a POST client", ex);
                    exception = ex;
                }
            }
            if (exception != null) {
                throw new ServiceInvocationException(exception);
            }

        }
        return u;
    }

    /**
     * Two Authentication tokens are equal to each other if and only if, none of
     * them is <code>null</code> and they have the same string representation when
     * non encoded.
     * @param obj
     *      Some other object for which equality to the current authentication token
     *      is under examination.
     * @return
     *          <code>true</code> if this object is the same as the obj
     *          argument; <code>false</code> otherwise.
     * @see #hashCode() 
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AuthenticationToken other = (AuthenticationToken) obj;
        if ((this.token == null) ? (other.token != null) : !this.token.equals(other.token)) {
            return false;
        }
        return true;
    }

    /**
     * Ask the SSO server whether the user with the given token is allowed to
     * perform an HTTP operation on a target URI. If the SSO server allows the
     * action, then  it replies with a status code <code>200</code> and the message
     * <code>boolean=true</code>, otherwise it returns a response with the status
     * code <code>401 (Unauthorized)</code> and the plain text message
     * <code>boolean=false</code>.
     *
     * @param httpMethod
     *      The HTTP for which permission is asked.
     * @param target
     *      The action URI on which the HTTP method will be applied once
     *      permission is granted to the client.
     * @return
     *      <code>true</code> if the user is allowed to perform the operation and
     *      <code>false</code> otherwise.
     * @throws ServiceInvocationException
     *      If a connection problem occurs with the remote or the communication is
     *      corrupted, or if the token the user uses is not active (because it 
     *      has been invalidated, expired, or not initialized yet).
     */
    public boolean authorize(String httpMethod, VRI target) throws ServiceInvocationException {
        if (!this.getStatus().equals(TokenStatus.ACTIVE)) {
            throw new ForbiddenRequest("This token is not active: " + getStatus());
        }
        IPostClient client = null;

        client = ClientFactory.createPostClient(Services.SingleSignOn.ssoAuthorize());
        client.addPostParameter("action", httpMethod);
        client.addPostParameter("uri", target.toString());
        client.addPostParameter("subjectid", stringValue());
        client.post();
        String textResponse = client.getResponseText();
        int httpResponseStatus = -1;
        try {
            httpResponseStatus = client.getResponseCode();
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException ex) {
                    logger.error(null, ex);
                    throw new ServiceInvocationException(ex);
                }
            }
        }
        if (httpResponseStatus == HttpStatusCodes.Success.getStatus() && textResponse.equals("boolean=true")) {
            return true;
        }
        return false;
    }

    /**
     * Ask the SSO server whether the user with the given token is allowed to
     * perform an HTTP operation on a target URI. If the SSO server allows the
     * action, then  it replies with a status code <code>200</code> and the message
     * <code>boolean=true</code>, otherwise it returns a response with the status
     * code <code>401 (Unauthorized)</code> and the plain text message
     * <code>boolean=false</code>.
     *
     * @param httpMethod
     *      The HTTP for which permission is asked.
     * @param target
     *      The action URI on which the HTTP method will be applied once
     *      permission is granted to the client. The target URI in this method is
     *      provided as a simple String.
     * @return
     *      <code>true</code> if the user is allowed to perform the operation and
     *      <code>false</code> otherwise.
     * @throws ServiceInvocationException
     *      If a connection problem occurs with the remote or the communication is
     *      corrupted or the provided target is not a valid URI.
     */
    public boolean authorize(String httpMethod, String target) throws ServiceInvocationException {
        try {
            return authorize(httpMethod, new VRI(target));
        } catch (URISyntaxException ex) {
            throw new ServiceInvocationException(ex);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.token != null ? this.token.hashCode() : 0);
        hash = 23 * hash + (this.encoding != null ? this.encoding.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Token               : ").append(stringValue());
        sb.append("\n");
        sb.append("Token URL-Encoded   : ").append(getTokenUrlEncoded());
        sb.append("\n");
        sb.append("Creation Timestamp  : ").append(getTokenCreationDate());
        sb.append("\n");
        sb.append("Status              : ").append(getStatus());
        return new String(sb);
    }
}
