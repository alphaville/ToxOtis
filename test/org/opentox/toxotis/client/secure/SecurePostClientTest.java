package org.opentox.toxotis.client.secure;

import org.opentox.toxotis.client.https.PostHttpsClient;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.util.aa.SSLConfiguration;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class SecurePostClientTest {

    public SecurePostClientTest() {
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
    public void testSomeMethod() throws URISyntaxException, ToxOtisException {
        SSLConfiguration.initializeSSLConnection();
        PostHttpsClient p = new PostHttpsClient(new VRI("https://opensso.in-silico.ch/opensso/identity/authenticate"));
        p.addParameter("username", "Sopasakis");
        p.addParameter("password", "xxx");
        p.post();
        System.out.println(p.getResponseCode());
        System.out.println(p.getResponseText());        
    }

}