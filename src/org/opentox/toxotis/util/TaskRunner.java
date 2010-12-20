package org.opentox.toxotis.util;

import java.util.concurrent.Callable;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.core.component.Task.Status;

/**
 * Reloads a task until its status is no longer {@link Status#RUNNING }.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class TaskRunner implements Callable<Task> {

    private Task task;
    private long delay = 100;

    private TaskRunner() {
        throw new AssertionError("Dummy constructor invokation in TaskRunner!");
    }

    public TaskRunner(final Task task) {
        this.task = task;
    }

    /**
     * Specify the delay between successive reloads of the task resource from the
     * remote location.
     *
     * @param delay
     *      Delay in milliseconds.
     */
    public void setDelay(long delay) {
        this.delay = delay;
    }

    private Task updateTask(Task old) throws ToxOtisException {
        task.loadFromRemote();
        float taskHttpStatus = old.getHttpStatus();
        if (taskHttpStatus == 201) { // Created new task
            System.out.println(task.getResultUri()+" <-- result of created task");
            Task created = new Task(task.getResultUri());
            task = created;
            return updateTask(created);
        } else if (taskHttpStatus == 200) {// Done!
            return task;
        } else if (taskHttpStatus == 202) {// Waiting for completion!
            try {
                // Waiting for completion!
                Thread.sleep(delay);
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
