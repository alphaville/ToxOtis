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
package org.opentox.toxotis.database.engine.qprf;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.database.DbWriter;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 */
class BatchAddFoaf extends DbWriter {

    private final Collection<VRI> foaf;
    private PreparedStatement ps;
    private static String SQL;

    public BatchAddFoaf(Collection<VRI> authors) {
        this.foaf = authors;
    }

    public BatchAddFoaf(VRI... foafUris) {
        this.foaf = new ArrayList<VRI>();
        Collections.addAll(this.foaf, foafUris);
    }

    @Override
    public int write() throws DbException {
        if (foaf == null || (foaf != null && foaf.isEmpty())) {
            return 0;
        }
        if (SQL == null) {
            //setInsertType(InsertType.INSERT_IGNORE);
            setTable("Foaf");
            setTableColumns("id");
            SQL = getSql();
        }
        try {
            ps = getConnection().prepareStatement(SQL);
            for (VRI vri : foaf) {
                ps.setString(1, vri.toString());
                ps.addBatch();
            }
            int[] ints = ps.executeBatch();
            int sum = 0;
            for (int i : ints) {
                sum += i;
            }
            return sum;
        } catch (SQLException ex) {
            Logger.getLogger(BatchAddFoaf.class.getName()).log(Level.SEVERE, null, ex);
            throw new DbException(ex);
        } finally {
            SQLException sqle = null;
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    final String msg = "SQL exception occured while closing the  SQL statement for "
                            + "adding a model in the database consisting of particular batched statements";
//                    logger.warn(msg, ex);
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
