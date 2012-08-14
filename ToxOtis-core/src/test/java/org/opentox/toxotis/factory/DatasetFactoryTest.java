package org.opentox.toxotis.factory;

import java.io.File;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class DatasetFactoryTest {

    private static final String MY_SECRET_KEY = System.getProperty("user.home") + "/toxotisKeys/.my.key";

    public DatasetFactoryTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test(timeout=25000)
    public void testPublishDataset() throws Exception {
        File f = new File(CompoundFactory.class.getClassLoader().getResource("samples/sample.sdf").toURI());
        assertTrue(f.exists());
        File passwordFile = new File(MY_SECRET_KEY);
        AuthenticationToken at = new AuthenticationToken(passwordFile);
        Task t = DatasetFactory.getInstance().publishFromFile(f, Media.CHEMICAL_MDLSDF, at);
        assertNotNull(t);
        t = t.loadFromRemote();
        while (!Task.Status.COMPLETED.equals(t.getStatus())) {
            t = t.loadFromRemote();
        }
        assertEquals("Unexpected HTTP status:" + t.getHttpStatus(), 200f, t.getHttpStatus(), 1E-6);
        assertNotNull(t.getResultUri());
        VRI resultVri = t.getResultUri();
        assertEquals(OTClasses.Dataset(), resultVri.getOntologicalClass());
    }
}
