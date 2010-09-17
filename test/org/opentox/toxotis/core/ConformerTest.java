package org.opentox.toxotis.core;

import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class ConformerTest {

    public ConformerTest() {
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
    public void testSomeMethod() throws Exception{
        VRI vri = new VRI("http://ambit.uni-plovdiv.bg:8080/ambit2/compound/226519/conformer/423169/?a=1&b=&c=6&&");
        System.out.println("QUERY : "+vri.getQueryAsString());

        //        Conformer c = new Conformer(vri);
//        System.out.println(c.getUri());
//        System.out.println(c.getProperty(new VRI("http://ambit.uni-plovdiv.bg:8080/ambit2/feature/22212")));
//
//        Set<VRI> features = c.getAvailableFeatures();
//        for (VRI f : features){
//            System.out.println(f);
//        }
        
    }

}