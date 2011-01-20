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
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.HttpsURLConnection;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.IGetClient;
import org.opentox.toxotis.client.RequestHeaders;
import org.opentox.toxotis.client.VRI;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class GetHttpsClient extends AbstractHttpsClient implements IGetClient{

    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(GetHttpsClient.class);
   

    public GetHttpsClient(VRI vri) {
        super(vri);
    }

    public GetHttpsClient() {
    }

    @Override
    protected HttpsURLConnection initializeConnection(URI uri) throws ToxOtisException {
        try {
            java.net.URL target_url = uri.toURL();
            con = (javax.net.ssl.HttpsURLConnection) target_url.openConnection();
            con.setRequestMethod(METHOD);
            con.setDoInput(true);
            con.setUseCaches(false);
            if (acceptMediaType != null) {
                con.setRequestProperty(RequestHeaders.ACCEPT, acceptMediaType);
            }
            if (!headerValues.isEmpty()) {
                for (Map.Entry<String, String> e : headerValues.entrySet()) {
                    con.setRequestProperty(e.getKey(), e.getValue());// These are already URI-encoded!
                }
            }
            return con;
        } catch (IOException ex) {
            throw new ToxOtisException(ex);
        }
    }


    /** Get the result as a URI list */
    @Override
    public Set<VRI> getResponseUriList() throws ToxOtisException {
        java.util.Set<VRI> list = new java.util.HashSet<VRI>();
        java.io.InputStreamReader isr = null;
        java.io.InputStream is = null;
        java.io.BufferedReader reader = null;
        try {
            if (con == null) {
                con = initializeConnection(vri.toURI());
            }
            is = getRemoteStream();
            isr = new java.io.InputStreamReader(is);
            reader = new java.io.BufferedReader(isr);
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    list.add(new VRI(line));
                } catch (URISyntaxException ex) {
                    logger.debug("Invalid URI",ex);
                }
            }
        } catch (ToxOtisException cl) {
            throw cl;
        } catch (IOException io) {
            throw new ToxOtisException(io);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    throw new ToxOtisException(ex);
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException ex) {
                    throw new ToxOtisException(ex);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    throw new ToxOtisException(ex);
                }
            }
        }
        return list;
    }

}