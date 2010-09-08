package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.OntModel;
import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.Parameter.ParameterScope;
import org.opentox.toxotis.util.spiders.ParameterSpider;
import org.opentox.toxotis.util.spiders.TypedValue;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class ParameterTest {

    public ParameterTest() {
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
    public void testSomeMethod() throws URISyntaxException {
        Parameter<String> p = new Parameter<String>();
        p.setUri(new VRI("http://sth.com/x/1"));
        p.setName("xx");
        p.setScope(ParameterScope.OPTIONAL);
        p.setTypedValue(new TypedValue<String>("yyy"));
        //p.getMeta().setDescription("My parameter");
        OntModel om = p.asOntModel();
        ParameterSpider pS = new ParameterSpider(om, om.getResource("http://sth.com/x/1"));

    }

}