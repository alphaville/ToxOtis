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

/**
 * A row of an HTML table.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface HTMLTableRow {

    /**
     * Number of columns.
     * @return 
     *      The number of columns of this row.
     */
    int getNumColumns();

    /**
     * Moves the cursor one position up.
     * 
     * @return 
     *      Updated HTML table row.
     */
    HTMLTableRow cursorUp();

    /**
     * Moves the cursor one position down.
     * 
     * @return 
     *      Updated HTML table row.
     */
    HTMLTableRow cursorDown();

    /**
     * Moves the cursor at some defined position.
     * 
     * @param i 
     *      Where to move the cursor to.
     * 
     * @return 
     *      Updated HTML table row.
     */
    HTMLTableRow moveCursorAt(int i);

    /**
     *
     * @param index
     *      Index starts from <b>1</b>.
     * @return
     *      The current modifiable HTML component.
     */
    HTMLComponent get(int index);

    /**
     * Get the current HTML component at the position of the 
     * cursor.
     * 
     * @return 
     *      The current HTML component.
     */
    HTMLComponent getCurrent();

    /**
     * Set a component at the position of the cursor.
     * @param component
     *      An HTML component to be added to the table row.
     * @return 
     *      Updated HTML table row.
     */
    HTMLTableRow setAtCursor(HTMLComponent component);

    /**
     * Set the width of the current column (where the cursor is).
     * @param width
     *      Column width in pixels.
     * @return 
     *      Updated HTML table row.
     */
    HTMLTableRow setCurrentColumnWidth(int width);

    /**
     * Set the width of a particular column.
     * @param column
     *      Index of the column. First is <code>0</code>.
     * @param width
     *      Width in pixels.
     * @return 
     *      Updated HTMLTableRow.
     */
    HTMLTableRow setColumnWidth(int column, int width);

    /**
     * Specify the widths of the columns.
     * @param widths
     *      Integer array with the widths of the columns.
     * @return 
     *      Updated instance of HTMLTableRow.
     */
    HTMLTableRow setColumnWidths(int... widths);

    /**
     * Whether the cursor is at the final position.
     * @return 
     *      <code>true</code> if the cursor cannot move any further and 
     *      <code>false</code> otherwise.
     */
    boolean isCursorLast();

    /**
     * Whether the cursor is at the beginning (initial position)
     * @return 
     *      <code>true</code> if the cursor is at the starting position
     *      and <code>false</code> otherwise.
     */
    boolean isCursorFirst();

    /**
     * The position of the cursor.
     */
    int cursorPosition();

    /**
     * Whether the row is empty.
     * 
     * @return 
     *       <code>true</code> if the row is empty and <code>false</code> otherwise.
     */
    boolean isRowEmpty();
}
