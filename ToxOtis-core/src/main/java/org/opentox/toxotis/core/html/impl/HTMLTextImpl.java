/*
 *
 * ToxOtis
 *
 * ToxOtis is the Greek word for Sagittarius, that actually means ‘archer’. ToxOtis
 * is a Java interface to the predictive toxicology services of OpenTox. ToxOtis is
 * being developed to help both those who need a painless way to consume OpenTox
 * services and for ambitious service providers that don’t want to spend half of
 * their time in RDF parsing and creation.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 *
 */
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
    private static final String BOLD = "<b>%s</b>";
    private static final String ITALIX = "<em>%s</em>";
    private static final String TRUETYPE = "<tt>%s</tt>";
    private static final String UNDERLINED = "<u>%s</u>";
    private static final String PREFORMATTED = "<pre>%s</pre>";

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
            text = String.format(BOLD, text);
        }
        if (italics) {
            text = String.format(ITALIX, text);
        }
        if (tt) {
            text = String.format(TRUETYPE, text);
        }
        if (undelined) {
            text = String.format(UNDERLINED, text);
        }
        if (pre) {
            text = String.format(PREFORMATTED, text);
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
