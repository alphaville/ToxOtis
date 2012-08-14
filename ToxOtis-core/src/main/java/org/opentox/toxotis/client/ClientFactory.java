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
package org.opentox.toxotis.client;

import org.opentox.toxotis.client.http.GetHttpClient;
import org.opentox.toxotis.client.http.PostHttpClient;
import org.opentox.toxotis.client.https.GetHttpsClient;
import org.opentox.toxotis.client.https.PostHttpsClient;

/**
 * Factory for creating clients. 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ClientFactory {

    /**
     * Create a Get-client as an instance of {@link IGetClient } providing it's
     * URI. Either an HTTP or an HTTPS client is created according to the protocol
     * of the provided URI.
     * @param actionUri
     *      The URI on which the client addresses the request.
     * @return
     *      Instance of a GET-client.
     */
    public static IGetClient createGetClient(VRI actionUri) {
        String protocol = actionUri.getProtocol();
        if (protocol.equals("http")) {
            return new GetHttpClient(actionUri);
        } else if (protocol.equals("https")) {
            return new GetHttpsClient(actionUri);
        }
        return null;
    }

    /**
     * Create a POST-client as an instance of {@link IPostClient } providing it's
     * URI. Either an HTTP or an HTTPS client is created according to the protocol
     * of the provided URI.
     * @param actionUri
     *      The URI on which the client addresses the request.
     * @return
     *      Instance of a POST-client.
     */
    public static IPostClient createPostClient(VRI actionUri) {
        String protocol = actionUri.getProtocol();
        if (protocol.equals("http")) {
            return new PostHttpClient(actionUri);
        } else if (protocol.equals("https")) {
            return new PostHttpsClient(actionUri);
        }
        return null;
    }

}
