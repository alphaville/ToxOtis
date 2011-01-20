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

import org.opentox.toxotis.core.html.Alignment;
import org.opentox.toxotis.core.html.HTMLComponent;
import org.opentox.toxotis.core.html.HTMLContainer;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class HTMLContainerImpl extends HTMLExpandableComponentImpl implements HTMLContainer {

    private String divClass;
    private Alignment align;
    private String id;

    public HTMLContainerImpl() {
        super();
    }

    @Override
    public HTMLContainer setDivClass(String divClass) {
        this.divClass = divClass;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<div");
        if (divClass != null) {
            builder.append(" class=\"");
            builder.append(divClass);
            builder.append("\"");
        }
        if (id != null) {
            builder.append(" id=\"");
            builder.append(id);
            builder.append("\"");
        }
        if (align != null) {
            builder.append(" align=\"");
            builder.append(align);
            builder.append("\"");
        }
        builder.append(">\n");
        for (HTMLComponent component : getComponents()) {
            builder.append(component.toString());
            builder.append("\n");
        }
        builder.append("</div>");
        return builder.toString();
    }

    @Override
    public HTMLContainer setAlignment(Alignment align) {
        this.align = align;
        return this;
    }

    @Override
    public HTMLContainer setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public HTMLContainer breakLine() {
        addComponent(new HTMLTextImpl("<br/>"));
        return this;
    }

    @Override
    public HTMLContainer horizontalSeparator() {
        addComponent(new HTMLTextImpl("<hr/>"));
        return this;
    }
}
