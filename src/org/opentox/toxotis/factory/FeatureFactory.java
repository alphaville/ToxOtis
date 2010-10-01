package org.opentox.toxotis.factory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
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

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class FeatureFactory {

//    private static FeatureFactory factory = null;
//
//    /**
//     * Returns the FeatureFactory object associated with the current Java application.
//     * All factories in ToxOtis are singletons and have a single access point.
//     *
//     * @return
//     *      The FeatureFactory object associated with the current Java application.
//     */
//    public static FeatureFactory getInstance() {
//        if (factory == null) {
//            factory = new FeatureFactory();
//        }
//        return factory;
//    }
//
//    /** dummy constructor */
//    private FeatureFactory() {
//        super();
//    }

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
            List<String> featureUris = client.getResponseUriList();
            Set<VRI> features = new HashSet<VRI>();
            for (String featureUri : featureUris) {
                try {
                    features.add(new VRI(featureUri));
                } catch (URISyntaxException ex) {
                    throw new ToxOtisException(ex);
                }
            }
            return features;
        } else if (responseStatus == 403) {
            throw new ToxOtisException(ErrorCause.AuthenticationFailed,
                    "Client failed to authenticate itself against the SSO service due to " +
                    "incorrect credentials or due to invalid token");
        } else if (responseStatus == 401) {
            throw new ToxOtisException(ErrorCause.UnauthorizedUser,
                    "The client is authenticated but not authorized to perform this operation");
        } else {
            throw new ToxOtisException(ErrorCause.UnknownCauseOfException,
                    "The remote service at "+service+"returned the unexpected status : " + responseStatus);
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
}
