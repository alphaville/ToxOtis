package org.opentox.toxotis.client;

import com.hp.hpl.jena.ontology.OntModel;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.collection.Media;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class PostClient extends AbstractClient {

    /** Type of the posted content*/
    private String contentType = null;
    /** Parameters to be posted as application/x-www-form-urlencoded (if any) */
    private Map<String, String> postParameters = new HashMap<String, String>();
    /** The method that this client applies */
    public static final String METHOD = "POST";
    private OntModel model;
    /** Arbitrary object to be posted to the remote server s*/
    private File fileContentToPost = null;
    /** */
    private String stringToPost;

    public PostClient() {
        super();
    }

    public PostClient(VRI vri) {
        super();
        this.vri = vri;
    }

    public String getContentType() {
        return contentType;
    }

    public PostClient setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public PostClient setContentType(Media media) {
        this.contentType = media.getMime();
        return this;
    }

    /**
     * Set an ontological data model which is to be posted to the remote location
     * as application/rdf+xml. Invokations of this method set automatically the content-type
     * to application/rdf+xml though it can be overriden afterwards.
     * @param model
     * @return
     */
    public PostClient setPostable(OntModel model) {
        this.model = model;
        return this;
    }

    /**
     * Set a file whose contents are to be posted to the remote server specified
     * in the constructor of this class. If the file is not found under the specified
     * path, an IllegalArgumentException is thrown. Because the type of the file is
     * in general unknown and it is not considered to be a good practise to deduce the
     * file type from the file extension, it is up to the user to specify the content
     * type of the posted object using the method {@link PostClient#setContentType(java.lang.String)
     * setContentType}. Since it is not possible to POST entities of different content
     * types to an HTTP server, any invokation to this method will override any previous
     * invokation of {@link PostClient#setPostable(com.hp.hpl.jena.ontology.OntModel)
     * setPostable(OntModel) } and {@link PostClient#setPostable(java.lang.String)
     * setPostable(String)}.
     *
     * @param objectToPost
     *      File whose contents are to be posted.
     * @return
     *      This post client
     * @throws IllegalArgumentException
     *      In case the provided file does not exist
     */
    public PostClient setPostable(File objectToPost) {
        if (objectToPost != null && !objectToPost.exists()) {
            throw new IllegalArgumentException(new FileNotFoundException("No file was found at the specified path!"));
        }
        this.fileContentToPost = objectToPost;
        return this;
    }

    /**
     * Provide a POSTable object as a string. Keep in mind that the this string will
     * <b>not</b> be URL-Encoded or by any means modified prior to the POST operation.
     * It is also up to the user to specify a proper Content-type.
     * @param string
     *      String representation of an entity to be posted to a remote server.
     * @return
     *      This post client with an updated value of the postable object.
     * @see Media Collection of Media Types
     */
    public PostClient setPostable(String string) {
        this.stringToPost = string;
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
    public PostClient addPostParameter(String paramName, String paramValue) throws NullPointerException {
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
    public AbstractClient addHeaderParameter(String paramName, String paramValue) throws NullPointerException, IllegalArgumentException {
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
                setContentType(MEDIATYPE_FORM_URL_ENCODED);
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
     * According to the the configuration of the PostClient, permorms a remote POST
     * request to the server identified by the URI provided in the contructor. First,
     * the protected method {@link PostClient#initializeConnection(java.net.URI)
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
            } else if (stringToPost != null) {
                wr.writeChars(stringToPost);
            } else if (fileContentToPost != null) {
                FileReader fr = new FileReader(fileContentToPost);
                BufferedReader br = new BufferedReader(fr);
                String line;
                while ((line = br.readLine()) != null) {
                    wr.writeBytes(line);
                    wr.writeChars("\n");
                }
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
                    throw new ToxOtisException(thr);
                }
            }
            wr.flush();
            wr.close();
        } catch (final IOException ex) {
            throw new ToxOtisException("I/O Exception caught while posting the parameters", ex);
        }
    }
}
