package org.opentox.toxotis.util.aa.policy;

/**
 * Any subject of a policy that can either be an individual user or a group of such.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class PolicySubject {

    protected String LDAP_Type;

    private String subjectName;

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public abstract String getValue();

}