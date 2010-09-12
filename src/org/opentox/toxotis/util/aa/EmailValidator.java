package org.opentox.toxotis.util.aa;

import java.util.regex.Pattern;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class EmailValidator {

    /** Regex copied from: http://www.regular-expressions.info/email.html */
    private static final String MAIL_REGEX_RFC_2822 = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*"
            + "|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]//|\\\\[\\x01-"
            + "\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9]"
            + "(?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}"
            + "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:"
            + "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    /** Regex from http://code.iamcal.com/php/rfc822/full_regexp.txt */
    private static final String MAIL_REGEX_RFC_2822_FULL = "(((((([\\x20\\x09]*(\\x0d\\x0a))?[\\x20\\x09]+)|([\\x20\\x09]+((\\x0d\\x0a)[\\x20\\x09]+)*))*?"
            + "(([\\x41-\\x5a\\x61-\\x7a]|[\\x30-\\x39]|[\\x21\\x23-\\x27\\x2a\\x2b\\x2d\\x2e\\x3d\\x3f\\x5e\\x5f\\x60\\x7b-\\x7e])"
            + "+(\\x2e([\\x41-\\x5a\\x61-\\x7a]|[\\x30-\\x39]|[\\x21\\x23-\\x27\\x2a\\x2b\\x2d\\x2e\\x3d\\x3f\\x5e\\x5f\\x60\\x7b-\\x7e])+)*)"
            + "((([\\x20\\x09]*(\\x0d\\x0a))?[\\x20\\x09]+)|([\\x20\\x09]+((\\x0d\\x0a)[\\x20\\x09]+)*))*?)|(((([\\x20\\x09]*(\\x0d\\x0a))?"
            + "[\\x20\\x09]+)|([\\x20\\x09]+((\\x0d\\x0a)[\\x20\\x09]+)*))*?\\x22(((([\\x20\\x09]*(\\x0d\\x0a))?[\\x20\\x09]+)|"
            + "([\\x20\\x09]+((\\x0d\\x0a)[\\x20\\x09]+)*))?(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|[\\x21\\x23-\\x5b\\x5d-\\x7e])|"
            + "(\\x5c([\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f]|(\\x0a*\\x0d*([\\x00-\\x09\\x0b\\x0c\\x0e-\\x7f]\\x0a*\\x0d*)*))|"
            + "(\\x5c[\\x00-\\x7f]))))*((([\\x20\\x09]*(\\x0d\\x0a))?[\\x20\\x09]+)|([\\x20\\x09]+((\\x0d\\x0a)[\\x20\\x09]+)*))?"
            + "\\x22((([\\x20\\x09]*(\\x0d\\x0a))?[\\x20\\x09]+)|([\\x20\\x09]+((\\x0d\\x0a)[\\x20\\x09]+)*))*?)|(((((([\\x20\\x09]*"
            + "(\\x0d\\x0a))?[\\x20\\x09]+)|([\\x20\\x09]+((\\x0d\\x0a)[\\x20\\x09]+)*))*?([\\x41-\\x5a\\x61-\\x7a]|[\\x30-\\x39]|"
            + "[\\x21\\x23-\\x27\\x2a\\x2b\\x2d\\x2e\\x3d\\x3f\\x5e\\x5f\\x60\\x7b-\\x7e])+((([\\x20\\x09]*(\\x0d\\x0a))?[\\x20\\x09]+)|"
            + "([\\x20\\x09]+((\\x0d\\x0a)[\\x20\\x09]+)*))*?)|(((([\\x20\\x09]*(\\x0d\\x0a))?[\\x20\\x09]+)|([\\x20\\x09]+((\\x0d\\x0a)"
            + "[\\x20\\x09]+)*))*?\\x22(((([\\x20\\x09]*(\\x0d\\x0a))?[\\x20\\x09]+)|([\\x20\\x09]+((\\x0d\\x0a)[\\x20\\x09]+)*))?"
            + "(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|[\\x21\\x23-\\x5b\\x5d-\\x7e])|(\\x5c([\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f]|"
            + "(\\x0a*\\x0d*([\\x00-\\x09\\x0b\\x0c\\x0e-\\x7f]\\x0a*\\x0d*)*))|(\\x5c[\\x00-\\x7f]))))*((([\\x20\\x09]*(\\x0d\\x0a))?"
            + "[\\x20\\x09]+)|([\\x20\\x09]+((\\x0d\\x0a)[\\x20\\x09]+)*))?\\x22((([\\x20\\x09]*(\\x0d\\x0a))?[\\x20\\x09]+)|"
            + "([\\x20\\x09]+((\\x0d\\x0a)[\\x20\\x09]+)*))*?))(\\x2e((((([\\x20\\x09]*(\\x0d\\x0a))?[\\x20\\x09]+)|([\\x20\\x09]+"
            + "((\\x0d\\x0a)[\\x20\\x09]+)*))*?([\\x41-\\x5a\\x61-\\x7a]|[\\x30-\\x39]|[\\x21\\x23-\\x27\\x2a\\x2b\\x2d\\x2e\\x3d\\x3f"
            + "\\x5e\\x5f\\x60\\x7b-\\x7e])+((([\\x20\\x09]*(\\x0d\\x0a))?[\\x20\\x09]+)|([\\x20\\x09]+((\\x0d\\x0a)[\\x20\\x09]+)*))*?)|"
            + "(((([\\x20\\x09]*(\\x0d\\x0a))?[\\x20\\x09]+)|([\\x20\\x09]+((\\x0d\\x0a)[\\x20\\x09]+)*))*?\\x22(((([\\x20\\x09]*"
            + "(\\x0d\\x0a))?[\\x20\\x09]+)|([\\x20\\x09]+((\\x0d\\x0a)[\\x20\\x09]+)*))?(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|"
            + "[\\x21\\x23-\\x5b\\x5d-\\x7e])|(\\x5c([\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f]|(\\x0a*\\x0d*([\\x00-\\x09\\x0b\\x0c\\x0e-\\x7f]\\x0a*\\x0d*)*))|"
            + "(\\x5c[\\x00-\\x7f]))))*((([\\x20\\x09]*(\\x0d\\x0a))?[\\x20\\x09]+)|([\\x20\\x09]+((\\x0d\\x0a)[\\x20\\x09]+)*))?"
            + "\\x22((([\\x20\\x09]*(\\x0d\\x0a))?[\\x20\\x09]+)|([\\x20\\x09]+((\\x0d\\x0a)[\\x20\\x09]+)*))*?)))*))\\x40"
            + "((((([\\x20\\x09]*(\\x0d\\x0a))?[\\x20\\x09]+)|([\\x20\\x09]+((\\x0d\\x0a)[\\x20\\x09]+)*))*?"
            + "(([\\x41-\\x5a\\x61-\\x7a]|[\\x30-\\x39]|[\\x21\\x23-\\x27\\x2a\\x2b\\x2d\\x2e\\x3d\\x3f\\x5e\\x5f\\x60\\x7b-\\x7e])+"
            + "(\\x2e([\\x41-\\x5a\\x61-\\x7a]|[\\x30-\\x39]|[\\x21\\x23-\\x27\\x2a\\x2b\\x2d\\x2e\\x3d\\x3f\\x5e\\x5f\\x60\\x7b-\\x7e])+)*)"
            + "((([\\x20\\x09]*(\\x0d\\x0a))?[\\x20\\x09]+)|([\\x20\\x09]+((\\x0d\\x0a)[\\x20\\x09]+)*))*?)|"
            + "(((([\\x20\\x09]*(\\x0d\\x0a))?[\\x20\\x09]+)|([\\x20\\x09]+((\\x0d\\x0a)[\\x20\\x09]+)*))*?\\x5b(((([\\x20\\x09]*"
            + "(\\x0d\\x0a))?[\\x20\\x09]+)|([\\x20\\x09]+((\\x0d\\x0a)[\\x20\\x09]+)*))?(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|"
            + "[\\x21-\\x5a\\x5e-\\x7e])|(\\x5c([\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f]|(\\x0a*\\x0d*"
            + "([\\x00-\\x09\\x0b\\x0c\\x0e-\\x7f]\\x0a*\\x0d*)*))|(\\x5c[\\x00-\\x7f]))))*((([\\x20\\x09]*(\\x0d\\x0a))?[\\x20\\x09]+)|"
            + "([\\x20\\x09]+((\\x0d\\x0a)[\\x20\\x09]+)*))?\\x5d((([\\x20\\x09]*(\\x0d\\x0a))?[\\x20\\x09]+)|([\\x20\\x09]+((\\x0d\\x0a)[\\x20\\x09]+)*))*?)|"
            + "((((([\\x20\\x09]*(\\x0d\\x0a))?[\\x20\\x09]+)|([\\x20\\x09]+((\\x0d\\x0a)[\\x20\\x09]+)*))*?([\\x41-\\x5a\\x61-\\x7a]|"
            + "[\\x30-\\x39]|[\\x21\\x23-\\x27\\x2a\\x2b\\x2d\\x2e\\x3d\\x3f\\x5e\\x5f\\x60\\x7b-\\x7e])+((([\\x20\\x09]*(\\x0d\\x0a))?"
            + "[\\x20\\x09]+)|([\\x20\\x09]+((\\x0d\\x0a)[\\x20\\x09]+)*))*?)(\\x2e(((([\\x20\\x09]*(\\x0d\\x0a))?[\\x20\\x09]+)|"
            + "([\\x20\\x09]+((\\x0d\\x0a)[\\x20\\x09]+)*))*?([\\x41-\\x5a\\x61-\\x7a]|[\\x30-\\x39]|"
            + "[\\x21\\x23-\\x27\\x2a\\x2b\\x2d\\x2e\\x3d\\x3f\\x5e\\x5f\\x60\\x7b-\\x7e])+((([\\x20\\x09]*"
            + "(\\x0d\\x0a))?[\\x20\\x09]+)|([\\x20\\x09]+((\\x0d\\x0a)[\\x20\\x09]+)*))*?))*)))";
    private static String[] mailExt = new String[]{
        "ac", "ad", "ae", "af", "ag", "ai", "al", "am", "an", "ao", "aq",
        "ar", "as", "at", "au", "aw", "az", "ba", "bb", "bd", "be", "bf",
        "bg", "bh", "bi", "bj", "bm", "bn", "bo", "br", "bs", "bt", "bv",
        "bw", "by", "bz", "ca", "cc", "cd", "cf", "cg", "ch", "ci", "ck",
        "cl", "cm", "cn", "co", "cr", "cu", "cv", "cx", "cy", "cz", "de",
        "dj", "dk", "dm", "do", "dz", "ec", "ee", "eg", "eh", "er", "es",
        "et", "fi", "fj", "fk", "fm", "fo", "fr", "fx", "ga", "gb", "gd",
        "ge", "gf", "gg", "gh", "gi", "gl", "gm", "gn", "gp", "gq", "gr",
        "gs", "gt", "gu", "gw", "gy", "hk", "hm", "hn", "hr", "ht", "hu",
        "id", "ie", "il", "im", "in", "io", "iq", "ir", "is", "it", "je",
        "jm", "jo", "jp", "ke", "kg", "kh", "ki", "km", "kn", "kp", "kr",
        "kw", "ky", "kz", "la", "lb", "lc", "li", "lk", "lr", "ls", "lt",
        "lu", "lv", "ly", "ma", "mc", "md", "mg", "mh", "mk", "ml", "mm",
        "mn", "mo", "mp", "mq", "mr", "ms", "mt", "mu", "mv", "mw", "mx",
        "my", "mz", "na", "nc", "ne", "nf", "ng", "ni", "nl", "no", "np",
        "nr", "nu", "nz", "om", "pa", "pe", "pf", "pg", "ph", "pk", "pl",
        "pm", "pn", "pr", "ps", "pt", "pw", "py", "qa", "re", "ro", "ru",
        "rw", "sa", "sb", "sc", "sd", "se", "sg", "sh", "si", "sj", "sk",
        "sl", "sm", "sn", "so", "sr", "st", "sv", "sy", "sz", "tc", "td",
        "tf", "tg", "th", "tj", "tk", "tm", "tn", "to", "tp", "tr", "tt",
        "tv", "tw", "tz", "ua", "ug", "uk", "um", "us", "uy", "uz", "va",
        "vc", "ve", "vg", "vi", "vn", "vu", "wf", "ws", "ye", "yt", "yu",
        "za", "zm", "zw", "aero", "biz", "coop", "com", "edu", "gov", "info",
        "mil", "museum", "name", "net", "org", "pro", "jobs"};

    public static boolean validate(String mail) {
        StringBuilder sb = new StringBuilder("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+(?:[A-Z]{2}|");
        for (int i = 0; i < mailExt.length; i++) {
            sb.append(mailExt[i]);
            if (i != mailExt.length - 1) {
                sb.append("|");
            }
        }
        sb.append(")\\b");
        String regex1 = new String(sb);
        Pattern pattern1 = Pattern.compile(regex1);
        Pattern pattern2 = Pattern.compile(MAIL_REGEX_RFC_2822);
        Pattern pattern3 = Pattern.compile(MAIL_REGEX_RFC_2822_FULL);
        if (!pattern1.matcher(mail).matches() || !pattern2.matcher(mail).matches() || !pattern3.matcher(mail).matches()) {
            return false;
        }
        return true;
    }
}
