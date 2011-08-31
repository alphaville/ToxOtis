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

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Feature;
import org.opentox.toxotis.database.DbOperation;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class FindModelFeatures extends DbOperation {

    public enum SEARCH_MODE {

        DEPENDENT("ModelDepFeatures"),
        INDEPENDENT("ModelIndepFeatures"),
        PREDICTED("ModelPredictedFeatures");
        private String tableName;

        private SEARCH_MODE(String tableName) {
            this.tableName = tableName;
        }

        public String getTableName() {
            return tableName;
        }
    }
    private final SEARCH_MODE searchMode;
    private final String modelId;
    private Statement statement = null;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(FindModelFeatures.class);
    

    public List<Feature> list() throws DbException {
        Connection connection = null;
        connection = getConnection();
        ResultSet rs = null;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(getSql());
            List<Feature> list = new ArrayList<Feature>();
            while (rs.next()) {
                try {
                    list.add(new Feature(new VRI(rs.getString(1))));
                } catch (final URISyntaxException ex) {
                    final String msg = "Invalid URI found in the database for feature";
                    logger.error(msg, ex);
                    throw new RuntimeException(msg, ex);
                }
            }
            return list;
        } catch (final SQLException ex) {
            final String msg = "SQL-related exception while looking for features in the database";
            logger.warn(msg, ex);
            throw new DbException(msg, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    final String msg = "Result set is uncloseable";
                    logger.warn(msg, ex);
                    throw new DbException(msg, ex);
                } finally {
                    close();
                }
            }
        }
    }

    public FindModelFeatures(final SEARCH_MODE searchMode, final String modelId) {
        super();
        this.searchMode = searchMode;
        this.modelId = modelId;
    }

    private String getSql() {
        return String.format(getSqlTemplate(), searchMode.getTableName(), modelId);
    }

    @Override
    public String getSqlTemplate() {
        return "SELECT featureUri FROM %s WHERE modelId='%s' ORDER BY idx";
    }

    @Override
    public void close() throws DbException {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (final SQLException ex) {
            final String msg = "statement uncloseable";
            logger.warn(msg, ex);
            throw new DbException(msg, ex);
        } finally {
            super.close();
        }
    }
}
