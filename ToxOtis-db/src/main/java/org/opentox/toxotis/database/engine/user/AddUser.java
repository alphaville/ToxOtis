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
