/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opentox.toxotis.ontology.collection;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ontology.impl.SimpleOntModelImpl;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class OTRestObjectPropertiesTest {

    public OTRestObjectPropertiesTest() {
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
    public void testInverse() {
        OntModel model = new SimpleOntModelImpl();
        OTRestObjectProperties.hasRESTOperation().asObjectProperty(model);
        OTRestObjectProperties.resource().asObjectProperty(model);        
        model.write(System.out);
    }

}