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

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.sql.Connection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pantelis Sopasakis
 */
public class DataSourceFactoryTest {

    public DataSourceFactoryTest() {
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
    public void testInvokeDataSource() {
        try {
            DataSourceFactory factory = DataSourceFactory.getInstance();

            Connection connection = factory.getDataSource().getConnection();
            assertNotNull(connection);
            assertTrue(factory.ping(50));
//            assertEquals("TEST00001==/", DbConfiguration.getInstasnce().getProperpties().getProperty("key"));
            assertEquals("jdbc:mysql://localhost:3306/toxotisdb2Test?useUnicode=true&characterEncoding=UTF8&characterSetResults=UTF-8",
                    DataSourceFactory.getInstance().getConnection().getMetaData().getURL());
            ComboPooledDataSource ds = (ComboPooledDataSource) DataSourceFactory.getInstance().getDataSource();
            assertEquals(1025, ds.getMaxPoolSize());
            assertEquals(51, ds.getMinPoolSize());
            assertEquals(99, ds.getInitialPoolSize());
            assertEquals(3, ds.getAcquireIncrement());
            assertEquals(48, ds.getAcquireRetryAttempts());
            assertEquals(1077, ds.getAcquireRetryDelay());
            assertEquals(111, ds.getNumHelperThreads());
            assertEquals(0, ds.getCheckoutTimeout());
            assertTrue(ds.isTestConnectionOnCheckin());
            assertTrue(ds.isTestConnectionOnCheckout());
            assertNotNull(ds.getUser());
            assertNotNull(ds.getPassword());            
        } catch (Exception ex) {
            fail("Database is inaccessible! " + ex);
        }
    }
}
