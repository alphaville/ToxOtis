package org.opentox.toxotis.util.aa.policy;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface IPolicy extends IPolicyWrapper {

    void addRule(IPolicyRule rule);

    void addSubject(PolicySubject subject);

    String getPolicyName();

    String getSubjectsCollectionName();

    String getSubjectsDescription();

    void setPolicyName(String policyName);

    void setSubjectsCollectionName(String subjectsCollectionName);

    void setSubjectsDescription(String subjectsDescription);

    Element xmlElement(Document doc, Element policies);
}
