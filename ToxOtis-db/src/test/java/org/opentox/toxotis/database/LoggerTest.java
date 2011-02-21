/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.toxotis.database;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class LoggerTest {

    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LoggerTest.class);

    public LoggerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSomeMethod() throws InterruptedException {
        for (int i = 0; i < 70; i++) {
            logger.error("message", new NullPointerException());
            Thread.sleep(1000);
        }
    }
}
