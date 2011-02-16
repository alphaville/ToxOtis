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
    public void put(K id, T object) {
        synchronized (objects) {
            this.objects.put(id, object);
        }
    }

    @Override
    public <R extends T> R get(K id) {
        synchronized (objects) {
            return (R) this.objects.get(id);
        }
    }

    @Override
    public boolean isEmpty() {
        return objects.isEmpty();
    }
}
