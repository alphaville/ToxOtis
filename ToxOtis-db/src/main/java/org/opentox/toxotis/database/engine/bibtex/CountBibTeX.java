package org.opentox.toxotis.database.engine.bibtex;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.opentox.toxotis.database.DbCount;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class CountBibTeX extends DbCount{

    private Statement statement = null;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CountBibTeX.class);

    @Override
    public int count() throws DbException {
        setTable("BibTeX");
        setCountableColumn("BibTeX.id");
        setInnerJoin("OTComponent ON BibTeX.id=OTComponent.id");
        if (!includeDisabled) {
            if (where != null) {
                setWhere(where + " AND OTComponent.enabled=true");
            } else {
                setWhere("OTComponent.enabled=true");
            }
        }
        Connection connection = null;
        connection = getConnection();
        ResultSet rs = null;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(getSql());
            rs.first();
            return rs.getInt(1);
        } catch (SQLException ex) {
            String msg = "SQLException caught while counting bibtex entries in the database";
            logger.debug(msg, ex);
            throw new DbException(msg,ex);
        } finally {
            SQLException sqlEx = null;
            String exceptionMessage = null;
            if (rs != null) {
                try {
                    rs.close();
                } catch (final SQLException ex) {
                    exceptionMessage = "Result set uncloseable (after counting bibtex entries)";
                    logger.debug(exceptionMessage, ex);
                    sqlEx = ex;
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (final SQLException ex) {
                    exceptionMessage = "statement uncloseable (after counting bibtex entries)";
                    logger.debug(exceptionMessage, ex);
                    sqlEx = ex;
                }
            }
            if (sqlEx != null) {
                throw new DbException(exceptionMessage, sqlEx);
            }
            // Do Nothing:  The client is expected to close the connection
        }
    }

    @Override
    public void close() throws DbException {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException ex) {
            logger.debug("SQL statement uncloseable", ex);
            throw new DbException(ex);
        } finally {
            super.close();
        }
    }


}