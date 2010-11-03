package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import java.util.Date;
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
        AnyValue<Integer> f = new AnyValue<Integer>(new Integer(1));
        assertEquals(XSDDatatype.XSDint, f.getType());
        f = new AnyValue(new Integer(1));
        assertEquals(XSDDatatype.XSDint, f.getType());
        f = new AnyValue(1.4353);
        assertEquals(XSDDatatype.XSDdouble, f.getType());
        f = new AnyValue(1.4353f);
        assertEquals(XSDDatatype.XSDfloat, f.getType());
        f = new AnyValue("haha");
        assertEquals(XSDDatatype.XSDstring, f.getType());
        f = new AnyValue(new Date(System.currentTimeMillis()));
        assertEquals(XSDDatatype.XSDdateTime, f.getType());
    }
}
