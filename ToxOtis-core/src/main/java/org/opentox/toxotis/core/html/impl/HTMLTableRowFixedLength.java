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
package org.opentox.toxotis.core.html.impl;

import org.opentox.toxotis.core.html.HTMLComponent;
import org.opentox.toxotis.core.html.HTMLTableRow;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class HTMLTableRowFixedLength implements HTMLTableRow {

    private final int nCols;
    private HTMLComponent[] rowData;
    private int[] columnWidths;
    private int cursor;

    public HTMLTableRowFixedLength(int nCols) {
        this.nCols = nCols;
        rowData = new HTMLComponent[nCols];
        columnWidths = new int[nCols];
        cursor = 0;
    }

    @Override
    public int getNumColumns() {
        return nCols;
    }

    @Override
    public HTMLComponent get(int index) {
        return rowData[index - 1];
    }

    @Override
    public HTMLTableRow cursorUp() {
        cursor = Math.min(nCols, cursor + 1);
        return this;
    }

    @Override
    public HTMLTableRow cursorDown() {
        cursor = Math.max(0, cursor - 1);
        return this;
    }

    @Override
    public HTMLTableRow moveCursorAt(int i) throws ArrayIndexOutOfBoundsException {
        if (i < 1 || i > nCols) {
            throw new ArrayIndexOutOfBoundsException();
        }
        cursor = i - 1;
        return this;
    }

    @Override
    public HTMLComponent getCurrent() {
        return rowData[cursor];
    }

    @Override
    public HTMLTableRow setAtCursor(HTMLComponent component) {
        rowData[cursor] = component;
        cursorUp();
        return this;
    }

    @Override
    public HTMLTableRow setCurrentColumnWidth(int width) {
        columnWidths[cursor] = width;
        return this;
    }

    @Override
    public HTMLTableRow setColumnWidth(int column, int width) {
        columnWidths[column] = width;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<tr>\n");
        int i = 0;
        for (HTMLComponent component : rowData) {
            String colWidthData = "";
            if (columnWidths[i] != 0) {
                colWidthData = " width=\"" + columnWidths[i] + "\"";
            }
            builder.append("<td");
            builder.append(colWidthData);
            builder.append(">");
            if (component != null) {
                builder.append(component.toString());
            } else {
                builder.append("");
            }
            builder.append("</td>\n");
            i++;
        }
        builder.append("</tr>\n");
        
        return builder.toString();
    }

    @Override
    public boolean isCursorLast() {
        return (cursor == nCols - 1);
    }

    @Override
    public boolean isCursorFirst() {
        return cursor == 0;
    }

    @Override
    public int cursorPosition() {
        return cursor;
    }

    @Override
    public boolean isRowEmpty() {
        if (rowData==null){
            return true;
        }
        for (HTMLComponent c : rowData){
            if (c!=null){
                return false;
            }
        }
        return true;
    }

    @Override
    public HTMLTableRow setColumnWidths(int... widths) {
        this.columnWidths = widths;
        return this;
    }
}
