package org.opentox.toxotis.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.database.pool.DataSourceFactory;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class DbOperation implements ISql{

    private Connection connection;

    public DbOperation() {
    }

    public DbOperation(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() throws DbException {
        if (connection == null){
            LoginInfo li = LoginInfo.LOGIN_INFO;
            DataSourceFactory factory = DataSourceFactory.getInstance();
            String connectionUri = factory.getConnectionURI(li);
            try {
                connection = factory.getDataSource(connectionUri).getConnection();
            } catch (final SQLException ex) {
                throw new DbException(ex);
            }
        }
        return connection;
    }
    

    public void closeConnection() throws DbException{
        if (connection!=null){
            try {
                connection.close();
            } catch (SQLException ex) {
                throw new DbException(ex);
            }
        }
    }







}