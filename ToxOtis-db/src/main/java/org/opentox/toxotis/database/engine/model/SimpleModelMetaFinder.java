package org.opentox.toxotis.database.engine.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.opentox.toxotis.database.DbReader;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.ResultSetIterator;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class SimpleModelMetaFinder extends DbReader<String> {

    public enum SEARCH_MODE {

        DEPENDENT("ModelDepFeatures"),
        INDEPENDENT("ModelIndepFeatures"),
        PREDICTED("ModelPredictedFeatures"),
        DATASET(),
        ALGORITHM();
        private String tableName;

        private SEARCH_MODE(String tableName) {
            this.tableName = tableName;
        }

        private SEARCH_MODE() {
        }

        public String getTableName() {
            return tableName;
        }
    }
    private final SEARCH_MODE searchMode;
    private final String modelId;
    private Statement statement = null;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SimpleModelMetaFinder.class);

    public SimpleModelMetaFinder(SEARCH_MODE searchMode, String modelId) {
        this.searchMode = searchMode;
        this.modelId = modelId;
    }

    @Override
    public IDbIterator<String> list() throws DbException {
        Connection connection = null;
        connection = getConnection();
        ResultSet rs = null;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(getSql());
            return new ResultSetIterator(rs);
        } catch (final SQLException ex) {
            final String msg = "SQL-related exception while looking for features in the database";
            logger.warn(msg, ex);
            throw new DbException(msg, ex);
        } 
//        finally {
            // Do nothing - the client is responsible for closing the result set!
//        }
    }

    @Override
    public String getSql() {
        String internalSearchSql = "SELECT %s FROM Model WHERE id='%s'";
        if (SEARCH_MODE.DATASET.equals(searchMode)) {
            return String.format(internalSearchSql,"dataset", modelId);
        }
        if (SEARCH_MODE.ALGORITHM.equals(searchMode)) {
            return String.format(internalSearchSql,"algorithm", modelId);
        }
        return String.format(getSqlTemplate(), searchMode.getTableName(), modelId);
    }

    @Override
    public String getSqlTemplate() {
        return "SELECT featureUri FROM %s WHERE modelId='%s' ORDER BY idx";
    }
}
