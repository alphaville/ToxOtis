package org.opentox.toxotis.util.aa.policy;

import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.aa.PasswordFileManager;
import org.opentox.toxotis.util.aa.SSLConfiguration;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class PolicyTest {

    public PolicyTest() {
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
    public void testSomeMethod() throws ToxOtisException, IOException {
        AuthenticationToken at = PasswordFileManager.CRYPTO.
                authFromFile("/home/chung/toxotisKeys/my.key");
        System.out.println(at.getTokenUrlEncoded());
        at.invalidate();
        Policy.listPolicyUris(null,at);
    }

}