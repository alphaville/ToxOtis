package org.opentox.toxotis.benchmark;

import org.jfree.chart.ChartFrame;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.benchmark.job.JobFactory;
import org.opentox.toxotis.client.collection.Services;

/**
 *
 * @author hampos
 */
public class BenchmarkTest {

    public BenchmarkTest() {
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
    public void testSomeMethod() throws CloneNotSupportedException, ToxOtisException, InterruptedException, Exception {

        final Benchmark benchmark = new Benchmark("My benchmark title");// Diagram Title

        /*
         * Names/Annotations for the different lines
         */

        String ds4GaugeName = "dataset/4";
        String ds9GaugeName = "dataset/9";
        String ds10GaugeName = "dataset/10";

        // Number for iterations for each measurement
        int nIter1 = 5;
        int nIter2 = 5;
        int nIter3 = 5;


        benchmark.addJobs(JobFactory.createDownloadOntModelJobs(
                "J1", 50, 500, 50000,
                Services.ambitUniPlovdiv().augment("dataset", "4").toString() + "?max=%s", nIter1,
                ds4GaugeName));

        benchmark.addJobs(JobFactory.createDownloadOntModelJobs(
                "J2", 100, 500, 50,
                Services.ambitUniPlovdiv().augment("dataset", "9").toString() + "?max=%s", nIter1,
                ds9GaugeName));


        benchmark.addJobs(JobFactory.createDownloadOntModelJobs(
                "J3", 400, 600, 100,
                Services.ambitUniPlovdiv().augment("dataset", "10").toString() + "?max=%s", nIter2,
                ds10GaugeName));

        benchmark.setHorizontalAxisTitle("#Compounds");// x-axis name
        benchmark.setVerticalAxisTitle("Download time");// y-axis name


        benchmark.start();

        final ChartFrame frame2 = new ChartFrame("Title of our benchmarking test", // Window title
                benchmark.getLineChart(ds4GaugeName, ds9GaugeName, ds10GaugeName));



        Runnable r = new Runnable() {

            public void run() {
                frame2.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public synchronized void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                frame2.pack();
                frame2.setVisible(true);
            }
        };

        java.awt.EventQueue.invokeAndWait(r);
        while (true) {
        }

    }
}
