package org.opentox.toxotis.core.html;

/**
 * Arbitrary user-defined HTML tag. Can be used to define a-tags or other.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface HTMLTag extends HTMLExpandableComponent {

    public void setTag(String tag);

    public String getTag();

    public HTMLTag addTagAttribute(String attribute, String value);
}
