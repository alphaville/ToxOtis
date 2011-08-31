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
package org.opentox.toxotis.database.engine.qprf;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.qprf.QprfReport;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.engine.ROG;
import static org.junit.Assert.*;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author chung
 */
public class AddQprfTest {

    public AddQprfTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAddReport() throws DbException {
//        ROG rog = new ROG();
//        for (int i = 1; i < 10; i++) {
//            System.out.println(rog.nextString(10));
//        }
//
//        if (true) {
//            return;
//        }
//        QprfReport report = rog.nextReport(4);
//        AddQprf qprfAdder = new AddQprf(report);
//        System.out.println(qprfAdder.write());
//        qprfAdder.close();
//
//        ListQprf lister = new ListQprf();
//        lister.setWhere(String.format("id='%s'", report.getUri().getId()));
//        IDbIterator<String> iterator = lister.list();
//
//        if (!iterator.hasNext()) {
//            fail("Report not added!");
//        }
//        lister.close();

    }
}
