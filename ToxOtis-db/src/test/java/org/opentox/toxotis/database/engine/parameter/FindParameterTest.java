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
import org.opentox.toxotis.core.component.Model;
import org.opentox.toxotis.core.component.Parameter;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.engine.ROG;
import org.opentox.toxotis.database.engine.model.AddModel;
import org.opentox.toxotis.database.engine.model.AddModelTest;
import org.opentox.toxotis.database.engine.model.ListModel;
import static org.junit.Assert.*;
import org.opentox.toxotis.database.pool.DataSourceFactory;
import org.opentox.toxotis.ontology.LiteralValue;
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
        org.opentox.toxotis.database.TestUtils.setUpDB();
        new AddModelTest().testAddAndFindModel();
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
        System.out.println("#testAddParameter");
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
        p.setScope(Parameter.ParameterScope.OPTIONAL);
        assertEquals(Parameter.ParameterScope.OPTIONAL, p.getScope());


        AddParameter adder = new AddParameter(p, modelVri);
        adder.write();
        adder.close();

        FindParameter finder = new FindParameter(Services.anonymous());
        finder.setSearchById(p.getUri().getId());
        IDbIterator<Parameter> iterator = finder.list();
        boolean hasNext = false;
        if (iterator.hasNext()) {
            hasNext = true;
            Parameter found = iterator.next();
            assertNotNull(found.getScope());
            assertEquals(Parameter.ParameterScope.OPTIONAL, found.getScope());
        }
        assertTrue(hasNext);
    }

    @Test
    public void testAddParameterNullMeta() throws Exception {
        System.out.println("#testAddParameterNullMeta");
        for (int i = 0; i < 10; i++) {
            ListModel lister = new ListModel();
            lister.setPageSize(1);
            IDbIterator<String> iter = lister.list();
            assertTrue(iter.hasNext());
            String modelId = iter.next();
            iter.close();

            Parameter p1 = new Parameter();
            p1.setName("d");
            p1.setScope(Parameter.ParameterScope.OPTIONAL);
            p1.setTypedValue(new LiteralValue(5.3254234, XSDDatatype.XSDdouble));
            p1.setUri(Services.ntua().augment("parameter", UUID.randomUUID().toString()));
            p1.setMeta(null);
            AddParameter adder = new AddParameter(p1, Services.anonymous().augment("model", modelId));
            adder.write();
            adder.close();

            FindParameter finder = new FindParameter(Services.anonymous());
            finder.setSearchById(p1.getUri().getId());
            IDbIterator<Parameter> iterator = finder.list();
            boolean hasNext = false;
            if (iterator.hasNext()) {
                hasNext = true;
                Parameter p = iterator.next();
                assertNotNull(p.getMeta());
                assertNotNull(p.getMeta().getTitles());
                assertTrue(p.getMeta().getTitles().contains(p1.getName()));
                assertEquals(p1.getName(), p.getName());
                assertEquals(p1.getType(), p.getType());
                assertEquals(p1.getTypedValue(), p.getTypedValue());
                assertEquals(p1.getScope(), p.getScope());
            } else {
                fail("Param not registered");
            }
            assertTrue(hasNext);
            iterator.close();
            finder.close();
        }
    }

    @Test
    public void testParameterRetrieval() throws Exception {
        System.out.println("#testParameterRetrieval");
        ROG rog = new ROG();
        String prmValue = rog.nextString(3);
        String prmId = String.valueOf(rog.nextLong());
        System.out.println(prmId);
        Model model = rog.nextModel();
        Parameter rndPrm = new Parameter(Services.anonymous().augment("parameter", prmId));
        rndPrm.setName("xyz").
                setScope(Parameter.ParameterScope.MANDATORY).
                setTypedValue(new LiteralValue(prmValue));
        model.getParameters().add(rndPrm);
        AddModel modelWriter = new AddModel(model);
        modelWriter.write();
        modelWriter.close();

        FindParameter finder = new FindParameter(Services.ntua());
        finder.setSearchById(prmId);
        IDbIterator<Parameter> paramIterator = finder.list();
        assertTrue(paramIterator.hasNext());
        Parameter found = paramIterator.next();
        System.out.println(found.getName());//Name is retri
        System.out.println(found.getMeta().getTitles());
        paramIterator.close();
        finder.close();
    }

    @Test
    public void testAddFindParameter() throws Exception {
        System.out.println("#testAddFindParameter");
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
                addSameAs(new ResourceValue(modelVri, OTClasses.conformer())));

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
        finder.setSearchById(p1.getUri().getId());
        IDbIterator<Parameter> iterator = finder.list();
        boolean hasNext = false;
        if (iterator.hasNext()) {
            hasNext = true;
            Parameter p = iterator.next();
            assertNotNull("META is NULL", p.getMeta());
            assertNotNull("Descriptions is NULL", p.getMeta().getDescriptions());
            assertTrue(!p.getMeta().getDescriptions().isEmpty());
            assertEquals(p.getMeta(), p1.getMeta());
            assertEquals(p.getName(), p1.getName());
            assertEquals(p.getScope(), p1.getScope());
            assertEquals(p.getType(), p1.getType());
            assertEquals(p.getTypedValue(), p1.getTypedValue());
        } else {
            fail("Param not registered");
        }
        assertTrue(hasNext);
        iterator.close();
        finder.close();
    }
}
