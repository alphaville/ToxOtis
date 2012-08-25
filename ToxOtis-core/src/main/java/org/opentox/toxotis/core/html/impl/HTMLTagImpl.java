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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.opentox.toxotis.core.html.HTMLComponent;
import org.opentox.toxotis.core.html.HTMLTag;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class HTMLTagImpl extends HTMLExpandableComponentImpl implements HTMLTag {

    private String tag;
    private Map<String, String> tagAttributes = new HashMap<String, String>();

    public HTMLTagImpl(String tag) {
        this.tag = tag;
    }

    public HTMLTagImpl(String tag, String content) {
        this.tag = tag;
        addComponent(new HTMLTextImpl(content));
    }

    public HTMLTagImpl(String tag, HTMLComponent content) {
        this.tag = tag;
        addComponent(content);
    }

    @Override
    public HTMLTag setTag(String tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public HTMLTag addTagAttribute(String attribute, String value) {
        tagAttributes.put(attribute, value);
        return this;
    }

    @Override
    public HTMLTag setContent(String content) {
        addComponent(new HTMLTextImpl(content));
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        StringBuilder attributesBuilder = null;
        if (!tagAttributes.isEmpty()) {
            Iterator<Entry<String, String>> attIt = tagAttributes.entrySet().iterator();
            attributesBuilder = new StringBuilder();
            while (attIt.hasNext()) {
                Entry<String, String> entry = attIt.next();
                attributesBuilder.append(" ");
                attributesBuilder.append(entry.getKey());
                attributesBuilder.append("=");
                attributesBuilder.append("\"").append(entry.getValue()).append("\"");
            }

        }

        String attributesString = attributesBuilder != null
                ? new String(attributesBuilder) : "";
        builder.append("<").append(tag).append(attributesString).append(">");
        for (HTMLComponent component : getComponents()) {
            builder.append(component.toString());
        }
        builder.append("</").append(tag).append(">");
        return builder.toString();
    }


}
