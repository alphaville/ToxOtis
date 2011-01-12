package org.opentox.toxotis.core.html;

/**
 * A <code>div</code> container for HTML content.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface HTMLContainer extends HTMLExpandableComponent {

    /**
     * Specify the class for the HTML container.
     * @param divClass
     *      Value for the <code>class</code> argument in the <code>div</code>
     *      tag.
     * @return
     *      This HTML container
     */
    HTMLContainer setDivClass(String divClass);

    HTMLContainer setId(String id);

    HTMLContainer setAlignment(Alignment align);

    HTMLContainer breakLine();

    HTMLContainer horizontalSeparator();
}
