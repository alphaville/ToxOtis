package org.opentox.toxotis.client.http;

import org.opentox.toxotis.client.IPostClient;
import com.hp.hpl.jena.ontology.OntModel;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.RequestHeaders;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.core.IStAXWritable;

/**
 * A client used to perform POST operations. It is used to perform POST requests in
 * a configurable way allowing users to specify the POSTed object and the various
 * header parameters.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class PostHttpClient extends AbstractHttpClient implements IPostClient {

    /** Type of the posted content*/
    private String contentType = null;
    /** Parameters to be posted as application/x-www-form-urlencoded (if any) */
    private Map<String, String> postParameters = new HashMap<String, String>();
    private OntModel model;
    /** Arbitrary object to be posted to the remote server s*/
    private File fileContentToPost = null;
    /** A simple string to be posted to the remote service */
    private String stringToPost;
    private String bytesToPost;
    /** A StAX component that implements the interface {@link IStAXWritable }
    that will be posted to the remote server via the method {@link IStAXWritable#writeRdf(java.io.OutputStream)
    write(OutputStream)} that writes the component to an outputstream pointing to the
    remote stream
     */
    private IStAXWritable staxComponent;

    public PostHttpClient() {
        super();
    }

    public PostHttpClient(VRI vri) {
        super();
        this.vri = vri;
    }

    public String getContentType() {
        return contentType;
    }

    public PostHttpClient setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public PostHttpClient setContentType(Media media) {
        this.contentType = media.getMime();
        return this;
    }

    /**
     * Set an ontological data model which is to be posted to the remote location
     * as application/rdf+xml. Invokations of this method set automatically the content-type
     * to application/rdf+xml though it can be overriden afterwards.
     * @param model
     *      Ontological Model to be posted
     * @return
     *      The PostHttpClient with the updated Ontological Model.
     */
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
    public PostHttpClient setPostable(File objectToPost) {
        if (objectToPost != null && !objectToPost.exists()) {
            throw new IllegalArgumentException(new FileNotFoundException("No file was found at the specified path!"));
        }
        this.fileContentToPost = objectToPost;
        return this;
    }

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
    public PostHttpClient addPostParameter(String paramName, String paramValue) throws NullPointerException {
        if (paramName == null) {
            throw new NullPointerException("paramName must be not null");
        }
        try {
            postParameters.put(URLEncoder.encode(paramName, URL_ENCODING), paramValue != null ? URLEncoder.encode(paramValue, URL_ENCODING) : "");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        return this;
    }

    @Override
    public AbstractHttpClient addHeaderParameter(String paramName, String paramValue) throws NullPointerException, IllegalArgumentException {
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

    /** Initialize a connection to the target URI */
    protected java.net.HttpURLConnection initializeConnection(final java.net.URI uri) throws ToxOtisException {
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
        } catch (final Exception ex) {
            throw new ToxOtisException(ex);
        }
    }

    /**
     * According to the the configuration of the PostHttpClient, permorms a remote POST
     * request to the server identified by the URI provided in the contructor. First,
     * the protected method {@link PostHttpClient#initializeConnection(java.net.URI)
     * initializeConnection(URI)} is invoked and then a DataOutputStream opens to
     * tranfer the data to the server.
     *
     * @throws ToxOtisException
     *      Encapsulates an IOException which might be thrown due to I/O errors
     *      during the data transaction.
     */
    public void post() throws ToxOtisException {
        initializeConnection(vri.toURI());
        DataOutputStream wr;
        try {
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
                        throw new ToxOtisException(ErrorCause.StreamCouldNotClose, thr);
                    }
                }
            }
            wr.flush();
            wr.close();
        } catch (final IOException ex) {
            throw new ToxOtisException("I/O Exception caught while posting the parameters", ex);
        }
    }
}
