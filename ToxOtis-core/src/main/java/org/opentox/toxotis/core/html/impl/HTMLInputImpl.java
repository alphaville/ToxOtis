package org.opentox.toxotis.core.html.impl;

import org.opentox.toxotis.core.html.HTMLInput;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class HTMLInputImpl implements HTMLInput {

    private HTMLInputType type;
    private String name;
    private String value;
    private int size = -1;
    private int maxLength = -1;

    @Override
    public HTMLInput setType(HTMLInputType type) {
        this.type = type;
        return this;
    }

    @Override
    public HTMLInput setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public HTMLInput setSize(int size) {
        this.size = size;
        return this;
    }

    @Override
    public HTMLInput setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    @Override
    public HTMLInput setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<input");
        if (name!=null){
            builder.append(" name=\"");
            builder.append(name);
            builder.append("\"");
        }
        if (value!=null){
            builder.append(" value=\"");
            builder.append(value);
            builder.append("\"");
        }
        if (type!=null){
            builder.append(" type=\"");
            builder.append(type.toString());
            builder.append("\"");
        }
        if (size>0){
            builder.append(" size=\"");
            builder.append(size);
            builder.append("\"");
        }
        if (maxLength>0){
            builder.append(" maxLength=\"");
            builder.append(maxLength);
            builder.append("\"");
        }
        builder.append(">");
        return builder.toString();
    }

    
}
