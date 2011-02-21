package org.opentox.toxotis.database.engine;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.database.pool.DataSourceFactory;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class CreateDatabase {

    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CreateDatabase.class);

    public static void createDB() throws DbException, IOException {
        DataSourceFactory factory = DataSourceFactory.getInstance();
        Connection connection = null;
        boolean connectionAutoCommit = false;

        try {
            connection = factory.getDataSource().getConnection();
            connection.setAutoCommit(connectionAutoCommit);
            ScriptRunner sr = new ScriptRunner(connection, connectionAutoCommit, true);
            URI uri = CreateDatabase.class.getClassLoader().getResource("databaseCreation.sql").toURI();
            sr.runScript(new FileReader(new File(uri)));
            connection.commit();
        } catch (final SQLException ex) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (final SQLException ex1) {
                    throw new DbException(ex1);
                }
            }
            throw new DbException(ex);
        } catch (final IOException ex) {
            throw ex;
        } catch (final URISyntaxException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (final SQLException ex1) {
                    throw new DbException(ex1);
                }
            }
        }
    }

    public static void main(String... args) throws Exception {
        createDB();
    }
}
