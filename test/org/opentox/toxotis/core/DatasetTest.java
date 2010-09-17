package org.opentox.toxotis.core;

import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.PostClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.ontology.WonderWebValidator;
import org.opentox.toxotis.util.spiders.TypedValue;
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
    public void testLoadFromRemote() throws URISyntaxException, ToxOtisException, InterruptedException {
        VRI vri = new VRI(Services.IDEACONSULT.augment("dataset", "54").addUrlParameter("max", "5"));
        System.out.println(vri);
        Dataset ds = new Dataset(vri).loadFromRemote();
        ds.getDataEntries().get(0).getFeatureValue(2).setValue(new TypedValue(4.32));
        ds.asOntModel().write(System.out);

        Task t = ds.publishOnline(Services.AMBIT_UNI_PLOVDIV.augment("dataset"), null);
        System.out.println(t.getHasStatus());
        while (t.getHasStatus().equals(Task.Status.RUNNING)) {
            t.loadFromRemote();
            Thread.sleep(100);
        }
        System.out.println(t.getResultUri());

        weka.core.Instances data = ds.getInstances();
        System.out.println(data);
        
    }
}
