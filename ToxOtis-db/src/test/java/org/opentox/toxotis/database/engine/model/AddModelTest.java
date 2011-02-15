/*
 *
 * ToxOtis
 *
 * ToxOtis is the Greek word for Sagittarius, that actually means ‘archer’. ToxOtis
 * is a Java interface to the predictive toxicology services of OpenTox. ToxOtis is
 * being developed to help both those who need a painless way to consume OpenTox
 * services and for ambitious service providers that don’t want to spend half of
 * their time in RDF parsing and creation.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 *
 */


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
        org.opentox.toxotis.database.pool.DataSourceFactory.getInstance().close();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAddModel() throws Exception{
        VRI vri1 = new VRI("http://opentox.ntua.gr:4000/model/"+UUID.randomUUID().toString());
        VRI datasetUri = new VRI("http://otherServer.com:7000/dataset/1");

        VRI f1 = new VRI("http://otherServer.com:7000/feature/1");
        VRI f2 = new VRI("http://otherServer.com:7000/feature/2");
        VRI f3 = new VRI("http://otherServer.com:7000/feature/3");


        Parameter p = new Parameter();
        p.setUri(new VRI("http://no.such.service.net/jaqpot/parameter/"+UUID.randomUUID().toString()));
        p.setName("alpha");
        p.setScope(Parameter.ParameterScope.OPTIONAL);
        p.setTypedValue(new LiteralValue(19, XSDDatatype.XSDint));


        Model m = new Model(vri1);
        m.setParameters(new HashSet<Parameter>());
        m.getParameters().add(p);
        m.setDataset(datasetUri);

        m.setDependentFeatures(new ArrayList<Feature>());
        m.setIndependentFeatures(new ArrayList<Feature>());

        m.getIndependentFeatures().add(new Feature(f1));
        m.getDependentFeatures().add(new Feature(f1));
        m.getDependentFeatures().add(new Feature(f2));
        m.getDependentFeatures().add(new Feature(f3));
        m.setCreatedBy(User.GUEST);
        m.setActualModel(new MetaInfoImpl());// just for the sake to write something in there!
        m.setLocalCode(UUID.randomUUID().toString());
        m.setAlgorithm(new Algorithm("http://algorithm.server.co.uk:9000/algorithm/mlr"));
        
        new AddModel(m).write();

    }

}