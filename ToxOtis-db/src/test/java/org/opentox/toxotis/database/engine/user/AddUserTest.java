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
package org.opentox.toxotis.database.engine.user;

import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.engine.ROG;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.database.pool.DataSourceFactory;

import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class AddUserTest {

    public AddUserTest() {
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

    @Test
    public void testAddUser() throws Exception {
        ROG rog = new ROG();
        User mockUser = rog.nextUser();
        
        AddUser au = new AddUser(mockUser);
        assertEquals(1, au.write());
        au.close();

        FindUser finder = new FindUser();
        finder.setWhere("uid='" + mockUser.getUid() + "'");
        IDbIterator<User> iterator = finder.list();
        if (iterator.hasNext()) {
            User user = iterator.next();
            assertEquals(mockUser.getMail(), user.getMail());
            assertEquals(mockUser.getUid(), user.getUid());
            assertEquals(mockUser.getName(), user.getName());
            assertEquals(mockUser.getMaxParallelTasks(), user.getMaxParallelTasks());
            assertEquals(mockUser.getMaxModels(), user.getMaxModels());
            assertEquals(mockUser.getMaxBibTeX(), user.getMaxBibTeX());
        } else {
            fail("No such user in the database");
        }
        iterator.close();
        finder.close();

        // CLOSE THE DATASOURCE!
    }
}
