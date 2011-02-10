package org.opentox.toxotis.database;

import java.io.Closeable;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ResultSetIterator extends DbIterator<String> {

    private final ResultSet rs;

    public ResultSetIterator(final ResultSet rs) {
        this.rs = rs;
    }

    @Override
    public boolean hasNext() throws DbException{
        try {
            return rs.next();
        } catch (final SQLException ex) {
            throw new DbException(ex);
        }
    }

    @Override
    public String next() throws DbException{
        try {
            return rs.getString(1);
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
    public void close() throws DbException {
        try {
            this.rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(ResultSetIterator.class.getName()).log(Level.SEVERE, null, ex);
            throw new DbException(ex);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if (rs != null) {
            rs.close();
        }
        super.finalize();
    }
}
