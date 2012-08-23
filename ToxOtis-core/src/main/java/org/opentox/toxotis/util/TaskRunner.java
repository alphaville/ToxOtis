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
package org.opentox.toxotis.util;

import java.util.concurrent.Callable;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.core.component.Task.Status;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 * Reloads a task until its status is no longer {@link Status#RUNNING }.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 * @see Task
 */
public class TaskRunner implements Callable<Task> {

    private Task task;
    private long delay = 100;
    private AuthenticationToken token;

    private TaskRunner() {
        throw new AssertionError("Dummy constructor invokation in TaskRunner!");
    }

    public TaskRunner(final Task task) {
        this.task = task;
    }

    public TaskRunner(Task task, AuthenticationToken token) {
        this.task = task;
        this.token = token;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public AuthenticationToken getToken() {
        return token;
    }

    public void setToken(AuthenticationToken token) {
        this.token = token;
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

    private Task updateTask(Task old) throws ServiceInvocationException {
        task.loadFromRemote(getToken());
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
                Thread.sleep(delay);
                return updateTask(old);
            } catch (InterruptedException ex) {
                throw new RuntimeException("Interruption!", ex);
            }
        } else {
            // This block is reached when the status code is some error code
            return old;
        }
    }

    /**
     * Runs a remote task until it completes. As long as the task returns a 202 or
     * 201 status code, the client keeps monitoring the progress of the task every
     * a certain time period which is specified by the method {@link #setDelay(long) setDelay}.
     * When the task is over ({@link Status#COMPLETED completed}), this method returns
     * the updated task which points to the result (see {@link Task#getResultUri() }.
     * If the task fails to complete, i.e. it has some status different from
     * {@link Status#RUNNING running} or {@link Status#QUEUED queued}, then the task
     * is updated and returned directly to the invoker, but <b>no exception is thrown</b>
     * in such a case. The invoking method should <b>always check</b> the status of
     * the returned task!
     * 
     * @return
     *      Completed or Failed Task
     * @throws ServiceInvocationException 
     *      In case the remote service is not accessible/available, the connection
     *      breaks or other unexpected exception is caught.
     */
    @Override
    public Task call() throws ServiceInvocationException {
        task.loadFromRemote();
        return updateTask(task);
    }
}
