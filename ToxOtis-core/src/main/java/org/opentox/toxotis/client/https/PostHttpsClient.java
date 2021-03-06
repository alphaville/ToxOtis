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

import com.hp.hpl.jena.ontology.OntModel;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import org.apache.commons.lang.StringUtils;
import org.opentox.toxotis.client.IPostClient;
import org.opentox.toxotis.client.RequestHeaders;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.core.IStAXWritable;
import org.opentox.toxotis.exceptions.impl.ConnectionException;
import org.opentox.toxotis.exceptions.impl.InternalServerError;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class PostHttpsClient extends AbstractHttpsClient implements IPostClient {

    /** Type of the posted content*/
    private String contentType = null;
    /** Parameters to be posted as application/x-www-form-urlencoded (if any) */
    private Map<String, String> postParameters = new HashMap<String, String>();
    private String postableString = null;
    private String postableBytes = null;
    private OntModel model;
    /** A StAX component that implements the interface {@link IStAXWritable }
    that will be posted to the remote server via the method {@link IStAXWritable#writeRdf(java.io.OutputStream)
    write(OutputStream)} that writes the component to an outputstream pointing to the
    remote stream
     */
    private IStAXWritable staxComponent;
    /** Arbitrary object to be posted to the remote server s*/
    private File fileContentToPost = null;
    private String fileUploadFieldName = "upload";
    private String fileUploadFilename = "uploadedFile";
    private WriteLock postLock = new ReentrantReadWriteLock().writeLock();

    public PostHttpsClient() {
        super();
    }

    public PostHttpsClient(VRI vri) {
        super(vri);
    }

    @Override
    public PostHttpsClient setPostable(String string, boolean binary) {
        if (binary) {
            this.postableBytes = string;
        } else {
            this.postableString = string;
        }
        return this;
    }

    /**
     * Set an ontological data model which is to be posted to the remote location
     * as application/rdf+xml. 
     * 
     * Invocations of this method set automatically the content-type
     * to application/rdf+xml though it can be overridden afterwards.
     * @param model
     *      Ontological Model to be posted
     * @return
     *      The PostHttpClient with the updated Ontological Model.
     */
    @Override
    public PostHttpsClient setPostable(OntModel model) {
        this.model = model;
        return this;
    }

    /** Initialize a connection to the target URI */
    @Override
    protected HttpURLConnection initializeConnection(URI uri) throws ServiceInvocationException {
        try {
            java.net.URL targetUrl = uri.toURL();
            setConnection((javax.net.ssl.HttpsURLConnection) targetUrl.openConnection());
            getConnection().setRequestMethod(METHOD);
            getConnection().setDoInput(true);
            getConnection().setDoOutput(true);
            getConnection().setUseCaches(false);
            if (contentType != null) {
                getConnection().setRequestProperty(RequestHeaders.CONTENT_TYPE, contentType);
            }
            if (getMediaType() != null) {
                getConnection().setRequestProperty(RequestHeaders.ACCEPT, getMediaType());
            }
            if (!getHeaderValues().isEmpty()) {
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

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public PostHttpsClient setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    private String getParametersAsQuery() {
        if (postParameters.isEmpty()) {
            return "";
        }
        StringBuilder string = new StringBuilder();
        final int nParams = postParameters.size();
        if (nParams > 0) {
            int counter = 0;
            for (Map.Entry<String, String> e : postParameters.entrySet()) {
                string.append(e.getKey());
                string.append("=");
                if (e.getValue() != null) {
                    string.append(e.getValue());
                }
                if (counter != nParams - 1) {
                    string.append("&");
                }
                counter++;
            }
        }
        return new String(string);
    }

    @Override
    public void post() throws ServiceInvocationException {
        String query = getParametersAsQuery();
        if (fileContentToPost != null && 
            query != null && !query.isEmpty()) {
            postMultiPart();
        } else {

            connect(getUri().toURI());
            DataOutputStream wr;
            try {
                getPostLock().lock(); // LOCK
                wr = new DataOutputStream(getConnection().getOutputStream());
                
                if (query != null && !query.isEmpty()) {
                    wr.writeBytes(getParametersAsQuery());// POST the parameters
                } else if (model != null) {
                    model.write(wr);
                } else if (staxComponent != null) {
                    staxComponent.writeRdf(wr);
                } else if (postableString != null) {
                    wr.writeChars(postableString);
                } else if (postableBytes != null) {
                    wr.writeBytes(postableBytes);
                } else if (fileContentToPost != null) {
                    FileReader fr = null;
                    BufferedReader br = null;
                    try {
                        fr = new FileReader(fileContentToPost);
                        br = new BufferedReader(fr);
                        String line;
                        while ((line = br.readLine()) != null) {
                            wr.writeBytes(line);
                            wr.writeChars("\n");
                        }
                    } catch (IOException ex) {
                        throw new ConnectionException("Unable to post data to the remote service at '" + getUri() + "' - The connection dropped "
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
                        if (fr != null) {
                            try {
                                fr.close();
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
                ConnectionException postException = new ConnectionException("Exception caught while posting the parameters to the " +
                        "remote web service located at '"+getUri()+"'", ex);
                postException.setActor(getUri() != null ? getUri().toString() : "N/A");
                throw postException;
            } finally {
                getPostLock().unlock(); // UNLOCK
            }
        }
    }

    private void postMultiPart() throws ServiceInvocationException {
        String charset="UTF-8";
        String LINE_FEED = "\r\n";
        InputStream is;
        
        try {
            getPostLock().lock(); // LOCK
            
            is = new FileInputStream(fileContentToPost);
            
            // creates a unique boundary based on time stamp
            long boundaryTs = System.currentTimeMillis();
            String boundary = "===" + boundaryTs + "===";

            HttpURLConnection httpConn = connect(getUri().toURI());
            httpConn.setUseCaches(false);
            httpConn.setDoOutput(true); // indicates POST method
            httpConn.setDoInput(true);
            httpConn.setRequestProperty("Content-Type","multipart/form-data; boundary=\"" + boundary+"\"");
            httpConn.setRequestProperty("User-Agent", "CodeJava Agent");
            httpConn.setRequestProperty("Test", "Bonjour");
            
            OutputStream outputStream = httpConn.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),true);
            
            final int nParams = postParameters.size();

            if (nParams > 0) {
                
                 for (Map.Entry<String, String> e : postParameters.entrySet()) {
                     writer.append("--" + boundary).append(LINE_FEED);
                    writer.append("Content-Disposition: form-data; name=\"" + e.getKey() + "\"")
                            .append(LINE_FEED);
                    writer.append("Content-Type: text/plain; charset=" + charset).append(
                            LINE_FEED);
                    writer.append(LINE_FEED);
                    writer.append( e.getValue()).append(LINE_FEED);
                    writer.flush();
                }
            }
            
            if(is.read() >0) {
                is.reset();
                writer.append("--" + boundary).append(LINE_FEED);
                writer.append("Content-Disposition: form-data; name=\"" + fileUploadFieldName
                                + "\"; filename=\"" + fileUploadFilename + "\"").append(LINE_FEED);
                writer.append(
                        "Content-Type: "
                                + java.net.URLConnection.guessContentTypeFromName(fileUploadFilename))
                        .append(LINE_FEED);
                writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
                writer.append(LINE_FEED);
                writer.flush();

                byte[] buffer = new byte[4096];
                int bytesRead = -1;
                while ((bytesRead = is.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
                is.close();

                writer.append(LINE_FEED);
                writer.flush();  
                writer.append(LINE_FEED).flush();
            }
                    
            writer.append("--" + boundary + "--").append(LINE_FEED);
            writer.close();
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
    public PostHttpsClient setContentType(Media media) {
        this.contentType = media.getMime();
        return this;
    }

    /**
     * Set a StAX-writeable component to be posted to the remote location
     * @param staxWritable
     *      A StAX component that implements the interface {@link IStAXWritable }
     *      that will be posted to the remote server via the method {@link IStAXWritable#writeRdf(java.io.OutputStream)
     *      write(OutputStream)} that writes the component to an output stream pointing to the remote stream
     * @return
     *      The PostHttpClient with the updated writeable component.
     */
    @Override
    public PostHttpsClient setPostable(IStAXWritable staxWritable) {
        this.staxComponent = staxWritable;
        return this;
    }

    /**
     * Set a file whose contents are to be posted to the remote server specified
     * in the constructor of this class. 
     * 
     * If the file is not found under the specified
     * path, an IllegalArgumentException is thrown. Because the type of the file is
     * in general unknown and it is not considered to be a good practice to deduce the
     * file type from the file extension, it is up to the user to specify the content
     * type of the posted object using the method {@link #setContentType(java.lang.String) #setContentType}. 
     * Since it is not possible to POST entities of different content
     * types to an HTTP server, any invocation to this method will override any previous
     * invocation of {@link PostHttpsClient#setPostable(com.hp.hpl.jena.ontology.OntModel) #setPostable}
     * setPostable(OntModel) } and {@link #setPostable(java.lang.String, boolean) setPostable(String)}.
     *
     * @param objectToPost
     *      File whose contents are to be posted.
     * @return
     *      This post client
     * 
     * @throws IllegalArgumentException
     *      In case the provided file does not exist
     */
    @Override
    public PostHttpsClient setPostable(File objectToPost) {
        if (objectToPost != null && !objectToPost.exists()) {
            throw new IllegalArgumentException(new FileNotFoundException("No file was found at the specified path!"));
        }
        this.fileContentToPost = objectToPost;
        return this;
    }

    /**
     * Add a parameter which will be posted to the target URI. Once the parameter is
     * submitted to the PostClient, it is stored as URL-encoded using the UTF-8 encoding.
     * @param paramName Parameter name
     * @param paramValue Parameter value
     * @return This object
     * @throws NullPointerException If paramName is <code>null</code>.
     */
    @Override
    public IPostClient addPostParameter(String paramName, String paramValue) throws NullPointerException {
        if (paramName == null) {
            throw new NullPointerException("paramName must be not null");
        }
        try {
            postParameters.put(URLEncoder.encode(paramName, URL_ENCODING), paramValue != null ? URLEncoder.encode(paramValue, URL_ENCODING) : "");
        } catch (final UnsupportedEncodingException ex) {
            throw new IllegalArgumentException("Unsupported Encoding... Hmmm, strange!",ex);
        }
        return this;
    }

    @Override
    public WriteLock getPostLock() {
        return postLock;
    }

    @Override
    public IPostClient setPostable(InputStream model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void setPostableFilename(String fieldName,String filename) {
        this.fileUploadFilename = (StringUtils.isEmpty(filename) ) ? this.fileUploadFilename :  filename;
        this.fileUploadFieldName = (StringUtils.isEmpty(fieldName) ) ? this.fileUploadFieldName :  fieldName;
    }
}
