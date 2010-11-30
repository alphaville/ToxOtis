package org.opentox.toxotis.core;

import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 * Declares that a class is supported by the ontology service.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface IOntologyServiceSupport<T extends OTOnlineResource> {

    /**
     * Publishes the underlying component to the ontology service. When an OpenTox
     * online resource is published to the ontology service then it will be easily
     * found through the lookup services of OpenTox and will be available in the
     * demo applications of OpenTox (ToxPredict and ToxCreate).
     *
     * @param token
     *      An authentication token is needed in case the ontology service
     *
     * @return
     *      The published instance.
     * 
     * @throws ToxOtisException
     *      In case it is not possible to publish the resource to the ontology
     *      service either because the posted representation is syntactically/
     *      semantically malformed or because the remote service experiences some
     *      internal errors (e.g. error 500 or 503).
     */
    T publishToOntService(VRI ontologyService, AuthenticationToken token) throws ToxOtisException;
}
