package org.opentox.toxotis.client;

import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;

/**
 *
 * @author chung
 */
public class PostClientTest {

    public PostClientTest() {
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
    public void testTrainAlgorithm() throws URISyntaxException, ToxOtisException {
        final String ambitUri = "http://ambit.uni-plovdiv.bg:8080/ambit2/%s";
        final String datasetUri = String.format(ambitUri, "dataset/6");
        final String targetFeatureUri = String.format(ambitUri, "feature/11954");
        PostClient pc = new PostClient(new VRI("http://localhost:3000/algorithm/mlr"));
        pc.addParameter("dataset_uri", datasetUri);
        pc.addParameter("prediction_feature", targetFeatureUri);
        pc.postParameters();        
        System.out.println(pc.getResponseText());
    }

}