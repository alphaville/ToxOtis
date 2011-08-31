package org.opentox.toxotis.database.engine.error;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Deque;
import java.util.LinkedList;
import org.opentox.toxotis.core.component.ErrorReport;
import org.opentox.toxotis.database.DbOperation;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.ontology.MetaInfoBlobber;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ErrorReportBatchWriter extends DbOperation {

    private final Connection connection;
    private final ErrorReport error;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ErrorReportBatchWriter.class);
    private static final String __INSERT_META = "INSERT IGNORE INTO MetaInfo (id, meta) VALUES (?,compress(?))";
    private static final String __INSERT_COMPONENT = "INSERT INTO OTComponent (id,enabled,meta) VALUES (?,?,?)";
    private static final String __INSERT_ERROR_REPORT = "INSERT INTO ErrorReport "
            + "(id, httpStatus, actor, message, details,errorCause,errorCode) VALUES (?,?,?,?,?,?,?)";
    private PreparedStatement metaPS;
    private PreparedStatement componentPS;
    private PreparedStatement errorReportPS;

    public ErrorReportBatchWriter(final Connection connection, final ErrorReport error) {
        if (connection == null) {
            final String msg = "Programatic error: null connectio provided "
                    + "in the constructor of ErrorReportBatchWriter";
            final NullPointerException npe = new NullPointerException(msg);
            logger.error(msg, npe);
        }
        if (error == null) {
            final String msg = "Programatic error: null error report "
                    + "provided in the constructor of ErrorReportBatchWriter. "
                    + "Cannot register a null object in the database";
            final NullPointerException npe = new NullPointerException(msg);
            logger.error(msg, npe);
            throw npe;
        }
        if (error.getUri() == null || (error.getUri() != null
                && (error.getUri().toString() == null) || (error.getUri().toString() != null && error.getUri().toString().isEmpty()))) {
            final String msg = "Programatic error: No URI for the submitted error reprort (No Primary Key can be assigned)";
            final IllegalArgumentException iae = new IllegalArgumentException(msg);
            logger.warn(msg, iae);
            throw iae;
        }
        this.connection = connection;
        this.error = error;
    }

    @Override
    public String getSqlTemplate() {
        AssertionError aser = new AssertionError("Should not be invoked!");
        logger.error("ErrorReportBatchWriter#getSqlTemplate() invoked - throws assertion error", aser);
        throw aser;
    }

    public int batchStatement() throws DbException {
        int count = 0;
        Deque<ErrorReport> deque = new LinkedList<ErrorReport>();
        ErrorReport currentER = error;
        while (currentER != null) {
            deque.addFirst(currentER);
            currentER = currentER.getErrorCause();
        }
        for (ErrorReport er : deque) {
            try {
                count += batchStatement(er);
            } catch (SQLException ex) {
                final String msg = "Error report while writing data regarding error report in the database";
                logger.warn(msg, ex);
                throw new DbException(msg, ex);
            }
        }
        return count;
    }

    private int batchStatement(ErrorReport er) throws SQLException {
        int count = 0;
        if (er.getMeta() != null) {
            metaPS = connection.prepareStatement(__INSERT_META);
            metaPS.setInt(1, er.getMeta().hashCode());
            MetaInfoBlobber mib = new MetaInfoBlobber(er.getMeta());
            Blob miBlob = null;
            try {
                miBlob = mib.toBlob();
                metaPS.setBlob(2, miBlob);
            } catch (Exception ex) {
                logger.error("Exception while creating and setting meta-info BLOB", ex);
            }
            count += metaPS.executeUpdate();
        }

        componentPS = connection.prepareStatement(__INSERT_COMPONENT);
        componentPS.setString(1, er.getUri().getId());
        componentPS.setBoolean(2, er.isEnabled());
        if (er.getMeta() != null) {
            componentPS.setInt(3, er.getMeta().hashCode());
        } else {
            componentPS.setNull(3, Types.INTEGER);
        }
        count += componentPS.executeUpdate();

        errorReportPS = connection.prepareStatement(__INSERT_ERROR_REPORT);
        // (id, httpStatus, actor, message, details,errorCause,errorCode)

        errorReportPS.setString(1, er.getUri().getId());
        errorReportPS.setFloat(2, er.getHttpStatus());

        if (er.getActor() != null) {
            errorReportPS.setString(3, er.getActor());
        } else {
            errorReportPS.setNull(3, Types.VARCHAR);
        }

        if (er.getMessage() != null) {
            errorReportPS.setString(4, er.getMessage());
        } else {
            errorReportPS.setNull(4, Types.VARCHAR);
        }
        if (er.getDetails() != null) {
            errorReportPS.setString(5, er.getDetails());
        } else {
            errorReportPS.setNull(5, Types.VARCHAR);
        }
        if (er.getErrorCause() != null) {
            errorReportPS.setString(6, er.getErrorCause().getUri().getId());
        } else {
            errorReportPS.setNull(6, Types.VARCHAR);
        }

        errorReportPS.setString(7, er.getErrorCode());
        count += errorReportPS.executeUpdate();
        return count;

    }
}
