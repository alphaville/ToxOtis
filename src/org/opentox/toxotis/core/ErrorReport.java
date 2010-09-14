package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;

/**
 * Error Reports are part of the OpenTox API since version 1.1. Error Reports define a
 * formal way to handle exceptional situations while invoking a service or during
 * inter-service communication thus facilitating debugging. They are sufficiently
 * documented online at <a href="http://opentox.org/dev/apis/api-1.1/Error%20Reports">
 * http://opentox.org/dev/apis/api-1.1/Error Reports</a>.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ErrorReport extends OTOnlineResource<ErrorReport> {

    /** The HTTP status that accompanied the Error Report */
    private int httpStatus;
    /** The peer that threw the exception or reported an exceptional event */
    private String actor;
    /** Brief explanatory message */
    private String message;
    /** Technical Details */
    private String details;
    /** Error Cause Identification Code */
    private String errorCode;
    /** Trace... */
    private ErrorReport errorCause;

    public ErrorReport() {
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public ErrorReport getErrorCause() {
        return errorCause;
    }

    public void setErrorCause(ErrorReport errorCause) {
        this.errorCause = errorCause;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int errorCode) {
        this.httpStatus = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected ErrorReport loadFromRemote(VRI uri) throws ToxOtisException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (uri != null) {
            builder.append("URI    : ");
            builder.append(uri);
            builder.append("\n");
        }
        if (actor != null) {
            builder.append("Actor  : ");
            builder.append(actor);
            builder.append("\n");
        }
        if (errorCode != null) {
            builder.append("Code   : ");
            builder.append(errorCode);
            builder.append("\n");
        }
        if (httpStatus != 0) {
            builder.append("Status : ");
            builder.append(httpStatus);
            builder.append("\n");
        }

        return new String(builder);
    }
}
