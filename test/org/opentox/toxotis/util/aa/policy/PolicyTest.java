package org.opentox.toxotis.util.aa.policy;

import java.io.IOException;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.aa.PasswordFileManager;


/**
 *
 * @author chung
 */
public class PolicyTest {

    public PolicyTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        org.apache.log4j.LogManager.resetConfiguration();
        org.apache.log4j.PropertyConfigurator.configure("config/log4j.properties");
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
    public void testListPolicies() throws IOException, ToxOtisException {
        AuthenticationToken at = PasswordFileManager.CRYPTO.authFromFile("/home/chung/toxotisKeys/my.key");

        try {
            System.out.println(at);
            System.out.println(at.getUser());

            /* CREATE THE POLICY */
            Policy p = new Policy();

            p.setPolicyName("bibtex_14e45f89-1223-43eb-82cc-5aaf674e5e72");
            PolicyRule rule = new PolicyRule("rule1");
            rule.setTargetUri("http://opentox.ntua.gr:3000/bibtex/14e45f89-1223-43eb-82cc-5aaf674e5e72");
            rule.setAllowGet(true);
            rule.setAllowPost(true);
            p.addRule(rule);
            p.addSubject(SingleSubject.YAQPservice);
            p.addSubject(SingleSubject.Admin1);
            p.addSubject(SingleSubject.Admin2);
            p.addSubject(GroupSubject.DEVELOPMENT);
            p.addSubject(GroupSubject.PARTNER);

//        System.out.println( p.getText() );
            System.out.println(p.publishPolicy(null, at));

//        System.out.println(at.validate() ? "valid" : "invalid");

            ArrayList<String> strings = Policy.listPolicyUris(null, at);
            System.out.println(strings);

//        String usr = p.getPolicyOwner(new VRI("http://opentox.ntua.gr:3000/model"), null, at);
//        System.out.println("Policy Owner is : " + usr);       


            final ArrayList<String> list = Policy.listPolicyUris(Services.SingleSignOn.ssoPolicyOld(), at);
            for (String pol : list) {
                System.out.println(pol);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // FINALLY....
            at.invalidate();
            System.out.println(at.validate() ? "valid" : "invalid");
        }
    }
}
