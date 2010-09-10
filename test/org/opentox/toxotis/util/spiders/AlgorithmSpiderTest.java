package org.opentox.toxotis.util.spiders;

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
    public void testAlgorithm() throws ToxOtisException, URISyntaxException {
        AlgorithmSpider spider = null;
        spider = new AlgorithmSpider(new VRI(OpenToxAlgorithms.AMBIT_LR.getServiceUri(),
                "username", "Sopasakis", "password", "secret")); // << This is not my password of course!
        Algorithm a = spider.parse();
        System.out.println(a.getMeta());
        System.out.println(a.getMeta().getDate());

        Iterator<Parameter> param = a.getParameters().iterator();
        while (param.hasNext()){
            System.out.println(param.next());
        }
    }
}
