package org.opentox.toxotis.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.GetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 * <p align=justify>
 * An OpenTox component is <code>OTPublishable</code> if it is a class of online resources
 * that can be created by clients,  i.e. a client can perform a POST request on a service
 * and obtain a URI for its resource. Then it will be available online on the corresponding server
 * to which it was posted.
 * </p>
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class OTPublishable<T extends OTPublishable> extends OTOnlineResource<T> {

    /**
     * Create a new empty publishable object
     */
    public OTPublishable() {
    }

    /**
     * Create a new publishable object providing its URI
     * @param uri
     *      URI of the publishable object
     */
    public OTPublishable(VRI uri) {
        super(uri);
    }

    /**
     * Publish the component to a proper server identified by the uri of the
     * publishing service provided in this method. The resource will be posted to the
     * server in RDF format (application/rdf+xml).
     * @param vri
     *      URI of the service that is responsible for the publication of
     *      this kind of resources.
     * @param token
     *      Provide an authentication token. If you think that the service does not
     *      require auhtentication/authorization, you can leave this field <code>null</code> or
     *      you can provide an empty authentication token. If the provided URI
     *      already contains an authentication token (as the URL parameter <code>
     *      tokenid</code>) it will be replaced by the new token provided to
     *      this method.
     * @return
     *      A Task for monitoring the progress of your request. If the service
     *      returns the URI of the resource right away and does not return a task,
     *      then the object you will receive from this method will now have an identifier,
     *      its status will be set to {@link Task.Status#COMPLETED }, its progress
     *      will be set to <code>100%</code> and the URI of the created resource will
     *      be available applying the method {@link Task#getResultUri() } on the returned
     *      task. In any case, the service's response will be wrapped in a {@link Task }
     *      object.
     * @throws ToxOtisException
     *      In case of invalid credentials, if the POSTed resource is not acceptable
     *      by the remote service (returns a status code 400), communication error
     *      occur with the remote server or other connection problems or the access
     *      to the service was denied (401 or 403).
     */
    public abstract Task publishOnline(VRI vri, AuthenticationToken token) throws ToxOtisException;

    /**
     * Publish the component to a standard server. The resource will be posted to the
     * server in RDF format (application/rdf+xml). If you want to specify at which
     * server the resource should be posted, use the {@link
     * OTPublishable#publishOnline(org.opentox.toxotis.client.VRI, org.opentox.toxotis.util.aa.AuthenticationToken) other method}.
     * @param token
     *      Provide an authentication token. If you think that the service does not
     *      require auhtentication/authorization, you can leave this field <code>null</code> or
     *      you can provide an empty authentication token.If the provided URI
     *      already contains an authentication token (as the URL parameter <code>
     *      tokenid</code>) it will be replaced by the new token provided to
     *      this method.
     * @return
     *      A Task for monitoring the progress of your request. If the service
     *      returns the URI of the resource right away and does not return a task,
     *      then the object you will receive from this method will now have an identifier,
     *      its status will be set to {@link Task.Status#COMPLETED }, its progress
     *      will be set to <code>100%</code> and the URI of the created resource will
     *      be available applying the method {@link Task#getResultUri() } on the returned
     *      task. In any case, the service's response will be wrapped in a {@link Task }
     *      object.
     * @throws ToxOtisException
     *      In case of invalid credentials, if the POSTed resource is not acceptable
     *      by the remote service (returns a status code 400), communication error
     *      occur with the remote server or other connection problems or the access
     *      to the service was denied (401 or 403).
     * @see OTPublishable#publishOnline(org.opentox.toxotis.client.VRI, org.opentox.toxotis.util.aa.AuthenticationToken) alternative method
     */
    public abstract Task publishOnline(AuthenticationToken token) throws ToxOtisException;

    /**
     * Downloads a certain representation of the compound in a specified MIME
     * type.
     * @param destination
     *      String where the data should be stored.
     * @param fileType
     *      Content type of the downloaded representation
     * @param token
     *      Token used for authenticating the client against the remote compound
     *      service (You can set it to <code>null</code>).
     * @throws ToxOtisException
     *      In case an authentication error occurs or the remote service responds
     *      with an error code like 500 or 503 or the submitted representation is
     *      syntactically or semantically wrong (status 400).
     * @see Media Collection of MIMEs
     */
    public void download(String destination, Media media, AuthenticationToken token) throws ToxOtisException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        download(stream, media, token);
        

        try {
            stream.close();
        } catch (IOException ex) {
            throw new ToxOtisException(ex);
        }
    }

    /**
     * Downloads a certain representation of the compound in a specified MIME
     * type.
     * @param destination
     *      Stream where the data should be streamed.
     * @param fileType
     *      Content type of the downloaded representation
     * @param token
     *      Token used for authenticating the client against the remote compound
     *      service (You can set it to <code>null</code>).
     * @throws ToxOtisException
     *      In case an authentication error occurs or the remote service responds
     *      with an error code like 500 or 503 or the submitted representation is
     *      syntactically or semantically wrong (status 400).
     * @see Media Collection of MIMEs
     */
    public void download(OutputStream destination, Media media, AuthenticationToken token) throws ToxOtisException {
        OutputStreamWriter writer = new OutputStreamWriter(destination);
        download(writer, media, token);
        try {
            writer.close();
        } catch (IOException ex) {
            throw new ToxOtisException(ex);
        }
    }

    /**
     * Downloads a certain representation of the compound in a specified MIME
     * type.
     * @param destination
     *      File where the data should be stored.
     * @param fileType
     *      Content type of the downloaded representation
     * @param token
     *      Token used for authenticating the client against the remote compound
     *      service (You can set it to <code>null</code>).
     * @throws ToxOtisException
     *      In case an authentication error occurs or the remote service responds
     *      with an error code like 500 or 503 or the submitted representation is
     *      syntactically or semantically wrong (status 400).
     * @see Media Collection of MIMEs
     */
    public void download(File destination, Media media, AuthenticationToken token) throws ToxOtisException {
        FileWriter writer = null;
        try {
            writer = new FileWriter(destination);
        } catch (IOException ex) {
            throw new ToxOtisException(ex);
        }
        download(writer, media, token);
        try {
            writer.close();
        } catch (IOException ex) {
            throw new ToxOtisException(ex);
        }
    }

    /**
     * Downloads a certain representation of the compound in a specified MIME
     * type.
     * @param destination
     *      Writer where the data should be written.
     * @param fileType
     *      Content type of the downloaded representation
     * @param token
     *      Token used for authenticating the client against the remote compound
     *      service (You can set it to <code>null</code>).
     * @throws ToxOtisException
     *      In case an authentication error occurs or the remote service responds
     *      with an error code like 500 or 503 or the submitted representation is
     *      syntactically or semantically wrong (status 400).
     * @see Media Collection of MIMEs
     */
    public void download(Writer destination, Media media, AuthenticationToken token) throws ToxOtisException {
        VRI newUri = new VRI(getUri());
        if (token != null) {
            newUri.clearToken().appendToken(token);
        }
        GetClient client = new GetClient(newUri);
        client.setMediaType(media.getMime());

        try {
            int responseStatus;
            try {
                responseStatus = client.getResponseCode();
            } catch (IOException ex) {
                throw new ToxOtisException(ex);
            }
            if (responseStatus == 200) {
                /* REMOTE STREAM */
                InputStream remote = client.getRemoteStream();
                InputStreamReader isr = new InputStreamReader(remote);
                BufferedReader remoteReader = new BufferedReader(isr);

                /* LOCAL STREAM */
                BufferedWriter bufferedWriter = new BufferedWriter(destination);
                String line = null;
                while ((line = remoteReader.readLine()) != null) {
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }

                Throwable failure = null;
                if (remote != null) {
                    try {
                        remote.close();
                    } catch (Throwable th) {
                        failure = th;
                    }
                }
                if (isr != null) {
                    try {
                        isr.close();
                    } catch (Throwable th) {
                        failure = th;
                    }
                }
                if (remoteReader != null) {
                    try {
                        remoteReader.close();
                    } catch (Throwable th) {
                        failure = th;
                    }
                }
                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.flush();
                        bufferedWriter.close();
                    } catch (Throwable th) {
                        failure = th;
                    }
                }
                if (failure != null) {
                    if (failure instanceof IOException) {
                        throw new ToxOtisException(ErrorCause.StreamCouldNotClose, failure);
                    } else {
                        throw new RuntimeException(failure);
                    }
                }

            } else if (responseStatus == 403) {
                throw new ToxOtisException(ErrorCause.AuthenticationFailed,
                        "Client failed to authenticate itself against the SSO service due to "
                        + "incorrect credentials or due to invalid token");
            } else if (responseStatus == 401) {
                throw new ToxOtisException(ErrorCause.UnauthorizedUser,
                        "The client is authenticated but not authorized to perform this operation");
            } else {
                throw new ToxOtisException(ErrorCause.UnknownCauseOfException,
                        "The remote service returned the unexpected status : " + responseStatus);
            }

        } catch (IOException ex) {
            throw new ToxOtisException("Remote stream from '" + newUri.getStringNoQuery() + "' is not readable!", ex);
        }
    }
}
