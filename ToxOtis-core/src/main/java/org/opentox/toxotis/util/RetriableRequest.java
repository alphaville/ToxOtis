package org.opentox.toxotis.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.opentox.toxotis.ToxOtisException;

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
