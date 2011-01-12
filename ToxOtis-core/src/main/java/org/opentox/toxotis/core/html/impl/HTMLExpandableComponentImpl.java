package org.opentox.toxotis.core.html.impl;

import java.util.ArrayList;
import org.opentox.toxotis.core.html.HTMLComponent;
import org.opentox.toxotis.core.html.HTMLExpandableComponent;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class HTMLExpandableComponentImpl implements HTMLExpandableComponent {

    protected ArrayList<HTMLComponent> componentList = new ArrayList<HTMLComponent>();

    public HTMLExpandableComponentImpl() {
    }

    @Override
    public void addComponent(HTMLComponent component) {
        componentList.add(component);
    }

    @Override
    public ArrayList<HTMLComponent> getComponents() {
        return componentList;
    }
    

}