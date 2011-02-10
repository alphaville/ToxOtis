package org.opentox.toxotis.database;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public abstract class DbIterator<T> implements IDbIterator<T> {

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
    
}
