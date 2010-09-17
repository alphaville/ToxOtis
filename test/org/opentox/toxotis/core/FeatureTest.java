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
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.util.spiders.TypedValue;
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
        Feature f = new Feature();
        f.setUnits("m^4*mA*s^2*kg^-2");
        f.getMeta().setTitle("Toxicity of my city");
        f.getMeta().setHasSource("http://otherserver.net:8283/opentox/model/15451");
        f.getMeta().setSameAs("http://www.youtube.com/watch?v=WMKmQmkJ9gg");
        Task t = f.publishOnline(Services.AMBIT_UNI_PLOVDIV.augment("feature"), null);
        
    }

}
