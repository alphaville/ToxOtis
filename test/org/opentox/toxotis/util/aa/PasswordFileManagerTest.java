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

    //@Test
    public void testDecryption() {
        System.out.println("--. Testing Encryption-Decryption w.r.t. the master key");
        final String initialMessage = "Haha";
        PasswordFileManager.CRYPTO.setCryptoIterations(56);
        String encrypted = PasswordFileManager.CRYPTO.encrypt(initialMessage);
        assertNotNull(encrypted);
        assertEquals(initialMessage, PasswordFileManager.CRYPTO.decrypt(encrypted));
    }

    //@Test
    public void testCredentialsFile() throws IOException {
        System.out.println("--. Testing Creation of Credentials File");
        PasswordFileManager.CRYPTO.createPasswordFile("john", "smith", "/home/chung/toxotisKeys/js.key");
    }

    //@Test
    public void testCreateMasterPass() throws IOException {
        System.out.println("--. Testing Creation of Master Key File");
        PasswordFileManager.CRYPTO.createMasterPasswordFile(null, "/home/chung/Desktop/alt.key", 100, false);
    }

    //@Test
    public void testFileAuth() throws IOException, ToxOtisException {
        AuthenticationToken at = PasswordFileManager.CRYPTO.authFromFile("/home/chung/toxotisKeys/my.key");        
        System.out.println(at.getUser());
        System.out.println(at);
    }

    @Test
    public void testCrypt(){
        System.out.println(PasswordFileManager.CRYPTO.decrypt("tFTiUPtJi3CeF+IqyeomiFZlsB/6qgfZ"));
    }

}
