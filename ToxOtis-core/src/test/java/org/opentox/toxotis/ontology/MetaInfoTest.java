/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.toxotis.ontology;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class MetaInfoTest {

    public MetaInfoTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testSomeMethod() {
        MetaInfo m1 = new MetaInfoImpl();
        MetaInfo m2 = new MetaInfoImpl();
        assertTrue(m1.hashCode() == m2.hashCode());
        ResourceValue rv = new ResourceValue(Services.ntua().augment("model", 1), OTClasses.Model());
        m1.addHasSource(rv);
        m2.addHasSource(rv);
        assertTrue(m1.hashCode() == m2.hashCode());

    }
}
