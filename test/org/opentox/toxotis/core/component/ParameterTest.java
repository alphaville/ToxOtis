package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.ontology.OntModel;
import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.util.spiders.ParameterSpider;

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
        p.setScope(Parameter.ParameterScope.OPTIONAL);
        p.setTypedValue(new LiteralValue<String>("t1"));
        p.getMeta().addDescription("My parameter");
        OntModel om = p.asOntModel();
        om.write(System.out);
        ParameterSpider pS = new ParameterSpider(om, null);
        System.out.println(p.getUri());
        System.out.println(p.equals(pS.parse()));

    }

}


