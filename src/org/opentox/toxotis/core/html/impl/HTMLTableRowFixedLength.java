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
