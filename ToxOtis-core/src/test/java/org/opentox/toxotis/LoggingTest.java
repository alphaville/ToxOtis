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
        org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LoggingTest.class);
        logger.info("This is a warning message (It's good if you can see it)");
        
    }

}