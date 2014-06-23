package org.opentox.toxotis.database.engine.error;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.ErrorReport;
import org.opentox.toxotis.database.DbIterator;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.MetaInfoDeblobber;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ErrorIterator extends DbIterator<ErrorReport> {

    private final VRI baseUri;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ErrorIterator.class);
    private boolean retrieveStackTrace = true;

    public boolean isRetrieveStackTrace() {
        return retrieveStackTrace;
    }

    public void setRetrieveStackTrace(boolean retrieveStackTrace) {
        this.retrieveStackTrace = retrieveStackTrace;
    }

    public ErrorIterator(final ResultSet rs, final VRI baseUri) {
        super(rs);
        this.baseUri = baseUri;
    }

    @Override
    public ErrorReport next() throws DbException {
        ErrorReport nextReport = null;
        try {
            VRI uriToSet = new VRI(baseUri).augment("error", rs.getString(1));
            nextReport = new ErrorReport(new VRI(baseUri).augment("error", rs.getString(1)));           
            nextReport.setHttpStatus(rs.getInt(2));
            nextReport.setActor(rs.getString(3));
            nextReport.setMessage(rs.getString(4));
            nextReport.setDetails(rs.getString(5));
            nextReport.setErrorCode(rs.getString(6));
            String errorCause = rs.getString("errorCause");
            if (errorCause != null) {
                if (!retrieveStackTrace) {
                    nextReport.setErrorCause(new ErrorReport(new VRI(baseUri).augment("error", errorCause)));
                }else{
                    FindError stackTraceFinder = new FindError(baseUri);
                    stackTraceFinder.setSearchById(errorCause);
                    IDbIterator<ErrorReport> stackTraceIterator = stackTraceFinder.list();
                    if (stackTraceIterator.hasNext()){
                        nextReport.setErrorCause(stackTraceIterator.next());
                    }
                    stackTraceIterator.close();
                    stackTraceFinder.close();
                }
            }

            Blob metaInfoBlob = rs.getBlob(8);
            if (metaInfoBlob != null) {
                MetaInfoDeblobber mid = new MetaInfoDeblobber(metaInfoBlob);
                MetaInfo mi = mid.toMetaInfo();
                nextReport.setMeta(mi);
                metaInfoBlob.free();
            }

        } catch (final SQLException ex) {
            final String msg = "Error reading result set on error reports";
            logger.warn(msg, ex);
            throw new DbException(msg, ex);
        }
        return nextReport;
    }
}
