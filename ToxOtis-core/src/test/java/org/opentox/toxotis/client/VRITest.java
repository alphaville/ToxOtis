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
import java.util.List;
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
        assertEquals(OTClasses.qprfReport(), report2.getOntologicalClass());
        assertEquals(QprfReport.class, report2.getOpenToxType());
    }

    @Test
    public void testModelUri() {
        VRI model = Services.anonymous().augment("model", "x");
        assertEquals(Services.anonymous(), model.getServiceBaseUri());
        assertEquals(Services.anonymous().getHost(), model.getHost());
        assertEquals(Model.class, model.getOpenToxType());
        assertEquals("x", model.getId());
        assertEquals(OTClasses.model(), model.getOntologicalClass());
    }

    @Test
    public void testModelUri2() {
        VRI model2 = Services.anonymous().augment("a", "model", "x");
        assertEquals(Services.anonymous().augment("a"), model2.getServiceBaseUri());
        assertEquals(Services.anonymous().getHost(), model2.getHost());
        assertEquals(Model.class, model2.getOpenToxType());
        assertEquals("x", model2.getId());
        assertEquals(OTClasses.model(), model2.getOntologicalClass());
        assertEquals("http", model2.getProtocol());
    }

    @Test
    public void testSomeMethod() throws URISyntaxException {

        VRI report = new VRI(Services.anonymous() + "reach_report/qprf/1");
        assertEquals(Services.anonymous(), report.getServiceBaseUri());
        assertEquals(Services.anonymous().getHost(), report.getHost());
        assertEquals("1", report.getId());
        assertEquals(QprfReport.class, report.getOpenToxType());
        assertEquals(OTClasses.qprfReport(), report.getOntologicalClass());
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
        assertEquals(OTClasses.algorithm(), algo.getOntologicalClass());
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
    public void testParseVri() throws URISyntaxException {
        VRI myuri = new VRI("https://myserver.com/a/?b=5");
        assertEquals("b=5", myuri.getQueryAsString());
        myuri = new VRI("myserver.com/a/?b=5");
        assertEquals("b=5", myuri.getQueryAsString());
    }

    @Test
    public void testParseVri2() throws URISyntaxException {
        VRI myuri = new VRI("https://myserver.com/a/?a&b=5");
        assertEquals("a=&b=5", myuri.getQueryAsString());
        assertEquals(2, myuri.getUrlParams().size());
        List<Pair<String, String>> mylist = myuri.getUrlParams();
        assertEquals("a", mylist.get(0).getKey());
        assertTrue(mylist.get(0).getValue().isEmpty());
        assertEquals("b", mylist.get(1).getKey());
        assertEquals(IClient.httpSecureProtocol.toLowerCase(), myuri.getProtocol());
        myuri = new VRI("myserver.com/a/?a&b=5");
        assertEquals("a=&b=5", myuri.getQueryAsString());
        assertEquals(2, myuri.getUrlParams().size());
        mylist = myuri.getUrlParams();
        assertEquals("a", mylist.get(0).getKey());
    }

    @Test
    public void testVriStrange() throws URISyntaxException {
        VRI vri1 = new VRI("asdf");
        VRI vri2 = new VRI("http://asdf");
        VRI vri3 = new VRI("https://asdf");
        assertEquals(vri1, vri2);
        assertNotSame(vri3, vri1);
    }

    @Test
    public void testVriTraillingSlash() throws URISyntaxException {
        VRI vri1 = new VRI("http://server.com:8081/resource/1/");
        VRI vri2 = new VRI("http://server.com:8081/resource/1");
        assertEquals("http", vri1.getProtocol());
        assertEquals(8081, vri1.getPort());
        assertEquals(vri1, vri2);

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
    //TODO: Test cases where new VRI("myserver.com/blah");.. i.e. http:// is missing!

    @Test
    public void testId() {
        VRI vri = Services.anonymous().augment("compound", "12", "conformer", "12345").addUrlParameter("a", 100);
        assertEquals("12345", vri.getId());
        vri = Services.anonymous().augment("compound", "125").addUrlParameter("a", 100);
        vri = Services.anonymous().augment("compound", "abcd");
        assertEquals("abcd", vri.getId());
    }

    @Test
    public void testOntologicalClasses() {
        final String compoundId = "123", conformerId = "12345678";
        final String compound = "compound", conformer = "conformer";
        VRI vri = Services.anonymous().augment(compound, compoundId, conformer, conformerId);
        assertEquals(OTClasses.conformer(), vri.getOntologicalClass());
        assertEquals(conformerId, vri.getId());
        vri = Services.anonymous().augment("compound", compoundId, "conformer", conformerId).addUrlParameter("a", 1);
        assertEquals(OTClasses.conformer(), vri.getOntologicalClass());
        vri = Services.anonymous().augment("compound", compoundId, "conformer", conformerId).addUrlParameter("feature_uris[]", "xyz");
        assertEquals(conformerId, vri.getId());
        assertEquals(OTClasses.dataset(), vri.getOntologicalClass());
    }
}