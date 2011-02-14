package org.opentox.toxotis.database.engine.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.database.DbIterator;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class UserIterator extends DbIterator<User> {

    public UserIterator(ResultSet rs) {
        super(rs);
    }

    @Override
    public User next() throws DbException {
        User nextUser = new User();
        try {
            nextUser.setUid(rs.getString(1));
            nextUser.setName(rs.getString(2));
            nextUser.setMail(rs.getString(3));
            nextUser.setHashedPass(rs.getString(4));
        } catch (SQLException ex) {
            throw new DbException(ex);
        } catch (ToxOtisException ex) {
            throw new DbException("Illegal mail is found in the DB for user " + nextUser.getUid() , ex);
        }
        return nextUser;
    }
}
