package org.opentox.toxotis.util.aa;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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

    public AuthenticationToken() {
        SSLConfiguration.initializeSSLConnection();
    }

    public AuthenticationToken(String token) {
        this();
        this.token = token;
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
         * The token is not yet initialized.
         */
        DEAD;
    }

    public TokenStatus getStatus() {
        if (tokenCreationTimestamp == -1) {
            return TokenStatus.DEAD;
        } else if ((System.currentTimeMillis() - tokenCreationTimestamp) < tokenLocalLifeTime) {
            return TokenStatus.ACTIVE;
        } else {
            return TokenStatus.INACTIVE;
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.tokenCreationTimestamp = System.currentTimeMillis(); // Set token creation timestamp
        this.token = token;
    }

    public String getTokenUrlEncoded() {
        try {
            return URLEncoder.encode(token, encoding);
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("Bad encoding :" + encoding);
        }
    }
}
