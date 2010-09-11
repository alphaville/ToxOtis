package org.opentox.toxotis.core;

import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class OTOnlineResource<T extends OTComponent> extends OTComponent<T> {

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
    public abstract T loadFromRemote() throws ToxOtisException;
}
