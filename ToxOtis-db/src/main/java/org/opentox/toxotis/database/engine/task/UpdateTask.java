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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.database.DbUpdater;
import org.opentox.toxotis.database.engine.error.ErrorReportBatchWriter;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.MetaInfoBlobber;

/**
 * Configurable Updater of a Task.
 * @author Pantelis Sopasakis
 */
public class UpdateTask extends DbUpdater {

    private final Task newTask;
    private String taskId;
    private boolean updatePercentageCompleted = false;
    private boolean updateTaskStatus = false;
    private boolean updateHttpStatus = false;
    private boolean updateMeta = false;
    private boolean updateDuration = false;
    private boolean updateResultUri = false;
    private boolean updateErrorReport = false;
    private boolean updateId = false;
    private boolean updateAndRegisterErrorReport = false;
    private String newId = null;
    private static final String __META = "INSERT IGNORE INTO MetaInfo (id,meta) VALUES (?,compress(?))";
    private static final String __COMPONENT = "UPDATE OTComponent SET %s WHERE id=?";
    private static final String __TASK = "UPDATE Task SET %s WHERE id=?";
    private PreparedStatement metaInsertPS;
    private PreparedStatement otComponentUpdatePS;
    private PreparedStatement taskUpdatePS;
    private Map<String, Integer> componentPsMapping = new HashMap<String, Integer>();
    private Map<String, Integer> taskPsMapping = new HashMap<String, Integer>();
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UpdateTask.class);

    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">
    public void setUpdateDuration(boolean updateDuration) {
        this.updateDuration = updateDuration;
    }

    public void setUpdateErrorReport(boolean updateErrorReport) {
        this.updateErrorReport = updateErrorReport;
    }

    public void setUpdateHttpStatus(boolean updateHttpStatus) {
        this.updateHttpStatus = updateHttpStatus;
    }

    public void setUpdateMeta(boolean updateMeta) {
        this.updateMeta = updateMeta;
    }

    public void setUpdatePercentageCompleted(boolean updatePercentageCompleted) {
        this.updatePercentageCompleted = updatePercentageCompleted;
    }

    public void setUpdateResultUri(boolean updateResultUri) {
        this.updateResultUri = updateResultUri;
    }

    public void setUpdateTaskStatus(boolean updateTaskStatus) {
        this.updateTaskStatus = updateTaskStatus;
    }

    public String getNewId() {
        return newId;
    }

    public void setNewId(String newId) {
        this.newId = newId;
    }

    public boolean isUpdateAndRegisterErrorReport() {
        return updateAndRegisterErrorReport;
    }

    public void setUpdateAndRegisterErrorReport(boolean updateAndRegisterErrorReport) {
        this.updateAndRegisterErrorReport = updateAndRegisterErrorReport;
    }
    //</editor-fold>

    public UpdateTask(Task task) {
        if (task == null) {
            throw new NullPointerException("NPE: On the basis of a null task, there can be no update");
        }
        if (task.getUri() == null) {
            throw new IllegalArgumentException("The provided task update-prototype does not have a specified URI");
        }
        taskId = task.getUri().getId();
        if (taskId == null) {
            throw new IllegalArgumentException("No task ID can be retrieved from the URI : " + task.getUri());
        }
        this.newTask = task;
    }

    private String getComponentUpdateSql() {
        StringBuilder set = new StringBuilder();
        int count = 0;
        if (updateMeta) {
            set.append("meta=?");
            count++;
            componentPsMapping.put("meta", count);
        }
        if (updateId) {
            if (count > 0) {
                set.append(", ");
            }
            set.append("id=?");
            count++;
            componentPsMapping.put("id", count);
        }
        if (!updateId && !updateMeta) {
            return null;
        } else {
            return String.format(__COMPONENT, set);
        }
    }

    /**
     * SQL for updating Task Table
     * @return
     */
    private String getTaskUpdateSql() {
        StringBuilder set = new StringBuilder();
        int count = 0;
        if (updateDuration) {
            set.append("duration=?");
            count++;
            taskPsMapping.put("duration", count);
        }
        if (updateHttpStatus) {
            if (count > 0) {
                set.append(", ");
            }
            set.append("httpStatus=?");
            count++;
            taskPsMapping.put("httpStatus", count);
        }
        if (updatePercentageCompleted) {
            if (count > 0) {
                set.append(", ");
            }
            set.append("percentageCompleted=?");
            count++;
            taskPsMapping.put("percentageCompleted", count);
        }
        if (updateResultUri) {
            if (count > 0) {
                set.append(", ");
            }
            set.append("resultUri=?");
            count++;
            taskPsMapping.put("resultUri", count);
        }
        if (updateTaskStatus) {
            if (count > 0) {
                set.append(", ");
            }
            set.append("status=?");
            count++;
            taskPsMapping.put("status", count);
        }
        if (updateErrorReport) {
            if (count > 0) {
                set.append(", ");
            }
            set.append("errorReport=?");
            count++;
            taskPsMapping.put("errorReport", count);
        }
        taskPsMapping.put("id", count + 1);
        return String.format(__TASK, set);
    }

    @Override
    public int update() throws DbException {
        Connection connection = getConnection();
        try {
            connection.setAutoCommit(false);
            /*
             * New Error Report
             */
            if (updateAndRegisterErrorReport) {
                ErrorReportBatchWriter writer = new ErrorReportBatchWriter(connection, newTask.getErrorReport());
                writer.batchStatement();
            }
            /*
             * Register new MetaInfo in the corresponding table... 
             */
            if (updateMeta) {
                metaInsertPS = connection.prepareStatement(__META);
                MetaInfo meta = newTask.getMeta();
                
                metaInsertPS.setInt(1, meta.hashCode());
                MetaInfoBlobber blobber = new MetaInfoBlobber(meta);
                Blob blob = null;
                try {
                    blob = blobber.toBlob();
                    metaInsertPS.setBlob(2, blob);
                } catch (final Exception ex) {
                    logger.error("Meta Blobbing Exception", ex);
                    throw new DbException(ex);
                }
                metaInsertPS.executeUpdate();
            }

            /**
             * Update OTComponent with the new meta data and new ID
             */
            if (updateMeta || updateId) {
                otComponentUpdatePS = connection.prepareStatement(getComponentUpdateSql());
                if (updateMeta) {
                    int metaHashCode = newTask.getMeta().hashCode();
                    otComponentUpdatePS.setInt(componentPsMapping.get("meta"), metaHashCode);
                    otComponentUpdatePS.setString(componentPsMapping.size() + 1, taskId);//WHERE clause
                }
                if (updateId) {
                    otComponentUpdatePS.setString(componentPsMapping.get("id"), newId);
                }
                otComponentUpdatePS.executeUpdate();
            }

            /*
             * Update task
             */

            if (doUpdateTask()) {
                taskUpdatePS = connection.prepareStatement(getTaskUpdateSql());
                if (updateDuration) {
                    taskUpdatePS.setLong(taskPsMapping.get("duration"), newTask.getDuration());
                }
                if (updateErrorReport) {
                    taskUpdatePS.setString(taskPsMapping.get("errorReport"),
                            newTask.getErrorReport().getUri().getId());
                }
                if (updateHttpStatus) {
                    taskUpdatePS.setFloat(taskPsMapping.get("httpStatus"),
                            newTask.getHttpStatus());
                }
                if (updatePercentageCompleted) {
                    taskUpdatePS.setFloat(taskPsMapping.get("percentageCompleted"),
                            newTask.getPercentageCompleted());
                }
                if (updateResultUri) {
                    taskUpdatePS.setString(taskPsMapping.get("resultUri"),
                            newTask.getResultUri().toString());
                }
                if (updateTaskStatus) {
                    taskUpdatePS.setString(taskPsMapping.get("status"),
                            newTask.getStatus().toString());
                }
                taskUpdatePS.setString(taskPsMapping.get("id"), newTask.getUri().getId());
                taskUpdatePS.executeUpdate();
            }

            connection.commit();

        } catch (SQLException ex) {
            logger.warn("UpdateTask::Failure", ex);
            throw new DbException(ex);
        }

        return -1;
    }

    private boolean doUpdateTask() {
        return updateDuration || updateErrorReport || updateHttpStatus
                || updatePercentageCompleted || updateResultUri || updateTaskStatus;
    }

    @Override
    public void close() throws DbException {
    }
}
