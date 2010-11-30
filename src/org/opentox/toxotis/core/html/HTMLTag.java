package org.opentox.toxotis.core.html;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface HTMLTag extends HTMLExpandableComponent {

    public void setTag(String tag);

    public String getTag();

    public HTMLTag addTagAttribute(String attribute, String value);
}
