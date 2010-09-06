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

    private String mediaType;
    private VRI vri;
    private java.net.HttpURLConnection con;
    private static int bufferSize = 4194304;

    public GetClient() {
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public java.net.URI getUri() {
        return vri.toURI();
    }

    public void setUri(VRI vri) {
        this.vri = vri;
    }
    

    public void setUri(String uri) throws java.net.URISyntaxException {
        this.vri = new VRI(uri);
    }

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

    public int getResponseCode() throws ToxOtisException, java.io.IOException {
        if (con == null) {
            con = initializeConnection(vri.toURI());
        }
        return con.getResponseCode();
    }

    public java.util.List<String> getUriList() throws ToxOtisException, IOException {
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

    public String getRemoteMessage() throws ToxOtisException, java.io.IOException {
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

    public com.hp.hpl.jena.ontology.OntModel getOntModel() throws ToxOtisException {
        try {
            OntModel om = new SimpleOntModelImpl();
            om.read(vri.toString(), null);
            return om;
        } catch (final Exception ex) {
            throw new ToxOtisException("Cannot read OntModel from " + vri.toString(), ex);
        }
    }
}

