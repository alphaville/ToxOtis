package org.opentox.toxotis.factory;

import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Dataset;
import org.opentox.toxotis.util.spiders.DatasetSpider;
import weka.core.Instances;
import static org.junit.Assert.*;

/**
 *
 * @author hampos
 */
public class DatasetFactoryTest {

    public DatasetFactoryTest() {
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
    public void testCreateFromArff() throws URISyntaxException , ToxOtisException{
        
        Dataset ds = new DatasetSpider(Services.ideaconsult().augment("dataset","9").addUrlParameter("max", "1")).parse();
        Instances instances = ds.getInstances();
        System.out.println(ds.getInstances());
        Dataset newds = DatasetFactory.createFromArff(instances);
        System.out.println(newds.getInstances());

    }

}