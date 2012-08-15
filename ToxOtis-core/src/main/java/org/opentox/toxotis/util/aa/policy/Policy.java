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

import java.util.HashSet;
import java.util.Set;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.util.aa.AuthenticationToken;
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

    Element xmlElement(Document doc, Element policies) {
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

    @Override
    public String getText() {
        PolicyWrapper pw = new PolicyWrapper(this);
        return pw.getText();
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    @Override
    public Document getDocument() {
        PolicyWrapper pw = new PolicyWrapper(this);
        return pw.getDocument();
    }

    @Override
    public String getPolicyName() {
        return policyName;
    }

    @Override
    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    @Override
    public void addRule(IPolicyRule rule) {
        rules.add(rule);
    }

    @Override
    public void addSubject(PolicySubject subject) {
        subjects.add(subject);
    }

    @Override
    public String getSubjectsCollectionName() {
        return subjectsCollectionName;
    }

    @Override
    public void setSubjectsCollectionName(String subjectsCollectionName) {
        this.subjectsCollectionName = subjectsCollectionName;
    }

    @Override
    public String getSubjectsDescription() {
        return subjectsDescription;
    }

    @Override
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
    @Override
    public int publish(VRI policyServer, AuthenticationToken token) throws ServiceInvocationException  {
        PolicyWrapper pw = new PolicyWrapper(this);
        return pw.publish(policyServer, token);
    }
}
