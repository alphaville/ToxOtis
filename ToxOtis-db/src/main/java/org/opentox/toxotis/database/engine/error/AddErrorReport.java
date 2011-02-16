package org.opentox.toxotis.database.engine.error;

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
        try {
            stmt = getConnection().createStatement();
            ErrorReportBatchWriter writer = new ErrorReportBatchWriter(stmt, error);
            writer.batchStatement();
            int[] ints = stmt.executeBatch();
            int result = 0;
            for (int i : ints) {
                result += i;
            }
            return result;
        } catch (final SQLException ex) {
            logger.warn("failed to execute statement", ex);
            throw new DbException(ex);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (final SQLException ex) {
                logger.warn("failed to close statement", ex);
                throw new DbException(ex);
            } finally {
                close();
            }
        }

    }
}
