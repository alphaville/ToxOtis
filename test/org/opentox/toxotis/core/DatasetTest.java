package org.opentox.toxotis.core;

import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;

/**
 *
 * @author chung
 */
public class DatasetTest {

    public DatasetTest() {
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
    public void testLoadFromRemote() throws URISyntaxException, ToxOtisException, InterruptedException {

        int datasetId = 9;
        System.out.println("Test on dataset : http://apps.ideaconsult.net:8080/ambit2/dataset/" + datasetId);
        for (int i = 1000; i <= 1000; i += 50) {
            for (int j = 0; j < 8; j++) {
                VRI vri = new VRI(Services.ideaconsult());
                vri.augment("dataset", Integer.toString(datasetId)).removeUrlParameter("max").addUrlParameter("max", i);
                long t1 = System.currentTimeMillis();
                Dataset ds = new Dataset(vri).loadFromRemote();
                long t2 = System.currentTimeMillis();
                weka.core.Instances data = ds.getInstances();
                long t3 = System.currentTimeMillis();
                System.out.println(i + "," + (t2 - t1) + "," + (t3 - t2));
            }
        }


    }
}
