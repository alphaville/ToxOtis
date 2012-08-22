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
package org.opentox.toxotis.database;

import org.junit.Test;
import org.opentox.toxotis.database.global.DbConfiguration;
import org.opentox.toxotis.database.pool.DataSourceFactory;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class TestUtils {

    public static void setUpDB() throws Exception {
        DbConfiguration.setC3p0PropertiedFile(DbConfiguration.TEST_C3P0_FILE);
        assertTrue(DataSourceFactory.getInstance().ping(10));
        if (!DbConfiguration.getInstance().getProperpties().getProperty("c3p0.jdbcUrl").
                equals("jdbc:mysql://localhost:3306/toxotisdb2Test?"
                + "useUnicode=true&characterEncoding=UTF8&characterSetResults=UTF-8")) {
            throw new Exception("Wrong JDBC URL");
        }
    }
    
    @Test
    public void testSomething(){
        assertTrue("Nothing to test!",1==1);
    }
}
