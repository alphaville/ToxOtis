package org.opentox.toxotis.util.aa.policy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.IGetClient;
import org.opentox.toxotis.client.IPostClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.client.https.DeleteHttpsClient;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.aa.InactiveTokenException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * An authentication/authorization policy defines explicitly the way in which clients
 * are granted permissions to access certain resource and perform HTTP requests. Policies
 * may grant access to certain individuals or subject groups.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Policy {

    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Policy.class);

    public Policy() {
    }

    public Policy(String policyName) {
        this.policyName = policyName;
    }
    private static final String _DocTypePublic =
            "-//Sun Java System Access Manager7.1 2006Q3 Admin CLI DTD//EN";
    private static final String _DocTypeSystem =
            "jar://com/sun/identity/policy/policyAdmin.dtd";
    private String policyName;
    private Set<PolicyRule> rules = new HashSet<PolicyRule>();
    private Set<PolicySubject> subjects = new HashSet<PolicySubject>();
    private String subjectsCollectionName = "mySubjects";
    private static final String SUBJECT_ID = "subjectid";
    private String subjectsDescription = "";
    private Document policyDocument = null;

    private void createDocument() {
        if (policyDocument != null) {
            return;
        }
        Document doc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            doc = builder.newDocument();

            Element policies = (Element) doc.createElement("Policies");
            doc.appendChild(policies);
            Element policy = (Element) doc.createElement("Policy");
            policy.setAttribute("name", policyName);
            policy.setAttribute("referralPolicy", "false");
            policy.setAttribute("active", "true");
            policies.appendChild(policy);

            Element ruleElement = null;
            for (PolicyRule rule : rules) {
                ruleElement = doc.createElement("Rule");
                ruleElement.setAttribute("name", rule.getName());

                Element serviceName = doc.createElement("ServiceName");
                serviceName.setAttribute("name", rule.getServiceName());
                ruleElement.appendChild(serviceName);
                Element resourceName = doc.createElement("ResourceName");
                resourceName.setAttribute("name", rule.getTargetUri());
                ruleElement.appendChild(resourceName);

                Element getAVP = actionElement(doc, "GET", rule.isAllowGet());
                ruleElement.appendChild(getAVP);
                Element postAVP = actionElement(doc, "POST", rule.isAllowPost());
                ruleElement.appendChild(postAVP);
                Element putAVP = actionElement(doc, "PUT", rule.isAllowPut());
                ruleElement.appendChild(putAVP);
                Element deleteAVP = actionElement(doc, "DELETE", rule.isAllowDelete());
                ruleElement.appendChild(deleteAVP);


                policy.appendChild(ruleElement);
            }

            Element subjectsAll = doc.createElement("Subjects");
            subjectsAll.setAttribute("name", subjectsCollectionName);
            subjectsAll.setAttribute("description", subjectsDescription);
            Element subjectElement = null;
            for (PolicySubject subject : subjects) {
                subjectElement = doc.createElement("Subject");
                subjectElement.setAttribute("name", subject.getSubjectName());
                subjectElement.setAttribute("type", subject.LDAP_Type);
                subjectElement.setAttribute("includeType", "inclusive");

                Element subjAVP = doc.createElement("AttributeValuePair");
                Element subjA = doc.createElement("Attribute");
                subjA.setAttribute("name", "Values");
                Element subjV = doc.createElement("Value");
                subjV.setTextContent(subject.getValue());
                subjAVP.appendChild(subjA);
                subjAVP.appendChild(subjV);

                subjectElement.appendChild(subjAVP);
                subjectsAll.appendChild(subjectElement);
            }
            policy.appendChild(subjectsAll);
        } catch (ParserConfigurationException ex) {
            String message = "Utterly unexpected condition while creating an XML document for an SSO Policy."
                    + "Definitely a BUG! Please report is ASAP at https://github.com/alphaville/ToxOtis/issues.";
            logger.error(message, ex);
            throw new RuntimeException(message, ex);
        }
        policyDocument = doc;
    }

    private Element actionElement(Document document, String actionName, boolean isAllowed) {
        Element actionAVP = document.createElement("AttributeValuePair");
        Element actionA = document.createElement("Attribute");
        actionA.setAttribute("name", actionName);
        Element actionV = document.createElement("Value");
        actionV.setTextContent(isAllowed ? "allow" : "deny");
        actionAVP.appendChild(actionA);
        actionAVP.appendChild(actionV);
        return actionAVP;
    }

    public String getText() {
        try {
            createDocument();
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            trans.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, _DocTypePublic);
            trans.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, _DocTypeSystem);
            //create string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(policyDocument);
            trans.transform(source, result);
            return sw.toString();
        } catch (TransformerException ex) {
            throw new RuntimeException(ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Document getDocument() {
        return policyDocument;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public void addRule(PolicyRule rule) {
        rules.add(rule);
    }

    public void addSubject(PolicySubject subject) {
        subjects.add(subject);
    }

    public String getSubjectsCollectionName() {
        return subjectsCollectionName;
    }

    public void setSubjectsCollectionName(String subjectsCollectionName) {
        this.subjectsCollectionName = subjectsCollectionName;
    }

    public String getSubjectsDescription() {
        return subjectsDescription;
    }

    public void setSubjectsDescription(String subjectsDescription) {
        this.subjectsDescription = subjectsDescription;
    }// </editor-fold>

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
     * Publish this policy to a remote server and acquire a URI for it.
     * 
     * @param policyServer
     *      URI of the policy server. If set to <code>null</code> then the standard
     *      policy service of OpenTox at https://opensso.in-silico.ch/pol will be
     *      used instead.
     * @param token
     *      Token used to authenticate the user that attempts to publish a new policy
     *      against the policy service. If you think that no authentication is needed
     *      to perform the HTTP request you may set it to <code>null</code>.
     * @return
     *      Server's response message
     * 
     * @throws ToxOtisException
     *      In case a HTTP related error occurs (I/O communication error, or the
     *      remote server is down), the service respondes in an unexpected manner
     *      like a status code 500 or 503 or authentication/authorization fails and
     *      a status code 403 or 401 are returned respectively.
     * @throws InactiveTokenException
     *      If the token the user uses is not active (because it has been invalidated,
     *      expired, or not initialized yet).
     */
    public int publishPolicy(VRI policyServer, AuthenticationToken token) throws ToxOtisException {
        if (!token.getStatus().equals(AuthenticationToken.TokenStatus.ACTIVE)) {
            throw new InactiveTokenException("This token is not active: " + token.getStatus());
        }
        if (policyServer == null) {
            policyServer = Services.SingleSignOn.ssoPolicyOld();
        }
        IPostClient spc = ClientFactory.createPostClient(policyServer);
        spc.setContentType(Media.APPLICATION_XML);
        spc.addHeaderParameter(SUBJECT_ID, token.stringValue());
        spc.setPostable(this.getText().trim(), true);
        spc.post();
        try {
            int httpStatus = spc.getResponseCode();
            if (httpStatus != 200) {
                String policyErrorMsg = "Policy server at " + policyServer
                        + " responded with a status code " + httpStatus + " with message \n" + spc.getResponseText();
                logger.debug(policyErrorMsg);
                throw new ToxOtisException(ErrorCause.PolicyCreationError, policyErrorMsg);
            }
            return spc.getResponseCode();
        } catch (IOException ex) {
            throw new ToxOtisException("Communication error with the SSO policy server at " + policyServer, ex);
        } finally {
            if (spc != null) {
                try {
                    spc.close();
                } catch (IOException ex) {
                    logger.error("IO Exception while closing an HTTPS client", ex);
                    throw new ToxOtisException(ex);
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

    public static void deleteAllMyPolicies(VRI policyService, AuthenticationToken token) throws ToxOtisException {
        ArrayList<String> policies = listPolicyUris(policyService, token);
        for (String policyName : policies){
            deleteRemotePolicy(policyService, policyName, token);
        }
    }

    public static Policy parsePolicy(String id, VRI policyService, AuthenticationToken token) throws ToxOtisException {
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
            sgt.addHeaderParameter(SUBJECT_ID, token.getTokenUrlEncoded());
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
                //TODO: PARSE XML!
                System.out.println(sgt.getResponseText());
            } else if (responseStatus == 403) {
                throw new ToxOtisException(ErrorCause.AuthenticationFailed, "User is not authenticated!");
            } else {
                throw new ToxOtisException(ErrorCause.UnknownCauseOfException, "Service returned status code : " + responseStatus);
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
}
