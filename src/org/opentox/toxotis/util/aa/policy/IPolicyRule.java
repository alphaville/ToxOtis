package org.opentox.toxotis.util.aa.policy;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface IPolicyRule {

    String getName();

    String getServiceName();

    String getTargetUri();

    boolean isAllowDelete();

    boolean isAllowGet();

    boolean isAllowPost();

    boolean isAllowPut();

    IPolicyRule setAllowDelete(boolean allowDelete);

    IPolicyRule setAllowGet(boolean allowGet);

    IPolicyRule setAllowPost(boolean allowPost);

    IPolicyRule setAllowPut(boolean allowPut);

    IPolicyRule setAllowances(boolean get, boolean post, boolean put, boolean delete);

    IPolicyRule setName(String name);

    IPolicyRule setServiceName(String serviceName);

    IPolicyRule setTargetUri(String targetUri);
}
