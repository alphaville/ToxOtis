package org.opentox.toxotis.factory;

import java.util.Set;
import org.opentox.toxotis.client.GetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTEchaEndpoints;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class FeatureFactory {

    //TODO: If the user does not provide a URI for a lookup service,
    // then the Ontology service should be used.

    private FeatureFactory factory = null;

    /**
     * Returns the FeatureFactory object associated with the current Java application.
     * All factories in ToxOtis are singletons and have a single access point.
     *
     * @return
     *      The FeatureFactory object associated with the current Java application.
     */
    public FeatureFactory getInstance() {
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
     * @return
     */
    public Set<VRI> lookupSameAs(VRI uri, OntologicalClass echaEndpoint, AuthenticationToken token){
        GetClient client = new GetClient(uri);
        return null;
    }
}
