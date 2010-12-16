package org.opentox.toxotis.util.spiders;

import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Feature;
import org.opentox.toxotis.core.component.Model;
import org.opentox.toxotis.core.component.Parameter;

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
        VRI vri = new VRI("http://opentox.ntua.gr:3000/model/dd2063a5-5ac4-40ca-8725-4d3701aafcdf");
//        ModelSpider mSpider = new ModelSpider(vri);
//        Model m = mSpider.parse();

        Model m = new Model(vri);
        m.loadFromRemote();
        System.out.println("META INFO :");
        System.out.println(m.getAlgorithm().getMeta());
        System.out.println("DEPENDENT FEATURE :");
        System.out.println(m.getDependentFeature());
        System.out.println("PREDICTED FEATURE :");
        System.out.println(m.getPredictedFeature());
        for (Feature f : m.getIndependentFeatures()){
            System.out.println(f.getUri());
        }

        for (Parameter p : m.getParameters()){
            System.out.println(p.getUri());
        }


        m.asOntModel().write(System.out);
    }
}
