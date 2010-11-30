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
