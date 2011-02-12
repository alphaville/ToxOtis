package org.opentox.toxotis.database.engine.user;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.database.DbIterator;
import org.opentox.toxotis.database.DbReader;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class FindUser extends DbReader<User> {

    @Override
    public IDbIterator<User> list() throws DbException {
        setTable("User");
        setTableColumns("uid", "name", "mail");
        Statement statement = null;
        Connection connection = null;
        connection = getConnection();
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(getSql());
            System.out.println(getSql());
            DbIterator<User> it = null;
            //    return it;
            throw new UnsupportedOperationException("Not supported yet.");        
        } catch (SQLException ex) {
            throw new DbException(ex);
        } finally {
            // Do Nothing:  The client is expected to close the statement and the connection
        }
        
    }
}
