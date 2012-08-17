package org.opentox.toxotis.core.component.qprf;

import java.io.Serializable;

/**
 *
 * @author Pantelis Sopasakis
 */
public class QprfAuthor implements Serializable {

    private static final long serialVersionUID = 2513498669827309L;
    private String firstName;
    private String lastName;
    private String affiliation;
    private String email;
    private String address;
    private String contactInfo;
    private String url;

    public QprfAuthor() {
    }

    public QprfAuthor(String firstName, String lastName, String affiliation, String email, String address, String contactInfo, String URL) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.affiliation = affiliation;
        this.email = email;
        this.address = address;
        this.contactInfo = contactInfo;
        this.url = URL;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String URL) {
        this.url = URL;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
