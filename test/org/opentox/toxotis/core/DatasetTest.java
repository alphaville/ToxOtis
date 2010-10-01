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

        int datasetId = 10;
        System.out.println("Test on dataset : http://apps.ideaconsult.net:8080/ambit2/dataset/" + datasetId);
        for (int i = 1000; i <= 1000; i += 100) {
            for (int j = 0; j < 8; j++) {
                VRI vri = new VRI(Services.ideaconsult());
                vri.augment("dataset", Integer.toString(datasetId)).removeUrlParameter("max").addUrlParameter("max", i == 0 ? 1 : i);
                Dataset ds = new Dataset(vri).loadFromRemote();
                weka.core.Instances data = ds.getInstances();
                System.out.println(
                        //(i == 0 ? 1 : i )+ "," +
                        ds.getTimeDownload() + "," + ds.getTimeParse() + "," + ds.getTimeInstancesConversion());
            }
        }


    }
}
