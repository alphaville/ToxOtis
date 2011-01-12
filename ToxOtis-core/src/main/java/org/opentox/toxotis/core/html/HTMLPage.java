package org.opentox.toxotis.core.html;

/**
 * A page in an HTML document. A page consists of the {@link HTMLHead header} and
 * the {@link HTMLBody body} of the document.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface HTMLPage {

    /**
     * Set the header of the HTML page.
     * @param header
     *      Header of the page as an instance of {@link HTMLHead }.
     */
    void setHead(HTMLHead header);

    /**
     * Getter method for the HTML Head of the HTML page.
     * @return
     *      Returns the HTML head of the page.
     */
    HTMLHead getHTMLHead();

    /**
     *
     * @param body
     */
    HTMLPage setHtmlBody(HTMLBody body);

    HTMLBody getHtmlBody();
}
