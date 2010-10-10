package org.opentox.toxotis.factory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.GetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTEchaEndpoints;
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
            VRI service, OntologicalClass echaEndpoint, AuthenticationToken token)
            throws ToxOtisException {
        GetClient client = new GetClient(service.addUrlParameter("sameas", echaEndpoint.getUri()));
        client.setMediaType(Media.TEXT_URI_LIST.getMime());
        final int responseStatus;
        try {
            responseStatus = client.getResponseCode();
        } catch (IOException ex) {
            throw new ToxOtisException(ex);
        }
        if (responseStatus == 200) {
            Set<VRI> featureUris = client.getResponseUriList();
            Set<VRI> features = new HashSet<VRI>();
            for (VRI featureUri : featureUris) {
                features.add(new VRI(featureUri));
            }
            return features;
        } else if (responseStatus == 403) {
            throw new ToxOtisException(ErrorCause.AuthenticationFailed,
                    "Client failed to authenticate itself against the SSO service due to "
                    + "incorrect credentials or due to invalid token");
        } else if (responseStatus == 401) {
            throw new ToxOtisException(ErrorCause.UnauthorizedUser,
                    "The client is authenticated but not authorized to perform this operation");
        } else {
            throw new ToxOtisException(ErrorCause.UnknownCauseOfException,
                    "The remote service at " + service + "returned the unexpected status : " + responseStatus);
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
            OntologicalClass echaEndpoint, AuthenticationToken token)
            throws ToxOtisException {
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
    public static Set<VRI> listAllFeatures(VRI featureService, int max, AuthenticationToken token) throws ToxOtisException {
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
    public static Set<VRI> listAllFeatures(VRI featureService, int page, int pagesize, AuthenticationToken token)
            throws ToxOtisException {
        try {
            VRI featureServiceWithToken = new VRI(featureService).clearToken().
                    appendToken(token).removeUrlParameter("page").removeUrlParameter("pagesize");
            if (page > 0) {
                featureServiceWithToken.addUrlParameter("page", page);
            }
            if (pagesize > 0) {
                featureServiceWithToken.addUrlParameter("pagesize", pagesize);
            }
            GetClient client = new GetClient(featureServiceWithToken);
            client.setMediaType(Media.TEXT_URI_LIST);
            final int httpStatus = client.getResponseCode();
            if (httpStatus != 200) {
                throw new ToxOtisException("Service returned status code :" + httpStatus);
            }            
            Set<VRI> result = client.getResponseUriList();
            if (client != null) {
                try {
                    client.close();
                } catch (IOException ex) {
                    throw new ToxOtisException(ex);
                }
            }
            return result;
        } catch (IOException ex) {
            throw new ToxOtisException(ex);
        }
    }
}
