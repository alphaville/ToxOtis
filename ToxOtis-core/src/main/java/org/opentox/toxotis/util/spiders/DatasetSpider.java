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
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.IGetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.core.component.DataEntry;
import org.opentox.toxotis.core.component.Dataset;
import org.opentox.toxotis.exceptions.impl.ConnectionException;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 * A parser for RDF representations of OpenTox datasets.
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class DatasetSpider extends Tarantula<Dataset> {

    private VRI datasetUri;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DatasetSpider.class);

    public DatasetSpider(VRI uri) throws ServiceInvocationException {
        this(uri, null);
    }

    public DatasetSpider(VRI uri, AuthenticationToken token) throws ServiceInvocationException {
        super();
        long timeFlag = System.currentTimeMillis();
        this.datasetUri = uri;
        IGetClient client = ClientFactory.createGetClient(uri);
        client.setMediaType(Media.APPLICATION_RDF_XML);
        client.authorize(token); // << OpenTox API 1.2. 
        try {
            int status = client.getResponseCode();
            assessHttpStatus(status, uri);
            setOntModel(client.getResponseOntModel());
            setResource(getOntModel().getResource(uri.getStringNoQuery()));
            setReadRemoteTime(System.currentTimeMillis() - timeFlag);
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException ex) {
                    throw new ConnectionException("Error while trying to close the stream "
                            + "with the remote location at :'" + ((uri != null) ? uri.toString() : null) + "'", ex);
                }
            }
        }
    }

    public DatasetSpider(Resource resource, OntModel model) {
        super(resource, model);
        try {
            if (resource.getURI()!=null){
            datasetUri = new VRI(resource.getURI());
            }else{
                datasetUri = null;
            }
        } catch (URISyntaxException ex) {
            logger.debug(null, ex);
        } 
    }

    public DatasetSpider(OntModel model, String uri) {
        super();
        setOntModel(model);
        try {
            this.datasetUri = new VRI(uri);
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
        setResource(model.getResource(uri));
    }

    @Override
    public Dataset parse() throws ServiceInvocationException  {
        long timeFlag = System.currentTimeMillis();
        Dataset dataset = new Dataset();
        dataset.setUri(datasetUri);
        /**
         * START **
         * The following lines is a workaround will all RDF representations from 
         * remote servers are well formed. This will force the data model of a dataset
         * to have explicit declarations of ot:Feature, ot:NominalFeautre, ot:NumericFeature 
         * and ot:StringFeature
         */
        OTClasses.feature().inModel(getOntModel());
        OTClasses.nominalFeature().inModel(getOntModel());
        OTClasses.numericFeature().inModel(getOntModel());
        OTClasses.stringFeature().inModel(getOntModel());
        /** END **
         */
        dataset.setMeta(new MetaInfoSpider(getResource(), getOntModel()).parse());
        StmtIterator entryIt = getOntModel().listStatements(
                new SimpleSelector(getResource(), OTObjectProperties.dataEntry().asObjectProperty(getOntModel()),
                (RDFNode) null));
        ArrayList<DataEntry> dataEntries = new ArrayList<DataEntry>();
        while (entryIt.hasNext()) {
            Resource entryResource = entryIt.nextStatement().getObject().as(Resource.class);
            DataEntrySpider dataEntrySpider = new DataEntrySpider(entryResource, getOntModel());
            dataEntries.add(dataEntrySpider.parse());
        }
        dataset.setDataEntries(dataEntries);
        setParseTime(System.currentTimeMillis() - timeFlag);
        getOntModel().close();
        return dataset;
    }
}
