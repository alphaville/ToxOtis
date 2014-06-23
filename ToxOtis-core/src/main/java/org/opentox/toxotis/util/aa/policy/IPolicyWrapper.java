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
 * Copyright (C) 2009-2010 Pantelis Sopas2akis & Charalampos Chomenides
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
package org.opentox.toxotis.util.aa.policy;

import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.w3c.dom.Document;

/**
 * A Policy Wrapper is a bundle of policies that can be sent/posted as one object
 * to the openSSO server.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface IPolicyWrapper {

    Document getDocument();

    String getText();
    

    /**
     * Publish this policy to a remote server and acquire a URI for it.
     *
     * @param policyServer
     * URI of the policy server. If set to <code>null</code> then the standard
     * policy service of OpenTox at https://opensso.in-silico.ch/pol will be
     * used instead.
     * @param token
     * Token used to authenticate the user that attempts to publish a new policy
     * against the policy service. If you think that no authentication is needed
     * to perform the HTTP request you may set it to <code>null</code>.
     * @return
     * Server's response message
     *
     * @throws ServiceInvocationException
     * In case a HTTP related error occurs (I/O communication error, or the
     * remote server is down), the service respondes in an unexpected manner
     * like a status code 500 or 503 or authentication/authorization fails and
     * a status code 403 or 401 are returned respectively.
     * If the token the user uses is not active (because it has been invalidated,
     * expired, or not initialized yet).
     */
    int publish(VRI policyServer, AuthenticationToken token) throws ServiceInvocationException;

}
