package org.opentox.toxotis.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ToxOtisException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class RetriableRequest<T> {

    private Method method;
    private Object object;

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
                throw new ToxOtisException("Method " + method.getName() + " defined in " + method.getDeclaringClass().getName() + "should be accessible!", ex);
            } catch (IllegalArgumentException ex) {
                throw new ToxOtisException("", ex);
            } catch (InvocationTargetException ex) {
                if (currentTry!=maxRetries){
                try {
                    Thread.sleep(milliSecondsDelay);
                } catch (InterruptedException ex1) {
                    Logger.getLogger(RetriableRequest.class.getName()).log(Level.SEVERE, null, ex1);
                }
                return retry(++currentTry, maxRetries, milliSecondsDelay, methodParams);
                }else{
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
