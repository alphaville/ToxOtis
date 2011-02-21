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

package org.opentox.toxotis.database.engine.task;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.opentox.toxotis.database.DbCount;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class CountTasks extends DbCount {

    private Statement statement = null;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CountTasks.class);

    @Override
    public int count() throws DbException {
        setTable("Task");
        setCountableColumn("Task.id");
        setInnerJoin("OTComponent ON Task.id=OTComponent.id");
        if (!includeDisabled) {
            if (where != null) {
                setWhere(where + " AND OTComponent.enabled=true");
            } else {
                setWhere("OTComponent.enabled=true");
            }
        }
        Connection connection = null;
        connection = getConnection();
        ResultSet rs = null;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(getSql());
            rs.first();
            return rs.getInt(1);
        } catch (SQLException ex) {
            String msg = "SQLException caught while counting task in the database";
            logger.debug(msg, ex);
            throw new DbException(msg, ex);
        } finally {
            SQLException sqlEx = null;
            String exceptionMessage = null;
            if (rs != null) {
                try {
                    rs.close();
                } catch (final SQLException ex) {
                    exceptionMessage = "Result set uncloseable (after counting models)";
                    logger.debug(exceptionMessage, ex);
                    sqlEx = ex;
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (final SQLException ex) {
                    exceptionMessage = "statement uncloseable (after counting models)";
                    logger.debug(exceptionMessage, ex);
                    sqlEx = ex;
                }
            }
            if (sqlEx != null) {
                throw new DbException(exceptionMessage, sqlEx);
            }
            // Do Nothing:  The client is expected to close the connection
        }
    }
}
