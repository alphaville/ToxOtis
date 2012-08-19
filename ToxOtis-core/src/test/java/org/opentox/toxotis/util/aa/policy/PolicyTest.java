package org.opentox.toxotis.util.aa.policy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class PolicyTest {

    public PolicyTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void createImportantPolicies() throws Exception {
        String guestSecretFile = System.getProperty("user.home") + "/toxotisKeys/.guest.key";
        String sopasakisSecretFile = System.getProperty("user.home") + "/toxotisKeys/.my.key";
        String hamposSecretFile = System.getProperty("user.home") + "/toxotisKeys/.my.key";
        AuthenticationToken at = new AuthenticationToken(new File(guestSecretFile));
        AuthenticationToken at_sopasakis = new AuthenticationToken(new File(sopasakisSecretFile));
        AuthenticationToken at_hampos = new AuthenticationToken(new File(hamposSecretFile));

        VRI modelService = Services.ntua().augment("model");
        String[] model1Ds = new String[]{
            "059d8bf0-cccc-4ad1-ad40-d15a663894fc",
            "192f9d28-cb99-4837-aad6-ba4f348adee9"};
        for (String mid : model1Ds) {
            VRI currentmodelUri = new VRI(modelService).augment(mid);
            String model_owner = PolicyManager.getPolicyOwner(currentmodelUri, null, at);
            System.out.println("Owner of model '" + mid + "' is '" + model_owner + "'");
            if (model_owner == null) {
                System.out.println("[Policy Maker] Publishing Policy!");
                IPolicyWrapper ipw = PolicyManager.defaultSignleUserPolicy("M" + mid, currentmodelUri, at);
                ipw.publish(null, at);
            }
            boolean allowSopasakis = at_sopasakis.authorize("GET", currentmodelUri),
                    allowHampos = at_hampos.authorize("GET", currentmodelUri);
            assertTrue(allowHampos);
            assertTrue(allowSopasakis);
            if (model_owner == null) {
                System.out.println("[Policy Maker] Publishing Policy!");
                IPolicyWrapper ipw = PolicyManager.defaultSignleUserPolicy("M" + mid, currentmodelUri, at);
                ipw.publish(null, at);
            }
        }
        at.invalidate();
        at_hampos.invalidate();
        at_sopasakis.invalidate();
    }

    @Test
    public void testCustomPolicy() throws Exception {
        final String policyName = "myPolicy000";
        final String ruleName = "myRule000";
        final String subjectsCollectionName = "mySubjects000";
        final String subjectsDescription = "mySubjects brief description";
        final String resourceUri = "http://someserver.org:8908/services/compound/1";

        Policy p = new Policy();
        p.setPolicyName(policyName);
        p.setSubjectsDescription(subjectsDescription);
        p.setSubjectsCollectionName(subjectsCollectionName);
        p.addSubject(SingleSubject.Admin1);
        assertEquals(policyName, p.getPolicyName());
        assertEquals(subjectsCollectionName, p.getSubjectsCollectionName());
        assertEquals(subjectsDescription, p.getSubjectsDescription());

        IPolicyRule ipr = new PolicyRule(ruleName);
        ipr.setAllowGet(true);
        ipr.setTargetUri(resourceUri);
        p.addRule(ipr);

        Document doc = p.getDocument();
        assertNotNull("DOM Document is null", doc);
        Element rootElement = doc.getDocumentElement();
        assertNotNull("Root Element is null", rootElement);
        assertEquals("Policies", doc.getDocumentElement().getNodeName());
        NodeList policyNodes = doc.getElementsByTagName("Policy");
        assertEquals(1, policyNodes.getLength());
        Element policyNode = (Element) policyNodes.item(0);
        assertNotNull("Policy is not declared to be active", policyNode.getAttribute("active"));
        assertEquals("Policy should be active", "true", policyNode.getAttribute("active"));
        assertNotNull("Policy has no name", policyNode.getAttribute("name"));
        assertEquals("Policy has the wrong name", policyName, policyNode.getAttribute("name"));
        assertNotNull("No referralPolicy was declared", policyNode.getAttribute("referralPolicy"));
        assertEquals("false", policyNode.getAttribute("referralPolicy"));
        NodeList rulesList = policyNode.getElementsByTagName("Rule");
        assertNotNull("No rules found", rulesList);
        assertEquals(1, rulesList.getLength());
        Element ruleElement = (Element) rulesList.item(0);
        assertEquals(ruleName, ruleElement.getAttribute("name"));
        NodeList serviceNameList = ruleElement.getElementsByTagName("ServiceName");
        assertNotNull(serviceNameList);
        assertEquals(1, serviceNameList.getLength());
        Element serviceNameElement = (Element) serviceNameList.item(0);
        assertNotNull(serviceNameElement);
        assertEquals(ipr.getServiceName(), serviceNameElement.getAttribute("name"));
        NodeList resourceNameList = ruleElement.getElementsByTagName("ResourceName");
        assertNotNull("No ResourceName tags found", resourceNameList);
        assertEquals(1, resourceNameList.getLength());
        Element resourceNameElement = (Element) resourceNameList.item(0);
        assertNotNull(resourceNameElement);
        assertEquals(resourceUri, resourceNameElement.getAttribute("name"));
        NodeList attrValPairs = ruleElement.getElementsByTagName("AttributeValuePair");
        assertNotNull(attrValPairs);
        assertEquals(4, attrValPairs.getLength());
        Element avPair = null;
        for (int i = 0; i < 4; i++) {
            avPair = (Element) attrValPairs.item(i);
            NodeList attributeChild = avPair.getElementsByTagName("Attribute");
            assertNotNull(attributeChild);
            assertEquals(1, attributeChild.getLength());
            Element attribute = (Element) attributeChild.item(0);
            assertNotNull(attribute);
            String attributeName = attribute.getAttribute("name");
            assertNotNull(attributeName);
            NodeList attributeValues = avPair.getElementsByTagName("Value");
            assertNotNull(attributeValues);
            assertEquals(1, attributeValues.getLength());
            Element aValEle = (Element) attributeValues.item(0);
            if ("GET".equals(attributeName)) {
                assertEquals("allow", aValEle.getTextContent());
            } else {
                assertEquals("deny", aValEle.getTextContent());
            }
        }
        NodeList subjectsList = policyNode.getElementsByTagName("Subjects");
        assertNotNull(subjectsList);
        assertEquals(1, subjectsList.getLength());
        Element subjectsEle = (Element) subjectsList.item(0);
        assertEquals(subjectsCollectionName, subjectsEle.getAttribute("name"));
        assertNotNull(p.getText());
    }

    @Test
    public void testCreateAPolicy() throws Exception {
        AuthenticationToken at = new AuthenticationToken("hampos", "arabela");

        Policy pol = new Policy("NTUA_all_users");
        pol.addSubject(GroupSubject.DEVELOPMENT);
        pol.addSubject(GroupSubject.PARTNER);
        PolicyRule pr = new PolicyRule("ot_only_access");
        pr.setTargetUri("http://opentox.ntua.gr:8080/user");
        pr.setAllowGet(true);
        pr.setAllowPost(false);
        pr.setAllowPut(false);
        pr.setAllowDelete(false);
        pol.addRule(pr);
        PolicyWrapper pw = new PolicyWrapper(pol);

        List<String> pols = PolicyManager.listPolicyUris(null, at);
        if (!pols.contains("NTUA_all_users")) {
            pw.publish(null, at);
        }

    }
}
