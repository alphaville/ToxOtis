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
package org.opentox.toxotis.core.html;

import org.opentox.toxotis.core.html.impl.HTMLAppendableTableImpl;
import org.opentox.toxotis.core.html.impl.HTMLContainerImpl;
import org.opentox.toxotis.core.html.impl.HTMLFormImpl;
import org.opentox.toxotis.core.html.impl.HTMLJSImpl;
import org.opentox.toxotis.core.html.impl.HTMLParagraphImpl;
import org.opentox.toxotis.core.html.impl.HTMLTagImpl;
import org.opentox.toxotis.core.html.impl.HTMLTextImpl;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class HTMLDivBuilder {

    private HTMLContainer div = new HTMLContainerImpl();

    public HTMLDivBuilder() {
    }

    public HTMLDivBuilder(String divClass) {
        div.setDivClass(divClass);
    }

    public HTMLContainer setDivClass(String divClass) {
        div.setDivClass(divClass);
        return div;
    }

    public HTMLContainer addComponent(HTMLComponent component) {
        div.addComponent(component);
        return getDiv();
    }

    public HTMLContainer addParagraph(HTMLComponent text, Alignment align) {
        div.addComponent(new HTMLParagraphImpl(text.toString()).setAlignment(align));
        return getDiv();
    }
    
    public HTMLContainer addParagraph(String text, Alignment align) {
        div.addComponent(new HTMLParagraphImpl(text).setAlignment(align));
        return getDiv();
    }
    
    public HTMLContainer addParagraph(String text, Alignment align,String style) {
        div.addComponent(new HTMLParagraphImpl(text).setAlignment(align).setStyle(style));
        return getDiv();
    }

    public HTMLContainer addComment(String comment) {
        div.addComponent(new HTMLTextImpl().setComment(comment));
        return div;
    }

    public HTMLContainer addHeading(String text) {
        div.addComponent(new HTMLTagImpl("h1", text));
        return div;
    }

    public HTMLContainer addSubHeading(String text) {
        div.addComponent(new HTMLTagImpl("h2", text));
        return div;
    }

    public HTMLContainer addSubSubHeading(String text) {
        div.addComponent(new HTMLTagImpl("h3", text));
        return div;
    }
    
    
    public HTMLContainer addSubSubHeading(String text,String style) {
        HTMLTagImpl tg = new HTMLTagImpl("h3", text);
        tg.addTagAttribute("style", style);
        div.addComponent(tg);
        return div;
    }

    public HTMLContainer addSubSubSubHeading(String text) {
        div.addComponent(new HTMLTagImpl("h4", text));
        return div;
    }

    public HTMLForm addForm() {
        HTMLForm form = new HTMLFormImpl();
        div.addComponent(form);
        return form;
    }
    
    
    public HTMLForm addForm(String actionUri, String method, String mediaType) {
        HTMLForm form = new HTMLFormImpl();
        form.setActionUrl(actionUri);
        form.setMethod(method);
        form.setMediaType(mediaType);
        div.addComponent(form);
        return form;
    }

    public HTMLForm addForm(String actionUri, String method) {
        HTMLForm form = new HTMLFormImpl();
        form.setActionUrl(actionUri);
        form.setMethod(method);
        div.addComponent(form);
        return form;
    }

    public HTMLTable addTable(int cols, int rows) {
        HTMLTable table = new HTMLAppendableTableImpl(cols, rows);
        div.addComponent(table);
        return table;
    }

    public HTMLTable addTable(int cols) {
        HTMLTable table = new HTMLAppendableTableImpl(cols);
        div.addComponent(table);
        return table;
    }

    public HTMLContainer getDiv() {
        return div;
    }

    public HTMLTag addLink(String target, String text) {
        HTMLTag linkTag = new HTMLTagImpl("a");
        linkTag.addTagAttribute("href", target);
        div.addComponent(linkTag);
        return linkTag;
    }
    
    
    public HTMLContainer addJS(String text) {
        div.addComponent(new HTMLJSImpl(text));
        return div;
    }

}
