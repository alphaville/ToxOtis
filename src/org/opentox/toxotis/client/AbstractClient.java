package org.opentox.toxotis.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.ontology.impl.SimpleOntModelImpl;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class AbstractClient {

    /** Target URI */
    protected VRI vri = null;
    /** Connection to the above URI */
    protected java.net.HttpURLConnection con = null;
    protected static final String MEDIATYPE_FORM_URL_ENCODED = "application/x-www-form-urlencoded";
    /** Standard UTF-8 Encoding */
    protected static final String URL_ENCODING = "UTF-8";
    /** Size of a buffer used to download the data from the remote server */
    protected static final int bufferSize = 4194304;
    /** Accepted mediatype  */
    protected String acceptMediaType = null;

    /**
     * Get the targetted URI
     * @return The target URI
     */
    public VRI getUri() {
        return vri;
    }

    public String getMediaType() {
        return acceptMediaType;
    }

    protected abstract java.net.HttpURLConnection initializeConnection(final java.net.URI uri) throws ToxOtisException;

     /** Get the normal stream of the response (body) */
    public java.io.InputStream getRemoteStream() throws ToxOtisException, java.io.IOException {
        if (con == null) {
            con = initializeConnection(vri.toURI());
        }
        if (con == null) {
            throw new ToxOtisException("Comminucation Error with the remote");
        }
        if (con.getResponseCode() == 200 || con.getResponseCode() == 202) {
            return new java.io.BufferedInputStream(con.getInputStream(), bufferSize);
        } else {
            return new java.io.BufferedInputStream(con.getErrorStream(), bufferSize);
        }
    }

    public String getResponseText() throws ToxOtisException {
        if (con == null) {
            con = initializeConnection(vri.toURI());
        }
        InputStream is = null;
        BufferedReader reader = null;
        try {
            is = getRemoteStream();
            reader = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return new String(sb);
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
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    throw new ToxOtisException(ex);
                }
            }
        }
    }

    public com.hp.hpl.jena.ontology.OntModel getResponseOntModel() throws ToxOtisException {
        try {
            com.hp.hpl.jena.ontology.OntModel om = new SimpleOntModelImpl();
            om.read(getRemoteStream(), null);
            return om;
        } catch (final Exception ex) {
            throw new ToxOtisException("Cannot read OntModel from " + vri.toString(), ex);
        }
    }

    /** Get the response status */
    public int getResponseCode() throws ToxOtisException, java.io.IOException {
        if (con == null) {
            con = initializeConnection(vri.toURI());
        }
        return con.getResponseCode();
    }

}