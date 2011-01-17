package org.opentox.toxotis;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Algorithm;
import org.opentox.toxotis.core.component.Compound;
import org.opentox.toxotis.core.component.Conformer;
import org.opentox.toxotis.core.component.Dataset;
import org.opentox.toxotis.core.component.Model;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {

    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AppTest.class);

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
        System.out.println("Testing download and parsing of algorithms");
        VRI algorithmMlrUri = Services.ntua().augment("algorithm", "mlr");
        Algorithm algorithm = null;
        try {
            algorithm = new Algorithm(algorithmMlrUri).loadFromRemote();
        } catch (ToxOtisException ex) {
            System.out.println(ex.getCode());
            if (!ErrorCause.CommunicationError.equals(ex.getCode()) && !ErrorCause.ConnectionException.equals(ex.getCode())) {
                logger.error("Exception while ", ex);
                throw ex;
            }
            logger.warn("It seems that the remote service at "
                    + algorithmMlrUri + " encounters some problems.", ex);
            return ;
        }
        assertNotNull(algorithm);
        assertNotNull(algorithm.getParameters());
        assertEquals(algorithmMlrUri, algorithm.getUri());
        assertEquals(0, algorithm.getParameters().size());
        assertTrue(!algorithm.getMeta().isEmpty());
    }

    public void testInitializeComponents() throws Exception {
        System.out.println("Testing initialization of components");
        VRI vri = new VRI();
        Dataset ds = new Dataset();
        Algorithm algorithm = new Algorithm();
        Compound compound = new Compound();
        Conformer conformer = new Conformer();
        Model model = new Model();
        Task task = new Task();

    }
//    public void testGetProtectedDataset() throws Exception {
//        AuthenticationToken at = new AuthenticationToken("Sopasakis", "abfhs8y");
//        Dataset ds = new Dataset(new VRI("https://ambit.uni-plovdiv.bg:8443/ambit2/dataset/6"));
//        System.out.println(at);
//        ds.loadFromRemote(at);
//    }
}
