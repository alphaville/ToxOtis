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
package org.opentox.toxotis.database.engine.bibtex;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.database.DbWriter;
import org.opentox.toxotis.database.exception.DbException;

/**
 * Associate a list of BibTeX with a Model.
 * @author Pantelis Sopasakis
 */
public class AssociateBibTeX extends DbWriter {

    private static String SQL;
    private String modelId;
    private String[] bibTexIds;
    private PreparedStatement prepared;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AssociateBibTeX.class);

    public AssociateBibTeX(VRI modelId, VRI... bibTex) {
        this.modelId = modelId.toString();
        bibTexIds = new String[bibTex.length];
        int i = 0;
        for (VRI vri : bibTex) {
            bibTexIds[i] = vri.toString();
            i++;
        }
    }

    /**
     *
     * @param modelId
     *      The id of the model
     * @param bibTex
     *      The <b>URIs</b> of the BibTeX resources (not the IDs)
     */
    public AssociateBibTeX(String modelId, String... bibTex) {
        this.modelId = modelId;
        this.bibTexIds = bibTex;
    }

    @Override
    public int write() throws DbException {
        if (SQL == null) {
            setInsertType(InsertType.INSERT_IGNORE);
            setTable("ModelBibTeX");
            setTableColumns("modelId", "bibTeXUri");
            SQL = getSql();
        }
        Connection connection = getConnection();
        try {
            prepared = connection.prepareStatement(SQL);
            for (String bibtex : bibTexIds) {
                prepared.setString(1, modelId);
                prepared.setString(2, bibtex);
                prepared.addBatch();
            }
            int[] ints = prepared.executeBatch();
            int sum = 0;
            for (int i : ints) {
                sum += i;
            }
            return sum;
        } catch (SQLException ex) {
            throw new DbException(ex);
        } finally {
            SQLException sqle = null;
            if (prepared != null) {
                try {
                    prepared.close();
                } catch (SQLException ex) {
                    final String msg = "SQL exception occured while closing the  SQL statement for "
                            + "adding a model in the database consisting of particular batched statements";
                    logger.warn(msg, ex);
                    sqle = ex;
                }
            }
            close();
            if (sqle != null) {
                throw new DbException(sqle);
            }
        }
    }
}
