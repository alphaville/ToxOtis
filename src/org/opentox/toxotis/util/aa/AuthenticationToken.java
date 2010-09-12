package org.opentox.toxotis.util.aa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Date;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.secure.SecurePostClient;
import org.opentox.toxotis.collection.Services;

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
    /** Local lifetime of the token. The token (as an object) will expire after 23 hours */
    private static final long tokenLocalLifeTime = 23 * 3600 * 1000L; // 23hrs
    /** Encoding used to encode tokens received from the SSO server */
    private String encoding = "UTF-8";
    /** SSO server (A&A) */
    private String ssoServer = Services.SSO_IDENTITY;
    /** Service for the validation of tokens: Client needs to know whether a token is valid */
    private String tokenValidationUrl = String.format(Services.SSO_IDENTITY, "isTokenValid");
    /** Authentication Service: The client provides credentials and acquires token */
    private String tokenAuthenticationUrl = String.format(Services.SSO_IDENTITY, "authenticate");
    /** Logout service */
    private String tokenInvalidationUrl = String.format(Services.SSO_IDENTITY, "logout");
    /** LDAP proxy service: The client needs a summary of its attributes (username, email etc) */
    private String attribtuesLDAP = String.format(Services.SSO_IDENTITY, "attributes");
    /** Flag used to tell if the token is logged out */
    private boolean logOut = false;

    /**
     * Initialize a new Authentication Token. The constructor also initializes
     * the SSL connection with the openSSO server.
     * @see Services#SSO_SERVER Default SSO Server
     */
    public AuthenticationToken() {
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
     * These credentials are posted to the {@link AuthenticationToken#getSsoServer() SSO} server
     * which (if they are valid) returns a token. This is used to construct a new
     * Authentication Token object. The timestamp of the method invokation is set as
     * the timestamp for the object construction. All data transactions take place
     * over secure layers (SSL) and using encryption (TLS).
     * </p>
     * @param username
     *      The username of an OpenTox user
     * @param password
     *      The password of an OpenTox user
     * @throws ToxOtisException
     *      In case the credentials are not valid, some exceptional event occurs
     *      during the data transaction or in case the SSO service returns an
     *      error code other than expected (e.g. encounters some internal error
     *      and returns a status code 500).
     */
    public AuthenticationToken(String username, String password) throws ToxOtisException {
        this();
        try {
            SecurePostClient poster = new SecurePostClient(new VRI(tokenAuthenticationUrl));
            poster.addParameter("username", username);
            poster.addParameter("password", password);
            username = null;
            password = null;
            poster.postParameters();
            int status = poster.getResponseCode();
            if (status != 200) {
                throw new ToxOtisException("Invalid Credentials!");
            }
            String response = poster.getResponseText();
            if (response.contains("token.id=")) {
                response = response.replaceAll("token.id=", "");
            }
            this.token = response;
            this.tokenCreationTimestamp = System.currentTimeMillis();
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String getSsoServer() {
        return ssoServer;
    }

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
     * Set the SSO server. Default value is https://opensso.in-silico.ch/opensso/identity/%s.
     * @param ssoServer
     *      The URL of the SSO server.
     */
    public void setSsoServer(String ssoServer) {
        if (!ssoServer.matches(".+/%s")) {
            if (ssoServer.matches(".+/")) {
                ssoServer += "%s";
            } else {
                ssoServer += "/%s";
            }
        }
        this.ssoServer = ssoServer;
    }

    /**
     * Status of the token.
     */
    public enum TokenStatus {

        /**
         * The token is still active and can be used in
         * an A&A session.
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

    public TokenStatus getStatus() {
        if (logOut) {
            return TokenStatus.INACTIVE;
        }
        if (tokenCreationTimestamp == -1) {
            return TokenStatus.DEAD;
        } else if ((System.currentTimeMillis() - tokenCreationTimestamp) < tokenLocalLifeTime) {
            return TokenStatus.ACTIVE;
        } else {
            return TokenStatus.INACTIVE;
        }
    }

    /**
     * Returns the token as a String (not URL encoded)
     * @return
     *      Token as string
     */
    public String getToken() {
        return token;
    }

    /**
     * Returns the token as a URL encoded String.
     * @return
     */
    public String getTokenUrlEncoded() {
        try {
            return URLEncoder.encode(token, encoding);
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("Bad encoding :" + encoding, ex);
        }
    }

    public long getTokenCreationTimestamp() {
        return tokenCreationTimestamp;
    }

    public Date getTokenCreationDate() {
        return new Date(tokenCreationTimestamp);
    }

    /**
     * Ask the remote SSO server whether this token is valid.
     * @return
     *      <code>true</code> if a positive response was returned from the remote
     *      server and <code>false</code> otherwise.
     * @throws ToxOtisException
     *      In case an error status is received from the remote service or there
     *      is some communication problem.
     */
    public boolean validate() throws ToxOtisException {
        if (token == null || (token != null && token.isEmpty())) {
            return false;
        }
        try {
            SecurePostClient poster = new SecurePostClient(new VRI(tokenValidationUrl));
            poster.addParameter("tokenid", getToken());
            poster.postParameters();
            final int status = poster.getResponseCode();
            final String message = (poster.getResponseText()).trim();
            if (status != 200 && status != 401) {
                throw new ToxOtisException("Status code " + status + " received from " + tokenValidationUrl);
            } else if (status == 401) {
                if (!message.equals("boolean=false")) {
                    return false;
                } else {
                    throw new ToxOtisException("Status code " + status + " received from " + tokenValidationUrl);
                }
            }

            if (message.equals("boolean=true")) {
                return true;
            } else {
                return false;
            }
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Sends an invalidation request to the remote server. Once the token is
     * invalidated it can no longer be used in any authentication/authorization
     * sessions.
     * @throws ToxOtisException
     *      In case an error status is received from the remote service or there
     *      is some communication problem.
     */
    public void invalidate() throws ToxOtisException {
        this.logOut = true;
        if (token == null || (token != null && token.isEmpty())) {
            return; // Nothing to invalidate!
        }
        try {
            SecurePostClient poster = new SecurePostClient(new VRI(tokenInvalidationUrl));
            poster.addParameter("subjectid", getToken());
            poster.postParameters();
            int status = poster.getResponseCode();
            if (status != 200) {
                throw new ToxOtisException("Status code " + status + " received from " + tokenInvalidationUrl);
            }
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Information about the user that holds the token (for which the token was
     * created).
     * @return
     *      User information as an instance of {@link User }.
     * @throws ToxOtisException
     *
     */
    public User getUser() throws ToxOtisException {
        try {
            User u = new User();
            SecurePostClient poster = new SecurePostClient(new VRI(attribtuesLDAP));
            poster.addParameter("subjectid", getToken());
            poster.postParameters();

            InputStream is = null;
            BufferedReader reader = null;
            try {
                final String valueKey = "userdetails.attribute.value=";
                is = poster.getRemoteStream();
                reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.equals("userdetails.attribute.name=uid")) {
                        line = reader.readLine();
                        if (line != null) {
                            line = line.trim();
                            u.setUid(line.replaceAll(valueKey, ""));
                        }
                    } else if (line.equals("userdetails.attribute.name=mail")) {
                        line = reader.readLine();
                        if (line != null) {
                            line = line.trim();
                            u.setMail(line.replaceAll(valueKey, ""));
                        }
                    } else if (line.equals("userdetails.attribute.name=sn")) {
                        line = reader.readLine();
                        if (line != null) {
                            line = line.trim();
                            u.setName(line.replaceAll(valueKey, ""));
                        }
                    } else if (line.equals("userdetails.attribute.name=userpassword")) {
                        line = reader.readLine();
                        if (line != null) {
                            line = line.trim();
                            u.setHashedPass(line.replaceAll(valueKey, ""));
                        }
                    }
                }
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

            return u;
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

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
        if ((this.encoding == null) ? (other.encoding != null) : !this.encoding.equals(other.encoding)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.token != null ? this.token.hashCode() : 0);
        hash = 23 * hash + (this.encoding != null ? this.encoding.hashCode() : 0);
        return hash;
    }
}
