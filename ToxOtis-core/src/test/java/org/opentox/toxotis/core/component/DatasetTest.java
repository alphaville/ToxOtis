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

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.WonderWebValidator;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.util.TaskRunner;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.aa.TokenPool;
import weka.core.Instances;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class DatasetTest {

    private static final Random RNG = new Random(System.currentTimeMillis());

    public DatasetTest() {
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

    @Test
    public void testRandomDsPublish() throws ToxOtisException, ServiceInvocationException {
        Dataset ds = new Dataset(Services.anonymous().augment("dataset", RNG.nextLong()));
        List<DataEntry> dataEntries = new ArrayList<DataEntry>();
        int nFeatures = 2;
        int nCompounds = 5;
        //Create features
        List<Feature> features = new LinkedList<Feature>();
        for (int i = 0; i < nFeatures; i++) {
            Feature currentFeature = new Feature(Services.anonymous().augment("feature", RNG.nextLong()));
            currentFeature.getOntologicalClasses().add(OTClasses.NumericFeature());
            features.add(currentFeature);
        }
        for (int i = 0; i < nCompounds; i++) {
            DataEntry randomDataEntry = new DataEntry();
            randomDataEntry.setConformer(new Compound(Services.anonymous().augment("compound", RNG.nextLong())));
            for (int j = 0; j < nFeatures; j++) {
                randomDataEntry.addFeatureValue(new FeatureValue(features.get(j),
                        new LiteralValue<Double>(RNG.nextDouble(), XSDDatatype.XSDdouble)));
            }
            dataEntries.add(randomDataEntry);
        }
        ds.setDataEntries(dataEntries);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ds.writeRdf(baos);
        assertNotNull("No RDF produced", baos.toString());
        WonderWebValidator validator = new WonderWebValidator(ds.asOntModel());
        boolean isValidDL = validator.validate(WonderWebValidator.OWL_SPECIFICATION.DL);
        assertTrue("Dataset is not DL-valid", isValidDL);
        AuthenticationToken at = TokenPool.getInstance().getToken("hampos");
        Task uploadTask = ds.publishOnline(at);
        assertNotNull("No Task Created", uploadTask.getUri());
        TaskRunner taskRunner = new TaskRunner(uploadTask, at);
        uploadTask = taskRunner.call();
        assertNotNull("No Task", uploadTask.getUri());
        assertNotNull("No result returned", uploadTask.getResultUri());
        System.out.println("Uploaded dataset at : "+uploadTask.getResultUri());
        Dataset retrievedDataset = new Dataset(uploadTask.getResultUri());
        retrievedDataset.loadFromRemote(at);
        Instances retrievedInstances = retrievedDataset.getInstances();
        assertNotNull(retrievedInstances);
        assertEquals(nCompounds, retrievedInstances.numInstances());
        //Note: Here it is nFeatures + 1 because the first feature is the compound URI...
        assertEquals(nFeatures + 1, retrievedInstances.numAttributes());

    }
}
