package org.opentox.toxotis.core.html;

/**
 * Header of an HTML document.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface HTMLHead {

    HTMLHead setCssUrl(java.net.URL url);

    java.net.URL getCssUrl();

    HTMLHead setDoRefresh(boolean doRegresh, int secondsDelay);

    HTMLHead setDoRedirect(java.net.URL url);

    HTMLHead setTitle(String title);

    String getTitle();

    HTMLHead setAuthor(String author);

    String getAuthor();
}
