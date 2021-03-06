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

package org.opentox.toxotis.ontology.collection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class OTClassesTest {
    
    public OTClassesTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testOtClasses() {
        assertNotNull(OTClasses.forName("Algorithm"));
        assertNotNull(OTClasses.compound());
        assertNotNull(OTClasses.conformer());
        assertNotNull(OTClasses.dataEntry());
        assertNotNull(OTClasses.dataset());
        assertNotNull(OTClasses.errorReport());
        assertNotNull(OTClasses.featureValueNumeric());
        assertNotNull(OTClasses.feature());
        assertNotNull(OTClasses.featureValue());
        assertNotNull(OTClasses.featureValueNominal());
        assertNotNull(OTClasses.featureValuePair());
        assertEquals(OTClasses.forName("Parameter"), OTClasses.parameter());
        assertEquals(OTClasses.forName("VariableInfo"), OTClasses.variableInfo());
        assertTrue(OTClasses.getAll().contains(OTClasses.forName("FeatureValueNumeric")));
        assertTrue(OTClasses.getAll().contains(OTClasses.feature()));
        assertTrue(OTClasses.getAll().contains(OTClasses.multiParameter()));
        assertTrue(OTClasses.getAll().contains(OTClasses.model()));
        assertTrue(OTClasses.getAll().contains(OTClasses.openToxResource()));
        assertTrue(OTClasses.getAll().contains(OTClasses.qprfReport()));
        assertTrue(OTClasses.getAll().contains(OTClasses.report()));
    }
}
