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
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.IOTComponent;
import org.opentox.toxotis.core.IRestOperation;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTRestClasses;
import org.opentox.toxotis.ontology.collection.OTRestObjectProperties;

/**
 * Provides a machine-readable documentation for the consumption of all available
 * services provided including information about different HTTP methods, possible
 * status codes, input parameters and more. This can be used to generate human-readable
 * guidelines for the invocation of the web service.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ServiceRestDocumentation extends OTComponent<ServiceRestDocumentation> {

    private final IOTComponent component;
    private Set<IRestOperation> restOperations;

    /**
     * A <code>ServiceRestDocumentation</code> object should be initialized with
     * an OpenTox component.
     * @param component
     *      An OpenTox component or an instance of {@link DummyComponent }
     */
    public ServiceRestDocumentation(IOTComponent component) {
        super();
        if (component == null) {
            throw new NullPointerException("Null component is not allowed in this constructor");
        }
        if (component.getUri() == null) {
            throw new IllegalArgumentException("Component with null URI was provided");
        }
        this.component = component;
    }

    private ServiceRestDocumentation(VRI uri) {
        throw new AssertionError("Should not be invoked!");
    }

    private ServiceRestDocumentation() {
        throw new AssertionError("Should not be invoked!");
    }

    public Set<IRestOperation> getRestOperations() {
        return restOperations;
    }

    public ServiceRestDocumentation setRestOperations(Set<IRestOperation> restOperations) {
        this.restOperations = restOperations;
        return this;
    }

    public ServiceRestDocumentation addRestOperations(IRestOperation... restOperations) {
        if (getRestOperations() == null) {
            setRestOperations(new HashSet<IRestOperation>());
        }
        getRestOperations().addAll(Arrays.asList(restOperations));
        return this;
    }

    public IOTComponent getComponent() {
        return component;
    }

    @Override
    public MetaInfo getMeta() {
        return getComponent().getMeta();
    }

    @Override
    public VRI getUri() {
        return getComponent().getUri();
    }

    @Override
    public Set<OntologicalClass> getOntologicalClasses() {
        Set<OntologicalClass> classes = component.getOntologicalClasses();
        classes.add(OTRestClasses.restOperation());
        return classes;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        OntologicalClass classForCoreComponent = null;
        if (getOntologicalClasses() != null && !getOntologicalClasses().isEmpty()) {
            classForCoreComponent = getOntologicalClasses().iterator().next();
        }
        if (classForCoreComponent==null){
            throw new NullPointerException("Null ontological class included in the "
                    + "collection of ont. classes!");
        }
        Individual indiv = model.createIndividual(getUri().toString(), 
                classForCoreComponent.inModel(model));
        if (getOntologicalClasses() != null && !getOntologicalClasses().isEmpty()) {
            for (OntologicalClass oc : getOntologicalClasses()) {
                indiv.addRDFType(oc.inModel(model));
            }
        }
        if (getMeta() != null) {
            getMeta().attachTo(indiv, model);
        }
        if (getRestOperations() != null && !getRestOperations().isEmpty()) {
            ObjectProperty hasRESTOperationProperty = OTRestObjectProperties.hasRESTOperation().asObjectProperty(model);
            ObjectProperty resourceProperty = OTRestObjectProperties.resource().asObjectProperty(model);
            for (IRestOperation ro : getRestOperations()) {
                Resource restOperationResource = ro.asIndividual(model);
                indiv.addProperty(hasRESTOperationProperty, restOperationResource);
                restOperationResource.addProperty(resourceProperty, indiv);

            }
        }
        return indiv;
    }

    @Override
    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
