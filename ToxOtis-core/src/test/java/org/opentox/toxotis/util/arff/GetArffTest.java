package org.opentox.toxotis.util.arff;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class GetArffTest {

    public GetArffTest() {
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
    public void testIrrelevantThings() throws Exception {
        long mem0 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        GetArff getArff = new GetArff(new VRI("http://apps.ideaconsult.net:8080/ambit2/dataset/585036?max=50"));
        getArff.getInstances();
        long mem1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }
}
