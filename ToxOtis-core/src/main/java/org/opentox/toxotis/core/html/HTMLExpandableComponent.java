package org.opentox.toxotis.core.html;

import java.util.ArrayList;

/**
 * A component that can include other HTML components in it like an HTML table. Following the
 * structure of an HTML document where tags can be nested in any prefered way and exploiting
 * Java's ability for a similar encapsulation, this interface defines the set of HTML entities
 * that can be nested.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface  HTMLExpandableComponent extends HTMLComponent {

    /**
     * Add an HTMLComponent object to the current expandable and modifiable
     * HTML component.
     * @param component
     *      The HTMLComponent to be added to the current component.
     */
    void addComponent(HTMLComponent component);

    /**
     * A list of all
     * @return
     *      A list of all HTMLComponent object wrapped with the current object.
     *      Returns <code>null</code> if the component does not include such a list and
     *      an empty list if the currect object has been initialized but no elements
     *      have been added in the list.
     */
    ArrayList<HTMLComponent> getComponents();

}