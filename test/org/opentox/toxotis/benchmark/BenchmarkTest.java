package org.opentox.toxotis.benchmark;

import org.opentox.toxotis.benchmark.gauge.NanoTimeGauge;
import org.opentox.toxotis.benchmark.gauge.MilliTimeGauge;
import org.jfree.chart.ChartFrame;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.benchmark.job.DownloadOntModelJob;
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
        
        Benchmark benchmark = new Benchmark("My benchmark title");// Diagram Title

        String myGaugeName = "dataset/9";// notation for each different line
        benchmark.setJobs(JobFactory.createDownloadOntModelJobs(
                "Ambit Compound Downloading", 1, 5, 2 ,
                Services.ideaconsult().augment("dataset","9").toString()+"?max=%s", 10,
                myGaugeName));


        String otherGaugeName = "dataset/10";// notation for each different line
        benchmark.addJobs(JobFactory.createDownloadOntModelJobs(
                "Ambit Compound Downloading", 1, 5, 2 ,
                Services.ideaconsult().augment("dataset","10").toString()+"?max=%s", 5,
                otherGaugeName));

        benchmark.setHorizontalAxisTitle("#Compounds");// x-axis name
        benchmark.setVerticalAxisTitle("Download and Parsing time");// y-axis name





        benchmark.start();

        ChartFrame frame2 = new ChartFrame("Title of our benchmarking test", // Window title
                benchmark.getLineChart(myGaugeName,otherGaugeName));
        frame2.pack();
        frame2.setVisible(true);

        while(true){
            
        }




    }
}
