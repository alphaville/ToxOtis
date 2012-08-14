package org.opentox.toxotis.util.aa.policy;

import java.io.File;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.util.aa.AuthenticationToken;
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
            
            boolean allowGuest = at.authorize("GET", currentmodelUri),
                    allowSopasakis = at_sopasakis.authorize("GET", currentmodelUri),
                    allowHampos = at_hampos.authorize("GET", currentmodelUri);
            System.out.println("Allowed for guest     = " + allowGuest);
            System.out.println("Allowed for sopasakis = " + allowSopasakis);
            System.out.println("Allowed for hampos    = " + allowHampos);
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

        ArrayList<String> pols = PolicyManager.listPolicyUris(null, at);
        if (!pols.contains("NTUA_all_users")) {
            pw.publish(null, at);
        }

    }
}
