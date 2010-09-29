package org.opentox.toxotis.core;

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
    public  T loadFromRemote() throws ToxOtisException{
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
    public T loadFromRemote(AuthenticationToken authentication) throws ToxOtisException{
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

    
}
