package org.opentox.toxotis.core.html;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface HTMLParagraph extends HTMLExpandableComponent {

    HTMLParagraph addText(HTMLText text);
    HTMLParagraph setAlignment(Alignment align);
    Alignment getAlignment();

}
