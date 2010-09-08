package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class TypedValueTest {

    public TypedValueTest() {
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
    public void testTypeCasting() {
        TypedValue<Integer> f = new TypedValue<Integer>(new Integer(1));
        assertEquals(XSDDatatype.XSDint,f.getType());
        f = new TypedValue(new Integer(1));
        assertEquals(XSDDatatype.XSDint,f.getType());
        f = new TypedValue(1.4353);
        assertEquals(XSDDatatype.XSDdouble,f.getType());
        f = new TypedValue(1.4353f);
        assertEquals(XSDDatatype.XSDfloat,f.getType());
        f = new TypedValue("haha");
        assertEquals(XSDDatatype.XSDstring,f.getType());
    }

}