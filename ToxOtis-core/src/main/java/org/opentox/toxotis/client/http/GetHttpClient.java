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
package org.opentox.toxotis.client.http;

import java.io.IOException;
import org.opentox.toxotis.client.IGetClient;
import java.util.Map;
import org.opentox.toxotis.client.RequestHeaders;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.exceptions.impl.ConnectionException;
import org.opentox.toxotis.exceptions.impl.InternalServerError;

/**
 * A client that performs GET requests.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class GetHttpClient extends AbstractHttpClient implements IGetClient {

    /** Create a new instance of GetHttpClient */
    public GetHttpClient() {
    }

    /**
     * Instantiates a GET client with a given target URI.
     * @param uri 
     *      The URI of the resource where the client should perform the request.
     */
    public GetHttpClient(final VRI uri) {
        setUri(uri);
    }

    @Override
    protected java.net.HttpURLConnection initializeConnection(final java.net.URI uri) throws ConnectionException, InternalServerError {
        if (uri == null) {
            throw new NullPointerException("Null Pointer while initializing connection (in GetHttpClient). The input "
                    + "argument 'uri' to the method GetHttpClient#initializeConnection(java.net.URI)::java.net.HttpURLConnection "
                    + "should not be null.");
        }
        try {
            java.net.HttpURLConnection.setFollowRedirects(true);
            java.net.URL url = uri.toURL();
            setConnection((java.net.HttpURLConnection) url.openConnection());
            getConnection().setDoInput(true);
            getConnection().setUseCaches(false);
            getConnection().setRequestMethod(METHOD);
            if (getMediaType() != null) {
                getConnection().setRequestProperty(RequestHeaders.ACCEPT, getMediaType());
            }
            if (getHeaderValues() != null && !getHeaderValues().isEmpty()) {
                for (Map.Entry<String, String> e : getHeaderValues().entrySet()) {
                    getConnection().setRequestProperty(e.getKey(), e.getValue());// These are already URI-encoded!
                }
            }
            return getConnection();
        } catch (final IOException ex) {
            throw new ConnectionException("Unable to connect to the remote service at '" + getUri() + "'", ex);
        } catch (final Exception unexpectedException) {
            throw new InternalServerError("Unexpected condition while attempting to "
                    + "establish a connection to '" + uri + "'", unexpectedException);
        }
    }
}

