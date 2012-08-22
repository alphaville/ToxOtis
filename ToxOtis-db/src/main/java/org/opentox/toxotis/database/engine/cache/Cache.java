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

    public Cache() {
        objects = Collections.synchronizedMap(new HashMap<K, T>());
    }

    @Override
    public void put(K id, T object) throws ClassCastException, IllegalArgumentException, NullPointerException {
        synchronized (objects) {
            this.objects.put(id, object);
        }
    }

    @Override
    public <R extends T> R get(K id) throws NullPointerException, ClassCastException {
        synchronized (objects) {
            return (R) this.objects.get(id);
        }
    }

    @Override
    public boolean isEmpty() {
        return objects.isEmpty();
    }
}
