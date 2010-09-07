package org.opentox.toxotis.client;

import com.hp.hpl.jena.ontology.OntModel;
import java.io.IOException;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.ontology.impl.SimpleOntModelImpl;

/**
 * A client that performs GET requests.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class GetClient {

    /** Accepted mediatype  */
    private String mediaType;
    /** URI at which the request is performed (GET) */
    private VRI vri;
    /** Connection to the above URI */
    protected java.net.HttpURLConnection con;
    /** Size of a buffer used to download the data from the remote server */
    private static int bufferSize = 4194304;


    /** Create a new instance of GetClient */
    public GetClient() {
    }

    public String getMediaType() {
        return mediaType;
    }

    /**
     * Specify the mediatype to be used in the <tt>Accept</tt> header.
     * @param mediaType Accepted mediatype
     */
    public GetClient setMediaType(String mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    /**
     * Get the targetted URI
     * @return The target URI
     */
    public java.net.URI getUri() {
        return vri.toURI();
    }

    /**
     * Set the URI on which the GET method is applied.
     * @param vri
     */
    public GetClient setUri(VRI vri) {
        this.vri = vri;
        return this;
    }

    /**
     * Provide the target URI as a String
     * @param uri The target URI as a String.
     * @throws java.net.URISyntaxException In case the provided URI is syntactically
     * incorrect.
     */
    public GetClient setUri(String uri) throws java.net.URISyntaxException {
        this.vri = new VRI(uri);
        return this;
    }

    /** Initialize a connection to the target URI */
    private java.net.HttpURLConnection initializeConnection(final java.net.URI uri) throws ToxOtisException {
        try {
            java.net.HttpURLConnection.setFollowRedirects(true);
            java.net.URL dataset_url = uri.toURL();
            con = (java.net.HttpURLConnection) dataset_url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            if (mediaType != null) {
                con.setRequestProperty("Accept", mediaType);
            }
            return con;
        } catch (final Exception ex) {
            throw new ToxOtisException(ex);
        }
    }

    /** Get the normal stream of the response (body) */
    public java.io.InputStream getRemoteStream() throws ToxOtisException, java.io.IOException {
        if (con == null) {
            con = initializeConnection(vri.toURI());
        }
        if (con == null) {
            throw new ToxOtisException("Comminucation Error with the remote");
        }
        if (con.getResponseCode() == 200) {
            return new java.io.BufferedInputStream(con.getInputStream(), bufferSize);
        } else {
            return new java.io.BufferedInputStream(con.getErrorStream(), bufferSize);
        }
    }

    /** Get the response status */
    public int getResponseCode() throws ToxOtisException, java.io.IOException {
        if (con == null) {
            con = initializeConnection(vri.toURI());
        }
        return con.getResponseCode();
    }

    /** Get the result as a URI list */
    public java.util.List<String> getResponseUriList() throws ToxOtisException, IOException {
        setMediaType("text/uri-list");// Set the mediatype to text/uri-list
        java.util.List<String> list = new java.util.ArrayList<String>();
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
                list.add(line);
            }
        } catch (ToxOtisException cl) {
            throw cl;
        } catch (IOException io) {
            throw io;
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (is != null) {
                is.close();
            }
        }

        return list;
    }

    public String getResponseText() throws ToxOtisException, java.io.IOException {
        if (con == null) {
            con = initializeConnection(vri.toURI());
        }
        java.io.InputStream is = null;
        java.io.BufferedReader reader = null;
        java.io.InputStreamReader isr = null;
        try {
            is = getRemoteStream();
            isr = new java.io.InputStreamReader(is);
            reader = new java.io.BufferedReader(isr);
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return new String(sb);
        } catch (ToxOtisException cl) {
            throw cl;
        } catch (IOException io) {
            throw io;
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }

    public com.hp.hpl.jena.ontology.OntModel getResponseOntModel() throws ToxOtisException {
        try {
            OntModel om = new SimpleOntModelImpl();
            om.read(vri.toString(), null);
            return om;
        } catch (final Exception ex) {
            throw new ToxOtisException("Cannot read OntModel from " + vri.toString(), ex);
        }
    }
}

