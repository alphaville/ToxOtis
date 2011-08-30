/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;
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

    /**
     * Test of endowUri method, of class Parameter.
     */
    @Test
    public void testEndowUri() {
        Parameter p = new Parameter();
        p.setName("myParam");
        p.setScope(Parameter.ParameterScope.OPTIONAL);
        p.setMeta(new MetaInfoImpl().addDescription("asdf"));
        p.setTypedValue(new LiteralValue((double) 100));
        p.endowUri(Services.ntua());
        System.out.println(p.getUri());
    }

    /**
     * Test of equals method, of class Parameter.
     */
    @Test
    public void testEquals() {
        Parameter p1 = new Parameter();
        Parameter p2 = new Parameter();
        assertEquals(p1, p2);
    }

    /**
     * Test of hashCode method, of class Parameter.
     */
    @Test
    public void testHashCode1() {
        Parameter p1 = new Parameter();
        Parameter p2 = new Parameter();
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testHashCode2() {
        Parameter p1 = new Parameter();
        p1.setName("a");
        Parameter p2 = new Parameter();
        p2.setName("b");
        assertNotSame(p1.hashCode(), p2.hashCode());
        assertNotSame(p1, p2);
        assertFalse(p1.equals(p2));
        assertFalse(p1.hashCode()==p2.hashCode());
    }
    
    @Test
    public void testHashCode3() {
        Parameter p1 = new Parameter();
        p1.setTypedValue(new LiteralValue("a"));
        Parameter p2 = new Parameter();
        p2.setTypedValue(new LiteralValue("a", XSDDatatype.XSDanyURI));
        assertNotSame(p1.hashCode(), p2.hashCode());
        assertNotSame(p1, p2);
    }
    
    @Test
    public void testHashCode4() {
        Parameter p1 = new Parameter();
        p1.setMeta(new MetaInfoImpl().addDescription("x"));
        Parameter p2 = new Parameter();        
        assertNotSame(p1.hashCode(), p2.hashCode());
        assertNotSame(p1, p2);
    }
    
    
    
}
