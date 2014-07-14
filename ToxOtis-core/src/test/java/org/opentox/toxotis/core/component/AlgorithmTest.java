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

import com.hp.hpl.jena.ontology.OntModel;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.html.HTMLContainer;
import org.opentox.toxotis.core.html.HTMLPage;
import org.opentox.toxotis.core.html.impl.HTMLPageImpl;
import org.opentox.toxotis.exceptions.IBadRequest;
import org.opentox.toxotis.exceptions.IClientException;
import org.opentox.toxotis.exceptions.IConnectionException;
import org.opentox.toxotis.exceptions.INotFound;
import static org.junit.Assert.*;
import org.opentox.toxotis.exceptions.impl.BadRequestException;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.RDFValidator;
import org.opentox.toxotis.ontology.ResourceValue;
import org.opentox.toxotis.ontology.collection.OTAlgorithmTypes;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;
import org.opentox.toxotis.util.spiders.AlgorithmSpider;

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

    @Test
    public void testDownload_and_parse_algorithm() throws ToxOtisException, ServiceInvocationException {
        new Algorithm(Services.NtuaAlgorithms.mlr()).loadFromRemote();
        new Algorithm(Services.NtuaAlgorithms.filter()).loadFromRemote();
        new Algorithm(Services.NtuaAlgorithms.svm()).loadFromRemote();
        new Algorithm(Services.NtuaAlgorithms.leverage()).loadFromRemote();
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
            OntModel ontModel = ex.asErrorReport().asOntModel();
            RDFValidator rdfValidator = new RDFValidator(ontModel);
            rdfValidator.validateDL();
            assertTrue("Not OWL-DL-valid error report", rdfValidator.isValid());
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
    @Test
    public void testDownload_badRDF() throws ToxOtisException, ServiceInvocationException, URISyntaxException {
        System.out.println("Testing loading algorithm from remote location that does not "
                + "provide RDF!");
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

    @Test
    public void testAlgorithmRdf() throws ToxOtisException, BadRequestException, ServiceInvocationException {
        Algorithm svm = new Algorithm();
        svm = new Algorithm(Services.anonymous().augment("algorithm", "svm"));
        MetaInfo algorithmMeta = new MetaInfoImpl().addTitle("svm", "Support Vector Machine Training Algorithm").
                addComment("Support Vector Machine Algorithm").
                addComment("For example cURL commands for this algorithm check out http://cut.gd/P6fa").
                addSubject("Regression", "Linear", "Training", "Multiple Linear Regression", "Machine Learning", "Single Target", "Eager Learning", "Weka").
                addContributor("Pantelis Sopasakis", "Charalampos Chomenides").
                addDescription("Algorithm for training regression models using the Support Vector Machine Learning Algorithm. "
                + "The training is based on the Weka implementation of SVM and specifically the class weka.classifiers.functions.SVMreg. "
                + "A comprehensive introductory text is provided by John Shawe-Taylor and Nello Cristianin in the book 'Support Vector Machines' "
                + "Cambridge University Press, 2000").
                addPublisher(Services.anonymous().getUri()).setDate(new LiteralValue<Date>(
                new Date(System.currentTimeMillis()))).
                addIdentifier(svm.getUri().toString()).
                addSeeAlso(new ResourceValue(Services.anonymous().augment("algorithm", "mlr"), OTClasses.algorithm()));
        svm.setParameters(new HashSet<Parameter>());

        Parameter kernel =
                new Parameter(
                Services.anonymous().augment("prm", "svm_kernel"), "kernel", new LiteralValue<String>("RBF")).setScope(
                Parameter.ParameterScope.OPTIONAL);
        kernel.getMeta().addDescription("Kernel of the Support Vector Machine. Available kernels include 'rbf', 'linear' and 'polynomial'.").
                addIdentifier(kernel.getUri().toString());
        svm.getParameters().add(kernel);

        Parameter gamma = new Parameter(Services.anonymous().augment("prm", "svm_gamma"));
        gamma.setName("gamma").setScope(Parameter.ParameterScope.OPTIONAL);
        gamma.setTypedValue(new LiteralValue<Double>(1.5d));
        gamma.getMeta().
                addDescription("Gamma Parameter for the SVM kernel").
                addComment("Only strictly positive values are acceptable").
                addIdentifier(gamma.getUri().toString());
        svm.getParameters().add(gamma);

        Parameter cost = new Parameter(Services.anonymous().augment("prm", "svm_cost"));
        cost.setName("cost").setScope(Parameter.ParameterScope.OPTIONAL);
        cost.setTypedValue(new LiteralValue<Double>(100.0d));
        cost.getMeta().addComment("Only strictly positive values are acceptable").
                addIdentifier(cost.getUri().toString());
        svm.getParameters().add(cost);

        Parameter epsilon = new Parameter(Services.anonymous().augment("prm", "svm_epsilon"));
        epsilon.setName("epsilon").setScope(Parameter.ParameterScope.OPTIONAL);
        epsilon.setTypedValue(new LiteralValue<Double>(0.1d));
        epsilon.getMeta().addComment("Only strictly positive values are acceptable").
                addIdentifier(epsilon.getUri().toString());
        svm.getParameters().add(epsilon);

        Parameter tolerance = new Parameter(Services.anonymous().augment("prm", "svm_tolerance"));
        tolerance.setName("tolerance").setScope(Parameter.ParameterScope.OPTIONAL);
        tolerance.setTypedValue(new LiteralValue<Double>(0.0001d));
        tolerance.getMeta().addComment("Only strictly positive values are acceptable and we advise users to use values that "
                + "do not exceed 0.10").
                addIdentifier(tolerance.getUri().toString());
        svm.getParameters().add(tolerance);

        Parameter degree = new Parameter(Services.anonymous().augment("prm", "svm_degree"));
        degree.setName("degree").setScope(Parameter.ParameterScope.OPTIONAL);
        degree.setTypedValue(new LiteralValue<Integer>(3));
        degree.getMeta().addDescription("Degree of polynomial kernel").
                addComment("To be used in combination with the polynomial kernel").
                addIdentifier(degree.getUri().toString());
        svm.getParameters().add(degree);


        svm.setMeta(algorithmMeta);
        svm.setOntologies(new HashSet<OntologicalClass>());
        svm.getOntologies().add(OTAlgorithmTypes.regression());
        svm.getOntologies().add(OTAlgorithmTypes.singleTarget());
        svm.getOntologies().add(OTAlgorithmTypes.eagerLearning());
        svm.getMeta().addRights("GPL v3");
        svm.setEnabled(true);

        OntModel om = svm.asOntModel();

        AlgorithmSpider as = new AlgorithmSpider(om.getResource(svm.getUri().toString()), om);
        Algorithm parsed = as.parse();
        assertNotNull(parsed);
        assertNotNull(parsed.getUri());
        assertNotNull(parsed.getMeta());
        assertNotNull(parsed.getOntologies());
        assertEquals(svm.getUri(), parsed.getUri());
        assertEquals(svm.getParameters(), parsed.getParameters());
        assertFalse(parsed.getMeta().isEmpty());
        assertFalse(parsed.getMeta().getComments().isEmpty());
        assertTrue(parsed.getMeta().getComments().containsAll(svm.getMeta().getComments()));
        assertTrue(parsed.getOntologies().containsAll(svm.getOntologies()));

        HTMLContainer container = svm.inHtml();
        assertTrue(container.toString().contains("<p align=\"left\"><small>Other formats:<a href"));
        assertTrue(container.toString().contains("<a href=\"http://anonymous.org/algorithm/svm?accept=application/x-turtle\">Turtle</a>"));
    }
}
