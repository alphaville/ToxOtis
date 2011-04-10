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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.BibTeX;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.engine.ROG;
import static org.junit.Assert.*;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author chung
 */
public class FindBibTeXTest {

    public FindBibTeXTest() {
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
    public void testSomeMethod() throws DbException {
        ROG rog = new ROG();        

        for (int i = 1; i < 10; i++) {
            BibTeX bibtex = rog.nextBibTeX();
            
            AddBibTeX adder = new AddBibTeX(bibtex);
            adder.write();
            adder.close();

            FindBibTeX finder = new FindBibTeX(Services.anonymous());
            finder.setResolveUsers(true);
            finder.setSearchById(bibtex.getUri().getId());
            IDbIterator<BibTeX> iterator = finder.list();
            while (iterator.hasNext()) {
                BibTeX nextBibTexFound = iterator.next();
                assertEquals(bibtex.getAbstract(), nextBibTexFound.getAbstract());
                assertEquals(bibtex.getAddress(), nextBibTexFound.getAddress());
                assertEquals(bibtex.getAnnotation(), nextBibTexFound.getAnnotation());
                assertEquals(bibtex.getAuthor(), nextBibTexFound.getAuthor());
                assertEquals(bibtex.getBibType(), nextBibTexFound.getBibType());
                assertEquals(bibtex.getBookTitle(), nextBibTexFound.getBookTitle());
                assertEquals(bibtex.getChapter(), nextBibTexFound.getChapter());
                assertEquals(bibtex.getCopyright(), nextBibTexFound.getCopyright());
                assertEquals(bibtex.getCreatedBy(), nextBibTexFound.getCreatedBy());
                assertEquals(bibtex.getCrossref(), nextBibTexFound.getCrossref());
                assertEquals(bibtex.getEdition(), nextBibTexFound.getEdition());
                assertEquals(bibtex.getEditor(), nextBibTexFound.getEditor());
                assertEquals(bibtex.getIsbn(), nextBibTexFound.getIsbn());
                assertEquals(bibtex.getIssn(), nextBibTexFound.getIssn());
                assertEquals(bibtex.getJournal(), nextBibTexFound.getJournal());
                assertEquals(bibtex.getKey(), nextBibTexFound.getKey());
                assertEquals(bibtex.getKeywords(), nextBibTexFound.getKeywords());
                assertEquals(bibtex.getMeta().getContributors().iterator().next().getValue(), nextBibTexFound.getMeta().getContributors().iterator().next().getValue());
                assertEquals(bibtex.getMeta().getComments().iterator().next().getValue(), nextBibTexFound.getMeta().getComments().iterator().next().getValue());
                assertEquals(bibtex.getMeta().getCreators().iterator().next().getValue(), nextBibTexFound.getMeta().getCreators().iterator().next().getValue());
                assertEquals(bibtex.getMeta().getDescriptions().iterator().next().getValue(), nextBibTexFound.getMeta().getDescriptions().iterator().next().getValue());
                assertEquals(bibtex.getMeta().getIdentifiers().iterator().next().getValue(), nextBibTexFound.getMeta().getIdentifiers().iterator().next().getValue());
                assertEquals(bibtex.getMeta().getPublishers().iterator().next().getValue(), nextBibTexFound.getMeta().getPublishers().iterator().next().getValue());
                assertEquals(bibtex.getMeta().getRights().iterator().next().getValue(), nextBibTexFound.getMeta().getRights().iterator().next().getValue());
                assertEquals(bibtex.getMeta().getSubjects().iterator().next().getValue(), nextBibTexFound.getMeta().getSubjects().iterator().next().getValue());
                assertEquals(bibtex.getMeta().getTitles().iterator().next().getValue(), nextBibTexFound.getMeta().getTitles().iterator().next().getValue());
                assertEquals(3, bibtex.getMeta().getSeeAlso().size());
            }
            iterator.close();
            finder.close();
        }
    }
}
