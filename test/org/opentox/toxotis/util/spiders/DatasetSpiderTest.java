package org.opentox.toxotis.util.spiders;

import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.collection.Services;
import org.opentox.toxotis.core.DataEntry;
import org.opentox.toxotis.core.Dataset;
import org.opentox.toxotis.core.FeatureValue;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTClasses;
import static org.junit.Assert.*;

/**
 *
 * @author hampos
 */
public class DatasetSpiderTest {

    public DatasetSpiderTest() {
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
    public void testDataset() throws URISyntaxException, ToxOtisException {
        VRI vri = new VRI(Services.IDEACONSULT.augment("dataset","5"));
        final int size = 10;
        vri.addUrlParameter("max", size);
        DatasetSpider spider = new DatasetSpider(vri);
        Dataset ds = spider.parse();
        System.out.println(ds.getMeta());
        assertEquals(10, ds.getDataEntries().size());

        DataEntry de = ds.getDataEntries().get(2);
        FeatureValue fv = de.getFeatureValue(0);

        System.out.println(de.getConformer().getUri());
        System.out.println(fv.getFeature().getUri() + " = " + fv.getValue());




    }
}
