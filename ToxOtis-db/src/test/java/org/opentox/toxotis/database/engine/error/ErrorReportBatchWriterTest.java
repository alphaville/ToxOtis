
package org.opentox.toxotis.database.engine.error;

import java.sql.SQLException;
import java.util.UUID;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.ErrorReport;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class ErrorReportBatchWriterTest {

    public ErrorReportBatchWriterTest() {
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
    public void testSomeMethod() throws SQLException {
        // TODO review the generated test code and remove the default call to fail.
        ErrorReport er = new ErrorReport(200, "fdsag", "agtdsfd", "asdfsaf", "jyfrggr");
        System.out.println(er.getUri().getId());
        
        
    }

}