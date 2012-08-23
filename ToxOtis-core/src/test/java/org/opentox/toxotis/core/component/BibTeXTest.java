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
package org.opentox.toxotis.core.component;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.collection.Services;
import static org.junit.Assert.*;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;

/**
 *
 * @author chung
 */
public class BibTeXTest {

    public BibTeXTest() {
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
    public void testParseBibTex() throws ToxOtisException, ServiceInvocationException {
        BibTeX bt = new BibTeX(Services.ntua().augment("bibtex", "caco2")).loadFromRemote();
        assertNotNull(bt.getAbstract());
        assertNotNull(bt.getAuthor());
        assertNotNull(bt.getJournal());
        assertNotNull(bt.getBibType());
        assertNotNull(bt.getBibTexService());
        String btDownloadedString = bt.toString();//downloaded and **converted** to string
        assertNotNull("Conversion to String returns null", btDownloadedString);
        assertFalse("Conversion to String returns empty string", btDownloadedString.isEmpty());
        assertTrue("Bad Conversion from BibTeX to String", 
                btDownloadedString.contains("Series = \"ADME Evaluation in Drug Discovery\""));
        assertTrue("Bad Conversion from BibTeX to String", 
                btDownloadedString.contains("Number = \"5\""));
        assertFalse(btDownloadedString.contains("Pass"));
        assertFalse(btDownloadedString.contains("opensso.in-silico.ch"));
    }

    @Test
    public void testPublishBibTeX() throws ToxOtisException, ServiceInvocationException {
    }
}