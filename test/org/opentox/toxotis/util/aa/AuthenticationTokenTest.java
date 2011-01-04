package org.opentox.toxotis.util.aa;

import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.collection.Services;

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
    public void testAcquireToken() throws ToxOtisException, IOException {
        long start = 0;
        int n = 20;
        long sum = 0;
        AuthenticationToken at = new AuthenticationToken(new File("/home/chung/toxotisKeys/my.key")); // << Provide your credentials here

        for (int i = 0; i < n; i++) {
            start = System.currentTimeMillis();
            at.authorize("GET", Services.ntua().augment("model"));
            sum += System.currentTimeMillis() - start;
            at = new AuthenticationToken(new File("/home/chung/toxotisKeys/my.key")); // << Provide your credentials here
        }

        double average = sum;
        average = average/n;
        System.out.println(average);
        

    }
}

