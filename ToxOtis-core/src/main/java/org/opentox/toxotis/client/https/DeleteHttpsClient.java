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
package org.opentox.toxotis.client.https;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.exceptions.impl.ConnectionException;
import org.opentox.toxotis.exceptions.impl.InternalServerError;
import org.opentox.toxotis.exceptions.impl.RemoteServiceException;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DeleteHttpsClient extends AbstractHttpsClient {

    /** The method that this client applies */
    public static final String METHOD = "DELETE";
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DeleteHttpsClient.class);

    public DeleteHttpsClient(VRI vri) {
        super(vri);
    }

    public DeleteHttpsClient() {
    }

    @Override
    protected HttpsURLConnection initializeConnection(URI uri) throws ServiceInvocationException {
        try {
            java.net.URL targetUrl = uri.toURL();
            con = (javax.net.ssl.HttpsURLConnection) targetUrl.openConnection();
            con.setRequestMethod(METHOD);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            if (!headerValues.isEmpty()) {
                for (Map.Entry<String, String> e : headerValues.entrySet()) {
                    con.setRequestProperty(e.getKey(), e.getValue());// These are already URI-encoded!
                }
            }
            return (HttpsURLConnection) con;
        } catch (final IOException ex) {
            throw new ConnectionException("Unable to connect to the remote service at '" + getUri() + "'", ex);
        } catch (final Exception unexpectedException) {
            throw new InternalServerError("Unexpected condition while attempting to "
                    + "establish a connection to '" + uri + "'", unexpectedException);
        }
    }

    public void doDelete() throws ServiceInvocationException {
        if (con == null) {
            con = initializeConnection(vri.toURI());
            try {
                int code = con.getResponseCode();
                if (code != 200) {
                    throw new RemoteServiceException("DELETE failed on '" + vri + "'. The remote service responded "
                            + "with status code " + code);
                }
            } catch (IOException ex) {
                throw new ConnectionException("Unable to connect to the remote service at '" + getUri() + "'", ex);
            }
        }
    }
}
