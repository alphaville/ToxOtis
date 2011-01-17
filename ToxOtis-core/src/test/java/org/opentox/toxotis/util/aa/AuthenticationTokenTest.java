package org.opentox.toxotis.util.aa;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ErrorCause;
import static org.junit.Assert.*;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.core.component.User;

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
    public void testUser() throws ToxOtisException {
        // The user guest:guest is the public user of OpenTox (like Anonymous)
        try {
            AuthenticationToken at = new AuthenticationToken("guest", "guest");
            User userGuest = at.getUser();
            assertEquals("anonymous@anonymous.org", userGuest.getMail());
            assertEquals("Guest", userGuest.getName());
            assertEquals("guest@opensso.in-silico.ch", userGuest.getUid());
            at.invalidate();
        } catch (ToxOtisException ex) {
            ex.printStackTrace();
        }

    }

    @Test
    public void testGetUserWrongCredentials() throws ToxOtisException {
        try {
            new AuthenticationToken("wrongUser", "wrongPass");
        } catch (ToxOtisException ex) {
            if (ErrorCause.CommunicationError.equals(ex.getCode())){
                System.out.println("[WARNING] SSO server seems to be down!");
                return;
            }
            assertEquals(ErrorCause.AuthenticationFailed,ex.getCode());
            return;
        }
        fail("Should have failed (Wrong credentials provided)");
    }
}
