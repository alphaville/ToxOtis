package org.opentox.toxotis;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Algorithm;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() throws Exception {
        VRI algorithmMlrUri = Services.ntua().augment("algorithm", "mlr");
        Algorithm algorithm = new Algorithm(algorithmMlrUri).loadFromRemote();
        assertNotNull(algorithm);
        assertNotNull(algorithm.getParameters());
        assertEquals(algorithmMlrUri, algorithm.getUri());
        assertEquals(0,algorithm.getParameters().size());
    }
}
