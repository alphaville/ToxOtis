package org.opentox.toxotis.core;

import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.collection.OpenToxAlgorithms;
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

    //@Test
    public void testSomeMethod() throws URISyntaxException, ToxOtisException, IOException {
        Algorithm a = new Algorithm(OpenToxAlgorithms.NTUA_MLR.getServiceUri());
        AuthenticationToken at = PasswordFileManager.CRYPTO.authFromFile("./secret/my.key");
        a.loadFromRemote(at);
        System.out.println(a.getMeta());
    }


        @Test
    public void testRdf() throws URISyntaxException, ToxOtisException, IOException {
        Algorithm a = new Algorithm(OpenToxAlgorithms.NTUA_MLR.getServiceUri());
        a.getMeta().setComment("This is a comment");
        a.asOntModel().write(System.out);
    }
}