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
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.ErrorReport;
import org.opentox.toxotis.database.DbWriter;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.util.ROG;
import static org.junit.Assert.*;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.database.pool.DataSourceFactory;
import org.opentox.toxotis.ontology.collection.OTClasses;

/**
 *
 * @author chung
 */
public class FindErrorTest {

    public FindErrorTest() {
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
    public synchronized void tearDown() {
    }

    @Test
    public void testFindError() throws DbException {
        FindError fe = new FindError(Services.ntua());
        fe.setRetrieveStackTrace(false);
        fe.setPageSize(10);
        IDbIterator<ErrorReport> it = fe.list();
        VRI currentVri = null;
        while (it.hasNext()) {
            currentVri = it.next().getUri();
            assertNotNull(currentVri);
            assertEquals(OTClasses.errorReport(), currentVri.getOntologicalClass());
        }
        it.close();
        fe.close();
    }

    @Test
    public void testFindError2() throws DbException {
        FindError fe = new FindError(Services.ntua());
        fe.setRetrieveStackTrace(false);
        fe.setPageSize(1);
        IDbIterator<ErrorReport> it = fe.list();

        while (it.hasNext()) {
            assertNotNull(it.next().getUri());
        }
        it.close();
        fe.close();
    }

    @Test
    public void testFindHugeReport() throws DbException {
        ErrorReport er = new ROG().nextErrorReport(100);
        DbWriter writer = new AddErrorReport(er);
        writer.write();
        writer.close();

        FindError fe = new FindError(Services.ntua());
        fe.setRetrieveStackTrace(true);
        fe.setPageSize(1);
        fe.setSearchById(er.getUri().getId());

        IDbIterator<ErrorReport> it = fe.list();
        boolean found = false;
        while (it.hasNext()) {
            found = true;
            assertEquals(er.getUri().getId(), it.next().getUri().getId());
        }
        assertTrue(found);
        it.close();
        fe.close();
    }

    @Test
    public void testCrashDB() throws DbException {
        int N = 10;
        for (int i = 0; i < N; i++) {
            ErrorReport er = new ROG().nextErrorReport(20);
            DbWriter writer = new AddErrorReport(er);
            writer.write();
            writer.close();

            FindError fe = new FindError(Services.ntua());
            fe.setRetrieveStackTrace(true);
            fe.setSearchById(er.getUri().getId());
            
            IDbIterator<ErrorReport> it = fe.list();
            boolean found = false;
            while (it.hasNext()) {                
                found = true;
                ErrorReport nextReport = it.next();
                assertEquals(er.getUri().getId(), nextReport.getUri().getId());
                assertEquals(er.getMessage(), nextReport.getMessage());
                assertEquals(er.getDetails(), nextReport.getDetails());
                assertEquals(er.getActor(), nextReport.getActor());
                assertEquals(er.getMeta(), nextReport.getMeta());
                assertEquals(er.getErrorCause().getUri().getId(), nextReport.getErrorCause().getUri().getId());
                assertEquals(er.getErrorCause().getActor(), nextReport.getErrorCause().getActor());
            }
            assertTrue(found);
            it.close();
            fe.close();
        }
    }
}