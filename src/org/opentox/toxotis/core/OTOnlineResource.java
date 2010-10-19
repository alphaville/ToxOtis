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
import java.io.Writer;
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.client.GetClient;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 * Any OTComponent that can be available online and has a URL.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class OTOnlineResource<T extends OTOnlineResource> extends OTComponent<T> {

    /**
     * Construct an OpenTox online resource providing its URI. This constructor is
     * supposed to be invoked only be subclasses of OTOnlineResource, that is why it
     * is <code>protected</code>.
     *
     * @param uri
     *      URI of the resource.
     */
    protected OTOnlineResource(VRI uri) {
        super(uri);
    }

    /**
     * Construct a new instance of OTOnlineResource. This constructor is intended to be
     * invoked by subclasses of OTOnlineResource and also invoked the dummy constructor
     * in {@link OTComponent }.
     */
    protected OTOnlineResource() {
        super();
    }

    /**
     * Update the current component according to some remote resource. Load information
     * from the remote location as is identified by the {@link VRI uri} of the resource.
     *
     * @return
     *      An OpenTox component as an instance of <code>T</code>, i.e. of the
     *      same type with the object performing the request.
     * @throws ToxOtisException
     *      In case the Ontological Model cannot be downloaded from the specified
     *      online resource.
     * @see OTComponent#getUri()
     * @see OTComponent#setUri(org.opentox.toxotis.client.VRI)
     */
    public T loadFromRemote() throws ToxOtisException {
        return loadFromRemote(uri);
    }

    /**
     * Downloads and parses a component that is hosted on a remote location identified
     * by the URI of the underlying component as returned by the method {@link OTComponent#getUri() }.
     * The resource is downloaded as application/rdf+xml and subsequently is parsed into
     * an instance of {@link OTComponent T}.
     *
     * @param authentication
     *      Token used to authenticate the user against the SSO server and acquire
     *      permission to download the resource.
     * @return
     *      Parsed instance of the component.
     * @throws ToxOtisException
     *      A ToxOtisException is thrown in case the remote resource is unreachable,
     *      the service responds with an unexpected or error status code (500, 503, 400 etc)
     *      or other potent communication error occur during the connection or the
     *      transaction of data. A ToxOtis exception is also thrown in case of insufficient
     *      priviledges to access the resource or if the submitted token is stale or
     *      in general invalid.
     */
    public T loadFromRemote(AuthenticationToken authentication) throws ToxOtisException {
        VRI authenticatedUri = new VRI(uri);
        authenticatedUri.appendToken(authentication);
        return loadFromRemote(authenticatedUri);
    }

    /**
     * Loads an OpenTox component from a remote location identified by its {@link VRI uri} and
     * parses it into an instance of <code>T</code>. This method is protected and should be
     * implemented by all subclasses of {@link OTOnlineResource }. The method in invoked by
     * its public counterpart {@link OTOnlineResource#loadFromRemote(org.opentox.toxotis.util.aa.AuthenticationToken) }
     * which accesses the remote location providing an authentication token.
     *
     * @param vri
     *      Identifier of the location from where the component should be downloaded
     *      and parsed
     * @return
     *      Parsed instance of the component.
     * @throws ToxOtisException
     *      A ToxOtisException is thrown in case the remote resource is unreachable,
     *      the service responds with an unexpected or error status code (500, 503, 400 etc)
     *      or other potent communication error occur during the connection or the
     *      transaction of data.
     */
    protected abstract T loadFromRemote(VRI vri) throws ToxOtisException;

    /**
     * Downloads a certain representation of the compound in a specified MIME
     * type.
     * @param destination
     *      String where the data should be stored.
     * @param media
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
        download(new File(destination), media, token);
    }

    /**
     * Downloads a certain representation of the compound in a specified MIME
     * type.
     * @param destination
     *      Stream where the data should be streamed.
     * @param media
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
     * @param media
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
     * @param media
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
                    if ((line = remoteReader.readLine()) != null){
                        bufferedWriter.newLine();
                        bufferedWriter.write(line);
                    }else{
                        break;
                    }
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
                        + "incorrect credentials or due to invalid token. Error thrown by "+newUri);
            } else if (responseStatus == 401) {
                throw new ToxOtisException(ErrorCause.UnauthorizedUser,
                        "The client is authenticated but not authorized to perform this operation at "+newUri);
            } else {
                throw new ToxOtisException(ErrorCause.UnknownCauseOfException,
                        "The remote service at "+newUri+" returned the unexpected status : " + responseStatus);
            }

        } catch (IOException ex) {
            throw new ToxOtisException("Remote stream from '" + newUri.getStringNoQuery() + "' is not readable!", ex);
        }
    }
}
