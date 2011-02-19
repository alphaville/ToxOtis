package org.opentox.toxotis.database.engine;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.opentox.toxotis.database.DbOperation;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 */
public class DisableComponent extends DbOperation {

    private String[] componentIds;
    private static final String quote = "'";
    private static final String comma = ", ";
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DisableComponent.class);

    @Override
    public String getSqlTemplate() {
        return "UPDATE OTComponent SET enabled=%s, deletionDate=CURRENT_TIMESTAMP WHERE id IN (%s)";
    }

    public DisableComponent(final String... componentIds) {
        if (componentIds == null) {
            throw new NullPointerException("Null is not accepted in this constructor");
        }
        this.componentIds = new String[componentIds.length];
        int index = 0;
        for (String s : componentIds) {
            this.componentIds[index] = s;
            index++;
        }
    }

    public int disable() throws DbException {
        return enable(false);
    }

    public int enable() throws DbException {
        return enable(true);
    }

    public int enable(boolean enable) throws DbException {
        if ((componentIds != null && componentIds.length == 0) || componentIds == null) {
            throw new RuntimeException("No component ids were provided!");
        }
        StringBuilder updatedComponentsSql = new StringBuilder();
        for (int i = 0; i < componentIds.length; i++) {
            updatedComponentsSql.append(quote);
            updatedComponentsSql.append(componentIds[i]);
            updatedComponentsSql.append(quote);
            if (i != componentIds.length - 1) {
                updatedComponentsSql.append(comma);
            }
        }
        String sql = String.format(getSqlTemplate(), enable, updatedComponentsSql);
        Statement stmt = null;
        try {
            stmt = getConnection().createStatement();
            stmt.addBatch(sql);
            return stmt.executeBatch()[0];
        } catch (final SQLException ex) {
            final String msg = "SQL statement execution failed while trying to enable/disable a component in the database";
            logger.warn(msg, ex);
            throw new DbException(msg, ex);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (final SQLException ex) {
                final String msg = "SQL statement execution failed while trying to enable/disable a component in the database";
                logger.warn(msg, ex);
                throw new DbException(msg, ex);
            } finally {
                close();
            }

        }
    }
}
