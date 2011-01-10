package org.opentox.toxotis.util.aa.policy;

import java.util.HashSet;
import java.util.Set;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
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
public class Policy implements IPolicy {

    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Policy.class);

    public Policy() {
    }

    public Policy(String policyName) {
        this.policyName = policyName;
    }
    private String policyName;
    private Set<IPolicyRule> rules = new HashSet<IPolicyRule>();
    private Set<PolicySubject> subjects = new HashSet<PolicySubject>();
    private String subjectsCollectionName = "mySubjects";
    private String subjectsDescription = "";
    private Document policyDocument = null;

    public Element xmlElement(Document doc, Element policies) {
        Element policy = (Element) doc.createElement("Policy");
        policy.setAttribute("name", getPolicyName());
        policy.setAttribute("referralPolicy", "false");
        policy.setAttribute("active", "true");
        policies.appendChild(policy);

        Element ruleElement = null;
        for (IPolicyRule rule : rules) {
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
        return policy;
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
        PolicyWrapper pw = new PolicyWrapper();
        return pw.getText();
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

    public void addRule(IPolicyRule rule) {
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
    public int publish(VRI policyServer, AuthenticationToken token) throws ToxOtisException {
        PolicyWrapper pw = new PolicyWrapper(this);
        return pw.publish(policyServer, token);
    }
}
