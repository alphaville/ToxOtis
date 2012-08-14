/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.toxotis.util.arff;

import java.io.IOException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.collection.Services;
import static org.junit.Assert.*;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import weka.core.Instances;

/**
 *
 * @author chung
 */
public class RemoteArffRertieverTest {

    public RemoteArffRertieverTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testRaR() throws ServiceInvocationException, IOException {
        int nCompounds = 5;
        RemoteArffRertiever rar = new RemoteArffRertiever(
                Services.ideaconsult().augment("dataset", "585036").addUrlParameter("max", nCompounds));

        
        boolean fail = true;
        Instances in = null;
        int i=0;
        while ((in = rar.nextData()) != null) {
            i++;
            assertNotNull(in);
            fail = false;
        }
        if (fail){
            fail("Nothing was retrieved!");
        }
        assertEquals(i, nCompounds);
        rar.close();
    }
}
