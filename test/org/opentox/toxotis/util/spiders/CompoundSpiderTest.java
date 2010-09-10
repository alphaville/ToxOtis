package org.opentox.toxotis.util.spiders;

import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.Compound;
import static org.junit.Assert.*;

/**
 *
 * @author hampos
 */
public class CompoundSpiderTest {

    public CompoundSpiderTest() {
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
    public void testCompound() throws URISyntaxException, ToxOtisException {
        CompoundSpider spider = new CompoundSpider(
                new VRI("http://ambit.uni-plovdiv.bg:8080/ambit2/compound"));
        Compound c = spider.parse();
        System.out.println(c.getUri());

    }

}