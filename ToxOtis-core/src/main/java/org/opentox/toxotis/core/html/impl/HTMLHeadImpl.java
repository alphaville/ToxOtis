package org.opentox.toxotis.core.html.impl;

import java.net.URL;
import org.opentox.toxotis.core.html.HTMLHead;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class HTMLHeadImpl implements HTMLHead {

    private java.net.URL cssUrl = null;
    private java.net.URL redirectUrl = null;
    private boolean doRefresh = false;
    private int refreshSecondsDelay = 1;
    private String title;
    private String author;

    public HTMLHeadImpl() {
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public HTMLHead setAuthor(String author) {
        this.author = author;
        return this;
    }

    @Override
    public HTMLHead setCssUrl(URL url) {
        this.cssUrl = url;
        return this;
    }

    @Override
    public URL getCssUrl() {
        return cssUrl;
    }

    @Override
    public HTMLHead setDoRefresh(boolean doRegresh, int secondsDelay) {
        this.doRefresh = doRegresh;
        this.refreshSecondsDelay = secondsDelay;
        return this;
    }

    @Override
    public HTMLHead setDoRedirect(URL url) {
        this.redirectUrl = url;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<head>\n");
        if (title != null) {
            builder.append("<title>");
            builder.append(title);
            builder.append("</title>\n");
        }
        if (author != null) {
            builder.append("<meta name=\"AUTHOR\" content=\"" + author + "\" />\n");
        }
        builder.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n");
        builder.append("</head>\n");
        return builder.toString();
    }

    @Override
    public HTMLHead setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
