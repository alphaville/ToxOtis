package org.opentox.toxotis.database.engine.parameter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.opentox.toxotis.database.DbReader;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.ResultSetIterator;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ListParameter extends DbReader<String> {

    private Statement statement = null;
    private ResultSet results;

    @Override
    public IDbIterator<String> list() throws DbException {
        setTable("Parameter");
        setTableColumns("Parameter.id");
        Connection connection = null;
        connection = getConnection();
        try {
            statement = connection.createStatement();
            results = statement.executeQuery(getSql());
            return new ResultSetIterator(results);
        } catch (final SQLException ex) {
            throw new DbException(ex);
        }
    }

    @Override
    public void close() throws DbException {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException ex) {
            throw new DbException(ex);
        } finally {
            super.close();
        }
    }
}
