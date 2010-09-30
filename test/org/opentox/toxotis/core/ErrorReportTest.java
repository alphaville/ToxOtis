package org.opentox.toxotis.core;

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
public class ErrorReportTest {

    public ErrorReportTest() {
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
    public void testRdfSerialization() {
        ErrorReport remote = new ErrorReport();
        remote.setActor("http://other.com/exception/100");
        remote.setHttpStatus(403);

        ErrorReport er = new ErrorReport();
        er.setActor("http://server.com/fault/12");
        er.setDetails("Details about the exceptional event");
        er.setErrorCode("143DD");
        er.setHttpStatus(400);
        er.setMessage("Client Error");
        er.setErrorCause(remote);
        
        er.asOntModel().write(System.out);
    }

}