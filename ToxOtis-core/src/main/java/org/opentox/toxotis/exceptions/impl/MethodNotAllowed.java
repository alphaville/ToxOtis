package org.opentox.toxotis.exceptions.impl;

import org.opentox.toxotis.exceptions.IMethodNotAllowed;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class MethodNotAllowed extends ServiceInvocationException implements IMethodNotAllowed{

    private static final int HTTP_STATUS = 405;

    public MethodNotAllowed(String message, Throwable cause) {
        super(message, cause);
        setHttpStatus(HTTP_STATUS);
    }

    public MethodNotAllowed(Throwable cause) {
        super(cause);
        setHttpStatus(HTTP_STATUS);
    }

    public MethodNotAllowed(String msg) {
        super(msg);
        setHttpStatus(HTTP_STATUS);
    }

    public MethodNotAllowed() {
        super();
        setHttpStatus(HTTP_STATUS);
    }





}