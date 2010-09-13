package org.opentox.toxotis.util.aa;

import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.opentox.toxotis.ToxOtisException;

/**
 *
 * @author chung
 */
public class PasswordFileManagerTest {

    public PasswordFileManagerTest() {
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
    public void testDecryption() {
        final String initialMessage = "Haha";
        String encrypted = PasswordFileManager.CRYPTO.encrypt(initialMessage);
        assertNotNull(encrypted);
        assertEquals(initialMessage,PasswordFileManager.CRYPTO.decrypt(encrypted));
    }

    @Test
    public void testFileAuth() throws IOException, ToxOtisException {
        AuthenticationToken at = PasswordFileManager.CRYPTO.authFromFile("./secret/my.key");
        System.out.println(at.getUser());
    }

    @Test
    public void testCreatePassFile() throws IOException, ToxOtisException {
        AuthenticationToken at = PasswordFileManager.CRYPTO.authFromFile(   "./secret/hampos.key");
        System.out.println(at.getUser());
    }

}