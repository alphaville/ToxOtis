package org.opentox.toxotis.core;

import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.collection.Services;
import static org.junit.Assert.*;

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
    public void testLoadFromRemote() throws URISyntaxException, ToxOtisException{
        VRI vri = new VRI(Services.AMBIT_UNI_PLOVDIV.augment("dataset","9"));
        Dataset ds = new Dataset(vri);
        ds.loadFromRemote();
        System.out.println(ds.getDataEntries().get(0).getFeatureValue(0).getFeature());
    }

}