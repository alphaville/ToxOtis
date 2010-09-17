package org.opentox.toxotis.core;

import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.collection.OpenToxAlgorithms;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class AlgorithmTest {

    public AlgorithmTest() {
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
    public void testSomeMethod() throws URISyntaxException, ToxOtisException, IOException {
        Algorithm a = new Algorithm(OpenToxAlgorithms.TUM_KNN_CLASSIFICATION.getServiceVri());
        //AuthenticationToken at = PasswordFileManager.CRYPTO.authFromFile("./secret/my.key");
//        System.out.println(at.validate());
        a.loadFromRemote();
//        for (Parameter oc : a.getParameters()){
//            System.out.println(oc);
//        }
        a.asOntModel().write(System.out);
    }


    @Test
    public void testRdf() throws URISyntaxException, ToxOtisException, IOException {
        Algorithm a = new Algorithm(OpenToxAlgorithms.TUM_KNN_CLASSIFICATION.getServiceUri());
        a.getMeta().setComment("This is a comment");
//        a.asOntModel().write(System.out);
    }
}