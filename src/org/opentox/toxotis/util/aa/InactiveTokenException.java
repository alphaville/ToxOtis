package org.opentox.toxotis.util.aa;

import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.core.component.ErrorReport;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class InactiveTokenException extends ToxOtisException {

    /**
     * Creates a new instance of <code>InactiveTokenException</code> without detail message.
     */
    public InactiveTokenException() {
    }

    /**
     * Constructs an instance of <code>InactiveTokenException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public InactiveTokenException(String msg) {
        super(msg);
    }

    public InactiveTokenException(ErrorCause cause, String message, ErrorReport remoteErrorReport) {
        super(cause, message, remoteErrorReport);
    }

    public InactiveTokenException(String message, ErrorReport remoteErrorReport) {
        super(message, remoteErrorReport);
    }

    public InactiveTokenException(Throwable cause) {
        super(cause);
    }

    public InactiveTokenException(ErrorCause cause, String explanation, Throwable throwable) {
        super(cause, explanation, throwable);
    }

    public InactiveTokenException(ErrorCause cause, Throwable throwable) {
        super(cause, throwable);
    }

    public InactiveTokenException(ErrorCause cause, String exaplanation) {
        super(cause, exaplanation);
    }

    public InactiveTokenException(String string, Exception ex) {
        super(string, ex);
    }
}
