/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.toxotis.util.spiders;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Feature;
import org.opentox.toxotis.ontology.OntologicalClass;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class FeatureSpiderTest {
    
    public FeatureSpiderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of parse method, of class FeatureSpider.
     */
    @Test
    public void testParse() throws Exception{
        Feature f = new Feature(new VRI("http://apps.ideaconsult.net:8080/ambit2/feature/644757"));
        f.loadFromRemote();
        for (OntologicalClass oc : f.getOntologicalClasses()){
            System.out.println(oc.getUri());
        }
    }
}
