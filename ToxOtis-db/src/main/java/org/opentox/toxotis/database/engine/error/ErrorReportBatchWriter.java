package org.opentox.toxotis.database.engine.error;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Deque;
import java.util.LinkedList;
import org.opentox.toxotis.core.component.ErrorReport;
import org.opentox.toxotis.database.DbOperation;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ErrorReportBatchWriter extends DbOperation {

    private final Statement stmt;
    private final ErrorReport error;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ErrorReportBatchWriter.class);

    public ErrorReportBatchWriter(final Statement stmt, final ErrorReport error) {
        this.stmt = stmt;
        this.error = error;
    }

    @Override
    public String getSqlTemplate() {
        throw new AssertionError("Should not be invoked!");
    }

    public void batchStatement() throws DbException {
        Deque<ErrorReport> deque = new LinkedList<ErrorReport>();
        ErrorReport currentER = error;
        while (currentER != null) {
            deque.addFirst(currentER);
            currentER = currentER.getErrorCause();
        }
        for (ErrorReport er : deque) {
            try {
                batchStatement(er);
            } catch (SQLException ex) {
                throw new DbException(ex);
            }
        }
    }

    private void batchStatement(ErrorReport er) throws SQLException {
        String preInsertStmt = "INSERT INTO OTComponent (id) VALUES (%s)";
        String I1 = String.format(preInsertStmt, "'" + er.getUri().getId() + "'");
        String insertStmt = "INSERT INTO ErrorReport (id, httpStatus, actor, message, details,errorCause) VALUES (%s)";
        StringBuilder values = new StringBuilder();
        values.append("'");
        values.append(er.getUri().getId());
        values.append("'");
        values.append(", ");
        values.append("'");
        values.append(er.getHttpStatus());
        values.append("'");
        values.append(", ");
        if (er.getActor() != null) {
            values.append("'");
            values.append(er.getActor());
            values.append("'");
        } else {
            values.append("NULL");
        }
        values.append(", ");
        if (er.getMessage() != null) {
            values.append("'");
            values.append(er.getMessage());
            values.append("'");
        } else {
            values.append("NULL");
        }
        values.append(", ");
        if (er.getDetails() != null) {
            values.append("'");
            values.append(er.getDetails());
            values.append("'");
        } else {
            values.append("NULL");
        }
        values.append(", ");
        if (er.getErrorCause() != null) {
            values.append("'");
            values.append(er.getErrorCause().getUri().getId());
            values.append("'");
        } else {
            values.append("NULL");
        }
        String I2 = String.format(insertStmt, values);
        stmt.addBatch(I1);
        stmt.addBatch(I2);
    }
}
