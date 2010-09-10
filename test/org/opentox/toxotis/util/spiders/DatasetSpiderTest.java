package org.opentox.toxotis.util.spiders;

import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.Dataset;
import static org.junit.Assert.*;

/**
 *
 * @author hampos
 */
public class DatasetSpiderTest {

    public DatasetSpiderTest() {
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
    public void testDataset() throws URISyntaxException, ToxOtisException {
        
        DatasetSpider spider = new DatasetSpider(
                new VRI("http://apps.ideaconsult.net:8080/ambit2/dataset/6"));
             //   new VRI("http://apps.ideaconsult.net:8080/ambit2/compound/999/conformer/999"));

        Dataset ds = spider.parse();
        System.out.println(ds.getMeta());
        System.out.println(ds.getDataEntries().get(0).getFeatureValue(7).getFeature().getUri());
    }

}