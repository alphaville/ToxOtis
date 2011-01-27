/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opentox.toxotis.client.http;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.ErrorReport;
import org.opentox.toxotis.core.component.Feature;
import static org.junit.Assert.*;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.util.spiders.ErrorReportSpider;

/**
 *
 * @author chung
 */
public class AbstractHttpClientTest {

    public AbstractHttpClientTest() {
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
    public void testSomeMethod() throws ServiceInvocationException {
        PostHttpClient poster = new PostHttpClient(Services.ntua().augment("bibtex"));
        poster.setMediaType(Media.APPLICATION_RDF_XML);
        poster.post();        
        ErrorReport er = new ErrorReportSpider(poster.getResponseOntModel()).parse();
        System.out.println(er.getMessage());
    }

    @Test
    public void testPOst() throws Exception {
        PostHttpClient client = new PostHttpClient(Services.ntua().augment("bibtex"));
        client.setContentType(Media.APPLICATION_RDF_XML);
        client.addPostParameter("x", "1");
        client.setMediaType("text/uri-list");
        client.post();
        System.out.println(client.getResponseCode());
        throw new UnsupportedOperationException("This test is not over yet!");
    }

}