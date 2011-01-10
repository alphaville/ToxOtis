package org.opentox.toxotis.util.aa.policy;

import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.w3c.dom.Document;

/**
 * A Policy Wrapper is a bundle of policies that can be sent/posted as one object
 * to the openSSO server.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface IPolicyWrapper {

    Document getDocument();

    String getText();
    

    /**
     * Publish this policy to a remote server and acquire a URI for it.
     *
     * @param policyServer
     * URI of the policy server. If set to <code>null</code> then the standard
     * policy service of OpenTox at https://opensso.in-silico.ch/pol will be
     * used instead.
     * @param token
     * Token used to authenticate the user that attempts to publish a new policy
     * against the policy service. If you think that no authentication is needed
     * to perform the HTTP request you may set it to <code>null</code>.
     * @return
     * Server's response message
     *
     * @throws ToxOtisException
     * In case a HTTP related error occurs (I/O communication error, or the
     * remote server is down), the service respondes in an unexpected manner
     * like a status code 500 or 503 or authentication/authorization fails and
     * a status code 403 or 401 are returned respectively.
     * @throws InactiveTokenException
     * If the token the user uses is not active (because it has been invalidated,
     * expired, or not initialized yet).
     */
    int publish(VRI policyServer, AuthenticationToken token) throws ToxOtisException;

}
