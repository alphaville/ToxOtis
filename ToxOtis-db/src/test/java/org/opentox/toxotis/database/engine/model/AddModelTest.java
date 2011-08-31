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

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Model;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Feature;
import org.opentox.toxotis.core.component.Parameter;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.engine.ROG;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.database.pool.DataSourceFactory;
import org.opentox.toxotis.ontology.ResourceValue;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class AddModelTest {

    private static Throwable failure = null;
    private static final VRI baseVri = Services.ntua();
    private static final ROG _ROG_ = new ROG();

    public AddModelTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        //DataSourceFactory.getInstance().ping(10);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        //DataSourceFactory.getInstance().close();
    }

    @Before
    public void setUp() {
    }

    @After
    public synchronized void tearDown() {
    }

    @Test
    public synchronized void testWriteBruteForce() throws InterruptedException {
        int poolSize = 50;
        int folds = 3 * poolSize + 100;// just to make sure!!! (brutal?!)
        final ExecutorService es = Executors.newFixedThreadPool(poolSize);
        for (int i = 1; i <= folds; i++) {
            es.submit(new Runnable() {

                @Override
                public void run() {
                    try {
                        new AddModelTest().testAddModel();
                    } catch (final Throwable ex) {
                        failure = ex;
                        ex.printStackTrace();
                    }
                }
            });
        }

        es.shutdown();
        while (!es.isTerminated()) {
            Thread.sleep(100);
        }

        if (failure != null) {
            fail();
        }
    }

    @Test
    public synchronized void testWriteAndFindBruteForce() throws InterruptedException {
        int poolSize = 50;
        int folds = 3 * poolSize + 100;// just to make sure!!! (brutal?!)
        final ExecutorService es = Executors.newFixedThreadPool(poolSize);
        for (int i = 1; i <= folds; i++) {
            es.submit(new Runnable() {

                @Override
                public void run() {
                    try {
                        new AddModelTest().testAddAndFindModel();
                    } catch (final Throwable ex) {
                        failure = ex;
                        ex.printStackTrace();
                    }
                }
            });
        }

        es.shutdown();
        while (!es.isTerminated()) {
            Thread.sleep(100);
        }

        if (failure != null) {
            fail();
        }
    }

   @Test
    public synchronized void testAddModel() throws Exception {
        Model m = _ROG_.nextModel();
        m.setMeta(null);
        AddModel adder = new AddModel(m);
        adder.write();
        adder.close();

    }

   @Test
    public synchronized void testAddAndFindModel() throws Exception {
        Model m = _ROG_.nextModel();
        AddModel adder = null;
        try {
            adder = new AddModel(m);
            adder.write();
        } catch (DbException ex) {
            throw ex;
        } finally {
            if (adder != null) {
                adder.close();
            }
        }

        // Now find the model...
        FindModel finder = null;
        IDbIterator<Model> list = null;
        try {
            finder = new FindModel(new VRI(baseVri));
            finder.setResolveUsers(true);
            assertNotNull(finder);
            assertTrue(finder.isResolveUsers());
            finder.setSearchById(m.getUri().getId());
            list = finder.list();
            assertNotNull(list);
            assertTrue(list.hasNext());
            Model found = list.next();
            assertNotNull(found);
            assertNotNull(found.getDataset());
            assertEquals(m.getDataset(), found.getDataset());
            assertEquals(m.getAlgorithm(), found.getAlgorithm());
            assertEquals(m.getMeta(), found.getMeta());
            assertNotNull(found.getActualModel());
            assertNotNull(found.getAlgorithm().getUri());
            assertEquals(m.getAlgorithm().getUri(), found.getAlgorithm().getUri());
            assertNotNull(found.getCreatedBy());
            assertNotNull("Mail of retrieved user not found", found.getCreatedBy().getMail());
            assertNotNull("Name of retrieved user not found", found.getCreatedBy().getName());

            assertNotNull(found.getIndependentFeatures());
            assertFalse(found.getIndependentFeatures().isEmpty());
            assertNotNull(found.getDependentFeatures());
            assertFalse(found.getDependentFeatures().isEmpty());

            /*
             * An empty list was set for the predicted features.
             */
            assertNotNull(found.getPredictedFeatures());
            assertTrue(found.getPredictedFeatures().isEmpty());

            Set<Parameter> foundParameters = found.getParameters();
            assertNotNull(foundParameters);
            assertFalse(foundParameters.isEmpty());
            assertTrue(foundParameters.size() == m.getParameters().size());

            for (Parameter q : foundParameters) {
                HashSet<ResourceValue> sources = q.getMeta().getHasSources();
                assertNotNull(sources);
                assertFalse(sources.isEmpty());
                assertTrue(sources.iterator().next().getUri().equals(m.getUri()));
            }

            for (Parameter p : m.getParameters()) {
                assertTrue(foundParameters.contains(p));
            }



        } catch (DbException ex) {
            throw ex;
        } finally {
            DbException dbex = null;
            if (list != null) {
                try {
                    list.close();
                } catch (DbException x) {
                    dbex = x;
                }
            }
            if (finder != null) {
                try {
                    finder.close();
                } catch (DbException x) {
                    dbex = x;
                }
            }
            if (dbex != null) {
                dbex.printStackTrace();
                fail(dbex.getMessage());
            }
        }
    }

    @Test
    public void testAddModelNoFeatures() throws Exception {
        Model m = _ROG_.nextModel();
        m.setDependentFeatures(null);
        m.setIndependentFeatures(null);
        m.setPredictedFeatures(null);
        AddModel adder = null;
        try {
            adder = new AddModel(m);
            adder.write();
        } catch (DbException ex) {
            throw ex;
        } finally {
            if (adder != null) {
                adder.close();
            }
        }
    }

    @Test
    public void testModelNullMeta() throws Exception {
        Model m = _ROG_.nextModel();
        m.setMeta(null);
        AddModel adder = null;
        try {
            adder = new AddModel(m);
            adder.write();
        } catch (DbException ex) {
            throw ex;
        } finally {
            if (adder != null) {
                adder.close();
            }
        }

        FindModel finder = new FindModel(new VRI(baseVri));
        finder.setSearchById(m.getUri().getId());
        IDbIterator<Model> list = finder.list();
        assertTrue(list.hasNext());
        Model found = list.next();
        assertNull(found.getMeta());
        assertFalse(list.hasNext());
        list.close();
        finder.close();
    }

    @Test
    public void testModelMeta() throws Exception {
        //TODO: Implementation WANTED!
    }

    @Test
    public void testModelCreator() throws Exception {
        //TODO: Implementation WANTED!
    }

    @Test
    public void testModelFeatureUnits() throws Exception {
        //TODO: Implementation WANTED!
    }
}
