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
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;
import java.util.HashSet;
import java.util.Set;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.ResourceValue;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;

/**
 * Parser of meta information over a given resource in an RDF graph.
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class MetaInfoSpider extends Tarantula<MetaInfo> {

    public MetaInfoSpider(OntModel model, String uri) {
        super();
        this.model = model;
        resource = model.getResource(uri);
    }

    public MetaInfoSpider(Resource resource, OntModel model) {
        super(resource, model);
    }

    @Override
    public MetaInfo parse() {
        MetaInfo dcmeta = new MetaInfoImpl();
        Set<LiteralValue> temp = null;
        Set<ResourceValue> temp2 = null;

        /* title */
        temp = retrievePropertyLiterals(DC.title);
        dcmeta.setTitles(temp);
        /* title */
        temp = retrievePropertyLiterals(DC.rights);
        dcmeta.setRights(temp);
        
        /* subject */
        temp = retrievePropertyLiterals(DC.subject);
        dcmeta.setSubjects(temp);
        /* creator */
        temp = retrievePropertyLiterals(DC.creator);
        dcmeta.setCreators(temp);
        /* description */
        temp = retrievePropertyLiterals(DC.description);
        dcmeta.setDescriptions(temp);
        /* publidher */
        temp = retrievePropertyLiterals(DC.publisher);
        dcmeta.setPublishers(temp);
        /* comment */
        temp = retrievePropertyLiterals(RDFS.comment);
        dcmeta.setComments(temp);
        /* contributors */
        temp = retrievePropertyLiterals(DC.contributor);
        dcmeta.setContributors(temp);
        /* identifier */
        temp = retrievePropertyLiterals(DC.identifier);
        dcmeta.setIdentifiers(temp != null ? temp : null);
        /* date */
        temp = retrievePropertyLiterals(DC.date);
        if (temp != null && !temp.isEmpty()) {// date declaration found!
            dcmeta.setDate(temp.iterator().next());
        }

        /* hasSource */
        temp2 = retrievePropertyNodes(org.opentox.toxotis.ontology.collection.OTObjectProperties.hasSource().asObjectProperty(model));
        dcmeta.setHasSources(temp2);
        /* sameAs */
        temp2 = retrievePropertyNodes(OWL.sameAs);
        dcmeta.setSameAs(temp2);
        /* see also... */
        temp2 = retrievePropertyNodes(RDFS.seeAlso);
        dcmeta.setSeeAlso(temp2);

        temp =retrievePropertyLiterals(model.createAnnotationProperty("http://purl.org/dc/elements/1.1/audience"));
        dcmeta.setAudiences(temp);

        if (dcmeta.isEmpty()){
            return null;
        }
        return dcmeta;
    }
}
