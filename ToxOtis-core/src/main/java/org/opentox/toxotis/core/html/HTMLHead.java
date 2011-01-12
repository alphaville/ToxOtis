package org.opentox.toxotis.core.html;

/**
 * Header of an HTML document.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface HTMLHead {

    /**
     * Set the URL where a CSS document can be found thus defining the style of
     * the HTML document. The CSS document has to be online for the HTML document
     * to access it.
     *
     * @param url
     *      URL of the CSS document.
     * @return
     *      The current modifiable HTMLHead object.
     */
    HTMLHead setCssUrl(java.net.URL url);

    /**
     * Returns the URL of the CSS document related to the current HTML document.
     * @return
     *      URL of CSS document or <code>null</code> if not any.
     */
    java.net.URL getCssUrl();

    /**
     * Set a refresh rate for the HTML page. The HTML document, through the browser,
     * will refresh itseld every some time.
     *
     * @param doRegresh
     *      Whether the page should refresh itself.
     * @param secondsDelay
     *      Delay between successive refreshes.
     * @return
     *      The current modifiable HTMLHead object.
     */
    HTMLHead setDoRefresh(boolean doRegresh, int secondsDelay);

    /**
     * If this page is a redirection to another URI, set the URL to redirect to.
     * If <code>null</code>, no redirection will happen.
     *
     * @param url
     *      The URL to redirect to.
     * @return
     *      The current modifiable HTMLHead object.
     */
    HTMLHead setDoRedirect(java.net.URL url);

    /**
     * Setter for the title of the HTML document, which is inccluded in the &lt;head&gt;
     * section of the document.
     * @param title
     *      The title of the HTML document.
     * @return
     *      The current modifiable HTMLHead object.
     */
    HTMLHead setTitle(String title);

    /**
     * Getter method for the title of the HTML document.
     * @return
     *      The title of the current HTMLHead object.
     */
    String getTitle();

    /**
     * Specify the author(s) of the current HTML document.
     * @param author
     *      Author of HTML document.
     * @return
     *      The current modifiable HTMLHead object.
     */
    HTMLHead setAuthor(String author);

    /**
     * Getter method for the authtor(s) of the current HTML document
     * @return
     *      The author(s) of this HTML document, as specified in the header.
     */
    String getAuthor();
}
