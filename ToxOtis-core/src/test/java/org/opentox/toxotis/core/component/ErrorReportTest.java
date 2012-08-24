/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.toxotis.core.component;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class ErrorReportTest {

    public ErrorReportTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testVriGeneration() {
        ErrorReport er = new ErrorReport();
        VRI uri1 = er.getUri();
        assertNotNull(uri1);
        er.setActor("Me");
        VRI uri2 = er.getUri();
        assertNotNull(uri2);
        assertNotSame(uri1, uri2);
    }
}
