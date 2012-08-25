package org.opentox.toxotis.core.html.impl;

import org.opentox.toxotis.core.html.HTMLComponent;

/**
 *
 * @author chung
 */
public class HTMLLink extends HTMLTagImpl {

    private static final String TAG = "a", HREF = "href";

    public HTMLLink(HTMLComponent content) {
        super(TAG, content);
    }

    public HTMLLink(String content) {
        super(TAG, content);
    }

    public HTMLLink(String link, String text) {
        super(TAG, text);
        addTagAttribute(HREF, link);
    }

    public HTMLLink() {
        super(TAG);
    }

    public HTMLLink setHref(String link) {
        addTagAttribute(HREF, link);
        return this;
    }
}
