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
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import java.util.HashSet;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTRestClasses;
import org.opentox.toxotis.ontology.collection.OTRestObjectProperties;

/**
 * Definition of the actions that the client should take, in term of parameters that
 * should be provided for Authentication and/or Authorization. Optional parameter that
 * specify the user's preferences regarding possible created policies are also listed
 * in the specifications in here.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 * @deprecated
 */
@Deprecated
public class AASpecifications extends OTComponent<AASpecifications> {

    private Set<HttpParameter> authParameters;

    public AASpecifications(VRI uri) {
        super(uri);
    }

    public AASpecifications() {
    }

    public Set<HttpParameter> getAuthParameters() {
        return authParameters;
    }

    public AASpecifications addSimpleHeaderParameter(String headerName, boolean optional, XSDDatatype xsdType) {
        if (getAuthParameters() == null) {
            setAuthParameters(new HashSet<HttpParameter>());
        }
        HttpParameter httpParam = new HttpParameter().setOntologicalClasses(new HashSet<OntologicalClass>()).
                addOntologicalClasses(OTRestClasses.inputParameterSimple(), OTRestClasses.header());
        httpParam.setOpentoxParameter(false);
        httpParam.setParamOptional(optional);
        getAuthParameters().add(httpParam);
        return this;
    }

    public AASpecifications setAuthParameters(Set<HttpParameter> authParameters) {
        this.authParameters = authParameters;
        return this;
    }

    public AASpecifications addAuthParameters(HttpParameter... authParameters) {
        if (getAuthParameters() == null) {
            setAuthParameters(new HashSet<HttpParameter>(authParameters.length));
        }
        for (HttpParameter p : authParameters) {
            getAuthParameters().add(p);
        }
        return this;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        String aaSpecUri = getUri() != null ? getUri().toString() : null;
        Individual indiv = model.createIndividual(aaSpecUri, OTRestClasses.aa().inModel(model));
        if (getOntologicalClasses() != null && !getOntologicalClasses().isEmpty()) {
            for (OntologicalClass oc : getOntologicalClasses()) {
                indiv.addRDFType(oc.inModel(model));
            }
        }
        if (getAuthParameters() != null && !getAuthParameters().isEmpty()) {
            Property aaProperty = OTRestObjectProperties.aa().asObjectProperty(model);
            for (HttpParameter param : getAuthParameters()) {
                indiv.addProperty(aaProperty, param.asIndividual(model));
            }
        }
        if (getMeta() != null) {
            getMeta().attachTo(indiv, model);
        }
        return indiv;
    }

    @Override
    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
