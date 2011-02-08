package org.opentox.toxotis.util.aa;

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Dataset;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.exceptions.IClientException;
import org.opentox.toxotis.exceptions.ISecurityException;
import org.opentox.toxotis.exceptions.IUnauthorized;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;

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
    public void testUser() throws Exception {
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
        } catch (ServiceInvocationException ex) {
            System.out.println(ex.getDetails());
        } finally {
            if (at != null) {
                at.invalidate();
            }
        }

    }

    @Test
    public void testGetUserWrongCredentials() throws Exception {
        try {
            new AuthenticationToken("wrongUser", "wrongPass");
        } catch (ServiceInvocationException ex) {
            assertTrue(ex instanceof IClientException);
            assertTrue(ex instanceof ISecurityException);
            assertTrue(ex instanceof IUnauthorized);
            return;
        }
        fail("Should have failed (Wrong credentials provided)");
    }

    @Test
    public void testAuthenticationFromFile() throws Exception {
        File passwordFile = new File(System.getProperty("user.home")+"/toxotisKeys/my.key");
        AuthenticationToken at = new AuthenticationToken(passwordFile);
        at.invalidate();
    }

    @Test
    public void testGetStatus() throws Exception {
        assertEquals(AuthenticationToken.TokenStatus.DEAD, new AuthenticationToken().getStatus());
        AuthenticationToken token = new AuthenticationToken("guest", "guest");
        assertEquals(AuthenticationToken.TokenStatus.ACTIVE,token.getStatus());
        token.invalidate();
        assertEquals(AuthenticationToken.TokenStatus.INACTIVE, token.getStatus());
    }

    @Test
    public void testGetUser() throws Exception {
        AuthenticationToken guest = new AuthenticationToken("guest", "guest");
        User u = guest.getUser();
        assertEquals("guest@opensso.in-silico.ch", u.getUid());
        assertEquals("Guest", u.getName());
        assertEquals("anonymous@anonymous.org", u.getMail());
    }

//    @Test
    public void testSecureDatasetService() throws Exception {
        AuthenticationToken at = null;
        try {
            at = new AuthenticationToken("guest", "guest");
        } catch (ServiceInvocationException ex) {
            ex.printStackTrace();
            return;
        }
        int maxCompounds = 2;
        Dataset ds = new Dataset(new VRI("https://ambit.uni-plovdiv.bg:8443/ambit2/dataset/54").addUrlParameter("max", maxCompounds)).loadFromRemote(at);
//        Dataset ds = new Dataset(new VRI("http://apps.ideaconsult.net:8080/ambit2/dataset/6").addUrlParameter("max", maxCompounds)).loadFromRemote(at);
        assertEquals(maxCompounds, ds.getInstances().numInstances());
        at.invalidate();
    }
}
