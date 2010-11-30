package org.opentox.toxotis.core.html;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface HTMLContainer extends HTMLExpandableComponent {

    HTMLContainer setDivClass(String divClass);

    HTMLContainer setId(String id);

    HTMLContainer setAlignment(Alignment align);

    HTMLContainer breakLine();

    HTMLContainer horizontalSeparator();
}
