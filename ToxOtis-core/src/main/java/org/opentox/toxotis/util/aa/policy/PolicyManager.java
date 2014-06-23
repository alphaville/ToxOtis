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
package org.opentox.toxotis.util.aa.policy;

import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.util.aa.SSLConfiguration;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.HttpStatusCodes;
import org.opentox.toxotis.client.IGetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.client.https.DeleteHttpsClient;
import org.opentox.toxotis.exceptions.impl.ConnectionException;
import org.opentox.toxotis.exceptions.impl.ForbiddenRequest;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.Unauthorized;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Utilities for policy creation, deletion and parsing.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public final class PolicyManager {

    private static final String SUBJECT_ID = "subjectid";

    private PolicyManager() {
        SSLConfiguration.initializeSSLConnection();
    }

    public static void deleteRemotePolicy(VRI policyServiceUri, String policyName, AuthenticationToken token) throws ServiceInvocationException {
        if (!token.getStatus().equals(AuthenticationToken.TokenStatus.ACTIVE)) {
            throw new ForbiddenRequest("This token is not active: " + token.getStatus());
        }
        //TODO: We need a secure DELETE client here!
        if (policyServiceUri == null) {
            policyServiceUri = Services.SingleSignOn.ssoPolicy();
        }
        DeleteHttpsClient sdc = null;
        try {
            sdc = new DeleteHttpsClient(policyServiceUri);
            sdc.addHeaderParameter("id", policyName);
            sdc.addHeaderParameter(SUBJECT_ID, token.stringValue());
            sdc.authorize(token);// Redundant (For potent future use)
            sdc.doDelete();
        } finally {
            if (sdc != null) {
                try {
                    sdc.close();
                } catch (IOException ex) {
                    throw new ConnectionException("Stream could not close", ex);
                }
            }
        }
    }

    /**
     * Delete all
     * @param policyService
     * @param token
     * @throws ServiceInvocationException
     */
    public static void deleteAllMyPolicies(VRI policyService, AuthenticationToken token) throws ServiceInvocationException {
        List<String> policies = listPolicyUris(policyService, token);
        for (String policyName : policies) {
            deleteRemotePolicy(policyService, policyName, token);
        }
    }

    /**
     * Obtain the username of the owner of a policy for a given URI.
     * @param resourceUri
     *      URI of an (OpenTox) resource for which the username of its creator
     *      is requested.
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
     * @throws ServiceInvocationException
     *      In case authentication/authorization against the SSO service fails
     *      due to invalid token or insufficient privileges or the service has
     *      encountered some unexpected internal condition (status code <code>500</code>).
     *      The OpenTox REST API specifies that the range of possible status codes
     *      includes <code>200</code> (OK, Success), <code>401</code> (Unauthorized)
     *      and <code>500</code> (other unexpected conditions).
     *      If the token the user uses is not active (because it has been invalidated,
     *      expired, or not initialized yet).
     */
    public static String getPolicyOwner(VRI resourceUri, VRI policyService, AuthenticationToken token) throws ServiceInvocationException {
        if (token==null){
            throw new NullPointerException("You should provide a token!");
        }
        if (!token.getStatus().equals(AuthenticationToken.TokenStatus.ACTIVE)) {
            throw new ForbiddenRequest("This token is not active: " + token.getStatus());
        }
        IGetClient sgt = null;
        if (policyService == null) {
            policyService = Services.SingleSignOn.ssoPolicy();
        }
        try {
            // REQUEST
            sgt = ClientFactory.createGetClient(policyService);
            sgt.addHeaderParameter(SUBJECT_ID, token.stringValue());
            sgt.addHeaderParameter("uri", resourceUri.toString());

            // RETURN RESPONSE
            int responseStatus = sgt.getResponseCode();
            if (responseStatus == HttpStatusCodes.Success.getStatus()) {// Successful!
                String response = sgt.getResponseText();
                if (response.trim().equals("null")) {
                    return null;
                }
                return response + "@" + policyService.getServiceBaseUri().getHost();
            } else if (responseStatus == HttpStatusCodes.Forbidden.getStatus()) {
                throw new ForbiddenRequest("User is not authenticated!");
            } else if (responseStatus == HttpStatusCodes.Unauthorized.getStatus()) {
                throw new Unauthorized("User is not authorized to perform the request!");
            } else {
                throw new ServiceInvocationException("Service returned status code : " + responseStatus);
            }
        } finally {
            if (sgt != null) {
                try {
                    sgt.close();
                } catch (IOException ex) {
                    throw new ConnectionException("Stream Could Not Close", ex);
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
     *      Token URI used to authenticate the client against the openSSO service
     *      and acquire permissions to get the list of policies!
     * @return
     *      A list of policy IDs that are hosted in the SSO service by the user
     *      that is identified by the provided authentication token. Note that the IDs of
     *      policies are not URIs.
     *
     * @throws ServiceInvocationException
     *      In case authentication/authorization fails, so the client does not have
     *      access privileges to the remote service, or the provided URI of the policyService
     *      is not found or the service responds with an error status code or exhibits
     *      some unexpected behavior. According to the OpenTox API, possible status codes
     *      include 200, 401/403 and 500.
     *      If the token the user uses is not active (because it has been invalidated,
     *      expired, or not initialized yet).
     */
    public static List<String> listPolicyUris(VRI policyService, AuthenticationToken token) throws ServiceInvocationException {
        if (!token.getStatus().equals(AuthenticationToken.TokenStatus.ACTIVE)) {
            throw new ForbiddenRequest("This token is not active: " + token.getStatus());
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
            if (responseStatus == HttpStatusCodes.Success.getStatus()) {
                // OK => List policies!
                remote = sgt.getRemoteStream();
                isr = new InputStreamReader(remote);
                reader = new BufferedReader(isr);
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        listOfPolicyNames.add(line);
                    }
                }
            } else if (responseStatus == HttpStatusCodes.Forbidden.getStatus()) {
                throw new ForbiddenRequest("Authentication Failed - User is not authenticated!");
            } else {
                throw new ServiceInvocationException("Service returned status code : " + responseStatus);
            }
        } catch (IOException ex) {
            String message = "IO Exception was thrown while communicating with the policy service at " + policyService;
            org.slf4j.LoggerFactory.getLogger(Policy.class).error(message, ex);
            throw new ConnectionException(message, ex);
        } finally {
            if (sgt != null) {
                try {
                    sgt.close();
                } catch (IOException ex) {
                    throw new ConnectionException("Stream could not close", ex);
                }
            }
        }
        return listOfPolicyNames;
    }

    // TODO: Return PolicyWrapper instead of Policy
    public static IPolicyWrapper parsePolicy(String id, VRI policyService, AuthenticationToken token) throws ServiceInvocationException {
        if (!token.getStatus().equals(AuthenticationToken.TokenStatus.ACTIVE)) {
            throw new ForbiddenRequest("This token is not active: " + token.getStatus());
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
            responseStatus = sgt.getResponseCode();

            if (responseStatus == HttpStatusCodes.Success.getStatus()) {
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

            } else if (responseStatus == HttpStatusCodes.Forbidden.getStatus()) {
                throw new ForbiddenRequest("Authentication Failed - User is not authenticated!");
            } else {
                String responseText = sgt.getResponseText();
                throw new ServiceInvocationException("Service returned status code : " + responseStatus + " and message:\n" + responseText);
            }
        } finally {
            if (sgt != null) {
                try {
                    sgt.close();
                } catch (IOException ex) {
                    throw new ConnectionException("Stream could not close", ex);
                }
            }
        }
        return null;
    }

    /**
     * Creates the default policy for a user. If the user if <code>guest</code>
     * then the created policy allows everyone to access the resource (it is 
     * made public). In any other case, only the creator has access to the resource.
     * By default, the policy allows users to GET, POST, PUT and DELETE.
     * @param policyName
     *      Name of the policy to be created (has to be unique).
     * @param componentUri
     *      The URI of the resource for which the policy is created.
     * @param userName
     *      The name of the user to which the privileges are assigned.
     * @return 
     *      A Policy which can be published to the policy server.
     */
    public static IPolicyWrapper defaultSignleUserPolicy(String policyName, VRI componentUri, String userName) {

        Policy pol = new Policy();
        pol.setPolicyName(policyName);

        PolicyRule pr = null;
        if ("guest".equals(userName)) {
            pol.addSubject(GroupSubject.MEMBER);
            pr = new PolicyRule("public_access_rule");
        } else {
            pol.addSubject(new SingleSubject(userName));
            pr = new PolicyRule("rule_allow_only_creator");
        }
        pr.setTargetUri(componentUri.toString());
        pr.setAllowGet(true);
        pr.setAllowPost(true);
        pr.setAllowPut(true);
        pr.setAllowDelete(true);
        pol.addRule(pr);

        return new PolicyWrapper(pol);
    }

    public static IPolicyWrapper defaultSignleUserPolicy(String policyName, VRI componentUri, AuthenticationToken token)
            throws ToxOtisException, ServiceInvocationException {
        return defaultSignleUserPolicy(policyName, componentUri, token.getUser().getUid().split("@")[0]);
    }
}

