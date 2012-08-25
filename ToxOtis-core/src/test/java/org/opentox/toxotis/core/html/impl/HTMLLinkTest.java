package org.opentox.toxotis.core.html.impl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.collection.Services;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class HTMLLinkTest {

    public HTMLLinkTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testFormat() {
        HTMLLink link = new HTMLLink();
        link.setHref(Services.anonymous().toString());
        link.setContent("My Link");
        assertEquals("<a href=\"http://anonymous.org/\">My Link</a>", link.toString());
        assertEquals("a", link.getTag());
    }
}
