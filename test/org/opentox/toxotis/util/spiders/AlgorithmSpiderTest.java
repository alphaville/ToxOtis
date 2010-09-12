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
import org.opentox.toxotis.collection.OpenToxAlgorithms;
import org.opentox.toxotis.core.Algorithm;
import org.opentox.toxotis.core.Parameter;
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
        AuthenticationToken tok = PasswordFileManager.CRYPTO.authFromFile("./secret/my.key");
        for (int i = 0; i < 50; i++) {
            spider = new AlgorithmSpider(new VRI(OpenToxAlgorithms.AMBIT_LR.getServiceUri()), tok); // << Authentication/Authorization using token
            Algorithm a = spider.parse();
            System.out.println(a.getMeta());
            Iterator<Parameter> param = a.getParameters().iterator();
            while (param.hasNext()) {
                System.out.println(param.next());
            }
        }
    }
}
