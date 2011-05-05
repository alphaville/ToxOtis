package org.opentox.toxotis.database.engine.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
public class ListUsers extends DbReader<String> {

    private Statement statement = null;
    private ResultSet results;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ListUsers.class);

    public enum ListUsersMode {

        BY_NAME("name"),
        BY_UID("uid");

        private ListUsersMode(String columnName) {
            this.columnName = columnName;
        }
        private String columnName;

        public String getColumnName() {
            return columnName;
        }
    }
    private ListUsersMode mode;

    public ListUsersMode getMode() {
        return mode;
    }

    public void setMode(ListUsersMode mode) {
        this.mode = mode;
    }

    @Override
    public IDbIterator<String> list() throws DbException {
        setTable("User");
        setTableColumns("User." + getMode().getColumnName());
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
