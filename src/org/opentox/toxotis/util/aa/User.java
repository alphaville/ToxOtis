package org.opentox.toxotis.util.aa;

import java.util.regex.Pattern;
import org.opentox.toxotis.ToxOtisException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class User {

    private String uid;
    private String name;
    private String mail;
    private String hashedPass;

    public User() {
    }

    public String getHashedPass() {
        return hashedPass;
    }

    public void setHashedPass(String hashedPass) {
        this.hashedPass = hashedPass;
    }

    public String getMail() {
        return mail;
    }

    /**
     * Checks whethe the provided e-mail address is RDF-2822 compliant and sets
     * the e-mail address of the user accordingly.
     * @param mail
     *      The e-mail address of the user (Must be RDF-2822 compliant).
     * @throws ToxOtisException
     *      In case the provided e-mail address is not compliant to the
     *      specifications of RFC 2822.
     */
    public void setMail(String mail) throws ToxOtisException {        
        if (!EmailValidator.validate(mail)) {
            throw new ToxOtisException("Bad email address according to RFC 2822 : '" + mail + "'");
        }
        this.mail = mail;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UID   : ");
        sb.append(uid);
        sb.append("\n");
        sb.append("Name  : ");
        sb.append(name);
        sb.append("\n");
        sb.append("Mail  : ");
        sb.append(mail);
        sb.append("\n");
        sb.append("Pass  : ");
        sb.append(hashedPass);
        return new String(sb);
    }
    
}
