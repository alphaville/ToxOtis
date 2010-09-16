package org.opentox.toxotis.core;

import com.hp.hpl.jena.query.Dataset;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.Feature;
import org.opentox.toxotis.util.spiders.TypedValue;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface IConformer {

    public TypedValue getProperty(VRI uri) throws ToxOtisException;
    public TypedValue getProperty(Feature feature) throws ToxOtisException;
    public Dataset getProperties(Feature... features) throws ToxOtisException;
}
