package org.opentox.toxotis.client;

import com.hp.hpl.jena.ontology.OntModel;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
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
public class GetClientTest {

    public GetClientTest() {
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
    public void testGETAlgorithm() throws URISyntaxException, ToxOtisException {
        System.out.println("--. Testing GetClient#getOntModel()");
        GetClient client = new GetClient();
        client.setUri("http://opentox.ntua.gr:3000/algorithm/mlr").setMediaType("application/rdf+xml");
        OntModel model = client.getResponseOntModel();
        assertNotNull(model);
    }

    @Test
    public void testGETURIlist() throws URISyntaxException, ToxOtisException, IOException {
        System.out.println("--. Testing GetClient#getUriList()");
        GetClient client = new GetClient();
        client.setUri(new VRI("http://opentox.ntua.gr:3000/algorithm")).setMediaType("will be ignored");
        List<String> algorithms = client.getResponseUriList();
        assertEquals(4, algorithms.size());
        assertTrue(algorithms.contains("http://opentox.ntua.gr:3000/algorithm/svm"));
        assertEquals("text/uri-list", client.getMediaType());
        assertEquals(200, client.getResponseCode());
    }
}
