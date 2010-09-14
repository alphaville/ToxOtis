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
public class FeatureTest {

    public FeatureTest() {
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
    public void testLoadRemoteFeature() throws URISyntaxException, ToxOtisException {
        Feature f = new Feature(Services.IDEACONSULT.augment("feature","10"));
        f.loadFromRemote();
        System.out.println(f.getMeta().getSameAs());
        System.out.println(f.getMeta().getHasSource());
        f.setUri(null);
        f.getMeta().setComment("Comment");
        f.getMeta().setTitle("Chemical Name");
        f.setUnits("");
        
        f.publishOnline(Services.IDEACONSULT.augment("feature"), null);
        
    }

}
