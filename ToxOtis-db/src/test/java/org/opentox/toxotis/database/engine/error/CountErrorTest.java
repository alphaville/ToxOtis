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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.core.component.ErrorReport;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.engine.DisableComponent;
import org.opentox.toxotis.database.engine.ROG;
import static org.junit.Assert.*;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.database.pool.DataSourceFactory;

/**
 *
 * @author chung
 */
public class CountErrorTest {

    public CountErrorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        org.opentox.toxotis.database.TestUtils.setUpDB();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        DataSourceFactory.getInstance().close();
    }

    @Test
    public void testCountError() throws Exception {
        CountError counter = new CountError();
        int count = counter.count();
        assertTrue(count >= 0);
        counter.close();

        ListError lister = new ListError();
        int flag = 0;
        IDbIterator<String> iterator = lister.list();
        while (iterator.hasNext()) {
            flag++;
        }
        assertEquals(flag, count);
        iterator.close();
        lister.close();
    }

    @Test
    public void testCountError2() throws Exception {
        CountError counter = new CountError();
        int countBefore = counter.count();
        assertTrue(countBefore >= 0);
        counter.close();

        ErrorReport er = new ErrorReport(502, "sfd", "34gtdgdf", "23ffsg43tsgff", "bg");
        er.setMeta(null);
        er.setErrorCause(null);

        AddErrorReport adder = new AddErrorReport(er);
        assertEquals(2, adder.write());
        adder.close();

        DisableComponent disabler = new DisableComponent(er.getUri().getId());
        disabler.disable();
        disabler.close();

        counter = new CountError();
        int countAfter = counter.count();
        assertEquals(countBefore, countAfter);
        counter.close();

        disabler = new DisableComponent(er.getUri().getId());
        disabler.enable();
        disabler.close();

        counter = new CountError();
        countAfter = counter.count();
        assertEquals(countBefore + 1, countAfter);
        counter.close();
    }
}
