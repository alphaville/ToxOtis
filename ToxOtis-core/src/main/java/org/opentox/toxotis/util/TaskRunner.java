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

    private Task updateTask(Task old) throws ServiceInvocationException  {
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
                Thread.sleep(delay);
                return updateTask(old);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            return old;
        }
    }

    public Task call() throws ServiceInvocationException {
        task.loadFromRemote();
        return updateTask(task);
    }
}
