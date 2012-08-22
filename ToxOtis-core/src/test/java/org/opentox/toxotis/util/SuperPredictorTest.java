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
package org.opentox.toxotis.util;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 *
 * @author chung
 */
public class SuperPredictorTest {

    public SuperPredictorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of getSuperServiceUri method, of class SuperPredictor.
     */
    @Test
    public void testGetSuperServiceUri() {
    }

    /**
     * Test of setSuperServiceUri method, of class SuperPredictor.
     */
    @Test
    public void testSetSuperServiceUri() {
    }

    /**
     * Test of predict method, of class SuperPredictor.
     */
    @Test
    public void testPredict() throws Exception {
        AuthenticationToken at = new AuthenticationToken("hampos", "arabela");
        VRI modelVri = new VRI("http://opentox.ntua.gr:8080/model/3094c2dc-7c86-474b-87a6-037ec5065221");
        VRI compoundVri = new VRI("http://apps.ideaconsult.net:8080/ambit2/compound/100");
        SuperPredictor predictor = new SuperPredictor(compoundVri, modelVri, at);
        predictor.prediction();
    }
}
