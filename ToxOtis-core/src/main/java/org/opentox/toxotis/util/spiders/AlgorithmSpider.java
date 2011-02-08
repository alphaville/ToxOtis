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
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.IGetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Algorithm;
import org.opentox.toxotis.core.component.ErrorReport;
import org.opentox.toxotis.exceptions.impl.BadRequestException;
import org.opentox.toxotis.exceptions.impl.ConnectionException;
import org.opentox.toxotis.exceptions.impl.ForbiddenRequest;
import org.opentox.toxotis.exceptions.impl.NotFound;
import org.opentox.toxotis.exceptions.impl.RemoteServiceException;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.Unauthorized;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 * A downloader parser for data models of OpenTox algorithms.
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class AlgorithmSpider extends Tarantula<Algorithm> {

    /**
     * URI of the algorithm to be downloaded and parsed stored in
     * a private field.
     */
    private VRI uri;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AlgorithmSpider.class);

    /**
     * Create a new AlgorithmSpider providing the URI of the algorithm to be
     * <em>spidered</em>, i.e. downloded from the remote location the URI specifies
     * (as an RDF/XML document) and then parsed into an {@link org.opentox.toxotis.core.component.Algorithm Algorithm}
     * object. Be aware that the invokation of this method assumes that no authentication or
     * authorization mechanism exists on the remote server to control access to the
     * resource and it can be freely accessed.
     *
     * @param uri
     *      The URI of the algorithm to be downloaded and parsed into an {@link
     *      Algorithm } object. You can pick an algorithm URI from the list inside
     *      {@link Services }; for example {@link
     *      org.opentox.toxotis.client.collection.Services.NtuaAlgorithms#mlr() NTUA MLR}
     *      from the {@link Services#ntua() NTUA SERVICES} server.
     *
     * @throws ToxOtisException
     *      In case some exceptional event occurs during the server-client communication,
     *      the connection is not possible (e.g. the remote server is down), or the
     *      response status is 403 (Authentication Failed), 401 (The user is not authorized),
     *      404 (Algorithm not found on the server), 500 (Some internal server error
     *      occured) or other exceptional status code.
     */
    public AlgorithmSpider(VRI uri) throws ServiceInvocationException {
        this(uri, null);
    }

    public AlgorithmSpider(Resource resource, OntModel model) throws BadRequestException {
        super(resource, model);
        try {
            uri = new VRI(resource.getURI());
            if (!Algorithm.class.equals(uri.getOpenToxType())) {
                throw new BadRequestException("Bad URI : Not an algorithm URI (" + uri + ")");
            }
        } catch (URISyntaxException ex) {
            logger.debug(null, ex);
        }
    }

    /**
     * Create a new AlgorithmSpider providing the URI of the algorithm to be
     * <em>spidered</em> and an authentication token that will allow the client
     * to access the content of the algorithm.
     * @param uri
     *      The URI of the algorithm to be downloaded and parsed into an {@link
     *      Algorithm } object. You can pick an algorithm URI from the list inside
     *      {@link Services }; for example {@link
     *      org.opentox.toxotis.client.collection.Services.NtuaAlgorithms#mlr() NTUA MLR}
     *      from the {@link org.opentox.toxotis.client.collection.Services.NtuaAlgorithms
     *      NTUA SERVICES} server.
     * @param token
     *      An authentication token that will grant the client access to the resource.
     * @throws ToxOtisException
     *      In case some exceptional event occurs during the server-client communication,
     *      the connection is not possible (e.g. the remote server is down), or the
     *      response status is 403 (Authentication Failed), 401 (The user is not authorized),
     *      404 (Algorithm not found on the server), 500 (Some internal server error
     *      occured) or other exceptional status code.
     */
    public AlgorithmSpider(VRI uri, AuthenticationToken token) throws ServiceInvocationException {
        super();
        this.uri = uri;
        IGetClient client = ClientFactory.createGetClient(uri);
        client.authorize(token);
        client.setMediaType(Media.APPLICATION_RDF_XML);
        try {
            /*
             * Handle excpetional events caused during the server-client communiation.
             */
            int status = client.getResponseCode();
            if (status != 200) {
                OntModel om = client.getResponseOntModel();
                ErrorReportSpider ersp = new ErrorReportSpider(om);
                ErrorReport er = ersp.parse();

                if (status == 403) {
                    ForbiddenRequest fr = new ForbiddenRequest("Access denied to : '" + uri + "'");
                    fr.setErrorReport(er);
                    throw fr;
                }
                if (status == 401) {
                    Unauthorized unauth =  new Unauthorized("User is not authorized to access : '" + uri + "'");
                    unauth.setErrorReport(er);
                    throw unauth;
                }
                if (status == 404) {
                    NotFound notFound = new NotFound("The following algorithm was not found : '" + uri + "'");
                    notFound.setErrorReport(er);
                    throw notFound;
                } else {
                    RemoteServiceException unexpected = new RemoteServiceException("Unexpected error from : '" + uri + "'. Service returned status "+status);
                    unexpected.setErrorReport(er);
                    throw unexpected;

                }
            }
            model = client.getResponseOntModel();
            resource = model.getResource(uri.getStringNoQuery());
        } finally { // Have to close the client (disconnect)
            if (client != null) {
                try {
                    client.close();
                } catch (IOException ex) {
                    throw new ConnectionException(
                            "Error while trying to close the stream "
                            + "with the remote location at :'" + ((uri != null) ? uri.clearToken().toString() : null) + "'", ex);
                }
            }
        }
    }

    @Override
    public Algorithm parse() throws ServiceInvocationException {
        Algorithm algorithm = null;
        try {
            algorithm = new Algorithm(uri.getStringNoQuery());
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
        if (algorithm == null) {
            throw new ServiceInvocationException("Make sure that the URI you provided holds a valid representation of an OpenTox algorithm.");
        }
        algorithm.setOntologies(getOTATypes(resource));
        MetaInfoSpider metaSpider = new MetaInfoSpider(model, uri.getStringNoQuery());
        algorithm.setMeta(metaSpider.parse());
        StmtIterator itParam = model.listStatements(
                new SimpleSelector(resource,
                OTObjectProperties.parameters().asObjectProperty(model),
                (RDFNode) null));
        while (itParam.hasNext()) {
            ParameterSpider paramSpider = new ParameterSpider(model, itParam.nextStatement().getObject().as(Resource.class));
            algorithm.getParameters().add(paramSpider.parse());
        }
        return algorithm;

    }
}
