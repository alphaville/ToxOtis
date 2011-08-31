package org.opentox.toxotis.database.engine.parameter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Parameter;
import org.opentox.toxotis.database.DbReader;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class FindParameter extends DbReader<Parameter> {

    private Statement statement = null;
    private final VRI baseUri;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(FindParameter.class);

    public FindParameter(VRI baseUri) {
        if (baseUri == null) {
            final String msg = "Null is not a valid value for baseUri in the constructor of FindParameter";
            final NullPointerException iae = new NullPointerException(msg);
            logger.warn(msg, iae);
            throw iae;
        }
        if (baseUri.toString() == null || (baseUri.toString() != null && (baseUri.toString().isEmpty()))) {
            final String msg = "Void base URI provided in the constructor of FindParameter";
            final IllegalArgumentException iae = new IllegalArgumentException(msg);
            logger.warn(msg, iae);
            throw iae;
        }
        this.baseUri = baseUri;
    }

    public void setSearchById(String id) {
        String whereTemplate = "Parameter.id='%s'";
        setWhere(String.format(whereTemplate, id));
    }

    @Override
    public IDbIterator<Parameter> list() throws DbException {
        setTable("Parameter");
        setTableColumns("Parameter.id", "name", "scope", "value", "valueType",
                "uncompress(MetaInfo.meta)");
        setInnerJoin("OTComponent ON Parameter.id=OTComponent.id "
                + "INNER JOIN MetaInfo ON OTComponent.meta=MetaInfo.id "
                + "OR OTComponent.meta IS NULL");
        
        statement = null;
        Connection connection = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(getSql());
            ParameterIterator it = new ParameterIterator(rs, baseUri);
            return it;
        } catch (SQLException ex) {
            final String msg = "Database exception while searching for error reports in the database.";
            logger.warn(msg, ex);
            throw new DbException(msg, ex);
        } finally {
            // Do Nothing:  The client is expected to close the statement and the connection
            // The client closes the result set applying a close() on the ErrorIterator
            // and then closes the statement and the connection invoking close() on this
            // object
        }
    }

    @Override
    public void close() throws DbException {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException ex) {
            final String msg = "statement uncloseable";
            logger.warn(msg, ex);
            throw new DbException(msg, ex);
        } finally {
            super.close();
        }
    }
}
