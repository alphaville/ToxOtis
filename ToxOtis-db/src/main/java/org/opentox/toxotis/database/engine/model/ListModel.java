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
package org.opentox.toxotis.database.engine.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.DbReader;
import org.opentox.toxotis.database.ResultSetIterator;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 */
public class ListModel extends DbReader<String> {

    private boolean includeDisabled = false;
    private Statement statement = null;
    private ResultSet results;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ListModel.class);

    public boolean isIncludeDisabled() {
        return includeDisabled;
    }

    public void setIncludeDisabled(boolean includeDisabled) {
        this.includeDisabled = includeDisabled;
    }

    /**
     * Lists BibTeX IDs
     * @return
     */
    @Override
    public IDbIterator<String> list() throws DbException {
        setTable("Model");
        setTableColumns("Model.id");
        if (!includeDisabled) {
            setInnerJoin("OTComponent ON Model.id=OTComponent.id");
            if (where != null) {
                setWhere(where + "AND enabled=true");
            } else {
                setWhere("enabled=true");
            }

        }        
        Connection connection = null;
        connection = getConnection();
        try {
            statement = connection.createStatement();
            results = statement.executeQuery(getSql());
            return new ResultSetIterator(results);
        } catch (final SQLException ex) {
            throw new DbException(ex);
        }
    }

    @Override
    public void close() throws DbException {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException ex) {
            throw new DbException(ex);
        } finally {
            super.close();
        }
    }
}
