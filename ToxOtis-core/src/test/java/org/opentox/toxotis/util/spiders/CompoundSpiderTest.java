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
package org.opentox.toxotis.util.spiders;

import java.util.ArrayList;
import java.util.HashSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.opentox.toxotis.core.component.Compound;
import org.opentox.toxotis.util.SimilarityRetriever;

/**
 *
 * @author chung
 */
public class CompoundSpiderTest {

    public CompoundSpiderTest() {
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

    /**
     * Test of parse method, of class CompoundSpider.
     */
    @Test
    public void testParse() throws Exception {
        System.out.println("parse");
//        CompoundSpider spider = new CompoundSpider("phenol", null);
        CompoundSpider spider = new CompoundSpider("http://apps.ideaconsult.net:8080/ambit2/compound/24793", null);
        Compound cmp = spider.parse();        
        System.out.println(cmp.getIupacName());
        System.out.println(cmp.getUri());
        System.out.println(cmp.getUri().getOntologicalClass());
        System.out.println(cmp.getUri().getOpenToxType());
        SimilarityRetriever sr = new SimilarityRetriever(0.95, cmp);
        ArrayList<Compound> sim = sr.similarCompounds();
        for (Compound c : sim){
            System.out.println("  o  -->  "+c.getUri()+c.getSynonyms());
        }
    }
}
