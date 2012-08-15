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

import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.html.HTMLPage;
import org.opentox.toxotis.core.html.impl.HTMLPageImpl;
import org.opentox.toxotis.exceptions.IBadRequest;
import org.opentox.toxotis.exceptions.IClientException;
import org.opentox.toxotis.exceptions.IConnectionException;
import org.opentox.toxotis.exceptions.INotFound;
import static org.junit.Assert.*;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.ontology.WonderWebValidator;

/**
 *
 * @author chung
 */
public class AlgorithmTest {

    public AlgorithmTest() {
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

    //@Test
    public void testDownload_and_parse_algorithm() throws ToxOtisException, ServiceInvocationException {
        new Algorithm(Services.tumDev().augment("algorithm", "kNNclassification")).loadFromRemote();
        new Algorithm(Services.tumDev().augment("algorithm", "J48")).loadFromRemote();
        new Algorithm(Services.tumDev().augment("algorithm", "kNNregression")).loadFromRemote();
        new Algorithm(Services.tumDev().augment("algorithm", "M5P")).loadFromRemote();
        new Algorithm(Services.tumDev().augment("algorithm", "GaussP")).loadFromRemote();
        new Algorithm(Services.tumDev().augment("algorithm", "LR")).loadFromRemote();
        new Algorithm(Services.tumDev().augment("algorithm", "BayesNet")).loadFromRemote();
        new Algorithm(Services.tumDev().augment("algorithm", "FTM")).loadFromRemote();
        new Algorithm(Services.tumDev().augment("algorithm", "CDKPhysChem")).loadFromRemote();
        new Algorithm(Services.tumDev().augment("algorithm", "ChiSquared")).loadFromRemote();
        new Algorithm(Services.tumDev().augment("algorithm", "PrincipalComponents")).loadFromRemote();
        new Algorithm(Services.tumDev().augment("algorithm", "InfoGainAttributeEval")).loadFromRemote();
        new Algorithm(Services.tumDev().augment("algorithm", "gSpan")).loadFromRemote();

    }

    @Test
    public void testHtml() throws ToxOtisException, ServiceInvocationException, URISyntaxException {
        Algorithm a = new Algorithm("http://opentox.ntua.gr:8080/algorithm/mlr");
        a.loadFromRemote();
        HTMLPage document = new HTMLPageImpl();
        document.getHtmlBody().addComponent(a.inHtml());
        assertNotNull(document.toString());
    }

    @Test
    public void testDownloadNotFoundAlgorithm() throws ToxOtisException, ServiceInvocationException {
        try {
            new Algorithm(Services.ntua().augment("algorithm", "notFoundAlgorithm")).loadFromRemote();
        } catch (ServiceInvocationException ex) {
            assertTrue(ex instanceof INotFound);
            assertTrue(ex instanceof IClientException);
            boolean isOWLDLvalid = new WonderWebValidator(ex.asErrorReport().asOntModel()).validate(WonderWebValidator.OWL_SPECIFICATION.DL);
            assertTrue("Not OWL-DL-valid error report", isOWLDLvalid);
        }
    }

    @Test
    public void testDownload_resourceNotAvailable() throws ServiceInvocationException, ToxOtisException, URISyntaxException {
        try {
            new Algorithm(new VRI("http://asdf.wqret.fd:8765").augment("algorithm", "xyz")).loadFromRemote();
        } catch (ServiceInvocationException ex) {
            assertTrue(ex instanceof IConnectionException);
        }
    }

    /*
     * WARNING: This test takes around 3 minutes to complete!!!
     */
    //@Test
    public void testDownload_badRDF() throws ToxOtisException, ServiceInvocationException, URISyntaxException {
        System.out.println("Testing loading algorithm from remote location that does not " +
                "provide RDF!");
        System.out.println("This test is excpected to take a long time (~3mins)");
        try {
             new Algorithm(new VRI("http://localhost:4000").augment("algorithm", "mlr").
                    addUrlParameter("accept", "text/html")).loadFromRemote();
            /*
             * Note: OK.. it might find some bloody RDF and parse it. Check at least if an instance
             * of 'algorithm' is found! (Might that be the case).
             */
        } catch (ServiceInvocationException ex) {
            ex.printStackTrace();
            System.out.println(ex);
            return;
        }
        fail("Should have failed");
    }
}
