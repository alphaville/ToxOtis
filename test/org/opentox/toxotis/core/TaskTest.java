package org.opentox.toxotis.core;

import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.Task.Status;
import org.opentox.toxotis.util.aa.PasswordFileManager;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class TaskTest {

    public TaskTest() {
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
    public void testSomeMethod() throws Exception {
        Task t =
                new Task(
                new VRI("http://opentox.ntua.gr:3000/task/0fc060a0-f69b-4a81-bb2e-b9b32c8a04b3")).loadFromRemote(
                PasswordFileManager.CRYPTO.authFromFile("./secret/my.key")
                );
        assertNotNull(t.getHasStatus());
        assertEquals(Status.COMPLETED, t.getHasStatus());

    }
}
