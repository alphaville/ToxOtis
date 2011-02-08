package org.opentox.toxotis.janitor;

import java.net.URISyntaxException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.IPostClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() throws URISyntaxException, ServiceInvocationException
    {
        IPostClient client = ClientFactory.createPostClient(new VRI("http://opentox.ntua.gr:4000/model/0d8a9a27-3481-4450-bca1-d420a791de9d"));
        client.addPostParameter("dataset_uri", "http://apps.ideaconsult.net:8080/ambit2/dataset/54");
        client.setMediaType(Media.TEXT_URI_LIST);
        client.post();
        int code = client.getResponseCode();
        while (code!=200){
            System.out.println(code);
            code = client.getResponseCode();
        }
        assertTrue( true );
    }

    
}
