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

package org.opentox.toxotis.database.engine.error;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.core.component.ErrorReport;
import static org.junit.Assert.*;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author chung
 */
public class AddErrorReportTest {

    public AddErrorReportTest() {
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
    public synchronized void tearDown() {
        org.opentox.toxotis.database.pool.DataSourceFactory.getInstance().close();
    }

    @Test
    public void testSomeMethod() throws DbException {
        ErrorReport er_trace_2 = new ErrorReport(400, "fdsag", "agtdsfd", "asdfsaf", "jyfrggr");
        ErrorReport er_trace_1 = new ErrorReport(400, "fdsag", "agtdsfd", "asdfsaf", "jyfrggr");
        ErrorReport er = new ErrorReport(502, "fdsag", "agtdsfd", "asdfsaf", "jyfrggr");
        er_trace_1.setErrorCause(er_trace_2);
        er.setErrorCause(er_trace_1);

        AddErrorReport adder = new AddErrorReport(er);
        System.out.println(adder.write());
        adder.close();
    }

}