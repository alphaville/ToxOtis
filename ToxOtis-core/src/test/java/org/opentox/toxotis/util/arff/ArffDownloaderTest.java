/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.toxotis.util.arff;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import weka.core.Instances;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class ArffDownloaderTest {
    
    public ArffDownloaderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testDownloadArff() {
        int nCompounds = 2;
        VRI datasetUri = Services.ideaconsult().augment("dataset","54").addUrlParameter("max", nCompounds);
        ArffDownloader ad = new ArffDownloader(datasetUri);
        Instances data = ad.getInstances();
        assertEquals(nCompounds, data.numInstances());
    }
}
