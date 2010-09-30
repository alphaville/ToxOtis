package org.opentox.toxotis.core;

import java.io.File;
import javax.swing.ImageIcon;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.factory.CompoundFactory;
import org.opentox.toxotis.util.aa.AuthenticationToken;
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

    @Test
    public void testDownload() throws ToxOtisException {
        Compound c =new Compound(Services.IDEACONSULT.augment("compound","4"));
        c.download(new File("/home/chung/Desktop/b.txt"), Media.CHEMICAL_MDLMOL, null);
    }

   // @Test
    public void testPublishFromFile() throws ToxOtisException {
       File f;
       Compound comp = new Compound(new VRI(Services.IDEACONSULT.augment("compound","100")));
       comp.download(f = new File("/home/chung/Desktop/b.sdf"), Media.CHEMICAL_MDLSDF, null);

       CompoundFactory factory = CompoundFactory.getInstance();
       Task task = factory.publishFromFile(f, Media.CHEMICAL_MDLSDF.getMime(), (AuthenticationToken)null);
       System.out.println(task.getResultUri());
    }

    //@Test
    public void testPublishFromRDF() throws ToxOtisException {
       Compound comp = new Compound(null);
       comp.getMeta().setTitle("My compound");
       Task tsk = comp.publishOnline(Services.AMBIT_UNI_PLOVDIV.augment("compound"),null);
       System.out.println(tsk);
    }

    //@Test
    public void testGetDepictionFromRemote() throws ToxOtisException {
        Compound comp = new Compound(new VRI(Services.IDEACONSULT.augment("compound","10")));
        ImageIcon icon = comp.getDepictionFromRemote();
        System.out.println(icon);
    }
}