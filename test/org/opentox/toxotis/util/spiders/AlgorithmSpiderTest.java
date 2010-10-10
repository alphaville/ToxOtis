package org.opentox.toxotis.util.spiders;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.OpenToxAlgorithms;
import org.opentox.toxotis.core.component.Algorithm;
import org.opentox.toxotis.core.component.Parameter;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.aa.PasswordFileManager;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class AlgorithmSpiderTest {

    public AlgorithmSpiderTest() {
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
    public void testAlgorithm() throws ToxOtisException, URISyntaxException, IOException {
        AlgorithmSpider spider = null;
        AuthenticationToken tok = PasswordFileManager.CRYPTO.authFromFile("/home/chung/toxotisKeys/my.key");
        spider = new AlgorithmSpider(new VRI(OpenToxAlgorithms.NTUA_MLR.getServiceUri()), tok); // << Authentication/Authorization using token
        Algorithm a = spider.parse();
        spider.close();
        System.out.println(a.getMeta());
        Iterator<Parameter> param = a.getParameters().iterator();
        while (param.hasNext()) {
            System.out.println(param.next());
        }
        for (OntologicalClass cl : a.getOntologies()) {
            System.out.println(cl.getUri());
        }
    }
}
