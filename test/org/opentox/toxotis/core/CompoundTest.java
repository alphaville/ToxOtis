package org.opentox.toxotis.core;

import java.io.File;
import java.net.URISyntaxException;
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
import org.opentox.toxotis.ontology.collection.OTFeatures;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.spiders.TypedValue;
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
    public void testDownload() throws Exception {
        Compound c = new Compound(Services.ideaconsult().augment("compound", "4"));
        c.download(new File(System.getProperty("user.home") + "/Desktop/b.txt"), Media.CHEMICAL_MDLMOL, null);
        TypedValue val = c.getProperty(new VRI("http://apps.ideaconsult.net:8080/ambit2/feature/1"), null);
        System.out.println(val);
        Dataset ds = c.getProperties(null, new VRI("http://apps.ideaconsult.net:8080/ambit2/feature/1"),
                new VRI("http://apps.ideaconsult.net:8080/ambit2/feature/2"));
        System.out.println(ds.getInstances());
    }

    // @Test
    public void testPublishFromFile() throws ToxOtisException {
        File f;
        Compound comp = new Compound(new VRI(Services.ideaconsult().augment("compound", "100")));
        comp.download(f = new File("/home/chung/Desktop/b.sdf"), Media.CHEMICAL_MDLSDF, null);

        CompoundFactory factory = CompoundFactory.getInstance();
        Task task = factory.publishFromFile(f, Media.CHEMICAL_MDLSDF, (AuthenticationToken) null);
        System.out.println(task.getResultUri());
    }

    //@Test
    public void testPublishFromRDF() throws ToxOtisException {
        Compound comp = new Compound(null);
        comp.getMeta().setTitle("My compound");
        Task tsk = comp.publishOnline(Services.ambitUniPlovdiv().augment("compound"), null);
        System.out.println(tsk);
    }

    // @Test
    public void testGetDepictionFromRemote() throws ToxOtisException {
        Compound comp = new Compound(new VRI(Services.ideaconsult()).augment("compound", "10"));
        ImageIcon icon = comp.getDepictionFromRemote(null);
        System.out.println(icon);
    }

    //@Test
    public void testWrapAsDataset() throws Exception {
        Compound c = new Compound(new VRI("somewhere.com/compound/1"));
        c.wrapInDataset(new VRI("myserver.com/dataset/1")).asOntModel().write(System.out);
    }

    @Test
    public void testCalculateDescriptors() throws ToxOtisException, URISyntaxException, InterruptedException {

        Compound c = new Compound(new VRI("http://apps.ideaconsult.net:8080/ambit2/compound/145419"));
        Task t = c.calculateDescriptors(Services.tumDev().augment("algorithm", "CDKPhysChem"), null);
        while (!Task.Status.COMPLETED.equals(t.getStatus())) {
            Thread.sleep(2000);
            System.out.println("Reloading... " + t.getUri());
            t.loadFromRemote();
            System.out.println(t);
        }
        System.out.println(t.getResultUri());
    }

    @Test
    public void testGetProperties() throws URISyntaxException, ToxOtisException {
        Compound c = new Compound(new VRI("http://apps.ideaconsult.net:8080/ambit2/compound/1"));
        Dataset ds = c.getPropertiesByOnt(OTFeatures.ChemicalName(), null);
        System.out.println(ds.getDataEntries().get(0).getFeatureValues().size());
    }
}
