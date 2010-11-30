package org.opentox.toxotis.core.html;

import java.util.ArrayList;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface  HTMLExpandableComponent extends HTMLComponent {

    void addComponent(HTMLComponent component);

    ArrayList<HTMLComponent> getComponents();

}