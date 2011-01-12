package org.opentox.toxotis.core.html.impl;

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
        builder.append("<p" + alignment + ">");
        for (HTMLComponent component : componentList) {
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
    public Alignment getAlignment() {
        return align;
    }
}
