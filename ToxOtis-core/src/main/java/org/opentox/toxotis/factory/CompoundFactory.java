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
package org.opentox.toxotis.factory;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Set;
import org.opentox.toxotis.client.IPostClient;
import org.opentox.toxotis.client.http.PostHttpClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.exceptions.impl.RemoteServiceException;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.spiders.TaskSpider;

/**
 * CompoundFactory provides methods for creating new compounds and publishing them
 * to remote compound services. You can create a new compound using an SDF, MOL or
 * SMILES representation of it provided as a file.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class CompoundFactory {

    private static CompoundFactory factory = null;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CompoundFactory.class);

    /**
     * Returns the CompoundFactory object associated with the current Java application.
     * All factories in ToxOtis are singletons and have a single access point.
     *
     * @return
     *      The CompoundFactory object associated with the current Java application.
     */
    public static CompoundFactory getInstance() {
        if (factory == null) {
            factory = new CompoundFactory();
        }
        return factory;
    }

    /** dummy constructor */
    private CompoundFactory() {
        super();
    }

    /**
     * POSTs a file to a default compound service using a specified Content-type header
     * in order to create a new Compound. The created compound is returned
     * to the user.
     * @param sourceFile
     *      File where information about the compound are stored. Can be a <code>mol</code>
     *      file, a <code>CML</code> one, an <code>SD</code> file or other file
     *      format that is accepted by the compound service.
     * @param token
     *      Token used for authenticating the client against the remote compound
     *      service (You can set it to <code>null</code>).
     * @param fileType
     *      The Content-type of the file to be posted.
     * @return
     *      The compound created by the Service.
     * @throws ServiceInvocationException
     *      In case an authentication error occurs or the remote service responds
     *      with an error code like 500 or 503 or the submitted representation is
     *      syntactically or semantically wrong (status 400).
     */
    public Task publishFromFile(File sourceFile, String fileType, AuthenticationToken token) 
            throws ServiceInvocationException {
        return publishFromFile(sourceFile, fileType, token, Services.ideaconsult().augment("compound").toString());
    }

    /**
     * POSTs a file to a default compound service using a specified Content-type header
     * in order to create a new Compound. The created compound is returned
     * to the user.
     * @param sourceFile
     *      File where information about the compound are stored. Can be a <code>mol</code>
     *      file, a <code>CML</code> one, an <code>SD</code> file or other file
     *      format that is accepted by the compound service.
     * @param token
     *      Token used for authenticating the client against the remote compound
     *      service (You can set it to <code>null</code>).
     * @param fileType
     *      The Content-type of the file to be posted.
     * @return
     *      The compound created by the Service.
     * @throws ServiceInvocationException
     *      In case an authentication error occurs or the remote service responds
     *      with an error code like 500 or 503 or the submitted representation is
     *      syntactically or semantically wrong (status 400).
     */
    public Task publishFromFile(File sourceFile, Media fileType, AuthenticationToken token)
            throws ServiceInvocationException {
        return publishFromFile(sourceFile, fileType.getMime(), token, Services.ideaconsult().augment("compound").toString());
    }

    /**
     * POSTs a file to a specified compound service using a specified Content-type header
     * in order to create a new Compound. The created compound is returned
     * to the user.
     * @param sourceFile
     *      File where information about the compound are stored. Can be a <code>mol</code>
     *      file, a <code>CML</code> one, an <code>SD</code> file or other file
     *      format that is accepted by the compound service.
     * @param token
     *      Token used for authenticating the client against the remote compound
     *      service (You can set it to <code>null</code>).
     * @param fileType
     *      The Content-type of the file to be posted.
     * @param service
     *      The URI of the service on which the new Compound will be posted.
     * @return
     *      The compound created by the Service.
     * @throws ServiceInvocationException
     *      In case an authentication error occurs or the remote service responds
     *      with an error code like 500 or 503 or the submitted representation is
     *      syntactically or semantically wrong (status 400).
     */
    public Task publishFromFile(
            File sourceFile, String fileType, AuthenticationToken token, String service)
            throws ServiceInvocationException {
        try {
            IPostClient postClient = new PostHttpClient(
                    new VRI(service));
            postClient.authorize(token);
            postClient.setPostable(sourceFile);
            postClient.setContentType(fileType);
            postClient.setMediaType(Media.TEXT_URI_LIST.getMime());
            postClient.post();
            VRI newVRI = new VRI(postClient.getResponseText());
            int responseStatus = -1;
            responseStatus = postClient.getResponseCode();

            if (responseStatus == 202) {
                TaskSpider tskSp = new TaskSpider(newVRI);
                return tskSp.parse();
            } else if (responseStatus == 200) {
                Task t = new Task();
                t.setResultUri(newVRI);
                t.setStatus(Task.Status.COMPLETED);
                return t;
            } else {
                String message = "HTTP Status : " + responseStatus;
                logger.debug(message);
                throw new ServiceInvocationException(message);
            }
        } catch (URISyntaxException ex) {
            String message = "Service URI is invalid";
            logger.debug(message, ex);
            throw new RemoteServiceException(message, ex);
        }
    }

    /**
     * POSTs a file to a specified compound service using a specified Content-type header
     * in order to create a new Compound. The created compound is returned
     * to the user.
     * @param sourceFile
     *      File where information about the compound are stored. Can be a <code>mol</code>
     *      file, a <code>CML</code> one, an <code>SD</code> file or other file
     *      format that is accepted by the compound service.
     * @param token
     *      Token used for authenticating the client against the remote compound
     *      service (You can set it to <code>null</code>).
     * @param fileType
     *      The Content-type of the file to be posted as an element from {@link Media }.
     * @param service
     *      The URI of the service on which the new Compound will be posted.
     * @return
     *      The compound created by the Service.
     * @throws ServiceInvocationException
     *      In case an authentication error occurs or the remote service responds
     *      with an error code like 500 or 503 or the submitted representation is
     *      syntactically or semantically wrong (status 400).
     */
    public Task publishFromFile(
            File sourceFile, Media fileType, AuthenticationToken token, String service)
            throws ServiceInvocationException {
        return publishFromFile(sourceFile, fileType.getMime(), token, service);
    }

    public Set<VRI> lookUpComponent(VRI lookUpService, String keyword) {
        throw new UnsupportedOperationException();
    }

    public Task publishFromStream(
            InputStream source, String fileType, AuthenticationToken token, String service)
            throws ServiceInvocationException {
        try {
            IPostClient postClient = new PostHttpClient(
                    new VRI(service));
            postClient.authorize(token);
            postClient.setPostable(source);
            postClient.setContentType(fileType);
            postClient.setMediaType(Media.TEXT_URI_LIST.getMime());
            postClient.post();
            VRI newVRI = new VRI(postClient.getResponseText());
            int responseStatus = -1;
            responseStatus = postClient.getResponseCode();

            if (responseStatus == 202) {
                TaskSpider tskSp = new TaskSpider(newVRI);
                return tskSp.parse();
            } else if (responseStatus == 200) {
                Task t = new Task();
                t.setResultUri(newVRI);
                t.setStatus(Task.Status.COMPLETED);
                return t;
            } else {
                String message = "HTTP Status : " + responseStatus;
                logger.debug(message);
                throw new ServiceInvocationException(message);
            }
        } catch (URISyntaxException ex) {
            String message = "Service URI is invalid";
            logger.debug(message, ex);
            throw new RemoteServiceException(message, ex);
        }
    }

    public Task publishFromStream(
            InputStream source, Media fileType, AuthenticationToken token, String service)
            throws ServiceInvocationException {
        return publishFromStream(source, fileType.getMime(), token, service);
    }

    public Task publishFromStream(InputStream source, Media fileType, AuthenticationToken token)
            throws ServiceInvocationException {
        return publishFromStream(source, fileType.getMime(), token, Services.ideaconsult().augment("compound").toString());
    }

    public Task publishFromStream(InputStream source, String fileType, AuthenticationToken token)
            throws ServiceInvocationException {
        return publishFromStream(source, fileType, token, Services.ideaconsult().augment("compound").toString());
    }
}
