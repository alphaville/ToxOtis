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

    private int errorCode;
    private String actor;
    private String message;
    private String details;
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

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected ErrorReport loadFromRemote(VRI uri) throws ToxOtisException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
