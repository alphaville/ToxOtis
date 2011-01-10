package org.opentox.toxotis.util.aa.policy;

import org.opentox.toxotis.util.aa.SSLConfiguration;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.IGetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.client.https.DeleteHttpsClient;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.aa.InactiveTokenException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Utilities for policy creation, deletion and parsing.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class PolicyManager {

    private static final String SUBJECT_ID = "subjectid";

    private PolicyManager() {
        SSLConfiguration.initializeSSLConnection();
    }

    public static void deleteRemotePolicy(VRI policyServiceUri, String policyName, AuthenticationToken token) throws ToxOtisException {
        if (!token.getStatus().equals(AuthenticationToken.TokenStatus.ACTIVE)) {
            throw new InactiveTokenException("This token is not active: " + token.getStatus());
        }
        //TODO: We need a secure DELETE client here!
        if (policyServiceUri == null) {
            policyServiceUri = Services.SingleSignOn.ssoPolicyOld();
        }
        DeleteHttpsClient sdc = null;
        try {
            sdc = new DeleteHttpsClient(policyServiceUri);
            sdc.addHeaderParameter("id", policyName);
            sdc.addHeaderParameter(SUBJECT_ID, token.stringValue());
            sdc.authorize(token);
            sdc.doDelete();
        } finally {
            if (sdc != null) {
                try {
                    sdc.close();
                } catch (IOException ex) {
                    throw new ToxOtisException(ErrorCause.StreamCouldNotClose, ex);
                }
            }
        }
    }

    /**
     * Delete all
     * @param policyService
     * @param token
     * @throws ToxOtisException
     */
    public static void deleteAllMyPolicies(VRI policyService, AuthenticationToken token) throws ToxOtisException {
        ArrayList<String> policies = listPolicyUris(policyService, token);
        for (String policyName : policies) {
            deleteRemotePolicy(policyService, policyName, token);
            System.out.println(policyName);
        }
    }

    /**
     * Obtain the username of the owner of a policy for a given URI.
     * @param serviceUri
     *      URI of an (OpenTox) web service for which the username of its creator
     *      is requested
     * @param policyService
     *      URI of the SSO service used to carry out our request. In case this
     *      argument is set to <code>null</code>, the default SSO service URI
     *      <code>https://opensso.in-silico.ch/pol</code> will be used instead.
     * @param token
     *      Authentication token used to grant you access to the policy management
     *      service.
     * @return
     *      The owner/creator of the policy for the provided service URI or <code>
     *      null</code> if this URI is not registered to the policy service and
     *      does not have a policy.
     * @throws ToxOtisException
     *      In case authentication/authorization against the SSO service fails
     *      due to invalid token or insufficient priviledges or the service has
     *      encoutnered some unexpected internal condition (status code <code>500</code>).
     *      The OpenTox REST API specifies that the range of possible status codes
     *      includes <code>200</code> (OK, Successs), <code>401</code> (Unauthorized)
     *      and <code>500</code> (other unexpected conditions).
     * @throws InactiveTokenException
     *      If the token the user uses is not active (because it has been invalidated,
     *      expired, or not initialized yet).
     */
    public static String getPolicyOwner(VRI serviceUri, VRI policyService, AuthenticationToken token) throws ToxOtisException {
        if (!token.getStatus().equals(AuthenticationToken.TokenStatus.ACTIVE)) {
            throw new InactiveTokenException("This token is not active: " + token.getStatus());
        }
        IGetClient sgt = null;
        if (policyService == null) {
            policyService = Services.SingleSignOn.ssoPolicy();
        }
        try {
            // REQUEST
            sgt = ClientFactory.createGetClient(policyService);
            sgt.addHeaderParameter(SUBJECT_ID, token.stringValue());
            sgt.addHeaderParameter("uri", serviceUri.clearToken().toString());

            // RETURN RESPONSE
            int responseStatus = sgt.getResponseCode();
            if (responseStatus == 200) {// Successful!
                String response = sgt.getResponseText();
                if (response.trim().equals("null")) {
                    return null;
                }
                return response + "@" + policyService.getServiceBaseUri().getHost();
            } else if (responseStatus == 403) {
                throw new ToxOtisException(ErrorCause.AuthenticationFailed, "User is not authenticated!");
            } else if (responseStatus == 401) {
                throw new ToxOtisException(ErrorCause.UnauthorizedUser, "User is not authorized to perform the request!");
            } else {
                throw new ToxOtisException(ErrorCause.UnknownCauseOfException, "Service returned status code : " + responseStatus);
            }
        } catch (IOException ex) {
            throw new ToxOtisException(ex);
        } finally {
            if (sgt != null) {
                try {
                    sgt.close();
                } catch (IOException ex) {
                    throw new ToxOtisException(ErrorCause.StreamCouldNotClose, ex);
                }
            }
        }
    }

    /**
     * List all policies that are hosted in the provided policy service.
     *
     * @param policyService
     *       Policy service in which this method will search for registered policies.
     *      If set to <code>null</code> then the standard policy service at
     *      https://opensso.in-silico.ch/pol will be automatically chosen.
     * @param token
     *      Token URI used to authenticate the client against the opensso service
     *      and acquire permissions to get the list of policies!
     * @return
     *      A list of policy IDs that are hosted in the SSO service by the user
     *      that is identified by the provided authenticaton token. Note that the IDs of
     *      policies are not URIs.
     *
     * @throws ToxOtisException
     *      In case authentication/authorization fails, so the client does not have
     *      acceess privileges to the remote service, or the provided URI of the policyService
     *      is not found or the service responds with an error status code or exhibits
     *      some unexpected behavior. According to the OpenTox API, possible status codes
     *      include 200, 401/403 and 500.
     * @throws InactiveTokenException
     *      If the token the user uses is not active (because it has been invalidated,
     *      expired, or not initialized yet).
     */
    public static ArrayList<String> listPolicyUris(VRI policyService, AuthenticationToken token) throws ToxOtisException {
        if (!token.getStatus().equals(AuthenticationToken.TokenStatus.ACTIVE)) {
            throw new InactiveTokenException("This token is not active: " + token.getStatus());
        }
        IGetClient sgt = null;
        ArrayList<String> listOfPolicyNames = new ArrayList<String>();
        if (policyService == null) {
            policyService = Services.SingleSignOn.ssoPolicy();
        }
        try {
            sgt = ClientFactory.createGetClient(policyService);
            sgt.addHeaderParameter(SUBJECT_ID, token.stringValue());
            int responseStatus = sgt.getResponseCode();

            InputStream remote = null;
            InputStreamReader isr = null;
            BufferedReader reader = null;
            String line = null;
            if (responseStatus == 200) {
                // OK => List policies!
                remote = sgt.getRemoteStream();
                isr = new InputStreamReader(remote);
                reader = new BufferedReader(isr);
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        listOfPolicyNames.add(line);
                    }
                }
            } else if (responseStatus == 403) {
                throw new ToxOtisException(ErrorCause.AuthenticationFailed, "User is not authenticated!");
            } else {
                throw new ToxOtisException(ErrorCause.UnknownCauseOfException, "Service returned status code : " + responseStatus);
            }
        } catch (IOException ex) {
            org.slf4j.LoggerFactory.getLogger(Policy.class).error("IO Exception was thrown while communicating with the policy service at " + policyService, ex);
            throw new ToxOtisException(ex);
        } finally {
            if (sgt != null) {
                try {
                    sgt.close();
                } catch (IOException ex) {
                    throw new ToxOtisException(ErrorCause.StreamCouldNotClose, ex);
                }
            }
        }
        return listOfPolicyNames;
    }

    // TODO: Return PolicyWrapper instead of Policy
    public static IPolicyWrapper parsePolicy(String id, VRI policyService, AuthenticationToken token) throws ToxOtisException {
        if (!token.getStatus().equals(AuthenticationToken.TokenStatus.ACTIVE)) {
            throw new InactiveTokenException("This token is not active: " + token.getStatus());
        }
        IGetClient sgt = null;
        if (policyService == null) {
            policyService = Services.SingleSignOn.ssoPolicyOld();
        }
        try {
            // REQUEST
            sgt = ClientFactory.createGetClient(policyService);
            sgt.addHeaderParameter(SUBJECT_ID, token.stringValue());
            sgt.addHeaderParameter("id", id);

            // PROCESS RESPONSE
            int responseStatus = 0;
            try {
                responseStatus = sgt.getResponseCode();
            } catch (IOException ex) {
                org.slf4j.LoggerFactory.getLogger(Policy.class).error(null, ex);
                throw new ToxOtisException(ex);
            }
            if (responseStatus == 200) {
//                System.out.println(sgt.getResponseText());
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                try {
                    Document d = factory.newDocumentBuilder().parse(sgt.getRemoteStream());
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(Policy.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SAXException ex) {
                    Logger.getLogger(Policy.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Policy.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else if (responseStatus == 403) {
                throw new ToxOtisException(ErrorCause.AuthenticationFailed, "User is not authenticated!");
            } else {
                String responseText = sgt.getResponseText();
                throw new ToxOtisException(ErrorCause.UnknownCauseOfException, "Service returned status code : " + responseStatus + " and message:\n" + responseText);
            }
        } finally {
            if (sgt != null) {
                try {
                    sgt.close();
                } catch (IOException ex) {
                    throw new ToxOtisException(ErrorCause.StreamCouldNotClose, ex);
                }
            }
        }
        return null;
    }

    public static IPolicyWrapper defaultSignleUserPolicy(String policyName, VRI componentUri, AuthenticationToken token) throws ToxOtisException {

        Policy pol = new Policy();
        pol.setPolicyName(policyName);
        pol.addSubject(new SingleSubject(token.getUser().getName()));
        PolicyRule pr = new PolicyRule("rule_allow_only_creator");
        pr.setTargetUri(componentUri.toString());
        pr.setAllowGet(true);
        pr.setAllowPost(true);
        pr.setAllowPut(true);
        pr.setAllowDelete(true);

        return new PolicyWrapper(pol);

    }
}

