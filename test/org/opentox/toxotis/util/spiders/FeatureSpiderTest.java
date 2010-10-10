package org.opentox.toxotis.util.spiders;

import java.net.URISyntaxException;
import java.util.Iterator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Feature;
import org.opentox.toxotis.ontology.OntologicalClass;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class FeatureSpiderTest {

    public FeatureSpiderTest() {
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
    public void testParseFeature() throws URISyntaxException, ToxOtisException {
        final String featureUri = "http://apps.ideaconsult.net:8080/ambit2/feature/22204";
        System.out.println("Feature : " + featureUri);
        FeatureSpider fSpider = new FeatureSpider(new VRI(featureUri));
        Feature f = fSpider.parse();
        System.out.println(f);
    }
}
