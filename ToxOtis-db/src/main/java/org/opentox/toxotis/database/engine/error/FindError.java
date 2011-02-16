package org.opentox.toxotis.database.engine.error;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.ErrorReport;
import org.opentox.toxotis.database.DbReader;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class FindError extends DbReader<ErrorReport> {

    private final VRI baseUri;
    private Statement statement = null;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(FindError.class);

    public FindError(final VRI baseUri) {
        this.baseUri = baseUri;
    }

    @Override
    public IDbIterator<ErrorReport> list() throws DbException {
        setTable("ErrorReport");
        setTableColumns("id", "httpStatus", "actor", "message", "details", "errorCode", "errorCause");
        statement = null;
        Connection connection = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(getSql());
            ErrorIterator it = new ErrorIterator(rs, baseUri);
            return it;
        } catch (SQLException ex) {
            throw new DbException(ex);
        } finally {
            // Do Nothing:  The client is expected to close the statement and the connection
        }
    }

    @Override
    public void close() throws DbException {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException ex) {
                throw new DbException(ex);
            }
        }
        super.close();
    }
}
