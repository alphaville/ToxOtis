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

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.collection.Services;
import static org.junit.Assert.*;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;
import org.opentox.toxotis.util.ROG;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 *
 * @author chung
 */
public class ModelTest {

    public ModelTest() {
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
    public void testModelAsIndividual() {
        ROG rog = new ROG();
        Model m = rog.nextModel();
        m.addBibTeXReferences(Services.anonymous().augment("bibtex", 1));
        OntModel m_ontModel = m.asOntModel();
        m_ontModel.write(System.out, "N3");
        assertNotNull(m_ontModel);
        ObjectProperty op_bibtex = m_ontModel.getObjectProperty(
                OTObjectProperties.bibTex().getUri());
        assertNotNull(op_bibtex);
        //TODO Test more
    }

    //@Test
    public void testLoadModel() throws ServiceInvocationException {
        AuthenticationToken token = new AuthenticationToken("guest", "guest");
        Model m = new Model(Services.ntua().augment("algorithm", "mlr")).loadFromRemote(token);
        assertNotNull(m.getMeta());
        assertNotNull(m.getMeta().getRights());
    }

}
