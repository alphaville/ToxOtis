package org.opentox.toxotis.util.aa.policy;

import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Policy {

    private static final String _DocTypePublic =
            "-//Sun Java System Access Manager7.1 2006Q3 Admin CLI DTD//EN";
    private static final String _DocTypeSystem =
            "jar://com/sun/identity/policy/policyAdmin.dtd";
    private String policyName;
    private Set<PolicyRule> rules = new HashSet<PolicyRule>();
    private Set<PolicySubject> subjects = new HashSet<PolicySubject>();
    private String subjectsCollectionName = "mySubjects";
    private String subjectsDescription = "";
    private static Document policyDocument = null;

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
            Logger.getLogger(Policy.class.getName()).log(Level.SEVERE, null, ex);
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
    }
}
