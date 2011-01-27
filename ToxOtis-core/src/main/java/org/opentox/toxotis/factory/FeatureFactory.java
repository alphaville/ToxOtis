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

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.IGetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Feature;
import org.opentox.toxotis.exceptions.IServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ConnectionException;
import org.opentox.toxotis.exceptions.impl.ForbiddenRequest;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.Unauthorized;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.ResourceValue;
import org.opentox.toxotis.ontology.collection.OTEchaEndpoints;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.aa.TokenPool;

/**
 * A factory-like class that provides methods for looking up features in remote
 * databases through the OpenTox REST API.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class FeatureFactory {

    /**
     * Retrieve a collection of Feature URIs that are <code>same as</code> a certain
     * ECHA endpoint as these are formalized using the OpenTox ontology. This ontology
     * can be downloaded from the <a href="http://www.opentox.org/data/documents/development/
     * RDF files/Endpoints/">OpenTox repository</a> for RDF files. Within ToxOtis,
     * you can refer to the various ECHA enpoitns using the class {@link OTEchaEndpoints }.
     *
     *
     * @param service
     *       URI of an OpenTox feature service. Feature service URI comply with the pattern
     *      <code>http://someserver.com/feature/id</code> which formally, in terms of
     *      regular expression matches the pattern <code>.+/(?i)feature(s||)/([^/]+/$|[^/]+)$</code>.
     *      If the URI is set to <code>null</code> then the Ontology Service is used.
     * @param echaEndpoint
     *      An ECHA enpoint as an Ontological Class. You may obtain a list of some default
     *      endpoint classes from {@link OTEchaEndpoints }.
     * @param token
     *      Auththentication token provided by the user to authenticate against the service in case
     *      it has restricted access.
     *
     * @return a Set of Feature URIs that are <code>same as</code> the ECHA endpoint provided.
     */
    public static Set<VRI> lookupSameAs(
            VRI service, OntologicalClass echaEndpoint, AuthenticationToken token) throws ServiceInvocationException {
        IGetClient client = ClientFactory.createGetClient(service.addUrlParameter("sameas", echaEndpoint.getUri()));
        client.setMediaType(Media.TEXT_URI_LIST.getMime());
        int responseStatus;
        responseStatus = client.getResponseCode();

        if (responseStatus == 200) {
            Set<VRI> featureUris = client.getResponseUriList();
            Set<VRI> features = new HashSet<VRI>();
            for (VRI featureUri : featureUris) {
                features.add(new VRI(featureUri));
            }
            return features;
        } else if (responseStatus == 403) {
            throw new ForbiddenRequest("Authentication Faile: "
                    + "Client failed to authenticate itself against the SSO service due to "
                    + "incorrect credentials or due to invalid token");
        } else if (responseStatus == 401) {
            throw new Unauthorized("UnauthorizedUser: "
                    + "The client is authenticated but not authorized to perform this operation");
        } else {
            throw new ServiceInvocationException("UnknownCauseOfException: "
                    + "The remote service at " + service + "returned the unexpected status : " + responseStatus);
        }
    }

    /**
     * Retrieve a collection of Feature URIs by querying a default Feature Service,
     * that are <code>same as</code> a certain
     * ECHA endpoint as these are formalized using the OpenTox ontology. This ontology
     * can be downloaded from the <a href="http://www.opentox.org/data/documents/development/
     * RDF files/Endpoints/">OpenTox repository</a> for RDF files. Within ToxOtis,
     * you can refer to the various ECHA enpoitns using the class {@link OTEchaEndpoints }.
     *
     *
     * @param echaEndpoint
     *      An ECHA enpoint as an Ontological Class. You may obtain a list of some default
     *      endpoint classes from {@link OTEchaEndpoints }.
     * @param token
     *      Auththentication token provided by the user to authenticate against the service in case
     *      it has restricted access.
     *
     * @return a Set of Feature URIs that are <code>same as</code> the ECHA endpoint provided.
     */
    public static Set<VRI> lookupSameAs(
            OntologicalClass echaEndpoint, AuthenticationToken token) throws ServiceInvocationException {
        return lookupSameAs(Services.ideaconsult().augment("feature"), echaEndpoint, token);
    }

    /**
     * Lists all features available from a feature service providing also an authentication
     * token.
     * @param featureService
     *      The URI of a feature service
     * @param max
     *      The maximum number of feature URIs to be returned from the feature
     *      service. This is supported by the web services at <code>apps.ideaconsult.net</code>
     *      and <code>ambit.uni-plovdiv.bg</code> but is not part of the official API
     *      (version 1.1) so it might not be supported by other OpenTox feature services. if set
     *      to <code>-1</code> the method returns all available features on the
     *      remote server. If the provided URI has already a directive for the
     *      maximum number of features using the URL query <code>?max={max}</code>,
     *      it will be overridden.
     * @param token
     *      An authentication token which can be obtained from the singleton class
     *      {@link TokenPool } that manages multiple logged in users.
     * @return
     *      Set of all URIs available from the feature service
     * @throws ToxOtisException
     *      In case a non-success HTTP status code is returned by the remote service,
     *      either due to authentication/authorization failure of due to other unexpected
     *      conditions (e.g. error 500 or 503).
     */
    public static Set<VRI> listAllFeatures(VRI featureService, int max, AuthenticationToken token) throws ServiceInvocationException {
        return listAllFeatures(featureService, 1, max, token);
    }

    /**
     * Lists all features available from a feature service providing also an authentication
     * token.
     * @param featureService
     *      The URI of a feature service
     * @param page
     *      When paging of results is supported, <code>page</code> stands fort the index
     *      of that page. If set to <code>-1</code> it will have no effect on the URI
     *      and the request so all pages will be returned.
     * @param pagesize
     *      Size of the page of results to be returned. If set to <code>-1</code> it
     *      will have no effect on the URI and the request so all pages will be returned,
     *      i.e. all features contained in the remote database.
     * @param token
     *      An authentication token which can be obtained from the singleton class
     *      {@link TokenPool } that manages multiple logged in users.
     * @return
     *      Set of all URIs available from the feature service
     * @throws ToxOtisException
     *      In case a non-success HTTP status code is returned by the remote service,
     *      either due to authentication/authorization failure of due to other unexpected
     *      conditions (e.g. error 500 or 503).
     */
    public static Set<VRI> listAllFeatures(VRI featureService, int page, int pagesize, AuthenticationToken token) throws ServiceInvocationException {
        VRI featureServiceWithToken = new VRI(featureService).clearToken().
                appendToken(token).removeUrlParameter("page").removeUrlParameter("pagesize");
        if (page > 0) {
            featureServiceWithToken.addUrlParameter("page", page);
        }
        if (pagesize > 0) {
            featureServiceWithToken.addUrlParameter("pagesize", pagesize);
        }
        IGetClient client = ClientFactory.createGetClient(featureServiceWithToken);
        client.setMediaType(Media.TEXT_URI_LIST);
        int httpStatus = client.getResponseCode();
        if (httpStatus != 200) {
            throw new ServiceInvocationException("Service returned status code :" + httpStatus);
        }
        Set<VRI> result = client.getResponseUriList();
        if (client != null) {
            try {
                client.close();
            } catch (IOException ex) {
                throw new ConnectionException(ex);
            }
        }
        return result;

    }

    /**
     * Created and publishes online a new Feature given its title and source (acting
     * as a reason for its creation).
     * 
     * @param title
     *      Title of the newly created feature
     * @param units
     *      Units for the created feature
     * @param hasSource
     *      An  <code>ot:hasSource</code> reference for the created Feature
     * @param fetureService
     *      Feature service URI to which the created feature should be posted
     * @param token
     *      Authentication token used to authenticate and authorize the user to
     *      publish the feature
     * @return
     *      The created feature with an updated URI. Use the method {@link Feature#getUri() }
     *      to obtain the URI of the newly created feature.
     * @throws ToxOtisException
     *      In case the publication of the feature is not possible, the process
     *      is interrupted while waiting for its completion of other unexpected
     *      exceptional events related to the publication of the feature occur.
     */
    public static Feature createAndPublishFeature(String title, String units, ResourceValue hasSource, VRI fetureService, AuthenticationToken token) throws ServiceInvocationException {
        MetaInfo mi = new MetaInfoImpl();
        mi.addTitle(title);
        mi.addHasSource(hasSource);
        return createAndPublishFeature(mi, units, fetureService, token);
    }

    /**
     * Created and publishes online a new Feature given its title and source (acting
     * as a reason for its creation).
     *
     * @param metaInfo
     *      Meta-information about the feature
     * @param units
     *      Units for the created feature
     * @param featureService 
     *      Feature service URI to which the created feature should be posted
     * @param token
     *      Authentication token used to authenticate and authorize the user to
     *      publish the feature
     * @return
     *      The created feature with an updated URI. Use the method {@link Feature#getUri() }
     *      to obtain the URI of the newly created feature.
     * @throws ToxOtisException
     *      In case the publication of the feature is not possible, the process
     *      is interrupted while waiting for its completion of other unexpected
     *      exceptional events related to the publication of the feature occur.
     */
    public static Feature createAndPublishFeature(MetaInfo metaInfo,
            String units, VRI featureService, AuthenticationToken token) throws ServiceInvocationException {
        Feature brandNewFeature = new Feature();
        brandNewFeature.setMeta(metaInfo);
        brandNewFeature.setUnits(units);
        Future<VRI> predictedFeatureUri = brandNewFeature.publish(featureService, token);
        /* Wait for remote server to respond */
        try {
            while (!predictedFeatureUri.isDone()) {
                Thread.sleep(1000);
            }
            VRI resultUri = predictedFeatureUri.get();
            brandNewFeature.setUri(resultUri);
            return brandNewFeature;
        } catch (InterruptedException ex) {
            throw new RuntimeException("Interrupted", ex);
        } catch (ExecutionException ex) {
            if (ex.getCause() != null && ex.getCause() instanceof ServiceInvocationException) {
                throw (ServiceInvocationException) ex.getCause();
            }
            throw new ServiceInvocationException(ex);
        }
    }
}
