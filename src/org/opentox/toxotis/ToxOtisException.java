package org.opentox.toxotis;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ToxOtisException extends Exception {

    private ErrorCause cause = null;
    private String explanation = "Unknown Cause of Exception";

    public ToxOtisException(String string, Exception ex) {
        super(string, ex);
    }

    public ErrorCause getCode() {
        return cause;
    }

    public String getExplanation() {
        return explanation;
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
        this.explanation = exaplanation;
    }

    public ToxOtisException(ErrorCause cause, Throwable throwable) {
        super(throwable);
        this.cause = cause;
    }

    public ToxOtisException(ErrorCause cause, String explanation, Throwable throwable) {
        super(cause + " - " + explanation, throwable);
        this.cause = cause;
        this.explanation = explanation;
    }

    public ToxOtisException(Throwable cause) {
        super(cause);
    }

    public ToxOtisException(String message) {
        super(message);
    }
    

    @Override
    public String toString() {
        return "(" + getCode() + ") - " + getExplanation();
    }
}
