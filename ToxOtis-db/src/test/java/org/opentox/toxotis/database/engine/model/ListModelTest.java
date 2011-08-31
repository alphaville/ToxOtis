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
package org.opentox.toxotis.database.engine.model;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.database.IDbIterator;
import static org.junit.Assert.*;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 */
public class ListModelTest {

    private static volatile Throwable failure = null;

    public ListModelTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        System.out.println("Done!!!");
        org.opentox.toxotis.database.pool.DataSourceFactory.getInstance().close();
    }

    @Before
    public void setUp() {
    }

    @After
    public synchronized void tearDown() {
    }

    @Test
    public void testListModelMultithreadedly() throws InterruptedException {
        System.out.println("Crash test: multi-threaded reading from the DB");
        /*
         * c3p0.numHelperThreads=110 and it works fine!
         * Note... Setting minPoolSize=maxPoolSize=initialPoolSize=1000 and numHelperThreads=50
         * does not solve the problem!
         */
        int poolSize = 50;
        int folds = 1000 * poolSize + 10;// just to make sure!!! (brutal?!)
        final ExecutorService es = Executors.newFixedThreadPool(poolSize);
        for (int i = 1; i <= folds; i++) {

            es.submit(new Runnable() {

                @Override
                public void run() {
                    try {
                        new ListModelTest().testListModels();
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

    @Test
    public void testListModels() throws DbException {
        ListModel lister = new ListModel();
        lister.setPageSize(10);
        IDbIterator<String> list = lister.list();
        int i=0;
        while (list.hasNext()) {
            assertNotNull(list.next());
            i++;
        }
        assertEquals(10, i);
        list.close();
        lister.close();

    }
}
