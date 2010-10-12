package org.opentox.toxotis.util.aa.policy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.aa.SSLConfiguration;


/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class PolicyManager {

    /*
     * NOTE: Only admins are allowed to create/modify policies.
     */
    private Policy policy;
    private String policyServiceUrl = Services.SingleSignOn.ssoPolicy().toString();
    private AuthenticationToken token;
    private static final String subjectIdHeader = "subjectid";

    public PolicyManager() {
        SSLConfiguration.initializeSSLConnection();
    }

    public PolicyManager(AuthenticationToken token) {
        this();
        this.token = token;
    }

    public PolicyManager(Policy policy, AuthenticationToken token) {
        this.policy = policy;
        this.token = token;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public String getPolicyServiceUrl() {
        return policyServiceUrl;
    }

    public void setPolicyServiceUrl(String policyServiceUrl) {
        this.policyServiceUrl = policyServiceUrl;
    }

    public AuthenticationToken getToken() {
        return token;
    }

    public void setToken(AuthenticationToken token) {
        this.token = token;
    }

    public void postToRemote() throws ToxOtisException {

        throw new UnsupportedOperationException();
    }

    public void deleteRemote() throws ToxOtisException {
        throw new UnsupportedOperationException();
    }

    public ArrayList<String> listRemotePolicies() throws ToxOtisException {
        throw new UnsupportedOperationException();
    }

    public static void main(String... args) throws Exception {

    }
}

