package org.opentox.toxotis.util.aa.policy;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class SingleSubject extends PolicySubject {

    public static final SingleSubject YAQPservice = new SingleSubject("YAQPservice");
    public static final SingleSubject Admin1 = new SingleSubject("Sopasakis");
    public static final SingleSubject Admin2 = new SingleSubject("hampos");


    public SingleSubject() {
        LDAP_Type = "LDAPUsers";
    }

    private SingleSubject(String subjectName) {
        this();
        setSubjectName(subjectName);
    }

    @Override
    public String getValue() {
        return "uid=" + getSubjectName() + ", ou=people, dc=opentox,dc=org";
    }
}
