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
 * A table in an HTML document.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface HTMLTable extends HTMLExpandableComponent {

    /**
     * Number of rows.
     * @return 
     *      number of rows.
     */
    int getRows();

    /**
     * Number of columns.
     * @return 
     *      number of columns.
     */
    int getCols();

    /**
     * Set an HTML components exactly at the position of the cursor.
     * @param entry
     *      The HTML component to be added.
     * @return
     *      Updated HTML Table.
     * @throws ArrayIndexOutOfBoundsException 
     *      In case the table has no more space and the cursor has
     *      or move off the predefined bounds.
     */
    HTMLTable setAtCursor(HTMLComponent entry);

    /**
     * Set some text exactly at the position of the cursor.
     * @param entry
     *      The text to be added.
     * @return
     *      Updated HTML Table.
     * @throws ArrayIndexOutOfBoundsException 
     *      In case the table has no more space and the cursor has
     *      or move off the predefined bounds.
     */
    HTMLTable setTextAtCursor(String entry);

    /**
     * The current row where the cursor points.
     * @return
     *      The current row.
     */
    HTMLTableRow currentRow();

    /**
     * Moves the cursor to the next row.
     * @return 
     *      The next row.
     */
    HTMLTableRow nextRow();

    /**
     * Moves the row-cursor back to the previous row and returns
     * the previous row.
     * @return 
     *      The previous row.
     */
    HTMLTableRow previousRow();

    /**
     * Set a summary for the HTML table.
     * @param summary
     *      The summary as String.
     * @return 
     *     Updated HTML table.
     */
    HTMLTable setSummary(String summary);

    /**
     * Set the overall width of the table.
     * @param tableWidth
     *      Width of the table in pixels.
     * @return 
     *      The updated table.
     */
    HTMLTable setTableWidth(int tableWidth);

    /**
     * Set the cell-spacing if the table in pixels.
     * @param cellSpacing
     *      The cell-to-cell spacing.
     * @return 
     *      The updated table.
     */
    HTMLTable setCellSpacing(int cellSpacing);

    /**
     * Set the thickness of the table border. By default
     * this is set to 0.
     * 
     * @param tableBorder
     *      The table border thickness.
     * 
     * @return 
     *      The updated HTML table.
     */
    HTMLTable setTableBorder(int tableBorder);

    /**
     * Set the cell padding distance.
     * @param cellPadding
     *      The cell padding in pixels.
     * @return 
     *      The current modifiable instance of HTML table with updated
     *      cell padding.
     */
    HTMLTable setCellPadding(int cellPadding);

    /**
     * Set the width of a particular column.
     * @param col
     *      Index of a column.
     * @param width
     *      Prescribed width for the column in pixels.
     * @return 
     *      Updated table.
     */
    HTMLTable setColWidth(int col, int width);
    
    /**
     * Set the style for the table
     * @param style
     *      the css for the style tag
     * @return 
     *      Updated table.
     */
    HTMLTable setStyle(String style);
}
