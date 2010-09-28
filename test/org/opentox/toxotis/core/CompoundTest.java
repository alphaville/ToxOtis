package org.opentox.toxotis.core;

import java.io.File;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.collection.Services;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class CompoundTest {

    public CompoundTest() {
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
    public void testDownloadCompound() throws ToxOtisException {
        Compound c = new Compound(Services.IDEACONSULT.augment("compound", "100"));
        c.downloadAsFile(new File("/home/chung/Desktop/myMolecule.sdf"), Media.CHEMICAL_MDLSDF.getMime(), null);
        c.downloadAsFile(new File("/home/chung/Desktop/myMolecule.mol"), Media.CHEMICAL_MDLMOL.getMime(), null);
        Set<Conformer> list = c.listConformers(null);
        for (Conformer conf : list){
            System.out.println(conf.getUri());
        }
    }

    //@Test
    public void testListConformers() throws ToxOtisException{
        Compound c = new Compound(Services.IDEACONSULT.augment("compound", "100"));
        Set<Conformer> list = c.listConformers(null);
        for (Conformer conf : list){
            System.out.println(conf.getUri());
        }
    }

    @Test
    public void testAvailableFeatures() throws ToxOtisException{
        Compound c = new Compound(Services.AMBIT_UNI_PLOVDIV.augment("compound", "100"));
        Set<VRI> list = c.listAvailableFeatures();
        for (VRI conf : list){
            System.out.println(conf);
        }
    }
}
