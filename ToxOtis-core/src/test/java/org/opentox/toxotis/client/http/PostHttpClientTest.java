package org.opentox.toxotis.client.http;

import com.hp.hpl.jena.util.FileUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.HttpStatusCodes;
import org.opentox.toxotis.client.IPostClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;

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
        /*PostHttpClient client = (PostHttpClient) ClientFactory.createPostClient(Services.anonymous());
        client.addPostParameter("a", "1");
        client.addPostParameter("a", "2");
        client.addPostParameter("b", "3");
        client.addPostParameter("c", "3");
        client.addPostParameter("c", "3");       
        assertEquals("Client was not parametrised properly","a=1&a=2&b=3&c=3&c=3",client.getParametersAsQuery());*/
        Boolean success = false;
        int status =0;
        
        String remoteResult;
        try {
            String targetFileStr = FileUtils.readWholeFileAsUTF8("C:\\Users\\philip\\Downloads\\testCSVFinal.csv");
           
            VRI vr = new VRI("http://localhost:8084/ambit2-www/substance");
              InputStream is = (InputStream) new ByteArrayInputStream(targetFileStr.getBytes());
            IPostClient client = null;
            try {
                client = ClientFactory.createPostClient(vr);
            } catch (Exception ex) {
            }
        
            client.setContentType(Media.MEDIA_MULTIPART_FORM_DATA);
            client.setMediaType(Media.TEXT_URI_LIST);
            client.addPostParameter("da_uri", "new");
            client.setPostable(is);
            client.setPostableFilename("files[]", "testCSV.csv");
            client.post();
            
            status = client.getResponseCode();
            remoteResult = client.getResponseText();
        } catch (ServiceInvocationException ex) {
            
        } catch (URISyntaxException ex) {
            
        } catch (IOException ex) {
            
        }

        
        if (status == HttpStatusCodes.Accepted.getStatus()) {
            success = true;
        } else if (status == HttpStatusCodes.Success.getStatus()) {
            success = true;
        }
        success = false;
    }
}
