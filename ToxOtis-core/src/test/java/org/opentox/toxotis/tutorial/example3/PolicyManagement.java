package org.opentox.toxotis.tutorial.example3;

import java.net.URISyntaxException;
import java.util.ArrayList;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.aa.policy.IPolicyWrapper;
import org.opentox.toxotis.util.aa.policy.PolicyManager;
import static org.junit.Assert.*;

/**
 *
 * @author Pantelis Sopasakis
 */
public class PolicyManagement {

    public PolicyManagement() {
    }

    @Test
    public void testPolicyManagement() throws ServiceInvocationException, URISyntaxException, ToxOtisException {
        /**
         * Authenticate and acquire a token from the SSO server
         */
        String policyName = "abcxyz1";
        String uri = "http://opentox.ntua.gr:4000/my/resource/1";

        /*
         * Policy Definition
         */
        AuthenticationToken at = new AuthenticationToken("guest", "guest");
        IPolicyWrapper myPolicy = PolicyManager.defaultSignleUserPolicy(policyName, new VRI(uri), at);
        System.out.println(myPolicy.getText());

        /*
         * Policy creation
         */
        myPolicy.publish(null, at);

        /*
         * Verify that policy has been created
         */
        ArrayList<String> policiesPlusOne = PolicyManager.listPolicyUris(null, at);
        assertTrue("A policy was created but is not found in the list of policies", policiesPlusOne.contains(policyName));

        /*
         * Delete remote policy
         */
        PolicyManager.deleteRemotePolicy(null, policyName, at);

        /*
         * Verify that policy has been deleted
         */
        ArrayList<String> policies = PolicyManager.listPolicyUris(null, at);
        assertTrue("Policy " + policyName + " was not deleted", !policies.contains(policyName));

        at.invalidate();
    }
}
