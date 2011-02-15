package org.opentox.toxotis.database.engine.task;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.database.DbUpdater;
import org.opentox.toxotis.database.exception.DbException;

/**
 * Configurable Updater of a Task.
 * @author Pantelis Sopasakis
 */
public class UpdateTask extends DbUpdater {

    private final Task newTask;
    private boolean updatePercentageCompleted = true;
    private boolean updateTaskStatus = true;
    private boolean updateHttpStatus = true;
    private boolean updateMeta = false;
    private boolean updateDuration = false;
    private boolean updateResultUri = false;
    private boolean updateErrorReport = false;

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
        if (task.getUri().getId() == null) {
            throw new IllegalArgumentException("No task ID can be retrieved from the URI : " + task.getUri());
        }
        this.newTask = task;
    }

    private String getMetaUpdateSql() {
        String sql = "UPDATE OTComponent SET meta=compress(?) WHERE id=?";
        return null;
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
            updates.add(String.format("resultUri='%s'", newTask.getResultUri().toString()));
        }
        if (updateTaskStatus) {
            updates.add(String.format("status='%s'", newTask.getStatus().toString()));
        }

        StringBuilder updateData = new StringBuilder("");
        Iterator<String> updateIterator = updates.iterator();
        int length = updates.size();
        if (length==0){
            throw new IllegalArgumentException("Illegal use of UpdateTask: nothing to update...");
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
        System.out.println(getTaskUpdateSql());
        return -1;
    }
}
