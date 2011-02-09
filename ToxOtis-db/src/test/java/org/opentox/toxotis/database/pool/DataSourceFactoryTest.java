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

package org.opentox.toxotis.database.pool;

import java.sql.Connection;
import java.sql.SQLException;
import junit.framework.TestCase;
import org.opentox.toxotis.database.LoginInfo;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 *
 * @author chung
 */
public class DataSourceFactoryTest extends TestCase {

    public DataSourceFactoryTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSomeMethod() throws ServiceInvocationException, ToxOtisException {
        try {
            LoginInfo li = LoginInfo.LOGIN_INFO;

            DataSourceFactory factory = DataSourceFactory.getInstance();
            String connectionUri = factory.getConnectionURI(li);
            System.out.println(connectionUri);


            Connection connection = factory.getDataSource(connectionUri).getConnection();

            assertNotNull(connection);
            assertTrue(factory.ping(li, 5));

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Database is inaccessible!");
        }
    }
}
