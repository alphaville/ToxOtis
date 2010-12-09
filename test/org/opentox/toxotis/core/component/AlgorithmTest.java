package org.opentox.toxotis.core.component;

import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.collection.OpenToxAlgorithms;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.ontology.WonderWebValidator;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.aa.PasswordFileManager;
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
        Algorithm algorithm = new Algorithm(Services.NtuaAlgorithms.mlr());
        System.out.println(algorithm.getUri());
        System.out.println("Authenticating...");
        AuthenticationToken at = PasswordFileManager.CRYPTO.authFromFile("/home/chung/toxotisKeys/my.key");
        System.out.println("Received token for user... "+at.getUser().getMail());
        algorithm.loadFromRemote(at);
        System.out.println("Algorithm meta-information...");
        System.out.println(algorithm.getMeta());
        WonderWebValidator wwv = new WonderWebValidator(algorithm.asOntModel());
        System.out.println(wwv.validate(WonderWebValidator.OWL_SPECIFICATION.DL)?"Document is OWL-DL valid":"Document not OWL-DL valid!");
        System.out.println("Invalidating token...");
        at.invalidate();
    }


    //@Test
    public void testRdf() throws URISyntaxException, ToxOtisException, IOException {
        Algorithm a = new Algorithm(OpenToxAlgorithms.TUM_KNN_CLASSIFICATION.getServiceUri());
        a.loadFromRemote();
        System.out.println(a.getMeta());
        a.asOntModel().write(System.out);
    }
}