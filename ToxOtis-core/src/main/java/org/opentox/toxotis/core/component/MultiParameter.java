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
import java.util.ArrayList;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.core.component.Parameter.ParameterScope;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class MultiParameter extends OTComponent<MultiParameter> {

    /** ParameterValue values to the multi-valued multi-parameter*/
    private ArrayList<ParameterValue> parameterValues = new ArrayList<ParameterValue>();
    private Parameter.ParameterScope scope;

    public ArrayList<ParameterValue> getParameterValues() {
        return parameterValues;
    }

    public void setParameterValues(ArrayList<ParameterValue> setValues) {
        this.parameterValues = setValues;
    }

    public ParameterScope getScope() {
        return scope;
    }

    public void setScope(ParameterScope scope) {
        this.scope = scope;
    }

    @Override
    public Individual asIndividual(OntModel model) {

        Individual indiv = model.createIndividual(getUri() != null ? getUri().toString() : null, OTClasses.SetValuedParameter().inModel(model));
        if (parameterValues.size() > 1) {
            indiv.addRDFType(OTClasses.VectorParameter().inModel(model));
            for (ParameterValue setValue : parameterValues) {
                Individual valueIndiv = setValue.asIndividual(model);
                indiv.addProperty(OTObjectProperties.parameterValues().asObjectProperty(model), valueIndiv);
            }
        } else if (parameterValues.size() == 1) {
            ParameterValue setValue = parameterValues.get(0);
            Individual valueIndiv = setValue.asIndividual(model);
            indiv.addProperty(OTObjectProperties.parameterValues().asObjectProperty(model), valueIndiv);
        }
        if (getMeta() != null) {
            getMeta().attachTo(indiv, model);
        }
        if (scope != null) {
            indiv.addLiteral(OTDatatypeProperties.paramScope().asDatatypeProperty(model),
                    model.createTypedLiteral(scope.toString(), XSDDatatype.XSDstring));
        }
        return indiv;
    }

    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
