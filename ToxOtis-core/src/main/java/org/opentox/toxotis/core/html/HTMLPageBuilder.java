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

import java.net.URL;
import org.opentox.toxotis.core.html.impl.*;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class HTMLPageBuilder {

    private HTMLPage page;

    public HTMLPageBuilder() {
        page = new HTMLPageImpl();
    }

    public void setDoRefresh(boolean doRegresh, int secondsDelay) {
        page.getHTMLHead().setDoRefresh(doRegresh, secondsDelay);
    }

    public void setDoRedirect(URL url) {
        page.getHTMLHead().setDoRedirect(url);
    }

    public HTMLTag addHeading(String heading) {
        HTMLTag h1 = new HTMLTagImpl("h1", heading);
        page.getHtmlBody().addComponent(h1);
        return h1;
    }

    public HTMLTag addSubHeading(String heading) {
        HTMLTag h2 = new HTMLTagImpl("h2", heading);
        page.getHtmlBody().addComponent(h2);
        return h2;
    }

    public HTMLTag addSubSubHeading(String heading) {
        HTMLTag h3 = new HTMLTagImpl("h3", heading);
        page.getHtmlBody().addComponent(h3);
        return h3;
    }

    public HTMLParagraph addParagraph(String text) {
        HTMLParagraph paragraph = new HTMLParagraphImpl(text);
        page.getHtmlBody().addComponent(paragraph);
        return paragraph;
    }

    public HTMLContainer addDiv(String divClass, HTMLComponent component) {
        HTMLContainer div = new HTMLContainerImpl().setDivClass(divClass);
        div.addComponent(component);
        page.getHtmlBody().addComponent(div);
        return div;
    }

    public HTMLPage getPage() {
        return page;
    }

    @Override
    public String toString() {
        return page.toString();
    }
}
