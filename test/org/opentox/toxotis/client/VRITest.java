package org.opentox.toxotis.client;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.core.Compound;
import org.opentox.toxotis.core.Dataset;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class VRITest {

    public VRITest() {
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
    public void testString() throws URISyntaxException {
        System.out.println("--. Testing VRI#toString()");
        final String baseUri = "http://opentox.ntua.gr:3000";
        VRI v = new VRI(baseUri, "a", "1");
        v.addUrlParameter("c", "8");
        assertEquals(2, v.getUrlParams().size());
        v = new VRI(baseUri);
        assertEquals(baseUri, v.toString());
        v = new VRI(baseUri, "a", "1", "b");
        assertTrue(v.getUrlParams().keySet().contains("a"));
        assertTrue(v.getUrlParams().keySet().contains("b"));
        assertTrue(v.getUrlParams().values().contains("1"));
        v = new VRI("https://tricky.net?a=7&d=1", "b", "1");
        assertEquals(3, v.getUrlParams().size());
        java.util.Map.Entry<String, String> e = v.getUrlParams().entrySet().iterator().next();
        assertEquals("a", e.getKey());
        assertEquals("7", e.getValue());
        assertEquals(443, v.getPort());
        assertEquals("https", v.getProtocol());
    }

    @Test
    public void testExceptionHandling() {
        System.out.println("--. Testing VRI Exception Handling");
        try {
            VRI v = new VRI("sfs.gsrs sgr ");
            fail("Should have failed!");
        } catch (URISyntaxException ex) {
        }
    }

    @Test
    public void testURlEncoding() throws URISyntaxException {
        System.out.print("--. Testing VRI URL Encoding");
        VRI v = new VRI("http://something.abc:8181/", "a", "f f%&&[]");
        System.out.println(": " + v);
    }

    @Test
    public void testGetProtocol() throws URISyntaxException {
        System.out.println("--. Testing VRI#getProtocol()");
        VRI v = new VRI("http://something.abc:8181/", "a", "100");
        assertEquals("http", v.getProtocol());
        v = new VRI("something.abc:8181/", "a", "100");
        assertEquals("http", v.getProtocol());
        v = new VRI("git://something.abc:8181/", "a", "100");
        assertEquals("git", v.getProtocol());
    }

    @Test
    public void testGetQueryAsString() throws URISyntaxException, MalformedURLException {
        System.out.println("--. Testing VRI#getQueryAsString()");
        VRI v = new VRI("http://something.abc:8181/", "a", "100", "b", "100", "c");
        assertEquals("a=100&b=100&c=", v.getQueryAsString());
        assertEquals(8181, v.getPort());
        assertEquals(v.toString(), v.toURI().toURL().toString());
    }

    @Test
    public void testVriClass() throws URISyntaxException {
        //TODO:  More testing needed
        System.out.println("--. Testing VRI#getOpenToxType()");
        String uri = "http://opentox.ntua.gr:3000/query/compound/Phenol/all";
        VRI vri = new VRI(uri);
        assertEquals(Dataset.class, vri.getOpenToxType());
    }

    @Test
    public void testBaseUri() throws URISyntaxException {
        System.out.println("--. Testing VRI#getServiceBaseUri()");
        String baseUri = "http://ambit.uni-plovdiv.bg:8080/ambit2%s";

        String uri = String.format(baseUri, "/query/compound/50-00-0/all", "tokenid", "LGL5EJETIJFLKJ2095TOEGD"); // << Dataset
        VRI vri = new VRI(uri);
        assertEquals(String.format(baseUri, ""), vri.getServiceBaseUri().getStringNoQuery());
        assertEquals(Dataset.class, vri.getOpenToxType());

        uri = String.format(baseUri, "/query/compound/143", "tokenid", "LGL5EJETIJFLKJ2095TOEGD"); // << This is not a compound!!!
        vri = new VRI(uri);
        assertEquals(String.format(baseUri, ""), vri.getServiceBaseUri().getStringNoQuery());
        assertNull(vri.getOpenToxType());

        uri = String.format(baseUri, "/compound/4234", "tokenid", "LGL5EJETIJFLKJ2095TOEGD"); // << Compound
        vri = new VRI(uri);
        assertEquals(String.format(baseUri, ""), vri.getServiceBaseUri().getStringNoQuery());
        assertEquals(Compound.class, vri.getOpenToxType());

        uri = String.format(baseUri, "/compound/4234/whatever", "tokenid", "LGL5EJETIJFLKJ2095TOEGD"); // << Not in a category
        vri = new VRI(uri);
        assertEquals(String.format(baseUri, ""), vri.getServiceBaseUri().getStringNoQuery());
        assertNull(vri.getOpenToxType());
        assertNull(new VRI("http://www.whatever.jk:9090/server").getOpenToxType());

        uri = String.format(baseUri, "/whatever/feature/1", "tokenid", "LGL5EJETIJFLKJ2095TOEGD"); // << Feature
        vri = new VRI(uri);
        assertEquals(String.format(baseUri, "/whatever"),vri.getServiceBaseUri().getStringNoQuery());
    }

    @Test
    public void testMultipleEquals() throws Exception{
        VRI v = new VRI("http://something.com?a=b=c");
        System.out.println(v);
    }
}
