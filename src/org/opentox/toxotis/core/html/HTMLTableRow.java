package org.opentox.toxotis.core.html;

/**
 * A row of an HTML table.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface HTMLTableRow {

    int getNumColumns();

    HTMLTableRow cursorUp();

    HTMLTableRow cursorDown();

    HTMLTableRow moveCursorAt(int i) throws ArrayIndexOutOfBoundsException;

    /**
     *
     * @param index
     *      Index starts from <b>1</b>.
     * @return
     *      The current modifiable HTML component.
     */
    HTMLComponent get(int index);

    HTMLComponent getCurrent();

    HTMLTableRow setAtCursor(HTMLComponent component);

    HTMLTableRow setCurrentColumnWidth(int width);

    HTMLTableRow setColumnWidth(int column, int width);

    HTMLTableRow setColumnWidths(int... widths);

    boolean isCursorLast();

    boolean isCursorFirst();

    int cursorPosition();

    boolean isRowEmpty();
}

