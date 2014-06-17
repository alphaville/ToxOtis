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

import java.util.UUID;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.ErrorReport;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.util.ROG;
import static org.junit.Assert.*;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.database.pool.DataSourceFactory;

/**
 *
 * @author chung
 */
public class AddErrorReportTest {

    public AddErrorReportTest() {
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
    public void testLongError() throws DbException {
        int N = 1500;
        ErrorReport er = new ROG().nextErrorReport(N);
        assertNotNull(er.getUri());
        ErrorReport current = er;
        int n = 0;
        while (current != null) {
            current = current.getErrorCause();
            n++;
        }
        assertEquals(N, n);
        AddErrorReport adder = new AddErrorReport(er);
        assertEquals(2 * N, adder.write());
        adder.close();
    }

    @Test
    public void testReportNullMeta() throws DbException {

        ErrorReport er = new ErrorReport(502, "sfd", "34gtdgdf", "23ffsg43tsgff", "bg");
        er.setMeta(null);
        er.setErrorCause(null);

        AddErrorReport adder = new AddErrorReport(er);
        assertEquals(2, adder.write());
        adder.close();

        FindError finder = new FindError(Services.ntua());
        finder.setSearchById(er.getUri().getId());
        IDbIterator<ErrorReport> iterator = finder.list();
        if (iterator.hasNext()) {
            ErrorReport found = iterator.next();
            assertEquals(er.getMessage(), found.getMessage());
            assertEquals(er.getDetails(), found.getDetails());
            assertEquals(er.getActor(), found.getActor());
            assertEquals(er.getHttpStatus(), found.getHttpStatus());
            assertNull(found.getErrorCause());

        } else {
            fail();
        }
        iterator.close();
        finder.close();
    }

    @Test
    public void testAddErrorReport() throws DbException {
        ErrorReport er_trace_2 = new ErrorReport(400, "fds43tgdfag", "agtdsfd", "asdfsaf", "jyfrggr");
        ErrorReport er_trace_1 = new ErrorReport(400, "fdsa5yaergg", "agtdsfd", "asdfsaf", "jyfrggr");
        ErrorReport er = new ErrorReport(502, "f3405u0t4985dsag", "agtdsfy5trefad", "asdfsaf", "jyfrggr");
        er.getMeta().addTitle(UUID.randomUUID().toString()).addTitle(UUID.randomUUID().toString());
        er_trace_1.getMeta().addTitle(UUID.randomUUID().toString()).addTitle(UUID.randomUUID().toString());
        er_trace_2.getMeta().addAudience(UUID.randomUUID().toString()).addCreator(UUID.randomUUID().toString());
        er_trace_2.getMeta().addComment(UUID.randomUUID().toString()).addIdentifier(UUID.randomUUID().toString());
        er_trace_1.setErrorCause(er_trace_2);
        er.setErrorCause(er_trace_1);

        AddErrorReport adder = new AddErrorReport(er);
        assertEquals(9, adder.write());
        adder.close();

        FindError finder = new FindError(Services.ntua());
        finder.setSearchById(er.getUri().getId());
        IDbIterator<ErrorReport> iterator = finder.list();
        if (iterator.hasNext()) {
            ErrorReport found = iterator.next();
            assertEquals(er.getMessage(), found.getMessage());
            assertEquals(er.getDetails(), found.getDetails());
            assertEquals(er.getActor(), found.getActor());
            assertEquals(er.getHttpStatus(), found.getHttpStatus());
            assertNotNull(found.getErrorCause());
            assertEquals(found.getMeta(), er.getMeta());
            assertNotNull(found.getErrorCause().getMeta());
            assertEquals(er_trace_1.getMeta(), found.getErrorCause().getMeta());
            assertEquals(er_trace_2.getMeta(), found.getErrorCause().getErrorCause().getMeta());
        } else {
            fail();
        }
        iterator.close();
        finder.close();
    }

    @Test
    public void testWriteReadErrorReport() throws DbException {
        ErrorReport er = new ROG().nextErrorReport(4);

        AddErrorReport adder = new AddErrorReport(er);
        adder.write();
        adder.close();

        FindError finder = new FindError(Services.ntua());
        finder.setSearchById(er.getUri().getId());
        IDbIterator<ErrorReport> iterator = finder.list();
        if (iterator.hasNext()) {
            ErrorReport found = iterator.next();
            assertEquals(er.getUri().getId(), found.getUri().getId());
            assertEquals(er.getMessage(), found.getMessage());
            assertEquals(er.getDetails(), found.getDetails());
            assertEquals(er.getActor(), found.getActor());
            assertEquals(er.getHttpStatus(), found.getHttpStatus());
            assertNotNull(found.getErrorCause());
            assertEquals(found.getMeta(), er.getMeta());
            assertNotNull(found.getErrorCause().getMeta());
        } else {
            fail();
        }
        iterator.close();
        finder.close();
    }
}
