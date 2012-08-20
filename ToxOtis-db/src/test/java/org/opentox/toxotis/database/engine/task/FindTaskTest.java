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

import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.database.IDbIterator;
import static org.junit.Assert.*;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.database.pool.DataSourceFactory;

/**
 *
 * @author chung
 */
public class FindTaskTest {

    public FindTaskTest() {
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
    public void testSomeMethod() throws DbException, URISyntaxException {

        //TODO: Re-implement
        VRI baseUri = new VRI("http://alphaville:4000/jaqpot");
        FindTask ft = new FindTask(baseUri, true, true);
        ft.setPageSize(1);
        ft.setWhere("Task.status='ERROR' AND errorReport IS NOT NULL");
        IDbIterator<Task> it = ft.list();
        while (it.hasNext()) {
            Task nextTsk = it.next();
            assertNotNull(nextTsk.getCreatedBy());
            assertNotNull(nextTsk.getCreatedBy().getUid());
            System.out.println(nextTsk.getUri());
            System.out.println(nextTsk.getDuration());
            System.out.println(nextTsk.getErrorReport().getUri());
            System.out.println(nextTsk.getResultUri());
            System.out.println(nextTsk.getStatus());
            System.out.println(nextTsk.getCreatedBy().getMail());
        }
        // GOOD PRACTISE : Close the iterator AND the Finder!!!
        // i.e. the result set and the connection
        it.close();
        ft.close();
    }
}
