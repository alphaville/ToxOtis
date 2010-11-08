/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.impl.OntModelImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;
import org.opentox.toxotis.util.spiders.AnyValue;
import static org.junit.Assert.*;

/**
 *
 * @author hampos
 */
public class MultiParameterTest {

    public MultiParameterTest() {
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
    public void testCreateIndiv() {
        VariableValue<String> varValue1 = new VariableValue<String>();
        varValue1.setValue(new AnyValue<String>("100"));
        VariableInfo varInfo = new VariableInfo();
        varInfo.setScope(Parameter.ParameterScope.MANDATORY);
        varInfo.setMeta(new MetaInfoImpl().setTitle("x"));
        varValue1.setVariableInfo(varInfo);

        VariableValue<String> varValue2 = new VariableValue<String>();
        varValue2.setValue(new AnyValue<String>("200"));
        VariableInfo varInfo2 = new VariableInfo();
        varInfo2.setScope(Parameter.ParameterScope.OPTIONAL);
        varInfo2.setMeta(new MetaInfoImpl().setTitle("y"));
        varValue2.setVariableInfo(varInfo2);

        SetValue setValue = new SetValue();
        setValue.getValues().add(varValue1);
        setValue.getValues().add(varValue2);

        MultiParameter multiParam = new MultiParameter();
        multiParam.getSetValues().add(setValue);

        OntModel model = new OntModelImpl(OntModelSpec.OWL_DL_MEM);
        Individual indiv = multiParam.asIndividual(model);

        model.write(System.out);



    }

}