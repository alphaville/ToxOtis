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

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import java.util.Set;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Parameter;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;

/**
 * Parser for Parameter resources in a given RDF graph.
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class ParameterSpider extends Tarantula<Parameter> {
    
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ParameterSpider.class);

    /**
     * ParameterSpider constructor with an ontological resource and an ontological model.
     * 
     * @param resource
     *      Ontological resource to be parsed.
     * @param model
     *      Ontological model to be used for parsing.
     */
    public ParameterSpider(OntModel model, Resource resource) {
        super(resource, model);
    }

    @Override
    public Parameter parse() {
        Parameter parameter = new Parameter();        

        /*
         * Parse parameter scope...
         */
        Set<LiteralValue> scopes = retrievePropertyLiterals(OTDatatypeProperties.paramScope().
                asDatatypeProperty(getOntModel()));
        if (scopes != null && !scopes.isEmpty()) {
            if (scopes.size() > 1) {
                logger.warn("[WARN ] Multiple scopes declared for the parsed parameter! "
                        + "Only one will be taken into account.");
            }
            LiteralValue scope = scopes.iterator().next();
            parameter.setScope(Parameter.ParameterScope.valueOf(scope.getValue().toString().toUpperCase()));
        }

        Set<LiteralValue> paramTypedValues = retrievePropertyLiterals(
                OTDatatypeProperties.paramValue().asDatatypeProperty(getOntModel()));
        if (paramTypedValues != null && !paramTypedValues.isEmpty()) {
            if (paramTypedValues.size() > 1) {
                logger.warn("[WARN ] Multiple parameter values are declared for a single parameter. "
                        + "Only one of them will be taken into account!");
            }
            LiteralValue paramTypedValue = paramTypedValues.iterator().next();
            parameter.setTypedValue(paramTypedValue);
        }

        MetaInfoSpider metaSpider = new MetaInfoSpider(getResource(), getOntModel());
        MetaInfo mi = metaSpider.parse();
        parameter.setMeta(mi);

        // Note: The name of the parameter is defined using the dc:title property
        
        // Set the parameter's URI
        parameter.setUri(Services.anonymous().augment("param", parameter.hashCode()));

        return parameter;
    }
}
