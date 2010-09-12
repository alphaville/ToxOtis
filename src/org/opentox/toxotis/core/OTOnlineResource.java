package org.opentox.toxotis.core;

import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class OTOnlineResource<T extends OTOnlineResource> extends OTComponent<T> {

    public OTOnlineResource(VRI uri) {
        super(uri);
    }

    public OTOnlineResource() {
        super();
    }

    /**
     * Update the current component according to some remote resource. Load information
     * from the remote location as is identified by the {@link VRI uri} of the resource.
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

    public T loadFromRemote(AuthenticationToken authentication) throws ToxOtisException{
        VRI authenticatedUri = new VRI(uri);
        authenticatedUri.addUrlParameter("tokenid", authentication.getToken());
        return loadFromRemote(authenticatedUri);
    }

    protected abstract T loadFromRemote(VRI vri) throws ToxOtisException;

    
}
