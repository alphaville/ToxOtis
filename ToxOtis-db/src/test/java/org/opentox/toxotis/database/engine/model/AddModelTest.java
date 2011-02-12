package org.opentox.toxotis.database.engine.model;

import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Algorithm;
import org.opentox.toxotis.core.component.Feature;
import org.opentox.toxotis.core.component.Model;
import org.opentox.toxotis.core.component.Parameter;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class AddModelTest {

    public AddModelTest() {
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
        VRI vri1 = new VRI("http://opentox.ntua.gr:4000/model/"+UUID.randomUUID().toString());
        VRI datasetUri = new VRI("http://otherServer.com:7000/dataset/1");

        VRI f1 = new VRI("http://otherServer.com:7000/feature/1");
        VRI f2 = new VRI("http://otherServer.com:7000/feature/2");
        VRI f3 = new VRI("http://otherServer.com:7000/feature/3");


        Parameter p = new Parameter();
        p.setUri(new VRI("http://no.such.service.net/jaqpot/parameter/"+UUID.randomUUID().toString()));
        p.setName("gamma");
        p.setScope(Parameter.ParameterScope.OPTIONAL);
        p.setTypedValue(new LiteralValue<Double>(100.0d, XSDDatatype.XSDdouble));


        Model m = new Model(vri1);
//        m.setParameters(new HashSet<Parameter>());
//        m.getParameters().add(p);
        m.setDataset(datasetUri);

        m.setDependentFeatures(new ArrayList<Feature>());
        m.setIndependentFeatures(new ArrayList<Feature>());

        m.getIndependentFeatures().add(new Feature(f1));
        m.getDependentFeatures().add(new Feature(f1));
        m.getDependentFeatures().add(new Feature(f2));
        m.getDependentFeatures().add(new Feature(f3));
        m.setCreatedBy(User.GUEST);
        m.setActualModel(new MetaInfoImpl());
        m.setLocalCode(UUID.randomUUID().toString());
        m.setAlgorithm(new Algorithm("http://algorithm.server.co.uk:9000/algorithm/mlr"));
        
        new AddModel(m).write();

    }

}