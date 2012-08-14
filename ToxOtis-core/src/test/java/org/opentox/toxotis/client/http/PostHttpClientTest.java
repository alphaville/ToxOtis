package org.opentox.toxotis.client.http;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.IPostClient;
import org.opentox.toxotis.client.collection.Services;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class PostHttpClientTest {
    
    public PostHttpClientTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testEasy() {
        PostHttpClient client = (PostHttpClient) ClientFactory.createPostClient(Services.anonymous());
        client.addPostParameter("a", "1");
        client.addPostParameter("a", "2");
        client.addPostParameter("b", "3");
        client.addPostParameter("c", "3");
        client.addPostParameter("c", "3");       
        assertEquals("Client was not parametrised properly","a=1&a=2&b=3&c=3&c=3",client.getParametersAsQuery());
    }
}
