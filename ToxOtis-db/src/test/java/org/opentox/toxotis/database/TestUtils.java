/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.toxotis.database;

import org.opentox.toxotis.database.global.DbConfiguration;
import org.opentox.toxotis.database.pool.DataSourceFactory;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class TestUtils {
    
    public static void setUpDB() throws Exception {
        DbConfiguration.setC3p0PropertiedFile(DbConfiguration.TEST_C3O0_FILE);
        assertTrue(DataSourceFactory.getInstance().ping(10));
        assertEquals("TEST00001==/", DbConfiguration.getInstance().getProperpties().getProperty("key"));
    }
    
}
