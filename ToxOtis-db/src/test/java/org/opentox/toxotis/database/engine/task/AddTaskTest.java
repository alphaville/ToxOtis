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

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.ErrorReport;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.database.DbWriter;
import org.opentox.toxotis.database.IDbIterator;
import static org.junit.Assert.*;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.ResourceValue;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;

/**
 *
 * @author chung
 */
public class AddTaskTest {

    private static volatile Throwable failure = null;

    public AddTaskTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        
    }

    @AfterClass
    public synchronized static void tearDownClass() throws Exception {
        org.opentox.toxotis.database.pool.DataSourceFactory.getInstance().close();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testWriteReadTask() throws Exception {
        ErrorReport er_trace_2 = new ErrorReport(400, "fdsag", "agtdsfd", "asdfsaf", "jyfrggr");
        ErrorReport er_trace_1 = new ErrorReport(400, "fdsag", "agtdsfd", "asdfsaf", "jyfrggr");
        ErrorReport er = new ErrorReport(502, "fdsag", "agtdsfd", "asdfsaf", "jyfrggr");
        er_trace_1.setErrorCause(er_trace_2);
        er.setErrorCause(er_trace_1);


        Task t = new Task(Services.ntua().augment("task", UUID.randomUUID()));
        t.setMeta(new MetaInfoImpl().addTitle("ZZZZZZZZZz").
                addContributor("ME").
                addHasSource(new ResourceValue(new VRI("http://something.org/resource/model.234"), OTClasses.Model())));
        t.setErrorReport(er);
        t.setPercentageCompleted(0);
        t.setHttpStatus(407);
        t.setStatus(Task.Status.ERROR);

        User u = User.GUEST; // this user is already in the database - no need to re-add it.
        t.setCreatedBy(u);
        DbWriter writer = new AddTask(t);
        assertTrue(writer.write() > 0);
        writer.close(); // CLOSE the writer!!! (SHORTLY after you don't need it!!!)


        FindTask ft = new FindTask(new VRI("http://alphaville:4000/jaqpot"), false, false);
        ft.setWhere("Task.id='" + t.getUri().getId() + "'");
        IDbIterator<Task> iter = ft.list();

        while (iter.hasNext()) {
            Task nextTask = iter.next();
            MetaInfo mi = nextTask.getMeta();
            assertEquals(t.getMeta(), mi);
            assertEquals(t.getUri().getId(), nextTask.getUri().getId());
            assertEquals(t.getDuration(), nextTask.getDuration());
            assertEquals(t.getHttpStatus(), nextTask.getHttpStatus());
            assertEquals(t.getStatus(), nextTask.getStatus());
            assertEquals(t.getResultUri(), nextTask.getResultUri());
        }

        iter.close();
        ft.close();       
    }

    @Test
    public void testWriteTaskMultithreadedly() throws InterruptedException {
        int poolSize = 50;
        int folds = 5 * poolSize + 10;// just to make sure!!! (brutal?!)
        final ExecutorService es = Executors.newFixedThreadPool(poolSize);
        for (int i = 1; i <= folds; i++) {

            es.submit(new Runnable() {

                @Override
                public void run() {
                    try {
                        new AddTaskTest().testWriteReadTask();
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
