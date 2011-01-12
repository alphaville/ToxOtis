package org.opentox.toxotis.core.html.impl;

import org.opentox.toxotis.core.html.HTMLBody;
import org.opentox.toxotis.core.html.HTMLHead;
import org.opentox.toxotis.core.html.HTMLPage;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class HTMLPageImpl extends HTMLExpandableComponentImpl implements HTMLPage {

    private static final String xhtmlStrict = "<!DOCTYPE html PUBLIC \""
            + "-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
    private static final String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
    private static final String xmlNs = "http://www.w3.org/1999/xhtml";
    private HTMLHead head;
    private HTMLBody body;

    public HTMLPageImpl() {
        super();
        head = new HTMLHeadImpl();
        body = new HTMLBodyImpl();
    }

    @Override
    public void setHead(HTMLHead head) {
        this.head = head;
    }

    @Override
    public HTMLHead getHTMLHead() {
        return head;
    }

    @Override
    public HTMLPage setHtmlBody(HTMLBody body) {
        this.body = body;
        return this;
    }

    @Override
    public HTMLBody getHtmlBody() {
        return body;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(xml);
        builder.append("\n");
        builder.append(xhtmlStrict);
        builder.append("\n");
        String namespaceDeclaration = " xmlns=\""+xmlNs+"\"";
        builder.append("<html");
        builder.append(namespaceDeclaration);
        builder.append(">\n");
        if (head != null) {
            builder.append(head.toString());
        }
        if (body != null) {
            builder.append(body.toString());
        }
        builder.append("</html>");
        return builder.toString();
    }
}
