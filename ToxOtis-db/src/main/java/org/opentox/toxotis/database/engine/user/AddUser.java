package org.opentox.toxotis.database.engine.user;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.database.DbWriter;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class AddUser extends DbWriter {

    private final User user;

    public AddUser(final User user) {
        this.user = user;
    }

    @Override
    public int write() throws DbException {
        setTable("User");
        setTableColumns("uid", "name", "mail", "password");
        try {
            PreparedStatement ps = getConnection().prepareStatement(getSql());
            ps.setString(1, user.getUid());
            ps.setString(2, user.getName());
            ps.setString(3, user.getMail());
            ps.setString(4, user.getHashedPass());
            int update = ps.executeUpdate();
            ps.close();
            return update;
        } catch (SQLException ex) {
            throw new DbException(ex);
        } finally {
            close();
        }
    }
}
