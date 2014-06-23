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
package org.opentox.toxotis.database.engine;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.engine.model.ListModel;
import org.opentox.toxotis.database.pool.DataSourceFactory;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class DisableComponentTest {

    public DisableComponentTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        org.opentox.toxotis.database.TestUtils.setUpDB();
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

    @Test
    public void testALot() throws Exception {

        for (int i = 0; i < 5; i++) {
            testDisable();
        }
    }

    @Test
    public void testEnableDisable() throws Exception {
        //TODO Implement this test
    }

    @Test
    public void testDisable() throws Exception {
        ListModel list = new ListModel();
        list.setPageSize(1);
        IDbIterator<String> modelIt = list.list();
        String toBeDeleted = null;
        while (modelIt.hasNext()) {
            toBeDeleted = modelIt.next();
            new DisableComponent(modelIt.next(), "no such thing!").disable();
        }
        modelIt.close();
        list.close();

        if (toBeDeleted != null) {
            list = new ListModel();
            list.setIncludeDisabled(false);
            list.setWhere(String.format("Model.id='%s'", toBeDeleted));
            IDbIterator<String> iter = list.list();
            assertFalse(iter.hasNext());
            iter.close();
            list.close();
        }
    }
}
