package org.opentox.toxotis.util.aa.policy;

/**
 * A Rule in a policy that defined the behavior of the SSO service when permission
 * is asked on a target URI for some method. Specifies where POST, GET, PUT and DELETE
 * should be allowed or denied on certain individual users or groups of users.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class PolicyRule {

    private String name;
    private String serviceName = "iPlanetAMWebAgentService";
    private String targetUri;
    private boolean allowGet, allowPost, allowDelete, allowPut;

    public PolicyRule(String name) {
        this.name = name;
    }


    public boolean isAllowDelete() {
        return allowDelete;
    }

    public void setAllowDelete(boolean allowDelete) {
        this.allowDelete = allowDelete;
    }

    public boolean isAllowGet() {
        return allowGet;
    }

    public void setAllowGet(boolean allowGet) {
        this.allowGet = allowGet;
    }

    public boolean isAllowPost() {
        return allowPost;
    }

    public void setAllowPost(boolean allowPost) {
        this.allowPost = allowPost;
    }

    public boolean isAllowPut() {
        return allowPut;
    }

    public void setAllowPut(boolean allowPut) {
        this.allowPut = allowPut;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getTargetUri() {
        return targetUri;
    }

    public void setTargetUri(String targetUri) {
        this.targetUri = targetUri;
    }




}