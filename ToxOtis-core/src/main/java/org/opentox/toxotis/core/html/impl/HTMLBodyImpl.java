package org.opentox.toxotis.core.html.impl;

import org.opentox.toxotis.core.html.HTMLBody;
import org.opentox.toxotis.core.html.HTMLComponent;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class HTMLBodyImpl  extends HTMLExpandableComponentImpl implements HTMLBody{

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<body>\n");
        for (HTMLComponent component : componentList){
            builder.append(component.toString());
            builder.append("\n");
        }
        builder.append("</body>\n");
        return builder.toString();
    }


}