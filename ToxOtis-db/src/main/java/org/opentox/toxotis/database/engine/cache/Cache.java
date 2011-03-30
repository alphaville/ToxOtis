package org.opentox.toxotis.database.engine.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Cache<K, T> implements ICache<K, T> {

    private final Map<K, T> objects;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Cache.class);

    public Cache() {
        objects = Collections.synchronizedMap(new HashMap<K, T>());
    }

    @Override
    public void put(K id, T object) throws ClassCastException, IllegalArgumentException, NullPointerException {
        try {
            synchronized (objects) {
                this.objects.put(id, object);
            }
        } catch (ClassCastException cce) {
            final String msg = "The key is of inappropriate type for this cache";
            logger.error(msg, cce);
            throw cce;
        } catch (final NullPointerException npe) {
            final String msg = "the specified key is null and this map does not permit null keys";
            logger.error(msg, npe);
            throw npe;
        } catch (final IllegalArgumentException iae) {
            final String msg = "some property of the specified key or value prevents it from being stored in this map";
            logger.error(msg, iae);
            throw iae;
        }
    }

    @Override
    public <R extends T> R get(K id) throws NullPointerException, ClassCastException {
        try {
            synchronized (objects) {
                return (R) this.objects.get(id);
            }
        } catch (final ClassCastException cce) {
            final String msg = "The key is of inappropriate type for this cache";
            logger.error(msg, cce);
            throw cce;
        } catch (final NullPointerException npe) {
            final String msg = "the specified key is null and this map does not permit null keys";
            logger.error(msg, npe);
            throw npe;
        }
    }

    @Override
    public boolean isEmpty() {
        return objects.isEmpty();
    }
}
