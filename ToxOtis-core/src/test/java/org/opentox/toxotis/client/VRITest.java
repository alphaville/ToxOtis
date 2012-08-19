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
package org.opentox.toxotis.client;

import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Algorithm;
import org.opentox.toxotis.core.component.Dataset;
import org.opentox.toxotis.core.component.Model;
import org.opentox.toxotis.core.component.qprf.QprfReport;
import org.opentox.toxotis.ontology.collection.OTClasses;
import static org.junit.Assert.*;

/**
 *
 * @author Pantelis Sopasakis
 */
public class VRITest {

    public VRITest() {
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
    public void testVriValidity() {
        VRI report2 = Services.anonymous().augment("xxx", "reach_report", "qprf", "1");
        assertEquals(Services.anonymous().augment("xxx"), report2.getServiceBaseUri());
        assertEquals("1", report2.getId());
        assertEquals(OTClasses.QPRFReport(), report2.getOntologicalClass());
        assertEquals(QprfReport.class, report2.getOpenToxType());
    }

    @Test
    public void testModelUri() {
        VRI model = Services.anonymous().augment("model", "x");
        assertEquals(Services.anonymous(), model.getServiceBaseUri());
        assertEquals(Services.anonymous().getHost(), model.getHost());
        assertEquals(Model.class, model.getOpenToxType());
        assertEquals("x", model.getId());
        assertEquals(OTClasses.Model(), model.getOntologicalClass());
    }

    @Test
    public void testModelUri2() {
        VRI model2 = Services.anonymous().augment("a", "model", "x");
        assertEquals(Services.anonymous().augment("a"), model2.getServiceBaseUri());
        assertEquals(Services.anonymous().getHost(), model2.getHost());
        assertEquals(Model.class, model2.getOpenToxType());
        assertEquals("x", model2.getId());
        assertEquals(OTClasses.Model(), model2.getOntologicalClass());
        assertEquals("http", model2.getProtocol());
    }

    @Test
    public void testSomeMethod() throws URISyntaxException {

        VRI report = new VRI(Services.anonymous() + "reach_report/qprf/1");
        assertEquals(Services.anonymous(), report.getServiceBaseUri());
        assertEquals(Services.anonymous().getHost(), report.getHost());
        assertEquals("1", report.getId());
        assertEquals(QprfReport.class, report.getOpenToxType());
        assertEquals(OTClasses.QPRFReport(), report.getOntologicalClass());
    }

    @Test
    public void testMore() {
        VRI dataset = Services.ambitUniPlovdiv().augment("dataset", "123").addUrlParameter("max", "10");
        Pair<String, String> pair = dataset.getUrlParams().iterator().next();
        assertEquals("max", pair.getKey());
        assertEquals("10", pair.getValue());
        assertEquals("max=10", dataset.getQueryAsString());
        assertEquals(Services.ambitUniPlovdiv().augment("dataset", "123").toString(), dataset.getStringNoQuery());
    }

    public void testVriOnceAgain() {
        VRI algo = Services.ideaconsult().augment("x", "y", "x", "x", "algorithm", "mlr");
        assertEquals("mlr", algo.getId());
        assertEquals(OTClasses.Algorithm(), algo.getOntologicalClass());
        assertEquals(Algorithm.class, algo.getOpenToxType());
        assertEquals("", algo.getQueryAsString());
        assertEquals("http", algo.getProtocol());
        assertEquals(8080, algo.getPort());
    }

    @Test
    public void testAugment() {
        VRI myuri = Services.ideaconsult().augment("dataset", 54);
        assertEquals("http://apps.ideaconsult.net:8080/ambit2/dataset/54", myuri.toString());
        myuri.addUrlParameter("max", 123);
        assertEquals("http://apps.ideaconsult.net:8080/ambit2/dataset/54?max=123", myuri.toString());
    }

    @Test
    public void testVriAsString() {
        VRI myuri = Services.ideaconsult().augment("services", "dataset", 54).
                addUrlParameter("a", 1).
                addUrlParameter("b", "5").
                addUrlParameter("b", "45");
        assertEquals("http://apps.ideaconsult.net:8080/ambit2/services/dataset/54?a=1&b=5&b=45", myuri.toString());
        assertEquals(myuri.getUri(), myuri.toURI().toString());
        myuri.removeUrlParameter("b");
        assertEquals("http://apps.ideaconsult.net:8080/ambit2/services/dataset/54?a=1", myuri.toString());
        myuri.addUrlParameter("a", " a b c @");
        assertEquals("a=1&a=+a+b+c+%40", myuri.getQueryAsString());
        assertEquals("apps.ideaconsult.net", myuri.getHost());
        assertEquals("54", myuri.getId());
        assertEquals(8080, myuri.getPort());
        assertEquals(myuri.getUri(), myuri.toURI().toString());
        assertEquals("http", myuri.getProtocol());
        assertEquals(Dataset.class, myuri.getOpenToxType());
        assertEquals(myuri.toString(), myuri.getUri());
        assertEquals("http://apps.ideaconsult.net:8080/ambit2/services/dataset/54", myuri.getStringNoQuery());
        assertEquals(Services.ideaconsult().augment("services", "dataset", 54).toString(), myuri.getStringNoQuery());
    }
}