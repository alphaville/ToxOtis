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
package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import org.opentox.toxotis.core.component.FeatureValue;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import org.opentox.toxotis.core.component.Feature;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 * Parser for a FeatureValue resource in a given RDF graph.
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class FeatureValueSpider extends Tarantula<FeatureValue> {

    public FeatureValueSpider(Resource resource, OntModel model) {
        super(resource, model);
    }

    @Override
    public FeatureValue parse() throws ServiceInvocationException {
        Statement featureResourceStatement = resource.getProperty(OTObjectProperties.feature().asObjectProperty(model));
        if (featureResourceStatement == null) {
            throw new ServiceInvocationException("Error while parsing a feature value node: No features where assigned to the feature value node!");
        }
        Resource featureResource = featureResourceStatement.getResource();
        FeatureSpider fSpider = new FeatureSpider(featureResource, model);
        Feature feature = fSpider.parse();
        Statement valueStatement = resource.getProperty(OTDatatypeProperties.value().asDatatypeProperty(model));
        if (valueStatement == null) {
            throw new ServiceInvocationException("Error while parsing a feature value node: No value is assigned to the feature value node!");
        }
        Literal value = valueStatement.getObject().as(Literal.class);
        FeatureValue fValue;
        /*
         * TODO: Also handle dates!
         */
        if (value.getDatatype().equals(XSDDatatype.XSDdouble)) {
            fValue = new FeatureValue(feature, new LiteralValue<Double>(value.getDouble(), XSDDatatype.XSDdouble));
        } else if (value.getDatatype().equals(XSDDatatype.XSDinteger)) {
            fValue = new FeatureValue(feature, new LiteralValue<Integer>(value.getInt(), XSDDatatype.XSDinteger));
        } else if (value.getDatatype().equals(XSDDatatype.XSDfloat)) {
            fValue = new FeatureValue(feature, new LiteralValue<Float>(value.getFloat(), XSDDatatype.XSDfloat));
        } else {
            fValue = new FeatureValue(feature, new LiteralValue<String>(value.getString(), XSDDatatype.XSDstring));
        }

        return fValue;
    }
}
