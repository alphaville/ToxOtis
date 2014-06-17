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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.BibTeX;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.util.ROG;
import static org.junit.Assert.*;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.database.pool.DataSourceFactory;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.ResourceValue;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;

/**
 *
 * @author chung
 */
public class FindBibTeXTest {

    public FindBibTeXTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        org.opentox.toxotis.database.TestUtils.setUpDB();
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
    public void testWriteReadBibTeXNullMeta() throws DbException {
        ROG rog = new ROG();
        BibTeX bibtex = rog.nextBibTeX();
        bibtex.setMeta(null);
        AddBibTeX adder = new AddBibTeX(bibtex);
        adder.write();
        adder.close();

        FindBibTeX finder = new FindBibTeX(Services.anonymous());
        finder.setResolveUsers(true);
        finder.setSearchById(bibtex.getUri().getId());
        IDbIterator<BibTeX> iterator = finder.list();
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testWriteReadBibTeX() throws DbException {
        ROG rog = new ROG();

        for (int i = 1; i < 10; i++) {
            BibTeX bibtex = rog.nextBibTeX();
            bibtex.getMeta().addSeeAlso(new ResourceValue(
                    Services.anonymous().augment("xyz", "compound", rog.nextString(10)),
                    OTClasses.compound())).addSeeAlso(new ResourceValue(
                                    Services.anonymous().augment("xyz", "compound", rog.nextString(10)),
                                    OTClasses.compound()));

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
                assertEquals(bibtex.getUrl(), nextBibTexFound.getUrl());
                assertEquals(bibtex.getVolume(), nextBibTexFound.getVolume());
                assertEquals(bibtex.getYear(), nextBibTexFound.getYear());
                
                
                
                /* Assertions on the retrieved metadata */               
                // Really exhaustive testing...               
                assertEquals(((MetaInfoImpl)bibtex.getMeta()).getHash(), nextBibTexFound.getMeta().getHash()); //this shoudl suffice! (Updated in v0.8.8-SNAPSHOT)
                assertEquals(bibtex.getMeta().getIdentifiers().size(), nextBibTexFound.getMeta().getIdentifiers().size());                                
                assertEquals(bibtex.getMeta().getIdentifiers().iterator().next().getValue(), nextBibTexFound.getMeta().getIdentifiers().iterator().next().getValue());
                assertEquals(bibtex.getMeta().getPublishers().iterator().next().getValue(), nextBibTexFound.getMeta().getPublishers().iterator().next().getValue());
                assertEquals(bibtex.getMeta().getRights().iterator().next().getValue(), nextBibTexFound.getMeta().getRights().iterator().next().getValue());

                if (bibtex.getMeta().getTitles() != null) {                    
                    assertNotNull(nextBibTexFound.getMeta().getTitles());
                    assertEquals(bibtex.getMeta().getTitles().size(), nextBibTexFound.getMeta().getTitles().size());
                    Iterator<LiteralValue> bibtexTitlesIterator = bibtex.getMeta().getTitles().iterator();
                    Iterator<LiteralValue> foubdBibtexTitlesIterator = nextBibTexFound.getMeta().getTitles().iterator();
                    Set<String> setTitles = new HashSet<String>();
                    Set<String> foundSetTitles = new HashSet<String>();
                    while (bibtexTitlesIterator.hasNext()) {
                        setTitles.add(bibtexTitlesIterator.next().getValueAsString());
                        foundSetTitles.add(foubdBibtexTitlesIterator.next().getValueAsString());
                    }
                    assertTrue(setTitles.containsAll(foundSetTitles));                    
                }
                assertEquals(3, bibtex.getMeta().getSeeAlso().size());
                assertEquals(nextBibTexFound.getMeta().getSeeAlso().size(), bibtex.getMeta().getSeeAlso().size());                
                assertEquals(3, bibtex.getMeta().getCreators().size());
            }
            iterator.close();
            finder.close();
        }
    }
}
