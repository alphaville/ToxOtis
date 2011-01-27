/*
 *
 * ToxOtis
 *
 * ToxOtis is the Greek word for Sagittarius, that actually means ‘archer’. ToxOtis
 * is a Java interface to the predictive toxicology services of OpenTox. ToxOtis is
 * being developed to help both those who need a painless way to consume OpenTox
 * services and for ambitious service providers that don’t want to spend half of
 * their time in RDF parsing and creation.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 *
 */
package org.opentox.toxotis.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;


/**
 * An invokation that will be repeated after a certain time if it fails. The invokation
 * is retried for a specified number of times.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class RetriableRequest<T> {

    private Method method;
    private Object object;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RetriableRequest.class);

    public RetriableRequest(Method method, Object object) {
        this.method = method;
        this.object = object;
    }

    private T retry(int currentTry, int maxRetries, long milliSecondsDelay, Object... methodParams) throws ToxOtisException {
        if (currentTry <= maxRetries) {
            try {
                T result = (T) method.invoke(object, methodParams);
                return result;
            } catch (IllegalAccessException ex) {
                logger.debug(null, ex);
                throw new ToxOtisException("Method " + method.getName() + " defined in " + method.getDeclaringClass().getName() + "should be accessible!", ex);
            } catch (IllegalArgumentException ex) {
                logger.debug(null,ex);
                throw new ToxOtisException("", ex);
            } catch (InvocationTargetException ex) {
                if (currentTry != maxRetries) {
                    try {
                        Thread.sleep(milliSecondsDelay);
                    } catch (InterruptedException ex1) {
                        logger.error(null, ex);
                    }
                    return retry(++currentTry, maxRetries, milliSecondsDelay, methodParams);
                } else {
                    logger.debug(null, ex);
                    throw new ToxOtisException(ex.getCause());
                }

            }
        }
        return null;
    }

    public T retry(int maxRetries, long milliSecondsDelay, Object... methodParams) throws ToxOtisException {
        return retry(1, maxRetries, milliSecondsDelay, methodParams);
    }

    public T retry(int maxRetries, long milliSecondsDelay) throws ToxOtisException {
        return retry(1, maxRetries, milliSecondsDelay);
    }
    
}
