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
import com.hp.hpl.jena.rdf.model.StmtIterator;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.IGetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.core.component.Feature;
import org.opentox.toxotis.core.component.Model;
import org.opentox.toxotis.core.component.Parameter;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.exceptions.impl.ConnectionException;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;

/**
 * Downloader and Parser for RDF representation of OpenTox Model resources.
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class ModelSpider extends Tarantula<Model> {

    private VRI uri;
    private AuthenticationToken token;

    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ModelSpider.class);

    public ModelSpider(VRI uri) throws ServiceInvocationException,ToxOtisException {
        this(uri, (AuthenticationToken) null);
    }

    public ModelSpider(VRI uri, AuthenticationToken token) throws ServiceInvocationException {
        super();
        this.uri = uri;
        this.token = token;
        IGetClient client = ClientFactory.createGetClient(uri);
        client.authorize(token);
        try {
            client.setMediaType(Media.APPLICATION_RDF_XML);
            int status = client.getResponseCode();
            assessHttpStatus(status, uri);
            setOntModel(client.getResponseOntModel());
            setResource(getOntModel().getResource(uri.toString()));
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException ex) {
                    throw new ConnectionException("StreamCouldNotClose: Error while trying to close the stream "
                            + "with the remote location at :'" + ((uri != null) ? uri.toString() : null) + "'", ex);
                }
            }
        }
    }

    public ModelSpider(Resource resource, OntModel model) {
        super(resource, model);
        try {
            uri = new VRI(resource.getURI());
        } catch (URISyntaxException ex) {
            logger.debug(null, ex);
        }
    }

    public ModelSpider(OntModel model, String uri) {
        super();
        setOntModel(model);
        try {
            this.uri = new VRI(uri);
        } catch (URISyntaxException ex) {
            logger.debug(null, ex);
        }
        setResource(model.getResource(uri));
    }

    @Override
    public Model parse() throws ServiceInvocationException {
        Model m = new Model();
        m.setUri(uri);
        m.setMeta(new MetaInfoSpider(getResource(), getOntModel()).parse());

        if (token != null) {
            try {
                User u = token.getUser();
                m.setCreatedBy(u);
            } catch (ToxOtisException ex) {
                logger.info(null, ex);
            }
        }

        StmtIterator itDataset = getOntModel().listStatements(
                new SimpleSelector(getResource(),
                OTObjectProperties.trainingDataset().asObjectProperty(getOntModel()),
                (RDFNode) null));
        if (itDataset.hasNext()) {
            try {
                m.setDataset(new VRI(itDataset.nextStatement().getObject().as(Resource.class).getURI()));
            } catch (URISyntaxException ex) {
                logger.debug(null, ex);
            }
        }

        StmtIterator itFeature = getOntModel().listStatements(
                new SimpleSelector(getResource(),
                OTObjectProperties.predictedVariables().asObjectProperty(getOntModel()),
                (RDFNode) null));

        while (itFeature.hasNext()) {
            FeatureSpider fspider = new FeatureSpider(getOntModel(),
                    itFeature.nextStatement().getObject().as(Resource.class).getURI());
            m.addPredictedFeatures(fspider.parse());
        }

        itFeature = getOntModel().listStatements(
                new SimpleSelector(getResource(),
                OTObjectProperties.dependentVariables().asObjectProperty(getOntModel()),
                (RDFNode) null));
        while (itFeature.hasNext()) {
            FeatureSpider fspider = new FeatureSpider(getOntModel(),
                    itFeature.nextStatement().getObject().as(Resource.class).getURI());
            m.addDependentFeatures(fspider.parse());
        }

        itFeature = getOntModel().listStatements(
                new SimpleSelector(getResource(),
                OTObjectProperties.independentVariables().asObjectProperty(getOntModel()),
                (RDFNode) null));
        List<Feature> indepFeatures = new ArrayList<Feature>();
        while (itFeature.hasNext()) {
            FeatureSpider fspider = new FeatureSpider(getOntModel(),
                    itFeature.nextStatement().getObject().as(Resource.class).getURI());
            indepFeatures.add(fspider.parse());
        }
        m.setIndependentFeatures(indepFeatures);

        StmtIterator itAlgorithm = getOntModel().listStatements(
                new SimpleSelector(getResource(),
                OTObjectProperties.algorithm().asObjectProperty(getOntModel()),
                (RDFNode) null));
        if (itAlgorithm.hasNext()) {
            AlgorithmSpider aspider;
            aspider = new AlgorithmSpider(itAlgorithm.nextStatement().getObject().as(Resource.class), getOntModel());
            m.setAlgorithm(aspider.parse());
        }

        StmtIterator itParam = getOntModel().listStatements(
                new SimpleSelector(getResource(),
                OTObjectProperties.parameters().asObjectProperty(getOntModel()),
                (RDFNode) null));

        Set<Parameter> parameters = new LinkedHashSet<Parameter>();
        while (itParam.hasNext()) {
            ParameterSpider paramSpider = new ParameterSpider(getOntModel(), 
                    itParam.nextStatement().getObject().as(Resource.class));
            parameters.add(paramSpider.parse());
        }
        m.setParameters(parameters);
        return m;
    }
}
