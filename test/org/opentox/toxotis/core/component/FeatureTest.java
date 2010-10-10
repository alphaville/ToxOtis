package org.opentox.toxotis.core.component;

import org.opentox.toxotis.core.component.Feature;
import com.hp.hpl.jena.vocabulary.OWLTest;
import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.ontology.WonderWebValidator;

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
        Feature f = new Feature(Services.ideaconsult().augment("feature","22200"));
        f.loadFromRemote();
        System.out.println(f.getMeta());
        f.writeRdf(System.out);
        WonderWebValidator wwv = new WonderWebValidator(f);
        System.out.println(
                wwv.validate(WonderWebValidator.OWL_SPECIFICATION.DL)
                );

    }


}
