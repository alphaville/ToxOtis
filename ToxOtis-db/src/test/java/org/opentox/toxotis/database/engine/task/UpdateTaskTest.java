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
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Task;
import static org.junit.Assert.*;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;

/**
 *
 * @author chung
 */
public class UpdateTaskTest {

    public UpdateTaskTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
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
    public void testUpdateSql() throws DbException {
        //TODO: First get a list of tasks and pick randomly one - then update it!
        Task newTask = new Task(Services.ntua().augment("task","00024363-e51e-468b-b42c-b1f6491614de")).setDuration(666L).setHttpStatus(101).setMeta(new MetaInfoImpl().addComment("XX"));
        newTask.setPercentageCompleted(23.5234f);
        UpdateTask ut = new UpdateTask(newTask);
            ut.setUpdateErrorReport(true);
            ut.setUpdateHttpStatus(true);
            ut.setUpdatePercentageCompleted(true);
            ut.setUpdateTaskStatus(true);
            ut.setUpdateResultUri(true);
            ut.setUpdateDuration(true);
            ut.setUpdateMeta(true);
        ut.update();
        ut.close();
    }
    
    @Test
    public void testCrash() throws DbException {
        
    }
    
    @Test
    public void testUpdateDuration() throws DbException {
        
    }
    
    @Test
    public void testUpdateErrorReportAndFind() throws DbException {
        
    }

}