package org.opentox.toxotis.core.html;

/**
 * Arbitrary user-defined HTML tag. Can be used to define a-tags or other.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface HTMLTag extends HTMLExpandableComponent {

    HTMLTag setTag(String tag);

    String getTag();

    HTMLTag addTagAttribute(String attribute, String value);

    HTMLTag setContent(String content);
}
