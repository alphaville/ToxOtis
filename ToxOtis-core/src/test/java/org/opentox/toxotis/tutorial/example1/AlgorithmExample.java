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
package org.opentox.toxotis.tutorial.example1;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.OntModel;
import org.junit.Test;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Algorithm;
import org.opentox.toxotis.core.component.Parameter;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.WonderWebValidator;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;
import static org.opentox.toxotis.ontology.collection.OTAlgorithmTypes.*;
import static org.junit.Assert.*;

/**
 *
 * @author Pantelis Sopasakis
 */
public class AlgorithmExample {

    public AlgorithmExample() {
    }

    @Test
    public void test1() throws ToxOtisException, ServiceInvocationException {
        /*
         *  Define the algorithm
         *  Specify the URI
         */
        Algorithm algorithm = new Algorithm(Services.anonymous().augment("algorithm", 100));

        /*
         * Provide some ontological class specifications for this algorithm entity
         */
        algorithm.addOntologicalClasses(EagerLearning(), Regression());

        /*
         * Some meta-information regarding the algorithm
         */
        MetaInfo mi = new MetaInfoImpl();
        mi.addComment("Very good algorithm").addCreator("Pantelis Sopasakis").addIdentifier(algorithm.getUri().toString());
        algorithm.setMeta(mi);

        /*
         * Let the algorithm have one optional parameter
         */
        Parameter p = new Parameter();
        p.setName("k");
        p.setScope(Parameter.ParameterScope.OPTIONAL);
        p.setTypedValue(new LiteralValue(1.054, XSDDatatype.XSDfloat));
        algorithm.getParameters().add(p);

        /*
         * Print out as RDF
         */
        OntModel ontModel = algorithm.asOntModel();
        ontModel.write(System.out);


        WonderWebValidator validator = new WonderWebValidator(ontModel);
        boolean isValid = validator.validate(WonderWebValidator.OWL_SPECIFICATION.DL);
        assertTrue("This is not an OWL-DL document!",isValid);

    }
}
