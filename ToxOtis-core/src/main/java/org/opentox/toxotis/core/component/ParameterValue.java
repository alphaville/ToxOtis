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
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ParameterValue extends OTComponent<ParameterValue> {  // ot:ParameterValue

    public ParameterValue() {
    }

    public ParameterValue(VariableValue... values) {
        this();
        if (values != null) {
            this.values.addAll(Arrays.asList(values));
        }
    }

    public ParameterValue(int index, VariableValue... values) {
        this(values);
        this.index = index;
    }
    private int index = -1;// optional index for the ot:ParameterValue node.
    private List<VariableValue> values = new ArrayList<VariableValue>();

    public List<VariableValue> getValues() {
        return values;
    }

    public void setValues(List<VariableValue> values) {
        this.values = values;
    }

    public int getIndex() {
        return index;
    }

    public ParameterValue setIndex(int index) {
        this.index = index;
        return this;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        Individual indiv = model.createIndividual(getUri() != null ? getUri().toString() : null, OTClasses.parameterValue().inModel(model));
        if (getMeta() != null) {
            getMeta().attachTo(indiv, model);
        }
        ObjectProperty multiParam = OTObjectProperties.variableValues().asObjectProperty(model);
        if (values != null) {
            for (VariableValue vv : values) {
                indiv.addProperty(multiParam, vv.asIndividual(model));
            }
        }
        if (index != -1) {
            indiv.addLiteral(OTDatatypeProperties.index().asDatatypeProperty(model), model.createTypedLiteral(index, XSDDatatype.XSDinteger));
        }
        return indiv;
    }

    @Override
    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
