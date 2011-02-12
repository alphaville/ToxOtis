package org.opentox.toxotis.database.engine.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.database.DbIterator;
import org.opentox.toxotis.database.exception.DbException;

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
        User nextUser= new User();
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
