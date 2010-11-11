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
        VariableValue<String> varValue1 = new VariableValue<String>("x", "100", Parameter.ParameterScope.MANDATORY);
        VariableValue<String> varValue2 = new VariableValue<String>("y", "200", Parameter.ParameterScope.OPTIONAL);
        VariableValue<String> varValue3 = new VariableValue<String>("x", "300");
        VariableValue<String> varValue4 = new VariableValue<String>("y", "400");

        ParameterValue setValue = new ParameterValue(1, varValue1, varValue2);
        ParameterValue setValue2 = new ParameterValue(2, varValue3, varValue4);

        MultiParameter multiParam = new MultiParameter();
        multiParam.getMeta().addTitle("myParam");
        multiParam.getParameterValues().add(setValue);
        multiParam.getParameterValues().add(setValue2);
        multiParam.setScope(Parameter.ParameterScope.OPTIONAL);

        OntModel model = new OntModelImpl(OntModelSpec.OWL_DL_MEM);
        multiParam.asIndividual(model);

        model.write(System.out);



    }
}
