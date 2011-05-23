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
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.qprf.QprfReport;
import org.opentox.toxotis.core.component.qprf.QprfReportMetaSerializer;
import org.opentox.toxotis.database.DbWriter;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 */
public class AddQprf extends DbWriter {

    private PreparedStatement ps;
    private final QprfReport report;

    public AddQprf(QprfReport report) {
        if (report == null) {
            throw new NullPointerException("You cannot register a null QPRFReport object "
                    + "in the database");
        }
        if (report.getUri() == null) {
            throw new IllegalArgumentException("You cannot register a QPRF report object in the "
                    + "database without an explicit URI");
        }
        if (!QprfReport.class.equals(report.getUri().getOpenToxType())) {
            throw new IllegalArgumentException("The URI of the submitted QPRF report is not a "
                    + "valid URI according to the OpenTox specification");
        }
        this.report = report;
    }

    @Override
    protected String getSql() {
        return "INSERT INTO QprfReport (id,modelUri,compoundUri,doaUri,keywords,"
                + "report_date,model_date,datasetStructuralAnalogues,applicabilityDomainResult,"
                + "predictionResult,experimentalResult,QMRFreference,more,createdBy) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,compress(?),?)";
    }

    @Override
    public int write() throws DbException {
        BatchAddFoaf foafAdder = new BatchAddFoaf(report.getAuthors());
        try {
            ps = getConnection().prepareStatement(getSql());
            ps.setString(1, report.getUri().getId());
            if (report.getModelUri() != null) {
                ps.setString(2, report.getModelUri().toString());
            } else {
                ps.setNull(2, Types.VARCHAR);
            }
            if (report.getCompoundUri() != null) {
                ps.setString(3, report.getCompoundUri().toString());
            } else {
                ps.setNull(3, Types.VARCHAR);
            }
            if (report.getDoaUri() != null) {
                ps.setString(4, report.getDoaUri().toString());
            } else {
                ps.setNull(4, Types.VARCHAR);
            }
            if (report.getKeywords() != null) {
                ps.setString(5, report.getKeywords().toString());
            } else {
                ps.setNull(5, Types.VARCHAR);
            }
            if (report.getReportDate() != null) {
                ps.setLong(6, report.getReportDate());
            } else {
                ps.setNull(6, Types.BIGINT);
            }
            if (report.getModelDate() != null) {
                ps.setLong(7, report.getModelDate());
            } else {
                ps.setNull(7, Types.BIGINT);
            }

            if (report.getDatasetStructuralAnalogues() != null) {
                ps.setString(8, report.getDatasetStructuralAnalogues().toString());
            } else {
                ps.setNull(8, Types.VARCHAR);
            }
            if (report.getApplicabilityDomainResult() != null) {
                ps.setString(9, report.getApplicabilityDomainResult().toString());
            } else {
                ps.setNull(9, Types.VARCHAR);
            }
            if (report.getPredictionResult() != null) {
                ps.setString(10, report.getPredictionResult().toString());
            } else {
                ps.setNull(10, Types.VARCHAR);
            }
            if (report.getExperimentalResult() != null) {
                ps.setString(11, report.getExperimentalResult().toString());
            } else {
                ps.setNull(11, Types.VARCHAR);
            }
            ps.setString(12, report.getQMRFreference());
            QprfReportMetaSerializer serializer = new QprfReportMetaSerializer(report.getReportMeta());
            try {
                ps.setBlob(13, serializer.toBlob());
            } catch (Exception ex) {
                Logger.getLogger(AddQprf.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            }
            ps.setString(14, report.getCreatedBy().getUid());
            int result = ps.executeUpdate();
            result += foafAdder.write();
            return result;
        } catch (SQLException ex) {
            throw new DbException(ex);
        } finally {
            DbException event = null;
            if (ps != null) {
                try {
                    ps.close();
                } catch (final SQLException ex) {
                    final String msg = "Prepared statement for adding BibTeX in the database cannot be closed";
                    event = new DbException(ex);
                    // logger.warn(msg, ex);
                }
            }
            try {
                foafAdder.close();
            } catch (final DbException ex) {
                event = ex;
            }
            close();
            if (event != null) {
                throw event;
            }
        }
    }
}
