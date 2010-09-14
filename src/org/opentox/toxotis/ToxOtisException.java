package org.opentox.toxotis;

import org.opentox.toxotis.core.ErrorReport;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ToxOtisException extends Exception {

    private ErrorCause cause = null;
    private ErrorReport remoteErrorReport;

    public ToxOtisException(String string, Exception ex) {
        super(string, ex);
    }

    public ErrorCause getCode() {
        return cause;
    }

    /**
     * Creates a new instance of <code>ToxOtisException</code> without detail message.
     */
    public ToxOtisException() {
        super();
    }

    /**
     * Constructs an instance of <code>ToxOtisException</code> with the specified detail message.
     * @param exaplanation the detail message.
     */
    public ToxOtisException(ErrorCause cause, String exaplanation) {
        super(cause + " - " + exaplanation);
        this.cause = cause;
    }

    public ToxOtisException(ErrorCause cause, Throwable throwable) {
        super(throwable);
        this.cause = cause;
    }

    public ToxOtisException(ErrorCause cause, String explanation, Throwable throwable) {
        super(cause + " - " + explanation, throwable);
        this.cause = cause;
    }

    public ToxOtisException(Throwable cause) {
        super(cause);
    }

    public ToxOtisException(String message) {
        super(message);
    }

    public ToxOtisException(String message, ErrorReport remoteErrorReport) {
        super(message);
        this.remoteErrorReport = remoteErrorReport;
    }

    public ToxOtisException(ErrorCause cause, String message, ErrorReport remoteErrorReport){
        this(cause, message);
        this.remoteErrorReport = remoteErrorReport;
    }

    public ErrorReport getRemoteErrorReport() {
        return remoteErrorReport;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (getCause() != null) {
            builder.append("[");
            builder.append(getCause());
            builder.append("] - ");
        }
        builder.append(getMessage());
        return new String(builder);
    }
}
