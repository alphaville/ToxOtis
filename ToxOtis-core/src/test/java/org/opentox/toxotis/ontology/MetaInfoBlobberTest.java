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
package org.opentox.toxotis.ontology;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class MetaInfoBlobberTest {

    public MetaInfoBlobberTest() {
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
    public void testBlobbinbDeblobbing() {
        MetaInfo mi = new MetaInfoImpl();
        mi.addTitle("any title").addTitle("other title").addDescription("abc").
                addHasSource(new ResourceValue(Services.anonymous(), OTClasses.Model())).
                addContributor("me").addContributor("you").
                addAudience("some audience").addSeeAlso(new ResourceValue(Services.ideaconsult().augment("feature","100"), OTClasses.Feature()));
        MetaInfoBlobber mib = new MetaInfoBlobber(mi);
        
        try {
            Blob blob = mib.toBlob();

            MetaInfoDeblobber mid = new MetaInfoDeblobber(blob);
            MetaInfo backFromBlob = mid.toMetaInfo();

            System.out.println(backFromBlob.equals(mi));

        } catch (Exception ex) {
            Logger.getLogger(MetaInfoBlobberTest.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
}
