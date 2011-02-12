package org.opentox.toxotis.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class DbIterator<T> implements IDbIterator<T> {

    protected final ResultSet rs;

    public DbIterator(final ResultSet rs) {
        this.rs = rs;
    }

    @Override
    public boolean hasNext() throws DbException {
        try {
            return rs.next();
        } catch (SQLException ex) {
            throw new DbException(ex);
        }
    }

    @Override
    public void close() throws DbException {
        try {
            rs.close();
        } catch (SQLException ex) {
            throw new DbException(ex);
        }
    }

    @Override
    public void remove() throws DbException{
        try {
            rs.deleteRow();
        } catch (final SQLException ex) {
            throw new DbException(ex);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
}
