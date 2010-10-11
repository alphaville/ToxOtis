package org.opentox.toxotis.util;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.core.component.Task;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class TaskRunner implements Callable<Task> {

    private Task task;

    private TaskRunner() {
        throw new AssertionError("Dummy constructor invokation in TaskRunner!");
    }

    public TaskRunner(final Task task) {
        this.task = task;
    }

    private Task updateTask(Task old) throws ToxOtisException {
        task.loadFromRemote();
        float taskHttpStatus = old.getHttpStatus();
        if (taskHttpStatus == 201) { // Created new task
            Task created = new Task(task.getResultUri());
            task = created;
            return updateTask(created);
        } else if (taskHttpStatus == 200) {// Done!
            return task;
        } else if (taskHttpStatus == 202) {// Waiting for completion!
            try {
                // Waiting for completion!
                Thread.sleep(100);
                return updateTask(old);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            return old;
        }
    }

    public Task call() throws ToxOtisException {
        task.loadFromRemote();
        return updateTask(task);
    }
}
