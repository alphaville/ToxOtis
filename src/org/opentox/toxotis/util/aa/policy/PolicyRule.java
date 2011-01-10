package org.opentox.toxotis.util.aa.policy;

/**
 * A Rule in a policy that defined the behavior of the SSO service when permission
 * is asked on a target URI for some method. Specifies where POST, GET, PUT and DELETE
 * should be allowed or denied on certain individual users or groups of users.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class PolicyRule implements IPolicyRule {

    private String name;
    private String serviceName = "iPlanetAMWebAgentService";
    private String targetUri;
    private boolean allowGet, allowPost, allowDelete, allowPut;

    public PolicyRule(String name) {
        this.name = name;
    }

    public IPolicyRule setAllowances(boolean get, boolean post, boolean put, boolean delete) {
        setAllowGet(get);
        setAllowPost(post);
        setAllowPut(put);
        setAllowDelete(delete);
        return this;
    }

    public boolean isAllowDelete() {
        return allowDelete;
    }

    public IPolicyRule setAllowDelete(boolean allowDelete) {
        this.allowDelete = allowDelete;
        return this;
    }

    public boolean isAllowGet() {
        return allowGet;
    }

    public IPolicyRule setAllowGet(boolean allowGet) {
        this.allowGet = allowGet;
        return this;
    }

    public boolean isAllowPost() {
        return allowPost;
    }

    public IPolicyRule setAllowPost(boolean allowPost) {
        this.allowPost = allowPost;
        return this;
    }

    public boolean isAllowPut() {
        return allowPut;
    }

    public IPolicyRule setAllowPut(boolean allowPut) {
        this.allowPut = allowPut;
        return this;
    }

    public String getName() {
        return name;
    }

    public IPolicyRule setName(String name) {
        this.name = name;
        return this;
    }

    public String getServiceName() {
        return serviceName;
    }

    public IPolicyRule setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public String getTargetUri() {
        return targetUri;
    }

    public IPolicyRule setTargetUri(String targetUri) {
        this.targetUri = targetUri;
        return this;
    }
}
