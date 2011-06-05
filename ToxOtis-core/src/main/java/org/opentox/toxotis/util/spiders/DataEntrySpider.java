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
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import java.util.ArrayList;
import java.util.List;
import org.opentox.toxotis.core.component.Compound;
import org.opentox.toxotis.core.component.DataEntry;
import org.opentox.toxotis.core.component.FeatureValue;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 * Parser for a DataEntry individual in an RDF graph.
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class DataEntrySpider extends Tarantula<DataEntry>{

    public DataEntrySpider() {
    }

    public DataEntrySpider(Resource resource, OntModel model) {
        super(resource, model);
    }

    @Override
    public DataEntry parse() throws ServiceInvocationException  {
        DataEntry dataEntry = new DataEntry();
        StmtIterator conformerIt = model.listStatements(
                    new SimpleSelector(resource,
                    OTObjectProperties.compound().asObjectProperty(model),
                    (RDFNode)null));
            if(conformerIt.hasNext()){
                Statement compStmt = conformerIt.nextStatement();
                CompoundSpider conformerSpider = new CompoundSpider(model, compStmt.getObject().as(Resource.class).getURI());
                Compound conformer = conformerSpider.parse();
                dataEntry.setConformer(conformer);
            }
        conformerIt.close();
        StmtIterator valuesIt = model.listStatements(
                new SimpleSelector(resource,
                OTObjectProperties.values().asObjectProperty(model),
                (RDFNode)null));
        List<FeatureValue> featureValues = new ArrayList<FeatureValue>();
        while(valuesIt.hasNext()){
            Resource valueResource = valuesIt.nextStatement().getObject().as(Resource.class);
            FeatureValueSpider featureValueSpider = new FeatureValueSpider(valueResource, model);
            featureValues.add(featureValueSpider.parse());
        }
        valuesIt.close();
        dataEntry.setFeatureValues(featureValues);
        return dataEntry;
    }

}
