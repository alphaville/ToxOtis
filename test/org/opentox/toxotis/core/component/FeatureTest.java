package org.opentox.toxotis.core.component;

import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.ontology.ResourceValue;
import org.opentox.toxotis.ontology.WonderWebValidator;
import org.opentox.toxotis.ontology.collection.OTClasses;

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

    //@Test
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

    @Test
    public void testRdfSerialization() throws Exception{
        Feature f = new Feature();
                f.getMeta().addHasSource(new ResourceValue(Services.ntua().augment("algorithm","pls"), OTClasses.Algorithm())).
                        addIdentifier("abc");
                f.asOntModel().write(System.out);
    }


}
