package org.opentox.toxotis.core.html.impl;

import java.util.ArrayList;
import org.opentox.toxotis.core.html.HTMLComponent;
import org.opentox.toxotis.core.html.HTMLTable;
import org.opentox.toxotis.core.html.HTMLTableRow;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class HTMLAppendableTableImpl extends HTMLExpandableComponentImpl implements HTMLTable {

    private int nRows;
    private final int nCols;
    private String summary;
    private int tableWidth;
    private int cellSpacing;
    private int cellPadding;
    private int tableBorder;
    private ArrayList<HTMLTableRow> data;
    private final int[] colWidths;
    private int cursor = 0;

    public HTMLAppendableTableImpl(int nCols) {
        this.nCols = nCols;
        data = new ArrayList<HTMLTableRow>();
        // add one row with no entries (all null)
        data.add(new HTMLTableRowFixedLength(nCols));
        colWidths = new int[nCols];
    }

    public HTMLAppendableTableImpl(int nCols, int rowEstimate) {
        this.nCols = nCols;
        this.nRows = rowEstimate;
        data = new ArrayList<HTMLTableRow>(nRows);
        // add nRows empty rows
        for (int index = 0; index < rowEstimate; index++) {
            data.add(new HTMLTableRowFixedLength(nCols));
        }
        colWidths = new int[nCols];
    }

    @Override
    public int getRows() {
        return data.size();
    }

    @Override
    public int getCols() {
        if (data == null || data.isEmpty()) {
            return 0;
        } else {
            return data.get(0).getNumColumns();
        }
    }

    @Override
    public String toString() {

        if (data.size() == cursor + 1 && data.get(cursor).isRowEmpty()) {
            data.remove(cursor);
        }
        StringBuilder builder = new StringBuilder();
        StringBuilder tableMetaData = new StringBuilder();
        if (tableWidth != 0) {
            tableMetaData.append(" width=\"" + tableWidth + "\"");
        }
        if (cellSpacing != 0) {
            tableMetaData.append(" cellspacing=\"" + cellSpacing + "\"");
        }
        if (tableBorder != 0) {
            tableMetaData.append(" border=\"" + tableBorder + "\"");
        }
        if (cellPadding != 0) {
            tableMetaData.append(" cellpadding=\"" + cellPadding + "\"");
        }
        if (summary != null) {
            tableMetaData.append(" summary=\"" + summary + "\"");
        }
        builder.append("<table");
        builder.append(tableMetaData);
        builder.append(">\n");
        builder.append("<tbody>\n");
        
        for (HTMLTableRow tr : data) {
            tr.setColumnWidths(colWidths);
            builder.append(tr.toString());
            
        }
        builder.append("</tbody>\n");
        builder.append("</table>\n");
        return builder.toString();
    }

    @Override
    public HTMLTable setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    @Override
    public HTMLTable setTableWidth(int tableWidth) {
        this.tableWidth = tableWidth;
        return this;
    }

    @Override
    public HTMLTable setCellSpacing(int cellSpacing) {
        this.cellSpacing = cellSpacing;
        return this;
    }

    @Override
    public HTMLTable setTableBorder(int tableBorder) {
        this.tableBorder = tableBorder;
        return this;
    }

    @Override
    public HTMLTable setCellPadding(int cellPadding) {
        this.cellPadding = cellPadding;
        return this;
    }

    @Override
    public HTMLTable setColWidth(int col, int width) {
        colWidths[col - 1] = width;
        return this;
    }

    @Override
    public HTMLTableRow currentRow() {
        if (cursor == data.size()) {
            data.add(new HTMLTableRowFixedLength(nCols));
        }
        return data.get(cursor);
    }

    @Override
    public HTMLTableRow nextRow() {
        if (cursor == 1) {
            HTMLTableRow cr = currentRow();
            cursor++;
            return cr;
        }
        cursor++;
        return currentRow();
    }

    @Override
    public HTMLTableRow previousRow() {
        previousRow();
        return currentRow();
    }

    @Override
    public HTMLTable setAtCursor(HTMLComponent entry) throws ArrayIndexOutOfBoundsException {
        boolean doMoveCursor = false;
        if (currentRow().isCursorLast()) {
            doMoveCursor = true;
        }
        currentRow().setAtCursor(entry);
        if (doMoveCursor) {
            nextRow();
        }
        return this;
    }

    @Override
    public HTMLTable setTextAtCursor(String entry) throws ArrayIndexOutOfBoundsException {
        return setAtCursor(new HTMLTextImpl(entry));
    }

 
}

