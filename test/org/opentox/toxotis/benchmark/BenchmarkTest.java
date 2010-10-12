/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.toxotis.benchmark;

import org.opentox.toxotis.benchmark.job.Job;
import org.opentox.toxotis.benchmark.gauge.NanoTimeGauge;
import org.opentox.toxotis.benchmark.gauge.MilliTimeGauge;
import com.hp.hpl.jena.ontology.OntModel;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartFrame;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.benchmark.job.DownloadOntModelJob;
import org.opentox.toxotis.benchmark.job.JobFactory;
import org.opentox.toxotis.client.GetClient;
import org.opentox.toxotis.client.collection.Services;
import static org.junit.Assert.*;

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
//        final NanoTimeGauge nano1 = new NanoTimeGauge();
//        final NanoTimeGauge nano2 = new NanoTimeGauge();
//        final MilliTimeGauge milli1 = new MilliTimeGauge();
//        final MilliTimeGauge milli2 = new MilliTimeGauge();
//        final Job job1 = new Job("Ambit Compound Downloading", 6) {
//            @Override
//            public void work() throws Exception {
//                nano1.start();
//                milli1.start();
//                GetClient client = new GetClient();
//                client.setUri(Services.ideaconsult().augment("compound").augment(parameter.toString())).setMediaType("application/rdf+xml");
//                OntModel model = client.getResponseOntModel();
//                nano1.stop();
//                milli1.stop();
//            }
//        };
//        job1.addGauge(nano1);
//        job1.addGauge(milli1);
//        job1.setAccuracy(50);

//        final Job job2 = new Job("Ambit Compound Downloading", 9) {
//            @Override
//            public void work() throws Exception {
//                nano2.start();
//                milli2.start();
//                GetClient client = new GetClient();
//                client.setUri(Services.ideaconsult().augment("compound").augment(parameter.toString())).setMediaType("application/rdf+xml");
//                OntModel model = client.getResponseOntModel();
//                nano2.stop();
//                milli2.stop();
//            }
//        };
//        job2.add(nano2);
//        job2.add(milli2);
//        job2.setAccuracy(50);

        DownloadOntModelJob job1 = new DownloadOntModelJob("Ambit Compound Downloading", 6);
        job1.setAccuracy(50);
        job1.setService(Services.ideaconsult().augment("compound"));

        DownloadOntModelJob job2 = new DownloadOntModelJob("Ambit Compound Downloading", 9);
        job2.setAccuracy(30);
        job2.setService(Services.ideaconsult().augment("compound"));
        //job2.setParameter(9);




        Benchmark benchmark = new Benchmark("my uber bench");
        benchmark.setJobs(JobFactory.createDownloadOntModelJobs("Ambit Compound Downloading", 1, 5, 1, Services.ideaconsult().augment("compound"), 20));
//        benchmark.addJob(job1);
//        benchmark.addJob(job2);

        benchmark.start();

        ChartFrame frame = new ChartFrame("First", benchmark.getBarChart(MilliTimeGauge.class));
        frame.pack();
        frame.setVisible(true);

        ChartFrame frame2 = new ChartFrame("Second", benchmark.getLineChart(MilliTimeGauge.class));
        frame2.pack();
        frame2.setVisible(true);

        while(true){
            
        }




    }
}
