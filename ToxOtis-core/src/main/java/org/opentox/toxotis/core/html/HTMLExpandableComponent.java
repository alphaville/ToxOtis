/*
 *
 * ToxOtis
 *
 * ToxOtis is the Greek word for Sagittarius, that actually means ‘archer’. ToxOtis
 * is a Java interface to the predictive toxicology services of OpenTox. ToxOtis is
 * being developed to help both those who need a painless way to consume OpenTox
 * services and for ambitious service providers that don’t want to spend half of
 * their time in RDF parsing and creation.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 *
 */
package org.opentox.toxotis.core.html;

import java.util.List;

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
     *      an empty list if the current object has been initialized but no elements
     *      have been added in the list.
     */
    List<HTMLComponent> getComponents();

}