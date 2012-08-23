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
package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.client.HttpStatusCodes;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.IHTMLSupport;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.core.html.Alignment;
import org.opentox.toxotis.core.html.HTMLContainer;
import org.opentox.toxotis.core.html.HTMLDivBuilder;
import org.opentox.toxotis.core.html.HTMLTable;
import org.opentox.toxotis.core.html.HTMLUtils;
import org.opentox.toxotis.core.html.impl.HTMLParagraphImpl;
import org.opentox.toxotis.core.html.impl.HTMLTextImpl;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;
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
public class ErrorReport extends OTComponent<ErrorReport>
        implements IHTMLSupport {

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
    private static final Map<Integer, String> ERROR_CODE_REFERENCE;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ErrorReport.class);

    static {
        ERROR_CODE_REFERENCE = new HashMap<Integer, String>();
        ERROR_CODE_REFERENCE.put(HttpStatusCodes.BadRequest.getStatus(),
                "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.1");// Bad request
        ERROR_CODE_REFERENCE.put(HttpStatusCodes.Unauthorized.getStatus(),
                "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.2");// Unauthorized
        ERROR_CODE_REFERENCE.put(HttpStatusCodes.Forbidden.getStatus(),
                "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.4");// Forbidden
        ERROR_CODE_REFERENCE.put(HttpStatusCodes.NotFound.getStatus(),
                "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.5");// Not found
        ERROR_CODE_REFERENCE.put(HttpStatusCodes.MethodNotAllowed.getStatus(),
                "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.6");// Method not allowed
        ERROR_CODE_REFERENCE.put(HttpStatusCodes.NotAcceptable.getStatus(),
                "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.7");// Not acceptable
        ERROR_CODE_REFERENCE.put(HttpStatusCodes.InternalServerError.getStatus(),
                "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.5.1");// Internal server error
        ERROR_CODE_REFERENCE.put(HttpStatusCodes.NotImplemented.getStatus(),
                "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.5.2");// Not implemented
        ERROR_CODE_REFERENCE.put(HttpStatusCodes.BadGateway.getStatus(),
                "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.5.3");// Bad Gateway
        ERROR_CODE_REFERENCE.put(HttpStatusCodes.ServiceUnavailable.getStatus(),
                "http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.5.4");// Service Unavailable

    }

    public ErrorReport() {
    }

    public ErrorReport(VRI uri) {
        super(uri);
    }

    public ErrorReport(int httpStatus, String actor, String message, String details, String errorCode) {
        this.httpStatus = httpStatus;
        this.actor = actor;
        this.message = message;
        this.details = details;
        this.errorCode = errorCode;
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
                logger.debug(null, ex);
                throw new IllegalArgumentException("Bad URI", ex);
            }
        }
        Individual indiv = model.createIndividual(getUri() != null ? getUri().getStringNoQuery()
                : null, OTClasses.errorReport().inModel(model));
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
        if (errorCause != null) {
            indiv.addProperty(OTObjectProperties.trace().asObjectProperty(model), errorCause.asIndividual(model));
        }
        return indiv;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (uri != null) {
            builder.append("URI     : ");
            builder.append(uri);
            builder.append("\n");
        }
        if (actor != null) {
            builder.append("Actor   : ");
            builder.append(actor);
            builder.append("\n");
        }
        if (errorCode != null) {
            builder.append("Code    : ");
            builder.append(errorCode);
            builder.append("\n");
        }
        if (httpStatus != 0) {
            builder.append("Status  : ");
            builder.append(httpStatus);
            builder.append("\n");
        }
        if (message != null) {
            builder.append("Message : ");
            builder.append(message);
            builder.append("\n");
        }
        if (details != null) {
            builder.append("Details : ");
            builder.append("\n");
            builder.append(details);
        }
        return new String(builder);
    }

    @Override
    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public HTMLContainer inHtml() {
        HTMLDivBuilder builder = new HTMLDivBuilder("jaqpot_algorithm");
        builder.addComment("Error Report Representation automatically generated by JAQPOT");
        builder.addSubHeading("Error Report").
                breakLine().horizontalSeparator();
        builder.getDiv().setAlignment(Alignment.justify).setId(getUri().toString());
        builder.addSubSubSubHeading("Information about the Exceptional Event");
        HTMLTable table = builder.addTable(2);

        table.setTextAtCursor("Report URI").setTextAtCursor(HTMLUtils.linkUrlsInText(getUri().toString())).
                setTextAtCursor("Error Code").setTextAtCursor(getErrorCode()).
                setTextAtCursor("Message").setAtCursor(new HTMLParagraphImpl(htmlNormalize(getMessage())).setAlignment(Alignment.justify)).
                setTextAtCursor("HTTP Code").setAtCursor(
                new HTMLParagraphImpl("<a href= \"" + ERROR_CODE_REFERENCE.get(getHttpStatus()) + "\" >" + Integer.toString(getHttpStatus()) + "</a>").setAlignment(Alignment.justify)).
                setTextAtCursor("Who is to Blame").setAtCursor(new HTMLParagraphImpl(htmlNormalize(getActor())).setAlignment(Alignment.justify));
        if (errorCause != null) {
            table.setTextAtCursor("Trace").setTextAtCursor(HTMLUtils.linkUrlsInText(getErrorCause().getUri().toString()));
        }
        table.setCellPadding(5).
                setCellSpacing(2).
                setTableBorder(0).
                setColWidth(1, 150).
                setColWidth(2, 550);

        builder.getDiv().breakLine();
        if (details != null) {
            builder.addSubSubSubHeading("Details that will help debugging");
            builder.addComponent(new HTMLTextImpl(htmlNormalize(getDetails())).formatPRE(true));
        }
        builder.getDiv().breakLine().horizontalSeparator();

        return builder.getDiv();
    }

    private String htmlNormalize(String in) {
        if (in == null) {
            return null;
        }
        return in.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }
}
