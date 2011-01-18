package org.opentox.toxotis.util.aa;

import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ErrorCause;
import static org.junit.Assert.*;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Dataset;
import org.opentox.toxotis.core.component.User;

/**
 *
 * @author Pantelis Sopasakis
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
    public void testUser() throws ToxOtisException {
        /*
         * Important todo:
         * export JAVA_HOME=/usr/lib/jvm/java-6-sun-1.6.0.22/
         */
        // The user guest:guest is the public user of OpenTox (like Anonymous)
        AuthenticationToken at = null;
        try {
            at = new AuthenticationToken("guest", "guest");
            User userGuest = at.getUser();
            assertEquals("anonymous@anonymous.org", userGuest.getMail());
            assertEquals("Guest", userGuest.getName());
            assertEquals("guest@opensso.in-silico.ch", userGuest.getUid());
            at.invalidate();
        } catch (ToxOtisException ex) {
            ex.printStackTrace();
        } finally {
            at.invalidate();
        }

    }

    @Test
    public void testGetUserWrongCredentials() throws ToxOtisException {
        try {
            new AuthenticationToken("wrongUser", "wrongPass");
        } catch (ToxOtisException ex) {
            if (ErrorCause.CommunicationError.equals(ex.getCode())) {
                System.out.println("[WARNING] SSO server seems to be down!");
                return;
            }
            assertEquals(ErrorCause.AuthenticationFailed, ex.getCode());
            return;
        }
        fail("Should have failed (Wrong credentials provided)");
    }

    @Test
    public void testSecureDatasetService() throws ToxOtisException, URISyntaxException {
        AuthenticationToken at = null;
        try {
            at = new AuthenticationToken("Sopasakis", "abfhs8y");
        } catch (ToxOtisException ex) {
            if (ErrorCause.CommunicationError.equals(ex.getCode())) {
                System.out.println("[WARNING] SSO server seems to be down!");
                return;
            }
            assertEquals(ErrorCause.AuthenticationFailed, ex.getCode());
            return;
        }
        int maxCompounds = 1;
        Dataset ds = new Dataset(new VRI("https://ambit.uni-plovdiv.bg:8443/ambit2/dataset/6").addUrlParameter("max", maxCompounds)).loadFromRemote(at);
        assertEquals(maxCompounds, ds.getInstances().numInstances());
        at.invalidate();
    }
}
