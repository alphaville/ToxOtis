package org.opentox.toxotis.util.spiders;

import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.Model;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class ModelSpiderTest {

    public ModelSpiderTest() {
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
    public void testSomeMethod() throws URISyntaxException, ToxOtisException {
        String modelUri = "http://opentox.ntua.gr:3000/model/f9a97443-6baf-4361-a55c-b08cf12c3e39";
        VRI vri = new VRI(modelUri);
        ModelSpider mSpider = new ModelSpider(vri);
        Model m = mSpider.parse();
        System.out.println(m.getAlgorithm().getMeta());

    }

}