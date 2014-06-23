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

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTClasses;

/**
 * A minimal implementation of a component with just a URI and a set of ontological
 * classes and not much functionality therein.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DummyComponent extends OTComponent<DummyComponent>{

    /**
     * Dummy constructor for this component with no arguments. Calls the constructor
     * in the <code>super</code> class and initializes the ontological classes for it
     * with {@link OTClasses#openToxResource()  }
     *
     */
    public DummyComponent() {
        super();
        addOntologicalClasses(OTClasses.openToxResource());
    }

    public DummyComponent(VRI uri) {
        super(uri);
        addOntologicalClasses(OTClasses.openToxResource());
    }

    @Override
    public Individual asIndividual(OntModel om) {
        OntologicalClass ontologicalClass = null;
        if (getOntologicalClasses()!=null && !getOntologicalClasses().isEmpty()){
            ontologicalClass = getOntologicalClasses().iterator().next();
        }
        if (ontologicalClass == null){
            ontologicalClass = OTClasses.openToxResource();
        }
        String componentUri = getUri()!=null?getUri().toString():null;
        Individual indiv = om.createIndividual(componentUri, ontologicalClass.inModel(om));
        getMeta().attachTo(indiv, om);
        return indiv;
    }

    @Override
    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }




}