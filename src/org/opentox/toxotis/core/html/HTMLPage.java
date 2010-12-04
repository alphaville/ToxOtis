package org.opentox.toxotis.core.html;

/**
 * A page in an HTML document. A page consists of the {@link HTMLHead header} and
 * the {@link HTMLBody body} of the document.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface HTMLPage  {

    void setHead(HTMLHead header);

    HTMLHead getHTMLHead();

    void setHtmlBody(HTMLBody body);

    HTMLBody getHtmlBody();
}
