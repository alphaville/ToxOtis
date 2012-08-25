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
package org.opentox.toxotis.training;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.IGetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.collection.OpenToxAlgorithms;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Algorithm;
import org.opentox.toxotis.core.component.Dataset;
import org.opentox.toxotis.core.component.Feature;
import org.opentox.toxotis.core.component.Model;
import org.opentox.toxotis.core.component.Parameter;
import org.opentox.toxotis.core.component.Task;
import static org.junit.Assert.*;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.util.TaskRunner;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.aa.TokenPool;
import org.opentox.toxotis.util.spiders.ModelSpider;

/**
 *
 * @author chung
 */
public class TrainerTest {

    public TrainerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        File passwordFile = new File(System.getProperty("user.home") + "/toxotisKeys/.my.key");
        TokenPool.getInstance().login(passwordFile);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        TokenPool.getInstance().logoutAll();
    }

    @Test(timeout = 4000)
    public void testTrainBadAlgorithm() throws ToxOtisException, IOException, ServiceInvocationException {
        Trainer t = new Trainer(null, null, null);
        try {
            t.setAlgorithm(null);
        } catch (NullPointerException npe) {
            return;
        }
        fail("Should have failed!");
    }

    @Test(timeout = 4000)
    public void testTrainNoDataset() throws ToxOtisException, IOException, ServiceInvocationException {
        Feature f = new Feature(Services.ideaconsult().augment("feature", "22200"));
        Algorithm algorithm = new Algorithm(Services.ntua().augment("algorithm", "mlr"));
        Trainer trainer = new Trainer(algorithm, null, f);
        try {
            trainer.train(null);
        } catch (ServiceInvocationException ex) {
            return;
        }
        fail("Should have failed!");
    }

    @Test(timeout = 30000)
    public void testTrain() throws ToxOtisException, IOException, ServiceInvocationException {
        AuthenticationToken at = TokenPool.getInstance().getToken("hampos");
        Dataset dataset = new Dataset(Services.ideaconsult().augment("dataset", "R545"));
        dataset.loadFromRemote(at);
        Feature f = new Feature(Services.ideaconsult().augment("feature", "22200"));
        Algorithm algorithm = new Algorithm(OpenToxAlgorithms.NTUA_MLR.getServiceVri());
        Trainer trainer = new Trainer(algorithm, dataset, f);
        assertEquals(algorithm, trainer.getAlgorithm());
        assertEquals(f, trainer.getPredictionFeature());
        assertEquals(dataset.getUri(), trainer.getDataset().getUri());
        Task task = trainer.train(at);
        task = task.loadFromRemote(at);
        assertNotNull(task);
        assertNotNull(task.getUri());
        assertEquals(OTClasses.task(), task.getUri().getOntologicalClass());
        assertEquals(202, task.getHttpStatus(), 1E-6);
        while (!Task.Status.COMPLETED.equals(task.getStatus())) {
            if (Task.Status.ERROR.equals(task.getStatus())) {
                fail("Task finished with error!");
            }
            task.loadFromRemote(at);
        }
        assertEquals(200f, task.getHttpStatus(), 1E-6);
        assertEquals(OTClasses.model(), task.getResultUri().getOntologicalClass());
    }

    @Test(timeout = 20000)
    public void testTrainWithParameters() throws ToxOtisException, IOException, ServiceInvocationException {
        AuthenticationToken at = TokenPool.getInstance().getToken("hampos");
        Dataset dataset = new Dataset(Services.ideaconsult().augment("dataset", "R545"));
        dataset.loadFromRemote(at);
        Feature f = new Feature(Services.ideaconsult().augment("feature", "22200"));
        Algorithm algorithm = new Algorithm(OpenToxAlgorithms.NTUA_SVM.getServiceVri());
        assertNotNull(algorithm.getParameters());
        algorithm.getParameters().add(new Parameter("gamma", new LiteralValue(0.875, XSDDatatype.XSDbyte)));
        Trainer trainer = new Trainer(algorithm, dataset, f);
        assertEquals(algorithm, trainer.getAlgorithm());
        assertEquals(f, trainer.getPredictionFeature());
        assertEquals(dataset.getUri(), trainer.getDataset().getUri());
        Task task = trainer.train(at);
        task = task.loadFromRemote(at);
        assertNotNull(task);
        assertNotNull(task.getUri());
        assertEquals(OTClasses.task(), task.getUri().getOntologicalClass());
        assertEquals(202, task.getHttpStatus(), 1E-6);
        TaskRunner runner = new TaskRunner(task);
        runner.setDelay(1000);
        task = runner.call();
        assertEquals(200f, task.getHttpStatus(), 1E-6);
        assertEquals(OTClasses.model(), task.getResultUri().getOntologicalClass());

        ModelSpider modelSpider = new ModelSpider(task.getResultUri(), at);
        Model model = modelSpider.parse();
        modelSpider.close();                

        assertNotNull(model);
        Set<Parameter> modelParameters = model.getParameters();
        assertNotNull(modelParameters);
        assertFalse(modelParameters.isEmpty());
        Iterator<Parameter> paramIterator = modelParameters.iterator();
        boolean failParamGammaNotFound = true;
        while (paramIterator.hasNext()) {
            Parameter currentParam = paramIterator.next();
            assertNotNull(currentParam);
            if (currentParam.getName()!=null && 
                    "gamma".equals(currentParam.getName().getValueAsString())) {
                failParamGammaNotFound = false;
                assertEquals(0.875d, Double.parseDouble(currentParam.getTypedValue().getValueAsString()), 1E-8);
            }
        }
        if (failParamGammaNotFound) {
            fail("Parameter missing from model!");
        }
        /*
         * Post some part of the training set to the model and get predictions...
         * 
         */
    }

    @Test
    public void trainUseLeverage() throws ServiceInvocationException, IOException, ToxOtisException {
        AuthenticationToken at = TokenPool.getInstance().getToken("hampos");
        VRI datasetUri = Services.ideaconsult().augment("dataset", 54).addUrlParameter("max", 10);
        System.out.println(datasetUri);
        Dataset ds = new Dataset();
        ds.setUri(datasetUri);
        ds.loadFromRemote(at);
        Set<VRI> featuresSet = ds.getContainedFeatureUris();
        assertNotNull(featuresSet);
        assertFalse(featuresSet.isEmpty());
        IGetClient getCli = null;
        try {
            getCli = ClientFactory.createGetClient(new VRI(datasetUri).removeUrlParameter("max").augment("feature"));
            getCli.setMediaType(Media.TEXT_URI_LIST);
            int httpStatus = getCli.getResponseCode();
            assertEquals(200, httpStatus);
            Set<VRI> responseFeaturesList = getCli.getResponseUriList();
            assertNotNull(responseFeaturesList);
            assertFalse(responseFeaturesList.isEmpty());
            assertEquals(responseFeaturesList, featuresSet);
        } catch (Exception ex) {
            fail(ex.getLocalizedMessage());
        } finally {
            getCli.close();
        }
    }
}
