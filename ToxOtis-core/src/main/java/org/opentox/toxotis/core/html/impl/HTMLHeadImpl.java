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
            builder.append("<meta name=\"AUTHOR\" content=\"").append(author).append("\" />\n");
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
