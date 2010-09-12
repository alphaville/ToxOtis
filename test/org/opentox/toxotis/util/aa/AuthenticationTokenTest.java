package org.opentox.toxotis.util.aa;

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
public class AuthenticationTokenTest {

    public AuthenticationTokenTest() {
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
    public void testAcquireToken() throws ToxOtisException {
        AuthenticationToken at = new AuthenticationToken("Sopasakis", "abfhs8y"); // << Provide your credentials here
        System.out.println(at.getToken());
        System.out.println(at.getTokenUrlEncoded());
        System.out.println(at.getTokenCreationTimestamp());
        System.out.println(at.getTokenCreationDate());
        System.out.println("Status : " + at.getStatus());
        System.out.println(at.validate() ? "Valid" : "Stale");
        System.out.println(at.getUser());
        System.out.println("Logging out...");
        at.invalidate();
        System.out.println("Status : " + at.getStatus());
        System.out.println(at.validate() ? "Valid" : "Stale");

    }
}

