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
package org.opentox.toxotis.factory;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Feature;
import org.opentox.toxotis.exceptions.IUnauthorized;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.ontology.ResourceValue;
import org.opentox.toxotis.ontology.collection.OTClasses;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class FeatureFactoryTest {

    public FeatureFactoryTest() {
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

    /*
     * Tests feature publication and parsing at the same time! Also checks if the remote
     * service works as expected!
     */
    //@Test
    public void testCreateFeature() throws ServiceInvocationException {
        String newFeatureTitle = "New feature - Just for testing";
        String units = "mA";
        Feature f = FeatureFactory.createAndPublishFeature(newFeatureTitle, units,
                new ResourceValue(Services.ntua().augment("model", "x"), OTClasses.Model()), Services.ideaconsult().augment("feature"), null);
        f.loadFromRemote();
        assertEquals(newFeatureTitle, f.getMeta().getTitles().iterator().next().getValueAsString());
        assertEquals(units, f.getUnits());
        System.out.println(f.getUri());
    }

    @Test
    public void testCreateFeatureOnBadFeatureService() throws ServiceInvocationException {
        String newFeatureTitle = "New feature - Just for testing";
        String units = "mA";
        try{
        Feature f = FeatureFactory.createAndPublishFeature(newFeatureTitle, units,
                new ResourceValue(Services.ntua().augment("model", "x"), OTClasses.Model()), Services.ntua().augment("bibtex"), null);
        } catch (ServiceInvocationException ex){            
            System.out.println(ex instanceof IUnauthorized);
            return;
        }
        fail("Should have failed!");
        
    }
}
