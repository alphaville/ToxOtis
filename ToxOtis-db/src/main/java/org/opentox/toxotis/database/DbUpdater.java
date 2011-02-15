package org.opentox.toxotis.database;

import org.opentox.toxotis.database.exception.DbException;

/**
 * Updates an already existing object in the database
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract  class DbUpdater extends DbOperation {

    private static final String WHERE_TEMPLATE = "WHERE %s";
    private String table;

    public DbUpdater() {
        super();
    }

    @Override
    public String getSqlTemplate() {
        String SQL_TEMPLATE = "UPDATE % SET % %";
        return SQL_TEMPLATE;
    }

    public abstract int update() throws DbException;
    

}
