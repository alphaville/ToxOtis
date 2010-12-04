package org.opentox.toxotis.core.html;

import java.util.ArrayList;

/**
 * A component that can include other HTML components in it like an HTML table.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface  HTMLExpandableComponent extends HTMLComponent {

    void addComponent(HTMLComponent component);

    ArrayList<HTMLComponent> getComponents();

}