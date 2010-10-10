package org.opentox.toxotis.client;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.core.component.Compound;
import org.opentox.toxotis.core.component.Dataset;
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
        v = new VRI("https://tricky.net?a=7&d=1", "b", "1");
        assertEquals(3, v.getUrlParams().size());       
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
        System.out.println("--. Testing VRI URL Encoding");
        VRI v = new VRI("something.abc:8181/", "a", "f f%&&[]");
        assertEquals("a=f+f%25%26%26%5B%5D", v.getQueryAsString());
        assertEquals("http", v.getProtocol());
        assertEquals("http://something.abc:8181/", v.getStringNoQuery());
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
        assertEquals(String.format(baseUri, "/whatever"), vri.getServiceBaseUri().getStringNoQuery());
    }

    @Test
    public void testMultipleEquals() throws Exception {
        System.out.println("--. Testing Equality Symbol in parameter value");
        VRI v = new VRI("http://something.com?a=b=c");
        assertEquals("http://something.com?a=b%3Dc", v.toString());
    }
   

    @Test
    public void testArrays() throws Exception {
        VRI v1 = new VRI("sth.com/a").addUrlParameter("a[]","1").addUrlParameter("a[]","2");
        assertEquals("a[]=1&a[]=2",URLDecoder.decode(v1.getQueryAsString(),"UTF-8"));
    }
}
