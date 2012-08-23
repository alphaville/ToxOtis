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
package org.opentox.toxotis.core;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.HttpStatusCodes;
import org.opentox.toxotis.client.IGetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.exceptions.impl.ConnectionException;
import org.opentox.toxotis.exceptions.impl.ForbiddenRequest;
import org.opentox.toxotis.exceptions.impl.NotFound;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.Unauthorized;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 * Any OTComponent that can be available online and has a URL.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class OTOnlineResource<T extends OTOnlineResource> extends OTComponent<T> implements IOnlineResource {

    private static final String XSD_DOC_PAGE_TEMPLATE = "http://www.w3.org/TR/xmlschema-2/#%s";
    /**
     * Assigns a documentation link to each XSDDataType element.
     */
    protected static final Map<String, String> XSD_DATATYPE_LINKS = new HashMap<String, String>();

    static {
        XSD_DATATYPE_LINKS.put(XSDDatatype.XSDstring.getURI(), String.format(XSD_DOC_PAGE_TEMPLATE, "string"));
        XSD_DATATYPE_LINKS.put(XSDDatatype.XSDint.getURI(), String.format(XSD_DOC_PAGE_TEMPLATE, "int"));
        XSD_DATATYPE_LINKS.put(XSDDatatype.XSDinteger.getURI(), String.format(XSD_DOC_PAGE_TEMPLATE, "integer"));
        XSD_DATATYPE_LINKS.put(XSDDatatype.XSDdouble.getURI(), String.format(XSD_DOC_PAGE_TEMPLATE, "double"));
        XSD_DATATYPE_LINKS.put(XSDDatatype.XSDdate.getURI(), String.format(XSD_DOC_PAGE_TEMPLATE, "date"));
        XSD_DATATYPE_LINKS.put(XSDDatatype.XSDboolean.getURI(), String.format(XSD_DOC_PAGE_TEMPLATE, "boolean"));
        XSD_DATATYPE_LINKS.put(XSDDatatype.XSDfloat.getURI(), String.format(XSD_DOC_PAGE_TEMPLATE, "float"));
        XSD_DATATYPE_LINKS.put(XSDDatatype.XSDtime.getURI(), String.format(XSD_DOC_PAGE_TEMPLATE, "time"));
        XSD_DATATYPE_LINKS.put(XSDDatatype.XSDlong.getURI(), String.format(XSD_DOC_PAGE_TEMPLATE, "long"));
        XSD_DATATYPE_LINKS.put(XSDDatatype.XSDshort.getURI(), String.format(XSD_DOC_PAGE_TEMPLATE, "short"));
        XSD_DATATYPE_LINKS.put(XSDDatatype.XSDanyURI.getURI(), String.format(XSD_DOC_PAGE_TEMPLATE, "anyURI"));
    }

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
     * This method assuemes that no Authentication or Authorization is required to
     * access the remote resource.
     *
     * @return
     *      An OpenTox component as an instance of <code>T</code>, i.e. of the
     *      same type with the object performing the request.
     * @throws ServiceInvocationException
     *      In case the Ontological Model cannot be downloaded from the specified
     *      online resource.
     * @see OTComponent#getUri()
     * @see OTComponent#setUri(org.opentox.toxotis.client.VRI)
     */
    public T loadFromRemote() throws ServiceInvocationException {
        return loadFromRemote(uri, null);
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
     * @throws ServiceInvocationException
     *      A ServiceInvocationException is thrown in case the remote resource is unreachable,
     *      the service responds with an unexpected or error status code (500, 503, 400 etc)
     *      or other potent communication error occur during the connection or the
     *      transaction of data. A ToxOtis exception is also thrown in case of insufficient
     *      priviledges to access the resource or if the submitted token is stale or
     *      in general invalid.
     */
    public T loadFromRemote(AuthenticationToken authentication) throws ServiceInvocationException {
        VRI authenticatedUri = new VRI(uri);
        return loadFromRemote(authenticatedUri, authentication);
    }

    /**
     * Loads an OpenTox component from a remote location identified by its {@link VRI uri} and
     * parses it into an instance of <code>T</code>. This method is protected and should be
     * implemented by all subclasses of {@link OTOnlineResource }. The method is invoked by
     * its public counterpart {@link OTOnlineResource#loadFromRemote(org.opentox.toxotis.util.aa.AuthenticationToken) }
     * which accesses the remote location providing an authentication token.
     *
     * @param vri
     *      Identifier of the location from where the component should be downloaded
     *      and parsed
     * @param token
     *      Token provided
     * @return
     *      Parsed instance of the component.
     * @throws ToxOtisException
     *      A ToxOtisException is thrown in case the remote resource is unreachable,
     *      the service responds with an unexpected or error status code (500, 503, 400 etc)
     *      or other potent communication error occur during the connection or the
     *      transaction of data.
     */
    protected abstract T loadFromRemote(VRI vri, AuthenticationToken token) throws ServiceInvocationException;

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
     * @throws ServiceInvocationException
     *      In case an authentication error occurs or the remote service responds
     *      with an error code like 500 or 503 or the submitted representation is
     *      syntactically or semantically wrong (status 400).
     * @see Media Collection of MIMEs
     */
    public void download(String destination, Media media, AuthenticationToken token) throws ServiceInvocationException {
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
    public void download(OutputStream destination, Media media, AuthenticationToken token) throws ServiceInvocationException {
        OutputStreamWriter writer = new OutputStreamWriter(destination);
        download(writer, media, token);
        try {
            writer.close();
        } catch (IOException ex) {
            throw new ConnectionException("IOException while trying to close the output stream writer", ex);
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
    public void download(File destination, Media media, AuthenticationToken token) throws ServiceInvocationException {
        FileWriter writer = null;
        try {
            writer = new FileWriter(destination);
        } catch (IOException ex) {
            throw new ServiceInvocationException(ex);
        }
        download(writer, media, token);
        try {
            writer.close();
        } catch (IOException ex) {
            throw new ServiceInvocationException(ex);
        }
    }

    /**
     *
     * @param imageMedia
     * @param token
     * @return
     *      A depiction downloaded from the remote compound service (as specified
     *      by {@link #getUri() } as a BufferedImage.
     * @throws ToxOtisException
     */
    public BufferedImage downloadImage(Media imageMedia, AuthenticationToken token) throws ServiceInvocationException {
        if (imageMedia == null) {
            imageMedia = Media.IMAGE_PNG;
        }
        if (!imageMedia.toString().contains("image")) {
            throw new ServiceInvocationException(imageMedia + " is not a valid image media type");
        }
        BufferedImage image = null;
        IGetClient client = ClientFactory.createGetClient(uri);
        client.setMediaType(imageMedia);
        client.authorize(token);
        try {
            InputStream iStream = client.getRemoteStream();
            image = ImageIO.read(iStream);
        } catch (IOException ex) {
            throw new ServiceInvocationException(ex);
        }
        return image;
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
    public void download(Writer destination, Media media, AuthenticationToken token) throws ServiceInvocationException {
        VRI newUri = new VRI(getUri());        
        IGetClient client = ClientFactory.createGetClient(newUri);
        client.setMediaType(media.getMime());
        client.authorize(token);
        try {
            int responseStatus = client.getResponseCode();

            if (responseStatus == HttpStatusCodes.Success.getStatus()) {
                /* REMOTE STREAM */
                InputStream remote = client.getRemoteStream();
                InputStreamReader isr = new InputStreamReader(remote);
                BufferedReader remoteReader = new BufferedReader(isr);

                /* LOCAL STREAM */
                BufferedWriter bufferedWriter = new BufferedWriter(destination);
                String line = null;
                while ((line = remoteReader.readLine()) != null) {
                    bufferedWriter.write(line);
                    if ((line = remoteReader.readLine()) != null) {
                        bufferedWriter.newLine();
                        bufferedWriter.write(line);
                    } else {
                        break;
                    }
                }

                Throwable failure = null;
                if (remote != null) {
                    try {
                        remote.close();
                    } catch (final Exception exc) {
                        failure = exc;
                    } catch (final Error er){
                        failure = er;
                    }
                }
                if (isr != null) {
                    try {
                        isr.close();
                    } catch (final Exception exc) {
                        failure = exc;
                    } catch (final Error er){
                        failure = er;
                    }
                }
                if (remoteReader != null) {
                    try {
                        remoteReader.close();
                    } catch (final Exception exc) {
                        failure = exc;
                    } catch (final Error er){
                        failure = er;
                    }
                }
                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.flush();
                        bufferedWriter.close();
                    } catch (final Exception exc) {
                        failure = exc;
                    } catch (final Error er){
                        failure = er;                       
                    }
                }
                if (failure != null) {
                    if (failure instanceof IOException) {
                        throw new ServiceInvocationException("Stream could not close", failure);
                    } else {
                        throw new RuntimeException(failure);
                    }
                }

            } else if (responseStatus == HttpStatusCodes.Forbidden.getStatus()) {
                throw new ForbiddenRequest(
                        "Client failed to authenticate itself against the SSO service due to "
                        + "incorrect credentials or due to invalid token. Error thrown by " + newUri);
            } else if (responseStatus == HttpStatusCodes.Unauthorized.getStatus()) {
                throw new Unauthorized(
                        "The client is authenticated but not authorized to perform this operation at " + newUri);
            } else if (responseStatus == HttpStatusCodes.BadRequest.getStatus()) {
                throw new NotFound("The compound you requested was not found at the remote location : '"+newUri+"'");
            }else {
                throw new ServiceInvocationException("The remote service at " + newUri + " returned the unexpected status : " + responseStatus);
            }

        } catch (IOException ex) {
            throw new ServiceInvocationException("Remote stream from '" + newUri.getStringNoQuery() + "' is not readable!", ex);
        }
    }
}
