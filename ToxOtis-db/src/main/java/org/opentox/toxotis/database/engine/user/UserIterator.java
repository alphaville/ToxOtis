/*
 *
 * ToxOtis
 *
 * ToxOtis is the Greek word for Sagittarius, that actually means ‘archer’. ToxOtis
 * is a Java interface to the predictive toxicology services of OpenTox. ToxOtis is
 * being developed to help both those who need a painless way to consume OpenTox
 * services and for ambitious service providers that don’t want to spend half of
 * their time in RDF parsing and creation.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 *
 */
package org.opentox.toxotis.database.engine.user;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserIterator.class);

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
        } catch (final SQLException ex) {
            final String msg = "Database exception while reading users from the database";
            logger.warn(msg, ex);
            throw new DbException(msg,ex);
        } catch (final ToxOtisException ex) {
            final String msg = "Illegal mail is found in the DB for user ".concat(nextUser.getUid() != null ? nextUser.getUid() : "-");
            logger.warn(msg, ex);
            throw new DbException(msg, ex);
        }
        return nextUser;
    }
}
