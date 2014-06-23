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
package org.opentox.toxotis.database.engine.metainfo;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.opentox.toxotis.util.ROG;
import static org.junit.Assert.*;
import org.junit.Test;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.database.engine.task.AddTask;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;

/**
 *
 * @author chung
 */
public class UpdateMetaInfoTest {

    public UpdateMetaInfoTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        org.opentox.toxotis.database.TestUtils.setUpDB();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        org.opentox.toxotis.database.pool.DataSourceFactory.getInstance().close();
    }

    @Test 
    public void testUpdateMetaData() throws DbException {
        final ROG rog = new ROG();
        
        Task t = rog.nextTask(2);
        t.setMeta(rog.nextMeta());
        
        AddTask task_adder = new AddTask(t);
        task_adder.write();
        task_adder.close();
        
        UpdateMetaInfo updater = new UpdateMetaInfo();
        updater.setUpdateMode(UpdateMetaInfo.UpdateMode.REPLACE);
        updater.setComponentId(t.getUri().getId());
        updater.setMeta(new MetaInfoImpl().addComment(rog.nextString(20)));
        assertEquals(2, updater.update()); // 2 rows updated: one for `Task` and 1 for `MetaInfo`.
        updater.close();
        
        //TODO Make sure the metadata object was updated properly!
    }
}
