package org.opentox.toxotis.core;

import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 * Declares that a class is supported by the ontology service.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface OntologyServiceSupport<T extends OTComponent> {

    /**
     * Publishes the underlying component to the ontology service
     * @param token
     *      An authentication token is needed in case the ontology service
     * @return
     * @throws ToxOtisException
     */
    T publishToOntService(AuthenticationToken token) throws ToxOtisException;
}
