package org.opentox.toxotis.client;

import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.core.BibTeX;

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
//        final String ambitUri = "http://ambit.uni-plovdiv.bg:8080/ambit2/%s";
//        final String datasetUri = String.format(ambitUri, "dataset/6");
//        final String targetFeatureUri = String.format(ambitUri, "feature/11954");
//        PostClient pc = new PostClient(new VRI("http://localhost:3000/algorithm/mlr"));
//        pc.addPostParameter("dataset_uri", datasetUri);
//        pc.addPostParameter("prediction_feature", targetFeatureUri);
//        pc.post();
//        System.out.println(pc.getResponseText());

        BibTeX b = new BibTeX(new VRI("http://localhost:3000/bibtex/123"));
        b.setBibType(BibTeX.BIB_TYPE.Conference);
        b.setAuthor("me");
        b.setTitle("haha");
        //b.setVolume(10);
        //b.setNumber(10);
        //b.setYear(2010);
        PostClient pc = new PostClient(new VRI("http://localhost:3000/bibtex"));
        pc.setMediaType("text/uri-list");
        pc.setContentType("application/rdf+xml");
        pc.setPostable(b.asOntModel());
        pc.post();
        System.out.println(pc.getResponseText());
    }

}