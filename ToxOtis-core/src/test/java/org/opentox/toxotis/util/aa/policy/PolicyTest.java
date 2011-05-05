package org.opentox.toxotis.util.aa.policy;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
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
    public void testSomeMethod() throws Exception {
        AuthenticationToken at = new AuthenticationToken("Sopasakis", "abfhs8y");        
        PolicyManager.defaultSignleUserPolicy("shutdown_opentox_8080", new VRI("http://opentox.ntua.gr:8080/shutdown"), at).publish(null, at);
//        ArrayList<String > policiesPlusOne = PolicyManager.listPolicyUris(null, at);
//        assertTrue("A policy was created but is not found in the list of policies",policiesPlusOne.contains("testPolicy_xy"));
//        PolicyManager.deleteRemotePolicy(null, "testPolicy_xy", at);
//        ArrayList<String > policies = PolicyManager.listPolicyUris(null, at);
//        assertTrue("Policy testPolicy_x was not deleted",!policies.contains("testPolicy_xy"));
    }
}
