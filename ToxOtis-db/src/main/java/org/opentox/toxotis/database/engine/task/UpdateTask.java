package org.opentox.toxotis.database.engine.task;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.database.DbUpdater;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.ontology.MetaInfoBlobber;

/**
 * Configurable Updater of a Task.
 * @author Pantelis Sopasakis
 */
public class UpdateTask extends DbUpdater {

    private final Task newTask;
    private String taskId;
    private boolean updatePercentageCompleted = true;
    private boolean updateTaskStatus = true;
    private boolean updateHttpStatus = true;
    private boolean updateMeta = false;
    private boolean updateDuration = false;
    private boolean updateResultUri = false;
    private boolean updateErrorReport = false;
    private Statement statement = null;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UpdateTask.class);

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

    private String getMetaUpdateSql() {
        String sql = "UPDATE OTComponent SET meta=compress(?) WHERE id=?";
        return sql;
    }

    /**
     * SQL for updating Task Table
     * @return
     */
    private String getTaskUpdateSql() {
        String mainSql = "UPDATE Task SET %s WHERE Task.id='%s'";
        Set<String> updates = new HashSet<String>();
        if (updateDuration) {
            updates.add("duration=" + newTask.getDuration());
        }
        if (updateErrorReport) {
            if (newTask.getErrorReport() != null) {
                updates.add("errorReport=" + newTask.getErrorReport().getUri().getId());
            } else {
                updates.add("errorReport=NULL");
            }
        }
        if (updateHttpStatus) {
            updates.add("httpStatus=" + newTask.getHttpStatus());
        }
        if (updatePercentageCompleted) {
            updates.add("percentageCompleted=" + newTask.getPercentageCompleted());
        }
        if (updateResultUri) {
            VRI resultUri = newTask.getResultUri();
            if (resultUri != null) {
                updates.add(String.format("resultUri='%s'", newTask.getResultUri()));
            } else {
                updates.add("resultUri=NULL");
            }
        }
        if (updateTaskStatus) {
            Task.Status taskStatus = newTask.getStatus();
            if (taskStatus != null) {
                updates.add(String.format("status='%s'", newTask.getStatus()));
            } else {
                updates.add("status=NULL");
            }
        }

        StringBuilder updateData = new StringBuilder("");
        Iterator<String> updateIterator = updates.iterator();
        int length = updates.size();
        if (length == 0) {
            String msg = "Possible programming error/bad practice - Illegal use of UpdateTask: nothing to update";
            logger.warn(msg);
            throw new IllegalArgumentException(msg);
        }
        for (int i = 0; i < length; i++) {
            updateData.append(updateIterator.next());
            if (i != length - 1) {
                updateData.append(", ");
            }
        }

        return String.format(mainSql, updateData, newTask.getUri().getId());
    }

    @Override
    public int update() throws DbException {
        Connection connection = getConnection();

        try {
            connection.setAutoCommit(false);
        } catch (SQLException ex) {
            Logger.getLogger(UpdateTask.class.getName()).log(Level.SEVERE, null, ex);
        }


        if (updateMeta) {
            try {
                statement = connection.prepareStatement(getMetaUpdateSql());
                MetaInfoBlobber blobber = new MetaInfoBlobber(newTask.getMeta());

                Blob blob = blobber.toBlob();
                ((PreparedStatement) statement).setBlob(1, blob);
                ((PreparedStatement) statement).setString(2, taskId);
                ((PreparedStatement) statement).executeUpdate();

            } catch (SQLException ex) {
                Logger.getLogger(UpdateTask.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(UpdateTask.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


        try {
            if (statement == null) {
                statement = connection.createStatement();
            }
            statement.addBatch(getTaskUpdateSql());
            statement.executeBatch();

            connection.commit();
        } catch (SQLException ex) {
            String msg ="SQL exception while updating task - rolling back";
            logger.debug(msg, ex);
            try {
                connection.rollback();
                logger.trace("connection rolled back after failure to update task");
            } catch (SQLException ex1) {
                String msg1 = "SQL exception caused connection rool back to fail";
                logger.trace(msg1, ex1);
                throw new DbException(ex1);
            }
            throw new DbException(msg,ex);
        }
        return -1;
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
