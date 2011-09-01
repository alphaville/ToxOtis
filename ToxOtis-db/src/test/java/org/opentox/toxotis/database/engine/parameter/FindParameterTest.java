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
package org.opentox.toxotis.database.engine.parameter;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import java.util.UUID;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Parameter;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.engine.model.ListModel;
import static org.junit.Assert.*;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.database.pool.DataSourceFactory;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.ResourceValue;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;

/**
 *
 * @author chung
 */
public class FindParameterTest {

    public FindParameterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        DataSourceFactory.getInstance().ping(100);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        DataSourceFactory.getInstance().close();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAddParameter() throws Exception {

        ListModel modelLister = new ListModel();
        IDbIterator<String> it = modelLister.list();
        String modelUri = null;
        if (it.hasNext()) {
            modelUri = it.next();
        }
        VRI modelVri = Services.ntua().augment("model", modelUri);
        it.close();
        modelLister.close();

        Parameter p = new Parameter(Services.ntua().augment(
                "parameter", System.currentTimeMillis()),
                "x", new LiteralValue(5, XSDDatatype.XSDbyte));
        AddParameter adder = new AddParameter(p, modelVri);
        adder.write();
        adder.close();
    }

    @Test
    public void testAddFindParameter() throws Exception {
        ListModel modelLister = new ListModel();
        IDbIterator<String> it = modelLister.list();
        String modelUri = null;
        if (it.hasNext()) {
            modelUri = it.next();
        }
        VRI modelVri = Services.ntua().augment("model", modelUri);
        it.close();
        modelLister.close();
        Parameter p1 = new Parameter();
        p1.setName("d");
        p1.setScope(Parameter.ParameterScope.OPTIONAL);
        p1.setTypedValue(new LiteralValue(1, XSDDatatype.XSDinteger));
        p1.setUri(Services.ntua().augment("parameter", UUID.randomUUID().toString()));

        /*
         * Add some meta-data
         */
        p1.setMeta(new MetaInfoImpl().addDescription("XXXYYY").
                addSameAs(new ResourceValue(modelVri, OTClasses.Conformer())));


        /*
         * Add parameter
         */
        AddParameter adder = new AddParameter(p1, modelVri);
        adder.write();
        adder.close();

        /*
         * Check whether exactly the same object is retrieved...
         */
        FindParameter finder = new FindParameter(Services.anonymous());
        finder.setWhere(String.format("Parameter.id='%s'", p1.getUri().getId()));
        IDbIterator<Parameter> iterator = finder.list();
        if (iterator.hasNext()) {
            Parameter p = iterator.next();
            assertNotNull("META is NULL", p.getMeta());
            assertNotNull("Descriptions is NULL", p.getMeta().getDescriptions());
            assertTrue(!p.getMeta().getDescriptions().isEmpty());
            assertEquals(p.getMeta(),p1.getMeta());
        } else {
            fail("Param not registered");
        }
        iterator.close();
        finder.close();
    }
}
