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
