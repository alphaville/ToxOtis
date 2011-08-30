/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.toxotis.util;

import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.aa.policy.GroupSubject;
import org.opentox.toxotis.util.aa.policy.Policy;
import org.opentox.toxotis.util.aa.policy.PolicyRule;
import org.opentox.toxotis.util.aa.policy.SingleSubject;

/**
 *
 * @author chung
 */
public class PolicyCreator {
    
    public static void main(String... args) throws Exception {
        AuthenticationToken token = new AuthenticationToken("Sopasakis", "longmonkeyplumbsin");
        Policy p = new Policy("access_own_user_profile");
        p.addSubject(new SingleSubject("Sopasakis"));
        PolicyRule policyRule = new PolicyRule("access_me");
        policyRule.setAllowances(true, false, false, false);
        policyRule.setTargetUri("http://opentox.ntua.gr:8080/user/Sopasakis@opensso.in-silico.ch");
        p.addRule(policyRule);
        p.publish(null, token);
    }
    
}
