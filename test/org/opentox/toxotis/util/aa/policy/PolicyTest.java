package org.opentox.toxotis.util.aa.policy;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.aa.PasswordFileManager;
import org.opentox.toxotis.util.aa.SSLConfiguration;
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
    public void testSomeMethod() throws ToxOtisException, IOException, URISyntaxException {
        AuthenticationToken at = PasswordFileManager.CRYPTO.authFromFile("/home/chung/toxotisKeys/my.key");
        System.out.println(at.getTokenUrlEncoded());
        /* CREATE THE POLICY */
//        Policy p = new Policy();
//        p.setPolicyName("xx");
//
//        PolicyRule rule = new PolicyRule("rule1");
//        rule.setTargetUri("http://opentox.ntua.gr:3000/model/f9a97443-6baf-4361-a55c-b08cf12c3e39");
//        rule.setAllowGet(true);
//        rule.setAllowPost(true);
//        p.addRule(rule);
//
//        p.addSubject(SingleSubject.YAQPservice);
//        p.addSubject(SingleSubject.Admin1);
//        p.addSubject(SingleSubject.Admin2);
//        p.addSubject(GroupSubject.DEVELOPMENT);
//        p.addSubject(GroupSubject.PARTNER);

        //Policy.deleteRemotePolicy(null, "myPolicy_14", at);

        //      System.out.println(p.getText());

//        p.publishPolicy(null, at);
        System.out.println(
                Policy.getPolicyOwner(new VRI("http://opentox.ntua.gr:3000/model/ce0c44bb-b3c2-4e72-a693-e1fa456879ab"), null, at)
                );
        System.out.println(
                Policy.getPolicyOwner(new VRI("http://opentox.ntua.gr:3000/model/f9a97443-6baf-4361-a55c-b08cf12c3e39"), null, at)
                );

        final ArrayList<String> list = Policy.listPolicyUris(Services.SingleSignOn.ssoPolicyOld(), at);
        for (String pol : list) {
            System.out.println(pol);
        }

    }
}
