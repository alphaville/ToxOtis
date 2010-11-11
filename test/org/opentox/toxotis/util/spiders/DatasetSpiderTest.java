package org.opentox.toxotis.util.spiders;

import java.io.File;
import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.DataEntry;
import org.opentox.toxotis.core.component.Dataset;
import org.opentox.toxotis.core.component.FeatureValue;
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
        VRI vri = new VRI(Services.ambitUniPlovdiv().augment("dataset", "9"));
        final int size = 4;
        vri.addUrlParameter("max", size);
        DatasetSpider spider = new DatasetSpider(vri);
        Dataset ds = spider.parse();
        System.out.println(ds.getMeta());
        assertEquals(size, ds.getDataEntries().size());

        DataEntry de = ds.getDataEntries().get(2);
        FeatureValue fv = de.getFeatureValue(0);

        ds.asOntModel().write(System.out);
        System.out.println(ds.getInstances());

    }

    @Test
    public void testDownloadDataset() throws URISyntaxException, ToxOtisException {
        VRI vri = new VRI(Services.ambitUniPlovdiv().augment("dataset", "9"));
        final int size = 10;
        vri.addUrlParameter("max", size);
        Dataset ds = new Dataset(vri);
        ds.download("/home/chung/Downloads/downloadedDS.rdf", Media.APPLICATION_RDF_XML, null);

    }
}
