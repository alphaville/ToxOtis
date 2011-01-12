package org.opentox.toxotis.core.html;

/**
 * A paragraph of formatted HTML text. A paragraph is descibed here by its content
 * (i.e. formatted text as an instance of {@link HTMLText } and its {@link Alignment } ).
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface HTMLParagraph extends HTMLExpandableComponent {

    HTMLParagraph addText(HTMLText text);

    HTMLParagraph setAlignment(Alignment align);

    Alignment getAlignment();
}
