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

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.database.DbWriter;
import org.opentox.toxotis.database.engine.error.ErrorReportBatchWriter;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.ontology.MetaInfoBlobber;

/**
 * Creates a new Task record in the database. The task is assumed to be newly created.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class AddTask extends DbWriter {

    private final Task task;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AddTask.class);

    public AddTask(final Task task) {
        this.task = task;
    }

    @Override
    public int write() throws DbException {
        /*
         *
         * `id` varchar(255) COLLATE utf8_bin NOT NULL,
         * `resultUri` varchar(255) COLLATE utf8_bin DEFAULT NULL,
         * `httpStatus` float DEFAULT NULL, `percentageCompleted` float DEFAULT NULL,
         * `status` varchar(16) COLLATE utf8_bin DEFAULT NULL,
         * `duration` bigint(20) DEFAULT NULL,
         * `errorReport` varchar(255) COLLATE utf8_bin DEFAULT NULL,
         * `createdBy` varchar(255) COLLATE utf8_bin DEFAULT NULL,
         */
        setTable("Task");
        setTableColumns("id", "httpStatus", "percentageCompleted", "status", "errorReport", "createdBy");

        Connection connection = getConnection();
        PreparedStatement stmtMeta = null;
        PreparedStatement stmt = null;
        try {
            connection.setAutoCommit(false);
            if (task.getMeta() != null) {
                stmtMeta = connection.prepareStatement("INSERT IGNORE MetaInfo (id, meta)  VALUES (?, compress(?))");
                stmtMeta.setInt(1, task.getMeta().hashCode());
                MetaInfoBlobber mib = new MetaInfoBlobber(task.getMeta());
                try {
                    Blob blob = mib.toBlob();
                    stmtMeta.setBlob(2, blob);
                } catch (final SQLException ex) {
                    logger.warn("Improper parametrization of prepared statement", ex);
                    throw ex;
                } catch (final Exception ex) {
                    logger.warn("MetaInfo serialization exception", ex);
                }
                stmtMeta.executeUpdate();
            }
            
            stmt = connection.prepareStatement("INSERT INTO OTComponent (id,meta) VALUES (?, ?)");
            stmt.setString(1, task.getUri().getId());
            if (task.getMeta() != null) {
                stmt.setInt(2, task.getMeta().hashCode());
            } else {
                stmt.setNull(2, Types.INTEGER);// no meta!
            }
            stmt.executeUpdate();

            if (task.getErrorReport() != null) {
                ErrorReportBatchWriter errorReportWriter = new ErrorReportBatchWriter(getConnection(), 
                        task.getErrorReport());
                errorReportWriter.batchStatement();
            }
            String sqlTask = getSql().replaceAll("\\?", "%s");
            String taskStatus = null;
            if (task.getStatus() != null) {
                taskStatus = "'" + task.getStatus().toString() + "'";
            } else {
                taskStatus = "NULL";
            }
            String errorReportInTask = null;
            if (task.getErrorReport() != null) {
                errorReportInTask = "'" + task.getErrorReport().getUri().getId() + "'";
            } else {
                errorReportInTask = "NULL";
            }
            String taskCreator = null;
            if (task.getCreatedBy() != null) {
                taskCreator = "'" + task.getCreatedBy().getUid() + "'";
            } else {
                taskCreator = "'guest@opensso.in-silico.ch'";
            }
            String taskWriteSql = String.format(sqlTask, "'" + task.getUri().getId() + "'",
                    task.getHttpStatus(), task.getPercentageCompleted(),
                    taskStatus, errorReportInTask, taskCreator);
            stmt.addBatch(taskWriteSql);
            int[] updates = stmt.executeBatch();
            connection.commit();
            int result = 0;
            for (int i : updates) {
                result += i;
            }
            
            return result;
        } catch (final SQLException ex) {
            logger.debug("SQLException caught while adding task in the database", ex);
            try {
                getConnection().rollback();
            } catch (final SQLException ex1) {
                logger.warn("SQLException such that connection cannot roll back", ex);
                logger.warn("Rolling back not possible", ex1);
                throw new DbException(ex1);
            }
            throw new DbException(ex);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (final SQLException ex) {
                    logger.debug("statement uncloseable", ex);
                    throw new DbException(ex);
                }
            }
            close();
        }
    }
}
