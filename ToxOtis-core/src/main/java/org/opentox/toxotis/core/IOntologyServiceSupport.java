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
package org.opentox.toxotis.core;

import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 * Declares that a class is supported by the ontology service.
 * 
 * @param <T>
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface IOntologyServiceSupport<T extends OTOnlineResource> extends IOTComponent {

    /**
     * Publishes the underlying component to the ontology service. When an OpenTox
     * online resource is published to the ontology service then it will be easily
     * found through the lookup services of OpenTox and will be available in the
     * demo applications of OpenTox (ToxPredict and ToxCreate).
     *
     * @param ontologyService 
     *      VRI of the ontology service.
     * @param token
     *      An authentication token is needed in case the ontology service
     *
     * @return
     *      The published instance.
     * 
     * @throws ServiceInvocationException
     *      In case it is not possible to publish the resource to the ontology
     *      service either because the posted representation is syntactically/
     *      semantically malformed or because the remote service experiences some
     *      internal errors (e.g. error 500 or 503).
     */
    T publishToOntService(VRI ontologyService, AuthenticationToken token) 
            throws ServiceInvocationException;
}
