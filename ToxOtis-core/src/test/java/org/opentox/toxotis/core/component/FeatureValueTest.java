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
import com.hp.hpl.jena.ontology.OntModel;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.WonderWebValidator;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.util.ROG;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class FeatureValueTest {

    private static final ROG ROG = new ROG();

    public FeatureValueTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testFVUri() {
        FeatureValue fv = new FeatureValue();
        VRI uri1 = fv.getUri();
        assertNotNull(uri1);
        fv.setFeature(new Feature(Services.anonymous().augment("feature", 123456)));
        VRI uri2 = fv.getUri();
        assertNotNull(uri2);
        assertNotSame(uri1, uri2);
    }

    @Test
    public void testFVOwlDl() throws ServiceInvocationException {
        FeatureValue fv = new FeatureValue();
        fv.setFeature(new Feature(Services.anonymous().augment("feature", ROG.nextInt(1000))));
        fv.setUri(ROG.nextVri());
        fv.getOntologicalClasses().add(OTClasses.featureValueNumeric());
        fv.setValue(new LiteralValue(ROG.nextDouble(), XSDDatatype.XSDdouble));
        OntModel om = fv.asOntModel();
        WonderWebValidator validator = new WonderWebValidator(om);
        boolean isFVValid = validator.validate(WonderWebValidator.OWL_SPECIFICATION.DL);
        assertTrue(isFVValid);
    }
}
