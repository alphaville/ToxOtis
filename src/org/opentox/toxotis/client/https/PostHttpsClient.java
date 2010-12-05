package org.opentox.toxotis.client.https;

import com.hp.hpl.jena.ontology.OntModel;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.IClient;
import org.opentox.toxotis.client.IPostClient;
import org.opentox.toxotis.client.RequestHeaders;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.http.PostHttpClient;
import org.opentox.toxotis.core.IStAXWritable;

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

    public PostHttpsClient() {
        super();
    }

    public PostHttpsClient(VRI vri) {
        super(vri);
    }

    public PostHttpsClient setPostable(String string, boolean binary) {
        if (binary) {
            this.postableBytes = string;
        } else {
            this.postableString = string;
        }
        return this;
    }

    /** Initialize a connection to the target URI */
    protected javax.net.ssl.HttpsURLConnection initializeConnection(final java.net.URI uri) throws ToxOtisException {
        try {
            java.net.URL target_url = uri.toURL();
            con = (javax.net.ssl.HttpsURLConnection) target_url.openConnection();
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
        } catch (IOException ex) {
            throw new ToxOtisException(ex);
        }

    }

    public String getContentType() {
        return contentType;
    }

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

    public void post() throws ToxOtisException {
        initializeConnection(vri.toURI());
        DataOutputStream wr = null;
        try {
            wr = new DataOutputStream(con.getOutputStream());
            String parametersQuery = getParametersAsQuery();
            if (parametersQuery != null && !parametersQuery.trim().isEmpty()) {
                wr.writeBytes(parametersQuery);// POST the parameters
            } else if (postableString != null && !postableString.trim().isEmpty()) {
                wr.writeChars(postableString);
            }else if (postableBytes != null && !postableBytes.trim().isEmpty()) {
                wr.writeBytes(postableBytes);
            }
            wr.flush();
            wr.close();
        } catch (final IOException ex) {
            throw new ToxOtisException("I/O Exception caught while posting the parameters", ex);
        }
    }

    public PostHttpsClient setContentType(Media media) {
        this.contentType = media.getMime();
        return this;
    }

    public PostHttpClient setPostable(OntModel model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PostHttpClient setPostable(IStAXWritable staxWritable) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PostHttpClient setPostable(File objectToPost) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Add a parameter which will be posted to the target URI. Once the parameter is
     * submitted to the PostClient, it is stored as URL-encoded using the UTF-8 encoding.
     * @param paramName Parameter name
     * @param paramValue Parameter value
     * @return This object
     * @throws NullPointerException If paramName is <code>null</code>.
     */
    public IPostClient addPostParameter(String paramName, String paramValue) throws NullPointerException {
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
}
