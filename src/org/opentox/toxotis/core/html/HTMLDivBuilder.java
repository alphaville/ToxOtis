package org.opentox.toxotis.core.html;

import org.opentox.toxotis.core.html.impl.HTMLAppendableTableImpl;
import org.opentox.toxotis.core.html.impl.HTMLContainerImpl;
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

    public HTMLContainer addParagraph(String text, Alignment align) {
        div.addComponent(new HTMLParagraphImpl(text).setAlignment(align));
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

    public HTMLContainer addSubSubSubHeading(String text) {
        div.addComponent(new HTMLTagImpl("h4", text));
        return div;
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
}
