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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.engine.ROG;
import static org.junit.Assert.*;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.database.pool.DataSourceFactory;

/**
 *
 * @author chung
 */
public class DeleteUserTest {

    public DeleteUserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        assertTrue(DataSourceFactory.getInstance().ping(10));
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        DataSourceFactory.getInstance().close();
    }

    @Test
    public void testDeleteGuest() throws DbException {
        boolean assertionError = false;
        try {
            DeleteUser delete = new DeleteUser(User.GUEST.getUid());
            delete.delete();
            fail("It should have failed");
        } catch (final AssertionError er) {
            assertionError = true;
        }
        assertTrue(assertionError);
    }

    @Test
    public void testDeleteUser() throws DbException {
        ROG rog = new ROG();
        User u = rog.nextUser();
        AddUser add = new AddUser(u);
        assertTrue(add.write()>0);
        add.close();

        ListUsers lister = new ListUsers();
        lister.setMode(ListUsers.ListUsersMode.BY_UID);
        IDbIterator<String> it = lister.list();
        String uid = null;       
        boolean hasNext = false;
        boolean execution_flag = false;
        loo:
        while (it.hasNext()) {
            hasNext = true;
            uid = it.next();
            if (!uid.equals(User.GUEST.getUid())) {
                execution_flag = true;
                DeleteUser deleter = new DeleteUser(uid);
                assertEquals(1, deleter.delete());
                break loo;
            }
        }
        it.close();
        lister.close();
        assertTrue(hasNext);
        assertTrue(execution_flag);
    }
}
