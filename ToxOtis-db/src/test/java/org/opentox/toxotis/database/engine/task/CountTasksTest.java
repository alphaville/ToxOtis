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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.database.engine.ROG;
import org.opentox.toxotis.database.engine.model.AddModelTest;
import org.opentox.toxotis.database.pool.DataSourceFactory;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class CountTasksTest {

    public CountTasksTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        assertTrue(DataSourceFactory.getInstance().ping(10));
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        DataSourceFactory.getInstance().close();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /*
     * Count tasks for a particular user (with given UID)
     */
    @Test
    public void testCountTasks() throws Exception {
             
        new AddTaskTest().testWriteReadTask();
        
        CountTasks ct = null;
        try {
            ct = new CountTasks();
            ct.setWhere(String.format("createdBy='%s'", User.GUEST.getUid()));
            int result = ct.count();
            assertTrue(result >= 1);
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (ct != null) {
                ct.close();
            }
        }
    }

    @Test
    public void testCountTasks_zero() throws Exception {
        CountTasks ct = null;
        try {
            ct = new CountTasks();
            ct.setWhere(String.format("createdBy='%s'", "nosuchuser@in-silico.ch"));
            int result = ct.count();
            assertEquals(0, result);
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (ct != null) {
                ct.close();
            }
        }
    }
}
