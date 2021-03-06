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
package org.opentox.toxotis.database.engine.bibtex;

import java.net.URISyntaxException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.BibTeX;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.database.DbWriter;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.util.ROG;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.database.global.DbConfiguration;
import org.opentox.toxotis.database.pool.DataSourceFactory;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class AddBibTeXTest {

    private static Throwable failure = null;

    public AddBibTeXTest() {
        super();
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        DbConfiguration.setC3p0PropertiedFile(DbConfiguration.TEST_C3P0_FILE);
        org.opentox.toxotis.database.TestUtils.setUpDB();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        DataSourceFactory.getInstance().close();
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws DbException {
    }

    @Test
    public void testWriteBibTex() throws InterruptedException {
        int poolSize = 100;
        int folds = 10 * poolSize + 100;// just to make sure!!! (brutal?!)
        final ExecutorService es = Executors.newFixedThreadPool(poolSize);
        for (int i = 1; i <= folds; i++) {
            es.submit(new Runnable() {

                @Override
                public void run() {
                    try {
                        new AddBibTeXTest().testRegisterBibTeX();
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
    public void testRegisterBibTeXWithNULL() throws DbException {
        BibTeX bt = new ROG().nextBibTeX();
        bt.setVolume(null);
        bt.setAddress(null);
        bt.setChapter(null);
        DbWriter writer = new AddBibTeX(bt);
        assertEquals(1, writer.write());
        writer.close();

        FindBibTeX finder = new FindBibTeX(Services.anonymous());
        finder.setSearchById(bt.getUri().getId());
        IDbIterator<BibTeX> iterator = finder.list();
        boolean hasNext = false;
        if (iterator.hasNext()) {
            hasNext = true;
            BibTeX next = iterator.next();
            assertNotNull(next.getAbstract());
            assertNull(next.getAddress());
            assertNull(next.getVolume());
            assertEquals(bt.getAuthor(), next.getAuthor());
            assertEquals(bt.getJournal(), next.getJournal());
            assertEquals(bt.getYear(), next.getYear());
        }
        assertTrue(hasNext);
        iterator.close();
        finder.close();
    }

    @Test
    public void testAddBibTeXNullMeta() throws DbException {
        BibTeX bt = new ROG().nextBibTeX();
        bt.setMeta(null);
        bt.setPages(null);
        DbWriter writer = new AddBibTeX(bt);
        assertEquals(1, writer.write());
        writer.close();

        assertNotNull(bt.getUri().getId());

        FindBibTeX finder = new FindBibTeX(Services.anonymous());
        finder.setSearchById(bt.getUri().getId());
        IDbIterator<BibTeX> iterator = finder.list();
        boolean hasNext = false;
        if (iterator.hasNext()) {
            hasNext = true;
            BibTeX next = iterator.next();
            assertNull(next.getPages());
            assertNotNull(next.getMeta());
            assertTrue(next.getMeta().isEmpty());
        }
        assertTrue(hasNext);
        iterator.close();
        finder.close();
    }

    @Test
    public void testRegisterBibTeX() throws DbException {
        User u = User.GUEST;
        BibTeX bt = null;
        try {
            bt = new BibTeX(new VRI("http://someservice.com/bibtex/" + UUID.randomUUID().toString()));
        } catch (final ToxOtisException ex) {
            throw new RuntimeException(ex);
        } catch (final URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
        bt.setAuthor("SOme Author");
        bt.setCreatedBy(u);
        bt.setVolume(1);
        bt.setAddress(UUID.randomUUID().toString());
        bt.setCopyright(UUID.randomUUID().toString());
        DbWriter writer = new AddBibTeX(bt);
        try {
            if (1 != writer.write()) {
                throw new DbException();
            }
        } catch (DbException ex) {
            throw ex;
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
