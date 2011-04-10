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
package org.opentox.toxotis.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class DbIterator<T> implements IDbIterator<T> {

    protected final ResultSet rs;

    public DbIterator(final ResultSet rs) {
        if (rs == null) {
            throw new NullPointerException("Null values are not allowed in the constructor of "
                    + "Iterator. The ResultSet object with which you instantiate a new DbIterator "
                    + "cannot be null");
        }
        this.rs = rs;
    }

    @Override
    public boolean hasNext() throws DbException {
        try {
            return rs.next();
        } catch (SQLException ex) {
            throw new DbException(ex);
        }
    }

    @Override
    public void close() throws DbException {
        try {
            rs.close();
        } catch (SQLException ex) {
            throw new DbException(ex);
        }
    }

    @Override
    public void remove() throws DbException {
        try {
            rs.deleteRow();
        } catch (final SQLException ex) {
            throw new DbException(ex);
        }
    }
}
