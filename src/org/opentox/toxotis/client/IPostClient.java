package org.opentox.toxotis.client;

import com.hp.hpl.jena.ontology.OntModel;
import java.io.File;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.http.PostHttpClient;
import org.opentox.toxotis.core.IStAXWritable;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface IPostClient extends IClient {

    /**
     * The method that this client applies
     */
    String METHOD = "POST";

    String getContentType();

    /**
     * According to the the configuration of the PostHttpClient, permorms a remote POST
     * request to the server identified by the URI provided in the contructor. First,
     * the protected method {@link PostHttpClient#initializeConnection(java.net.URI)
     * initializeConnection(URI)} is invoked and then a DataOutputStream opens to
     * tranfer the data to the server.
     *
     * @throws ToxOtisException
     * Encapsulates an IOException which might be thrown due to I/O errors
     * during the data transaction.
     */
    void post() throws ToxOtisException;

    IPostClient setContentType(String contentType);

    IPostClient setContentType(Media media);

    /**
     * Set an ontological data model which is to be posted to the remote location
     * as application/rdf+xml. Invokations of this method set automatically the content-type
     * to application/rdf+xml though it can be overriden afterwards.
     * @param model
     * Ontological Model to be posted
     * @return
     * The PostHttpClient with the updated Ontological Model.
     */
    IPostClient setPostable(OntModel model);

    /**
     * Set a StAX-writeable component to be posted to the remote location
     * @param staxWritable
     * A StAX component that implements the interface {@link IStAXWritable }
     * that will be posted to the remote server via the method {@link IStAXWritable#writeRdf(java.io.OutputStream)
     * write(OutputStream)} that writes the component to an outputstream pointing to the remote stream
     * @return
     * The PostHttpClient with the updated writeable component.
     */
    IPostClient setPostable(IStAXWritable staxWritable);

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
     * setPostable(OntModel) } and {@link PostHttpClient#setPostable(java.lang.String)
     * setPostable(String)}.
     *
     * @param objectToPost
     * File whose contents are to be posted.
     * @return
     * This post client
     * @throws IllegalArgumentException
     * In case the provided file does not exist
     */
    IPostClient setPostable(File objectToPost);

    /**
     * Provide a POSTable object as a string. Keep in mind that the this string will
     * <b>not</b> be URL-Encoded or by any means modified prior to the POST operation.
     * It is also up to the user to specify a proper Content-type.
     * @param string
     * String representation of an entity to be posted to a remote server.
     * @return
     * This post client with an updated value of the postable object.
     * @see Media Collection of Media Types
     */
    IPostClient setPostable(String string);

    /**
     * Add a parameter which will be posted to the target URI. Once the parameter is
     * submitted to the PostHttpClient, it is stored as URL-encoded using the UTF-8 encoding.
     * @param paramName Parameter name
     * @param paramValue Parameter value
     * @return This object
     * @throws NullPointerException If paramName is <code>null</code>.
     */
    IPostClient addPostParameter(String paramName, String paramValue) throws NullPointerException;
}
