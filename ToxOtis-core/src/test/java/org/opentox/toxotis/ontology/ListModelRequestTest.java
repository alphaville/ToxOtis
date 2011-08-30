/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.toxotis.ontology;

import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class ListModelRequestTest {
    
    public ListModelRequestTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of getModelInfo method, of class ListModelRequest.
     */
    @Test
    public void testGetModelInfo() throws Exception {
        ListModelRequest request = new ListModelRequest();
        ArrayList<String[]> result = request.getModelInfo();
        for (String[] array : result){
            System.out.println(array[4]);
        }
    }
}
