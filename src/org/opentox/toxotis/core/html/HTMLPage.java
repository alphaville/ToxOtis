package org.opentox.toxotis.core.html;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface HTMLPage  {

    void setHead(HTMLHead header);

    HTMLHead getHTMLHead();

    void setHtmlBody(HTMLBody body);

    HTMLBody getHtmlBody();
}
