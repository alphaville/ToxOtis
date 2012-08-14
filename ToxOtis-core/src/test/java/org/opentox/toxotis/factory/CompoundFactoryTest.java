package org.opentox.toxotis.factory;

import java.io.File;
import java.net.URISyntaxException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.aa.PasswordFileManager;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class CompoundFactoryTest {

    public CompoundFactoryTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testPublishFromFile() throws ServiceInvocationException, URISyntaxException {
        AuthenticationToken at = new AuthenticationToken("guest", "guest");
        File f = new File(CompoundFactory.class.getClassLoader().getResource("samples/sample.sdf").toURI());
        if (!f.exists()) {
            fail("File not found!");
        }
        Task t = CompoundFactory.getInstance().publishFromFile(f, Media.CHEMICAL_MDLSDF, at);
        t = t.loadFromRemote(at);
        System.out.println(t.getUri());
        while (!Task.Status.COMPLETED.equals(t.getStatus())) {
            t = t.loadFromRemote(at);
            System.out.println(t.getUri());
        }
        System.out.println("Result URI = " + t.getResultUri());
        at.invalidate();
    }
}
