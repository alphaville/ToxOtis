package org.opentox.toxotis.core.html;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface HTMLTable extends HTMLExpandableComponent {

    int getRows();

    int getCols();

    HTMLTable setAtCursor(HTMLComponent entry) throws ArrayIndexOutOfBoundsException;

    HTMLTable setTextAtCursor(String entry) throws ArrayIndexOutOfBoundsException;

    HTMLTableRow currentRow();

    HTMLTableRow nextRow();

    HTMLTableRow previousRow();


    HTMLTable setSummary(String summary);

    HTMLTable setTableWidth(int tableWidth);

    HTMLTable setCellSpacing(int cellSpacing);

    HTMLTable setTableBorder(int tableBorder);

    HTMLTable setCellPadding(int cellPadding);

    HTMLTable setColWidth(int col, int width);
}
