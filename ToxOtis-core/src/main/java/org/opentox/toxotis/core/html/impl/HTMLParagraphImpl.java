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

import org.apache.commons.lang.StringUtils;
import org.opentox.toxotis.core.html.Alignment;
import org.opentox.toxotis.core.html.HTMLComponent;
import org.opentox.toxotis.core.html.HTMLParagraph;
import org.opentox.toxotis.core.html.HTMLText;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class HTMLParagraphImpl extends HTMLExpandableComponentImpl implements HTMLParagraph {

    private Alignment align;
    private String style;

    public HTMLParagraphImpl() {
    }

    public HTMLParagraphImpl(String paragraphText) {
        addComponent(new HTMLTextImpl(paragraphText));
    }

    public HTMLParagraphImpl(HTMLText htmlText) {
        addComponent(htmlText);
    }

    @Override
    public HTMLParagraph addText(HTMLText text) {
        addComponent(text);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String alignment = "";
        if (align != null) {
            alignment = " align=\"" + align + "\"";
        }
        if (StringUtils.isNotEmpty(style)) {
            alignment = " style=\"" + style + "\"";
        }
        builder.append("<p").append(alignment).append(">");
        for (HTMLComponent component : getComponents()) {
            builder.append(component.toString());
        }
        builder.append("</p>");
        return builder.toString();
    }

    @Override
    public HTMLParagraph setAlignment(Alignment align) {
        this.align = align;
        return this;
    }
    
    @Override
    public HTMLParagraph setStyle(String varStyle) {
        this.style = varStyle;
        return this;
    }

    @Override
    public Alignment getAlignment() {
        return align;
    }

    @Override
    public HTMLParagraph addText(String text) {
        addText(new HTMLTextImpl(text));
        return this;
    }
}
