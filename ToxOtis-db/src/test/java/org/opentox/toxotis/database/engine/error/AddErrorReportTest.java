package org.opentox.toxotis.database.engine.error;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.core.component.ErrorReport;
import static org.junit.Assert.*;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author chung
 */
public class AddErrorReportTest {

    public AddErrorReportTest() {
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
    public void testSomeMethod() throws DbException {
        ErrorReport er_trace_2 = new ErrorReport(400, "fdsag", "agtdsfd", "asdfsaf", "jyfrggr");
        ErrorReport er_trace_1 = new ErrorReport(400, "fdsag", "agtdsfd", "asdfsaf", "jyfrggr");
        ErrorReport er = new ErrorReport(502, "fdsag", "agtdsfd", "asdfsaf", "jyfrggr");
        er_trace_1.setErrorCause(er_trace_2);
        er.setErrorCause(er_trace_1);

        
        System.out.println(new AddErrorReport(er).write());
    }

}