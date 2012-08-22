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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.engine.ROG;
import static org.junit.Assert.*;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;

/**
 *
 * @author chung
 */
public class UpdateTaskTest {

    private static final ROG __ROG = new ROG();
    private static String taskInDb = null;
    private static volatile Throwable failure = null;

    public UpdateTaskTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        org.opentox.toxotis.database.TestUtils.setUpDB();
        AddTask adder = new AddTask(__ROG.nextTask(1));
        adder.write();
        adder.close();

        ListTasks lister = new ListTasks();
        IDbIterator<String> it = lister.list();
        if (it.hasNext()) {
            taskInDb = it.next();
        }
        it.close();
        lister.close();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        org.opentox.toxotis.database.pool.DataSourceFactory.getInstance().close();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testUpdateHttpStatus() throws DbException {
        Task t = new Task(Services.anonymous().augment("task", taskInDb));
        t.setHttpStatus(__ROG.nextFloat());
        UpdateTask ut = new UpdateTask(t);
        ut.setUpdateHttpStatus(true);
        ut.update();
        ut.close();

        FindTask finder = new FindTask(Services.anonymous(), true, true);
        finder.setSearchById(taskInDb);
        IDbIterator<Task> iterator = finder.list();
        if (iterator.hasNext()) {
            Task found = iterator.next();
            assertTrue(Math.pow(t.getHttpStatus() - found.getHttpStatus(), 2) < 0.00000001);
        }
        iterator.close();
        finder.close();
    }

    @Test
    public void testUpdateResultUri() throws DbException {
        Task t = new Task(Services.anonymous().augment("task", taskInDb));
        t.setResultUri(__ROG.nextVri());
        UpdateTask ut = new UpdateTask(t);
        ut.setUpdateResultUri(true);
        ut.update();
        ut.close();

        FindTask finder = new FindTask(Services.anonymous(), true, true);
        finder.setSearchById(taskInDb);
        IDbIterator<Task> iterator = finder.list();
        if (iterator.hasNext()) {
            Task found = iterator.next();
            assertEquals(t.getResultUri(), found.getResultUri());
        }
        iterator.close();
        finder.close();
    }

    @Test
    public void testUpdateStatus() throws DbException {
        Task t = new Task(Services.anonymous().augment("task", taskInDb));
        t.setStatus(__ROG.nextTaskStatus());
        UpdateTask ut = new UpdateTask(t);
        ut.setUpdateTaskStatus(true);
        ut.update();
        ut.close();

        FindTask finder = new FindTask(Services.anonymous(), true, true);
        finder.setSearchById(taskInDb);
        IDbIterator<Task> iterator = finder.list();
        if (iterator.hasNext()) {
            Task found = iterator.next();
            assertEquals(t.getStatus(), found.getStatus());
        }
        iterator.close();
        finder.close();
    }

    @Test
    public void testUpdateVarious() throws DbException {
        Task t = new Task(Services.anonymous().augment("task", taskInDb));
        t.setStatus(__ROG.nextTaskStatus());
        t.setErrorReport(__ROG.nextErrorReport(3));
        t.setDuration(__ROG.nextLong());
        UpdateTask ut = new UpdateTask(t);
        ut.setUpdateTaskStatus(true);
        ut.setUpdateErrorReport(true);
        ut.setUpdateDuration(true);
        ut.setUpdateAndRegisterErrorReport(true);
        ut.update();
        ut.close();

        FindTask finder = new FindTask(Services.anonymous(), true, true);
        finder.setSearchById(taskInDb);
        IDbIterator<Task> iterator = finder.list();
        if (iterator.hasNext()) {
            Task found = iterator.next();
            assertEquals(t.getStatus(), found.getStatus());
            assertEquals(t.getDuration(), found.getDuration());
            assertEquals(t.getErrorReport().getUri().getId(),
                    found.getErrorReport().getUri().getId());
            assertEquals(t.getErrorReport().getActor(), found.getErrorReport().getActor());
            assertEquals(t.getErrorReport().getDetails(), found.getErrorReport().getDetails());
            assertEquals(t.getErrorReport().getMessage(), found.getErrorReport().getMessage());
            assertEquals(t.getErrorReport().getMeta(), found.getErrorReport().getMeta());
            assertEquals(t.getErrorReport().getErrorCause().getMeta(),
                    found.getErrorReport().getErrorCause().getMeta());
            assertNotNull(t.getErrorReport().getErrorCause().getErrorCause());
            assertNotNull(t.getErrorReport().getErrorCause().getErrorCause().getMeta());
            assertNotNull(t.getErrorReport().getErrorCause().getErrorCause().getActor());
        }
        iterator.close();
        finder.close();
    }

    @Test
    public void testUpdateError() throws DbException {
        Task t = new Task(Services.anonymous().augment("task", taskInDb));
        t.setErrorReport(__ROG.nextErrorReport(5));
        UpdateTask ut = new UpdateTask(t);
        ut.setUpdateErrorReport(true);
        ut.setUpdateAndRegisterErrorReport(true);
        ut.update();
        ut.close();

        FindTask finder = new FindTask(Services.anonymous(), true, true);
        finder.setSearchById(taskInDb);
        IDbIterator<Task> iterator = finder.list();
        if (iterator.hasNext()) {
            Task found = iterator.next();
            assertEquals(t.getErrorReport().getUri().getId(),
                    found.getErrorReport().getUri().getId());
            assertEquals(t.getErrorReport().getActor(), found.getErrorReport().getActor());
            assertEquals(t.getErrorReport().getDetails(), found.getErrorReport().getDetails());
            assertEquals(t.getErrorReport().getMessage(), found.getErrorReport().getMessage());
            assertEquals(t.getErrorReport().getMeta(), found.getErrorReport().getMeta());
        }
        iterator.close();
        finder.close();
    }

    @Test
    public void testUpdateDuration() throws DbException {
        Task t = new Task(Services.anonymous().augment("task", taskInDb));
        t.setDuration(__ROG.nextLong());
        UpdateTask ut = new UpdateTask(t);
        ut.setUpdateDuration(true);
        ut.update();
        ut.close();

        FindTask finder = new FindTask(Services.anonymous(), true, true);
        finder.setSearchById(taskInDb);
        IDbIterator<Task> iterator = finder.list();
        if (iterator.hasNext()) {
            Task found = iterator.next();
            assertEquals(t.getDuration(), found.getDuration());
        }
        iterator.close();
        finder.close();
    }

    @Test
    public void testUpdateSql() throws DbException {
        Task t = __ROG.nextTask(2);
        AddTask adder = new AddTask(t);
        adder.write();
        adder.close();

        //TODO: First get a list of tasks and pick randomly one - then update it!
        Task newTask = t;
        newTask.setDuration(666L).
                setHttpStatus(101).
                setMeta(new MetaInfoImpl().addComment("XX"));
        newTask.setPercentageCompleted(23.5234f);
        UpdateTask ut = new UpdateTask(newTask);
        ut.setUpdateErrorReport(true);
        ut.setUpdateHttpStatus(true);
        ut.setUpdatePercentageCompleted(true);
        ut.setUpdateTaskStatus(true);
        ut.setUpdateResultUri(true);
        ut.setUpdateDuration(true);
        ut.setUpdateMeta(false);
        ut.update();
        ut.close();

        FindTask finder = new FindTask(Services.anonymous(), true, true);
        finder.setSearchById(t.getUri().getId());
        IDbIterator<Task> iterator = finder.list();
        if (iterator.hasNext()) {
            Task found = iterator.next();
            assertEquals(Task.Status.ERROR, found.getStatus());
            assertEquals(101f, found.getHttpStatus());
        }
        iterator.close();
        finder.close();
    }

    @Test
    public void testCrash() throws DbException, InterruptedException {
        int poolSize = 80;
        int folds = 4 * poolSize + 100;// just to make sure!!! (brutal?!)
        final ExecutorService es = Executors.newFixedThreadPool(poolSize);
        for (int i = 1; i <= folds; i++) {
            es.submit(new Runnable() {

                @Override
                public void run() {
                    try {
                        Task t = new Task(Services.anonymous().augment("task", taskInDb));
                        t.setHttpStatus(__ROG.nextFloat());
                        UpdateTask ut = new UpdateTask(t);
                        ut.setUpdateHttpStatus(true);
                        ut.update();
                        ut.close();
                    } catch (final Throwable ex) {
                        failure = ex;
                        ex.printStackTrace();
                    }
                }
            });
        }

        es.shutdown();
        while (!es.isTerminated()) {
            Thread.sleep(100);
        }

        if (failure != null) {
            fail();
        }
    }

    
}