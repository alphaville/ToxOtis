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

import java.io.InputStream;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import org.opentox.toxotis.client.IPostClient;
import com.hp.hpl.jena.ontology.OntModel;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.opentox.toxotis.client.AbstractClient;
import org.opentox.toxotis.client.RequestHeaders;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.core.IStAXWritable;
import org.opentox.toxotis.exceptions.impl.ConnectionException;
import org.opentox.toxotis.exceptions.impl.InternalServerError;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;

/**
 * A client used to perform POST operations. It is used to perform POST requests in
 * a configurable way allowing users to specify the POSTed object and the various
 * header parameters.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class PostHttpClient extends AbstractHttpClient implements IPostClient {

    //TODO: Add postable InputStream (What else?)
    /** Type of the posted content*/
    private String contentType = null;
    /** Parameters to be posted as application/x-www-form-urlencoded (if any) */
    //TODO: Convert into a Map<String, List<String>> postParameters;
    private Map<String, List<String>> postParameters = new LinkedHashMap<String, List<String>>();
    private OntModel model;
    /** Arbitrary object to be posted to the remote server s*/
    private File fileContentToPost = null;
    private InputStream inputStream = null;
    /** A simple string to be posted to the remote service */
    private String stringToPost;
    private String bytesToPost;
    /** A StAX component that implements the interface {@link IStAXWritable }
    that will be posted to the remote server via the method {@link IStAXWritable#writeRdf(java.io.OutputStream)
    write(OutputStream)} that writes the component to an output stream pointing to the
    remote stream
     */
    private IStAXWritable staxComponent;
    public WriteLock postLock = new ReentrantReadWriteLock().writeLock();

    public PostHttpClient() {
        super();
    }

    public PostHttpClient(VRI vri) {
        super();
        this.vri = vri;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public PostHttpClient setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    @Override
    public PostHttpClient setContentType(Media media) {
        this.contentType = media.getMime();
        return this;
    }

    /**
     * Set an ontological data model which is to be posted to the remote location
     * as application/rdf+xml. Invocations of this method set automatically the content-type
     * to application/rdf+xml though it can be overridden afterwards.
     * @param model
     *      Ontological Model to be posted
     * @return
     *      The PostHttpClient with the updated Ontological Model.
     */
    @Override
    public PostHttpClient setPostable(OntModel model) {
        this.model = model;
        return this;
    }

    /**
     * Set a StAX-writeable component to be posted to the remote location
     * @param staxWritable
     *      A StAX component that implements the interface {@link IStAXWritable }
     *      that will be posted to the remote server via the method {@link IStAXWritable#writeRdf(java.io.OutputStream)
     *      write(OutputStream)} that writes the component to an outputstream pointing to the remote stream
     * @return
     *      The PostHttpClient with the updated writeable component.
     */
    @Override
    public PostHttpClient setPostable(IStAXWritable staxWritable) {
        this.staxComponent = staxWritable;
        return this;
    }

    /**
     * Set a file whose contents are to be posted to the remote server specified
     * in the constructor of this class. If the file is not found under the specified
     * path, an IllegalArgumentException is thrown. Because the type of the file is
     * in general unknown and it is not considered to be a good practise to deduce the
     * file type from the file extension, it is up to the user to specify the content
     * type of the posted object using the method {@link PostHttpClient#setContentType(java.lang.String)
     * setContentType}. Since it is not possible to POST entities of different content
     * types to an HTTP server, any invokation to this method will override any previous
     * invokation of {@link PostHttpClient#setPostable(com.hp.hpl.jena.ontology.OntModel)
     * setPostable(OntModel) } and {@link PostHttpClient#setPostable(java.lang.String, boolean) 
     * setPostable(String)}.
     *
     * @param objectToPost
     *      File whose contents are to be posted.
     * @return
     *      This post client
     * @throws IllegalArgumentException
     *      In case the provided file does not exist
     */
    @Override
    public PostHttpClient setPostable(File objectToPost) {
        if (objectToPost != null && !objectToPost.exists()) {
            throw new IllegalArgumentException(new FileNotFoundException("No file was found at the specified path!"));
        }
        this.fileContentToPost = objectToPost;
        return this;
    }

    @Override
    public PostHttpClient setPostable(String string, boolean binary) {
        if (binary) {
            this.bytesToPost = string;
        } else {
            this.stringToPost = string;
        }
        return this;
    }

    /**
     * Add a parameter which will be posted to the target URI. Once the parameter is
     * submitted to the PostHttpClient, it is stored as URL-encoded using the UTF-8 encoding.
     * @param paramName Parameter name
     * @param paramValue Parameter value
     * @return This object
     * @throws NullPointerException If paramName is <code>null</code>.
     */
    @Override
    public PostHttpClient addPostParameter(String paramName, String paramValue) {
        if (paramName == null) {
            throw new NullPointerException("paramName must be not null");
        }
        try {
            String encodedParamName = URLEncoder.encode(paramName, URL_ENCODING);
            String encodedParamvalue = paramValue != null ? URLEncoder.encode(paramValue, URL_ENCODING) : "";
            List<String> list = postParameters.get(encodedParamName); // check if param exists
            if (list != null) { // param exists
                list.add(encodedParamvalue);
            } else { // param does not exist
                list = new ArrayList<String>();
                list.add(encodedParamvalue);
                postParameters.put(encodedParamName, list);
            }
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        return this;
    }

    @Override
    public AbstractClient addHeaderParameter(String paramName, String paramValue) {
        if (paramName == null) {
            throw new NullPointerException("ParamName is null");
        }
        if (paramValue == null) {
            throw new NullPointerException("ParamValue is null");
        }
        if (RequestHeaders.ACCEPT.equalsIgnoreCase(paramName)) {
            setMediaType(paramValue);
            return this;
        }
        if (RequestHeaders.CONTENT_TYPE.equalsIgnoreCase(paramName)) {
            setContentType(paramValue);
            return this;
        }
        return super.addHeaderParameter(paramName, paramValue);
    }

    String getParametersAsQuery() {
        if (postParameters.isEmpty()) {
            return "";
        }
        StringBuilder string = new StringBuilder();
        final int nParams = postParameters.size();

        if (nParams > 0) {
            for (Map.Entry<String, List<String>> e : postParameters.entrySet()) {
                List<String> values = e.getValue();
                for (String value : values) {
                    string.append(e.getKey());
                    string.append("=");
                    if (e.getValue() != null) {
                        string.append(value);
                    }
                    string.append("&");
                }
            }
        }
        string.deleteCharAt(string.length() - 1);
        return new String(string);
    }

    /** Initialize a connection to the target URI */
    @Override
    protected java.net.HttpURLConnection initializeConnection(final java.net.URI uri) throws InternalServerError, ConnectionException {
        try {
            java.net.HttpURLConnection.setFollowRedirects(true);
            java.net.URL target = uri.toURL();
            con = (java.net.HttpURLConnection) target.openConnection();
            con.setRequestMethod(METHOD);
            con.setAllowUserInteraction(false);
            con.setDoInput(true);
            con.setDoOutput(true); // allow data to be posted
            con.setUseCaches(false);
            if (contentType != null) {
                con.setRequestProperty(RequestHeaders.CONTENT_TYPE, contentType);
            }
            if (acceptMediaType != null) {
                con.setRequestProperty(RequestHeaders.ACCEPT, acceptMediaType);
            }
            if (!headerValues.isEmpty()) {
                for (Map.Entry<String, String> e : headerValues.entrySet()) {
                    con.setRequestProperty(e.getKey(), e.getValue());// These are already URI-encoded!
                }
            }
            /* If there are some parameters to be posted, then the POST will
             * declare the posted data as application/x-form-urlencoded.
             */
            if (!postParameters.isEmpty()) {
                setContentType(Media.APPLICATION_FORM_URL_ENCODED);
                con.setRequestProperty(RequestHeaders.CONTENT_TYPE, contentType);
                con.setRequestProperty(RequestHeaders.CONTENT_LENGTH,
                        Integer.toString(getParametersAsQuery().getBytes().length));
            }
            return con;
        } catch (final IOException ex) {
            throw new ConnectionException("Unable to connect to the remote service at '" + getUri() + "'", ex);
        } catch (final Exception unexpectedException) {
            throw new InternalServerError("Unexpected condition while attempting to "
                    + "establish a connection to '" + uri + "'", unexpectedException);
        }
    }

    /**
     * According to the the configuration of the PostHttpClient, performs a remote POST
     * request to the server identified by the URI provided in the constructor. First,
     * the protected method {@link PostHttpClient#initializeConnection(java.net.URI)
     * initializeConnection(URI)} is invoked and then a DataOutputStream opens to
     * transfer the data to the server.
     *
     * @throws ToxOtisException
     *      Encapsulates an IOException which might be thrown due to I/O errors
     *      during the data transaction.
     */
    @Override
    public void post() throws ServiceInvocationException {
        connect(vri.toURI());
        DataOutputStream wr;
        try {
            getPostLock().lock(); // LOCK
            wr = new DataOutputStream(con.getOutputStream());
            String query = getParametersAsQuery();
            if (query != null && !query.isEmpty()) {
                wr.writeBytes(getParametersAsQuery());// POST the parameters
            } else if (model != null) {
                model.write(wr);
            } else if (staxComponent != null) {
                staxComponent.writeRdf(wr);
            } else if (stringToPost != null) {
                wr.writeChars(stringToPost);
            } else if (bytesToPost != null) {
                wr.writeBytes(bytesToPost);
            } else if (fileContentToPost != null || inputStream != null) {
                // Post from file (binary data) or from other input stream
                InputStream is = null;
                InputStreamReader isr = null;
                BufferedReader br = null;
                boolean doCloseInputStream = false;
                // choose input stream (used-defined or from file)
                if (fileContentToPost != null) {
                    is = new FileInputStream(fileContentToPost);
                    doCloseInputStream = true;
                } else {
                    is = inputStream;
                }
                try {
                    isr = new InputStreamReader(is);
                    br = new BufferedReader(isr);
                    String line;
                    while ((line = br.readLine()) != null) {
                        wr.writeBytes(line);
                        wr.writeChars("\n");
                    }
                } catch (IOException ex) {
                    throw new ConnectionException(
                            "Unable to post data to the remote service at '"
                            + getUri() + "' - The connection dropped "
                            + "unexpectidly while POSTing.", ex);
                } finally {
                    Throwable thr = null;
                    if (br != null) {
                        try {
                            br.close();
                        } catch (final IOException ex) {
                            thr = ex;
                        }
                    }
                    if (isr != null) {
                        try {
                            isr.close();
                        } catch (final IOException ex) {
                            thr = ex;
                        }
                    }
                    if (doCloseInputStream) {
                        try {
                            is.close();
                        } catch (final IOException ex) {
                            thr = ex;
                        }
                    }
                    if (thr != null) {
                        ConnectionException connExc = new ConnectionException("Stream could not close", thr);
                        connExc.setActor(getUri() != null ? getUri().toString() : "N/A");
                    }
                }
            }
            wr.flush();
            wr.close();
        } catch (final IOException ex) {
            ConnectionException postException = new ConnectionException("Exception caught while posting the parameters to the "
                    + "remote web service located at '" + getUri() + "'", ex);
            postException.setActor(getUri() != null ? getUri().toString() : "N/A");
            throw postException;
        } finally {
            getPostLock().unlock(); // UNLOCK
        }
    }

    @Override
    public WriteLock getPostLock() {
        return postLock;
    }

    @Override
    public IPostClient setPostable(InputStream inputStream) {
        this.inputStream = inputStream;
        return this;
    }
}
