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
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 *
 * @param <T> The variable value type for internal storage of the value
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class VariableValue<T> extends OTComponent<VariableValue<T>> {

    public VariableValue() {
        super();
    }

    public VariableValue(String variableName, T value, Parameter.ParameterScope scope) {
        this();        
        setVariableInfo(new VariableInfo());
        getVariableInfo().getMeta().addTitle(variableName);
        getVariableInfo().setScope(scope);
        setValue(new LiteralValue<T>(value));
    }

    public VariableValue(String variableName, T value) {
        this();
        setVariableInfo(new VariableInfo());
        getVariableInfo().getMeta().addTitle(variableName);
        setValue(new LiteralValue<T>(value));
    }

    private LiteralValue<T> value;
    private VariableInfo variableInfo;

    public LiteralValue<T> getValue() {
        return value;
    }

    public void setValue(LiteralValue<T> value) {
        this.value = value;
    }

    public VariableInfo getVariableInfo() {
        return variableInfo;
    }

    public void setVariableInfo(VariableInfo variableInfo) {
        this.variableInfo = variableInfo;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        Individual indiv = model.createIndividual(getUri() != null ? getUri().toString() : null, OTClasses.VariableValue().inModel(model));
        if (getMeta() != null) {
            getMeta().attachTo(indiv, model);
        }
        if (value != null) {
            T v = value.getValue();
            if (v != null) {
                indiv.addLiteral(OTDatatypeProperties.value().asDatatypeProperty(model),
                        model.createTypedLiteral(v, value.getType()));
            }
        }
        if (variableInfo != null) {
            indiv.addProperty(OTObjectProperties.variable().asObjectProperty(model),
                    variableInfo.asIndividual(model));
        }
        return indiv;
    }

    @Override
    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
