package org.opentox.toxotis.util.aa;

import java.io.IOException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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

    @Test
    public void testCreateMasterPWDFile() throws IOException {
        PasswordFileManager.CRYPTO.createMasterPasswordFile("/dev/urandom", "/home/chung/toxotisKeys/test-master.key", 500, true);
    }
}
