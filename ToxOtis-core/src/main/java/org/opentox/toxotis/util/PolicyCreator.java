package org.opentox.toxotis.util;

import java.io.File;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.aa.policy.Policy;
import org.opentox.toxotis.util.aa.policy.PolicyManager;
import org.opentox.toxotis.util.aa.policy.PolicyRule;
import org.opentox.toxotis.util.aa.policy.PolicyWrapper;
import org.opentox.toxotis.util.aa.policy.SingleSubject;

/**
 *
 * @author chung
 */
public class PolicyCreator {

    private static final String GUEST_SECRET_KEY = System.getProperty("user.home") + "/toxotisKeys/.sopasakis.key";

    public static void main(String... args) throws Exception {
        final File secretFile = new File(GUEST_SECRET_KEY);
        AuthenticationToken token = new AuthenticationToken(secretFile);

        String myUserResource = String.format("http://opentox.ntua.gr:8080/user/%s@opensso.in-silico.ch",
                SingleSubject.Admin1.getSubjectName());

        String myUserPolicyOwner = PolicyManager.getPolicyOwner(new VRI(myUserResource), null, token);
        System.out.println("Policy 'access_own_user_profile' owned by : '" + myUserPolicyOwner + "'.");
        if (myUserPolicyOwner == null) {
            Policy p = new Policy("access_own_user_profile");
            p.addSubject(SingleSubject.Admin1);
            PolicyRule policyRule = new PolicyRule("access_me");
            policyRule.setAllowances(true, false, false, false);
            policyRule.setTargetUri(
                    myUserResource);
            p.addRule(policyRule);
            p.publish(null, token);
        }

        token = new AuthenticationToken(secretFile);
        String shutdownResource = "http://opentox.ntua.gr:8080/shutdown";
        String shutdownPolicyOwner =
                PolicyManager.getPolicyOwner(new VRI(shutdownResource), null, token);
        System.out.println("Policy 'onoffpolicy' owned by : '" + shutdownPolicyOwner + "'.");
        if (shutdownPolicyOwner == null) {
            PolicyManager.defaultSignleUserPolicy("onoffpolicy", new VRI(shutdownResource), token).
                    publish(null, token);
        }
        token.invalidate();
    }
}
