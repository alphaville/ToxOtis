package org.opentox.toxotis.core;

import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
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
        Feature f = new Feature(new VRI("http://apps.ideaconsult.net:8080/ambit2/feature/10"));
        f.loadFromRemote();
        System.out.println(f.meta);
        
    }

}