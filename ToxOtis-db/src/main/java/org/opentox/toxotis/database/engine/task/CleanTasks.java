package org.opentox.toxotis.database.engine.task;

import org.opentox.toxotis.core.component.Task.Status;
import org.opentox.toxotis.database.engine.DisableComponent;
import org.opentox.toxotis.database.exception.DbException;
import static org.opentox.toxotis.core.component.Task.Status.*;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class CleanTasks {

    private static final String deleteTemplate = "DELETE FROM Task WHERE status IN (%s)";

    public enum CleaningMode {
        Disable,
        Delete;
    }
    private final CleaningMode cleaningMode;

    public CleanTasks(CleaningMode cleaningMode) {
        this.cleaningMode = cleaningMode;
    }

    private int disable(Status... status) throws DbException {
        
        return 0;
    }

    private int delete(Status... status) throws DbException {
        return 0;
    }

    public int clean(Status... status) throws DbException {
        if (CleaningMode.Disable.equals(cleaningMode)) {
            return disable(status);
        } else if (CleaningMode.Delete.equals(cleaningMode)) {
            return delete(status);
        }
        return 0;
    }
}
