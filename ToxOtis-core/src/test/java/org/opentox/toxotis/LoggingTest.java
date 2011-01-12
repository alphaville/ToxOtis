package org.opentox.toxotis;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class LoggingTest extends TestCase{

    public LoggingTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(LoggingTest.class);
    }

    public void testLogging() throws Exception {

    }

}