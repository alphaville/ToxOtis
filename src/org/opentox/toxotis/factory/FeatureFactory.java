package org.opentox.toxotis.factory;

import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.GetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.core.Feature;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTEchaEndpoints;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.spiders.FeatureSpider;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class FeatureFactory {

    //TODO: If the user does not provide a URI for a lookup service,
    // then the Ontology service should be used.

    private static FeatureFactory factory = null;

    /**
     * Returns the FeatureFactory object associated with the current Java application.
     * All factories in ToxOtis are singletons and have a single access point.
     *
     * @return
     *      The FeatureFactory object associated with the current Java application.
     */
    public static FeatureFactory getInstance() {
        if (factory == null) {
            factory = new FeatureFactory();
        }
        return factory;
    }

    /** dummy constructor */
    private FeatureFactory() {
        super();
    }

    /**
     * Retrieve a collection of features that are <code>same as</code> a certain
     * ECHA endpoint as these are formalized using the OpenTox ontology. This ontology
     * can be downloaded from the <a href="http://www.opentox.org/data/documents/development/
     * RDF files/Endpoints/">OpenTox repository</a> for RDF files. Within ToxOtis,
     * you can refer to the various ECHA enpoitns using the class {@link OTEchaEndpoints }.
     *
     *
     * @param uri
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
     * @return a Set of Features that are <code>same as</code> the ECHA endpoint provided.
     */
    public Set<VRI> lookupSameAs(VRI service, OntologicalClass echaEndpoint, AuthenticationToken token) throws ToxOtisException{
        GetClient client = new GetClient(service.addUrlParameter("sameas", echaEndpoint.getUri()));
        client.setMediaType(Media.TEXT_URI_LIST.getMime());

        List<String> featureUris = client.getResponseUriList();

        Set<VRI> features = new HashSet<VRI>();
        for(String featureUri : featureUris){
            try {
                features.add(new VRI(featureUri));
            } catch (URISyntaxException ex) {
                throw new ToxOtisException(ex);
            }
        }
        return features;
    }
}
