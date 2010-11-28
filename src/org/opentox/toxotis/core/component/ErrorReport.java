package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import java.net.URISyntaxException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;

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
public class ErrorReport extends OTComponent<ErrorReport> {

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
    private UUID uuid = UUID.randomUUID();
    private static final String DISCRIMINATOR = "error";

    public ErrorReport() {
    }

    @Override
    public VRI getUri() {
        if (uri == null) {
            uri = Services.anonymous().augment(DISCRIMINATOR, uuid.toString());
        }
        return uri;
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
        if (getUri() == null) {
            try {
                long hc = (long) hashCode();
                long sgn = (long) Math.signum((double) hc);
                sgn = (sgn == 1L) ? 1L : 2L;
                hc *= sgn;
                hc = Math.abs(hc);
                String URI = "http://opentox.org/errorReport/#" + hc;
                setUri(new VRI(URI));
            } catch (URISyntaxException ex) {
                Logger.getLogger(ErrorReport.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            }
        }
        Individual indiv = model.createIndividual(getUri() != null ? getUri().getStringNoQuery()
                : null, OTClasses.ErrorReport().inModel(model));
        if (getMeta() == null) {
            setMeta(new MetaInfoImpl());
        }
        getMeta().addIdentifier(getUri() != null ? getUri().toString() : null);
        getMeta().addTitle("Error report produced by '" + getActor() + "'");
        getMeta().attachTo(indiv, model);
        if (message != null) {
            indiv.addLiteral(OTDatatypeProperties.message().asDatatypeProperty(model),
                    model.createTypedLiteral(message, XSDDatatype.XSDstring));
        }
        if (details != null) {
            indiv.addLiteral(OTDatatypeProperties.details().asDatatypeProperty(model),
                    model.createTypedLiteral(details, XSDDatatype.XSDstring));
        }
        if (actor != null) {
            indiv.addLiteral(OTDatatypeProperties.actor().asDatatypeProperty(model),
                    model.createTypedLiteral(actor, XSDDatatype.XSDstring));
        }
        if (errorCode != null) {
            indiv.addLiteral(OTDatatypeProperties.errorCode().asDatatypeProperty(model),
                    model.createTypedLiteral(errorCode, XSDDatatype.XSDstring));
        }
        if (httpStatus != 0) {
            indiv.addLiteral(OTDatatypeProperties.httpStatus().asDatatypeProperty(model),
                    model.createTypedLiteral(httpStatus, XSDDatatype.XSDint));
        }
        return indiv;
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

    @Override
    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
