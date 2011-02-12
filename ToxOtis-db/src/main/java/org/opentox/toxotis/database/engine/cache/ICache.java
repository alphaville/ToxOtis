package org.opentox.toxotis.database.engine.cache;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface ICache<K, T> {

    <R extends T> R get(K id);

    void put(K id, T object);

    boolean isEmpty();
}
