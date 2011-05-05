package org.opentox.toxotis.database;

import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class DbWriter extends DbOperation  {

    private InsertType insertType = InsertType.INSERT;
    private String[] tableColumns;
    private String table;

    @Override
    public String getSqlTemplate() {
        return "%s INTO %s (%s) VALUES (%s)";
    }

    public enum InsertType {

        INSERT("INSERT"),
        INSERT_IGNORE("INSERT IGNORE"),
        REPLACE("REPLACE");
        private String type;

        private InsertType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return getType();
        }
    }

    public InsertType getInsertType() {
        return insertType;
    }

    public void setInsertType(InsertType insertType) {
        this.insertType = insertType;
    }

    protected void setTableColumns(String... columns) {
        int length = columns.length;
        tableColumns = new String[length];
        int index = 0;
        for (String col : columns) {
            tableColumns[index] = col;
            index++;
        }
    }

    protected  String getTable() {
        return table;
    }

    protected void setTable(String table) {
        this.table = table;
    }

    protected String getSql() {
        StringBuilder tableColumnsString = new StringBuilder();
        StringBuilder questionMarks = new StringBuilder();
        for (int i = 0; i < tableColumns.length; i++) {
            tableColumnsString.append(tableColumns[i]);
            questionMarks.append("?");
            if (i != tableColumns.length - 1) {
                tableColumnsString.append(",");
                questionMarks.append(",");
            }
        }
        return String.format(getSqlTemplate(), getInsertType().toString(), table, tableColumnsString.toString(), questionMarks.toString());
    }

    public abstract int write() throws DbException;
}
