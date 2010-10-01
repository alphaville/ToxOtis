package org.opentox.toxotis.factory;

import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.Feature;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTEchaEndpoints;
import org.opentox.toxotis.ontology.collection.OTFeatures;
import static org.junit.Assert.*;

/**
 *
 * @author hampos
 */
public class FeatureFactoryTest {

    public FeatureFactoryTest() {
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
    public void testLookUpSameAs() throws ToxOtisException {
        Set<VRI> features = FeatureFactory.lookupSameAs(
                new VRI(Services.ideaconsult()).
                augment("feature"), OTEchaEndpoints.Gastrointestinal_absorption(), null);
        for (VRI f : features) {
            System.out.println(f.toString());
        }

    }

       @Test
    public void testLookUpSameAsNoService() throws ToxOtisException, InterruptedException {
        Set<VRI> features = FeatureFactory.lookupSameAs(OTFeatures.SMILES(), null);
        for (VRI f : features) {
            System.out.println(f.toString());
        }

    }
}
