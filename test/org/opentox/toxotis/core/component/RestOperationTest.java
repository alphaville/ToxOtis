/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opentox.toxotis.core.component;

import java.util.HashSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.collection.HttpMethods.MethodsEnum;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTRestClasses;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class RestOperationTest {

    public RestOperationTest() {
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
    public void testCreateRDF() {
        RestOperation ro = new RestOperation();
        ro.setMethod(MethodsEnum.POST);
        ro.addRestClasses(OTRestClasses.POST_Operation(), OTRestClasses.OperationAlgorithm(), OTRestClasses.OperationResultTask());

        HttpParameter param = new HttpParameter();
        param.addParamContent(OTClasses.Algorithm()).addInputParamClass(OTRestClasses.InputParameterCompound(), OTRestClasses.InputParameterDataset());
        param.setParamName("myParam");
        param.setParamOptional(true);        
        ro.addHttpParameters(param);        

        ro.addHttpStatusCodes(new HttpStatus(OTRestClasses.STATUS_200().setMetaInfo(new MetaInfoImpl().addDescription("It's client's fault"))));
        

      
        ro.asOntModel().write(System.out);       



    }

}