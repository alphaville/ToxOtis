package org.opentox.toxotis.database.engine.error;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.ErrorReport;
import org.opentox.toxotis.database.DbIterator;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ErrorIterator extends DbIterator<ErrorReport>{

    private final VRI baseUri;

    public ErrorIterator(final ResultSet rs, final VRI baseUri) {
        super(rs);
        this.baseUri = baseUri;
    }
    

    @Override
    public ErrorReport next() throws DbException {
        ErrorReport nextReport = null;
        try {
            nextReport = new ErrorReport(new VRI(baseUri).augment("error", rs.getString(1)));
            nextReport.setHttpStatus(rs.getInt(2));
            nextReport.setActor(rs.getString(3));
            nextReport.setMessage(rs.getString(4));
            nextReport.setDetails(rs.getString(5));
            nextReport.setErrorCode(rs.getString(6));            
        } catch (SQLException ex) {
            throw new DbException(ex);
        }
        return nextReport;
    }



}