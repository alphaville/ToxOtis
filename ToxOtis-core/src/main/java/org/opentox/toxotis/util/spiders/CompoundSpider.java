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
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.IGetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.core.component.Compound;
import org.opentox.toxotis.core.component.ErrorReport;
import org.opentox.toxotis.exceptions.impl.BadRequestException;
import org.opentox.toxotis.exceptions.impl.ConnectionException;
import org.opentox.toxotis.exceptions.impl.ForbiddenRequest;
import org.opentox.toxotis.exceptions.impl.InternalServerError;
import org.opentox.toxotis.exceptions.impl.NotFound;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.exceptions.impl.Unauthorized;

/**
 * Downloader and parser for a compound resource available in RDF.
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class CompoundSpider extends Tarantula<Compound> {

    VRI uri;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CompoundSpider.class);

    public CompoundSpider(VRI uri) throws ServiceInvocationException, ToxOtisException {
        super();
        this.uri = uri;
        IGetClient client = ClientFactory.createGetClient(uri);
        try {
            client.setMediaType(Media.APPLICATION_RDF_XML.getMime());
            client.setUri(uri);
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
                    Unauthorized unauth = new Unauthorized("User is not authorized to access : '" + uri + "'");
                    unauth.setErrorReport(er);
                    throw unauth;
                }
                if (status == 404) {
                    NotFound notFound = new NotFound("The following algorithm was not found : '" + uri + "'");
                    notFound.setErrorReport(er);
                    throw notFound;
                } else {
                    ConnectionException connectionException = new ConnectionException("Communication Error with : '" + uri + "'");
                    connectionException.setErrorReport(er);
                    throw connectionException;

                }
            }
            model = client.getResponseOntModel();
            resource = model.getResource(uri.toString());
        } finally { // Have to close the client (disconnect)
            if (client != null) {
                try {
                    client.close();
                } catch (IOException ex) {
                    throw new ConnectionException("Error while trying to close the stream "
                            + "with the remote location at :'" + ((uri != null) ? uri.clearToken().toString() : null) + "'", ex);
                }
            }
        }
    }

    public CompoundSpider(Resource resource, OntModel model) {
        super(resource, model);
        try {
            uri = new VRI(resource.getURI());
        } catch (URISyntaxException ex) {
            logger.debug(null, ex);
        }
    }

    public CompoundSpider(OntModel model, String uri) {
        super();
        this.model = model;
        try {
            this.uri = new VRI(uri);
        } catch (URISyntaxException ex) {
            logger.debug(null, ex);
        }
        this.resource = model.getResource(uri);
    }

    @Override
    public Compound parse() throws ServiceInvocationException {
        Compound compound = null;
        try {
            compound = new Compound(uri);
        } catch (ToxOtisException ex) {
            throw new BadRequestException("Not a valid compound URI : '"+uri+"'. " +
                    "Parsing of remote resource won't continue!",ex);
        }
        compound.setMeta(new MetaInfoSpider(resource, model).parse());
        return compound;
    }
}
