package org.opentox.toxotis.core;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.net.URISyntaxException;
import java.util.concurrent.Future;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 *
 * @author chung
 */
public class DatasetTest {

    public DatasetTest() {
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
    public void testAllocationResources() throws URISyntaxException, ToxOtisException, InterruptedException, FileNotFoundException {
        int datasetId = 9;
        VRI vri = Services.ideaconsult();
        vri.augment("dataset", Integer.toString(datasetId));
        PrintStream ps = new PrintStream("/home/chung/Documents/OpenTox/ToxOtis/Experiments/allocation.csv");
        ps.println("Dataset : " + vri.toString());
        ps.println("#Compounds,free,max,total,heap,sys load");
        System.out.println("Test on dataset : http://apps.ideaconsult.net:8080/ambit2/dataset/" + datasetId);
        for (int i = 0; i <= 1000; i += 50) {
            System.out.println("* " + i);
            for (int j = 0; j < 10; j++) {
                long freeMem = Runtime.getRuntime().freeMemory();
                long maxMem = Runtime.getRuntime().maxMemory();
                long totalMem = Runtime.getRuntime().totalMemory();
                long heap = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
                double sysLoad = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage() * 50;
                vri.removeUrlParameter("max").addUrlParameter("max", i == 0 ? 1 : i);
                Dataset ds = new Dataset(vri).loadFromRemote();
                ps.println((i == 0 ? 1 : i) + "," + freeMem + "," + maxMem + "," + totalMem + "," + heap + "," + sysLoad);
            }
        }
        ps.flush();
        ps.close();
    }

    //@Test
    public void testJenaVsStax() throws URISyntaxException, ToxOtisException, InterruptedException, FileNotFoundException {
        int datasetId = 9;
        VRI vri = Services.ideaconsult();
        vri.augment("dataset", Integer.toString(datasetId));
        PrintStream ps = new PrintStream("/home/chung/Documents/OpenTox/ToxOtis/Experiments/staxVsJena.csv");
        ps.println("Dataset : " + vri.toString());
        ps.println("#Compounds,StAX,Jena");
        for (int i = 1000; i <= 1000; i += 50) {
            System.out.println("* " + i);
            for (int j = 0; j < 20; j++) {
                vri.removeUrlParameter("max").addUrlParameter("max", i == 0 ? 1 : i);
                vri.removeUrlParameter("rdfwriter").addUrlParameter("rdfwriter", "jena");
                Dataset ds_jena = new Dataset(vri).loadFromRemote();
                vri.removeUrlParameter("rdfwriter").addUrlParameter("rdfwriter", "stax");
                Dataset ds_stax = new Dataset(vri).loadFromRemote();
                ps.println((i == 0 ? 1 : i) + "," + ds_stax.getTimeDownload() + "," + ds_jena.getTimeDownload());
                System.out.println((i == 0 ? 1 : i) + "," + ds_stax.getTimeDownload() + "," + ds_jena.getTimeDownload());
            }
        }
        ps.flush();
        ps.close();
    }

    //@Test
    public void testCompareNoDBQuery() throws URISyntaxException, ToxOtisException, InterruptedException, FileNotFoundException {
    }

    @Test
    public void testUploadTask() throws Exception {
        VRI vri = new VRI(Services.ideaconsult().augment("dataset", "54").addUrlParameter("max", "5"));
        Dataset ds = new Dataset(vri).loadFromRemote();        
        Future<VRI> t = ds.publish(Services.ambitUniPlovdiv().augment("dataset"),(AuthenticationToken)null);
        while (!t.isDone()){
        }
        System.out.println(t.get());


    }
}
