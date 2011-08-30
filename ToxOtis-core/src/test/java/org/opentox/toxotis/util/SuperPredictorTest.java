/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opentox.toxotis.util;

import java.net.URISyntaxException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class SuperPredictorTest {
    
    public SuperPredictorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of getSuperServiceUri method, of class SuperPredictor.
     */
    @Test
    public void testGetSuperServiceUri() {
    }

    /**
     * Test of setSuperServiceUri method, of class SuperPredictor.
     */
    @Test
    public void testSetSuperServiceUri() {
    }

    /**
     * Test of predict method, of class SuperPredictor.
     */
    @Test
    public void testPredict() throws Exception {
        AuthenticationToken at = new AuthenticationToken("guest","guest");
        SuperPredictor predictor = new SuperPredictor(
                new VRI("http://apps.ideaconsult.net:8080/ambit2/compound/101"),
                new VRI("http://opentox.ntua.gr:8080/model/059d8bf0-cccc-4ad1-ad40-d15a663894fc"),at);
        predictor.prediction();
    }
}
