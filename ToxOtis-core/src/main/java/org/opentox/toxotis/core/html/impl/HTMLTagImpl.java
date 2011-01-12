package org.opentox.toxotis.core.html.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.opentox.toxotis.core.html.HTMLComponent;
import org.opentox.toxotis.core.html.HTMLTag;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class HTMLTagImpl extends HTMLExpandableComponentImpl implements HTMLTag {

    private String tag;
    private Map<String, String> tagAttributes = new HashMap<String, String>();

    public HTMLTagImpl(String tag) {
        this.tag = tag;
    }

    public HTMLTagImpl(String tag, String content) {
        this.tag = tag;
        setContent(content);
    }

    public HTMLTagImpl(String tag, HTMLComponent content) {
        this.tag = tag;
        addComponent(content);
    }

    @Override
    public HTMLTag setTag(String tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public HTMLTag addTagAttribute(String attribute, String value) {
        tagAttributes.put(attribute, value);
        return this;
    }

    public HTMLTag setContent(String content) {
        addComponent(new HTMLTextImpl(content));
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        StringBuilder attributesBuilder = null;
        if (!tagAttributes.isEmpty()) {
            Iterator<Entry<String, String>> attIt = tagAttributes.entrySet().iterator();
            attributesBuilder = new StringBuilder();
            while (attIt.hasNext()) {
                Entry<String, String> entry = attIt.next();
                attributesBuilder.append(" ");
                attributesBuilder.append(entry.getKey());
                attributesBuilder.append("=");
                attributesBuilder.append("\"" + entry.getValue() + "\"");
            }

        }

        String attributesString = attributesBuilder != null
                ? new String(attributesBuilder) : new String();
        builder.append("<" + tag + attributesString + ">");
        for (HTMLComponent component : componentList) {
            builder.append(component.toString());
        }
        builder.append("</" + tag + ">");
        return builder.toString();
    }


}
