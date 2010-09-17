package org.opentox.toxotis.util.aa.policy;


/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class GroupSubject extends PolicySubject {

    public static final GroupSubject DEVELOPMENT = new GroupSubject("development");
    public static final GroupSubject PARTNER = new GroupSubject("partner");

    public GroupSubject() {
        LDAP_Type = "LDAPGroups";
    }

    private GroupSubject(String groupName) {
        this();
        setSubjectName(groupName);
    }

    @Override
    public String getValue() {
        return "cn=" + getSubjectName() + ", ou=groups, dc=opentox,dc=org";
    }
}
