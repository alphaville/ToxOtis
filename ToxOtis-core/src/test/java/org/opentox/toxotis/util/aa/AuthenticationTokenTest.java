package org.opentox.toxotis.util.aa;

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Dataset;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.exceptions.IClientException;
import org.opentox.toxotis.exceptions.ISecurityException;
import org.opentox.toxotis.exceptions.IUnauthorized;
import org.opentox.toxotis.exceptions.impl.ForbiddenRequest;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.util.aa.policy.IPolicyWrapper;
import org.opentox.toxotis.util.aa.policy.PolicyManager;

/**
 *
 * @author Pantelis Sopasakis
 */
public class AuthenticationTokenTest {

    public AuthenticationTokenTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        File passwordFile = new File(System.getProperty("user.home") + "/toxotisKeys/.my.key");
        TokenPool.getInstance().login(passwordFile);
        TokenPool.getInstance().login("guest","guest");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        TokenPool.getInstance().logoutAll();
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
            at = TokenPool.getInstance().getToken("guest");
            assertNotNull(at.toString());
            User userGuest = at.getUser();
            assertEquals("anonymous@anonymous.org", userGuest.getMail());
            assertEquals("Guest", userGuest.getName());
            assertEquals("guest@opensso.in-silico.ch", userGuest.getUid());
        } catch (ServiceInvocationException ex) {
            System.out.println(ex.getDetails());
        } 
    }

    @Test
    public void testGetUserWrongCredentials() throws Exception {
        try {
            AuthenticationToken authenticationToken = new AuthenticationToken("wrongUser", "wrongPass");
        } catch (ServiceInvocationException ex) {
            assertTrue(ex instanceof IClientException);
            assertTrue(ex instanceof ISecurityException);
            assertTrue(ex instanceof IUnauthorized);
            return;
        }
        fail("Should have failed (Wrong credentials provided)");
    }
    

    @Test
    public void testGetStatus() throws Exception {
        File passwordFile = new File(System.getProperty("user.home") + "/toxotisKeys/.sopasakis.key");
        AuthenticationToken token = new AuthenticationToken(passwordFile);
        assertEquals(AuthenticationToken.TokenStatus.ACTIVE, token.getStatus());
        token.invalidate();
        assertEquals(AuthenticationToken.TokenStatus.INACTIVE, token.getStatus());
    }

    @Test
    public void testGetUser() throws Exception {
        AuthenticationToken guest = TokenPool.getInstance().getToken("guest");
        User u = guest.getUser();
        assertEquals("guest@opensso.in-silico.ch", u.getUid());
        assertEquals("Guest", u.getName());
        assertEquals("anonymous@anonymous.org", u.getMail());
    }

    @Test
    public void testSecureDatasetService() throws Exception {        
        AuthenticationToken at = TokenPool.getInstance().getToken("hampos");
        int maxCompounds = 2;
        VRI datasetUri = Services.ambitUniPlovdiv().augment("dataset", "54").
                addUrlParameter("max", maxCompounds);
        Dataset ds = null;
        try {
            ds = new Dataset(datasetUri).loadFromRemote(at);
            assertNotNull(ds);
            assertNotNull(ds.getInstances());
        } catch (final ForbiddenRequest ex) {
            String policyOwner = PolicyManager.getPolicyOwner(datasetUri, null, at);
            if (policyOwner == null) {
                System.out.println("[INFO] No policy owner found");
                VRI dsUriNoParams = new VRI(datasetUri).removeUrlParameter("max");
                IPolicyWrapper ipw = PolicyManager.defaultSignleUserPolicy("dataset_54_ambitUniPlovdiv", dsUriNoParams, at);
                ipw.publish(null, at);
            }
            System.out.println("[INFO] Policy Owner is : " + policyOwner);
            ds = new Dataset(datasetUri).loadFromRemote(at);
            assertNotNull(ds);
        } 
    }
}
