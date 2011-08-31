package org.opentox.toxotis.database.engine.error;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.opentox.toxotis.core.component.ErrorReport;
import org.opentox.toxotis.database.DbWriter;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class AddErrorReport extends DbWriter {

    private final ErrorReport error;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AddErrorReport.class);

    public AddErrorReport(final ErrorReport error) {
        this.error = error;
    }

    @Override
    public int write() throws DbException {
        Statement stmt = null;
        Connection connection = getConnection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException ex) {
            throw new RuntimeException("SQLException happened while applying the method .setAutoCommit(boolean) on a fresh "
                    + "connection. This most probably means that the connection is closed.", ex);
        }
        try {
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            ErrorReportBatchWriter writer = new ErrorReportBatchWriter(connection, error);
            int result = writer.batchStatement();
            connection.commit();          
            return result;
        } catch (final SQLException ex) {
            final String msg = "failed to execute statement";
            logger.warn(msg, ex);
            if (connection != null) {
                try {
                    logger.info("Rolling back");
                    connection.rollback();
                } catch (final SQLException rollBackException) {
                    final String m1 = "Rolling back failed";
                    logger.error(m1);
                    throw new DbException(m1, rollBackException);
                }
            }
            throw new DbException(msg, ex);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (final SQLException ex) {
                final String msg = "failed to close statement";
                logger.warn(msg, ex);
                throw new DbException(msg, ex);
            } finally {
                close();
            }
        }

    }
}
