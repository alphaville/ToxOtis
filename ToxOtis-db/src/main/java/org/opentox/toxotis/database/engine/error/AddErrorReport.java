package org.opentox.toxotis.database.engine.error;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            for (int i : ints){
                result += i;
            }
            return result;
        } catch (SQLException ex) {
            throw new DbException(ex);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(AddErrorReport.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            close();
        }

    }
}
