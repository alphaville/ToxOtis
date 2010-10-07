package org.opentox.toxotis.ontology;

import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.shared.ReificationStyle;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.collection.Services;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class AlternativeModelTest {

    public AlternativeModelTest() {
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
    public void testModel1() {
        Model m1 = ModelFactory.createDefaultModel();
        Model m2 = ModelFactory.createNonreifyingModel();
        Model m3 = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        String location = Services.ideaconsult().augment("dataset", "9").toString();
        int N = 13;
        for (int i = 0; i < N; i++) {
            long now = System.currentTimeMillis();
            m1.read(location);
            long m1Time = System.currentTimeMillis() - now;

            now = System.currentTimeMillis();
            m2.read(location);
            long m2Time = System.currentTimeMillis() - now;

            now = System.currentTimeMillis();
            m3.read(location);
            long m3Time = System.currentTimeMillis() - now;

            System.out.println(m1Time + "," + m2Time + "," + m3Time);
        }
    }
}
