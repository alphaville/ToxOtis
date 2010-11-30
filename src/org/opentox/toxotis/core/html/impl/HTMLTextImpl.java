package org.opentox.toxotis.core.html.impl;

import org.opentox.toxotis.core.html.HTMLText;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class HTMLTextImpl
        extends HTMLExpandableComponentImpl implements HTMLText {

    private String text;
    private boolean bold;
    private boolean italics;
    private boolean tt;
    private boolean undelined;
    private boolean pre;
    private static final String _bold = "<b>%s</b>";
    private static final String _italix = "<em>%s</em>";
    private static final String _tt = "<tt>%s</tt>";
    private static final String _underlined = "<u>%s</u>";
    private static final String _PRE = "<pre>%s</pre>";

    public HTMLTextImpl() {
    }

    public HTMLTextImpl(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public HTMLText setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public HTMLText setComment(String text) {
        this.text = "<!--\n" + text + "\n-->";
        return this;
    }

    @Override
    public String toString() {
        if (bold) {
            text = String.format(_bold, text);
        }
        if (italics) {
            text = String.format(_italix, text);
        }
        if (tt) {
            text = String.format(_tt, text);
        }
        if (undelined) {
            text = String.format(_underlined, text);
        }
        if (pre) {
            text = String.format(_PRE, text);
        }
        return text;
    }

    @Override
    public HTMLText formatBold(boolean isBold) {
        this.bold = isBold;
        return this;

    }

    @Override
    public HTMLText formatItalics(boolean isItalics) {
        this.italics = isItalics;
        return this;
    }

    @Override
    public HTMLText formatUnderlined(boolean isUnderlined) {
        this.undelined = isUnderlined;
        return this;
    }

    @Override
    public HTMLText formatTrueType(boolean isTrueType) {
        this.tt = isTrueType;
        return this;
    }

    @Override
    public HTMLText formatPRE(boolean isPRE) {
        this.pre = isPRE;
        return this;
    }
}
