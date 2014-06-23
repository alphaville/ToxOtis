package org.opentox.toxotis.database;

import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 * @param <T>
 */
public interface IDbIterator<T> {

    /**
     * Closes this stream and releases any system resources associated
     * with it. If the stream is already closed then invoking this
     * method has no effect.
     *
     * @throws org.opentox.toxotis.database.exception.DbException
     */
    public void close() throws DbException;

    /**
     * Returns <tt>true</tt> if the iteration has more elements. (In other
     * words, returns <tt>true</tt> if <tt>next</tt> would return an element
     * rather than throwing an exception.)
     *
     * @return <tt>true</tt> if the iterator has more elements.
     * @throws org.opentox.toxotis.database.exception.DbException
     */
    boolean hasNext() throws DbException;

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration.
     * @throws org.opentox.toxotis.database.exception.DbException
     */
    T next() throws DbException;
    /**
     *
     * Removes from the underlying collection the last element returned by the
     * iterator (optional operation).  This method can be called only once per
     * call to <tt>next</tt>.  The behavior of an iterator is unspecified if
     * the underlying collection is modified while the iteration is in
     * progress in any way other than by calling this method.
     *
     * @exception UnsupportedOperationException if the <tt>remove</tt>
     *		  operation is not supported by this Iterator.

     * @exception IllegalStateException if the <tt>next</tt> method has not
     *		  yet been called, or the <tt>remove</tt> method has already
     *		  been called after the last call to the <tt>next</tt>
     *		  method.
     * 
     * @throws org.opentox.toxotis.database.exception.DbException
     */
    void remove() throws DbException;

    

}