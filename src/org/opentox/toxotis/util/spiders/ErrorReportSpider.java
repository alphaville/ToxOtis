package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.ErrorReport;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class ErrorReportSpider extends Tarantula<ErrorReport> {

    public ErrorReportSpider() {
    }

    public ErrorReportSpider(Resource resource, OntModel model) {
        super(resource, model);
    }

    /**
     * Create a new Error Spider that searches in a data model of an error
     * report for a node that has a specified actor.
     * @param uri
     * @param model
     */
    ErrorReportSpider(VRI uri, OntModel model) {
        this.model = model;
        Property actorProperty = OTDatatypeProperties.actor().asDatatypeProperty(model);
        if (actorProperty != null) {
            StmtIterator reportIt = model.listStatements(new SimpleSelector(null, actorProperty, uri.getStringNoQuery()));
            if (reportIt.hasNext()) {
                resource = reportIt.nextStatement().getSubject().as(Resource.class);
            }
        }
    }

    @Override
    public ErrorReport parse() throws ToxOtisException {
        ErrorReport errorReport = new ErrorReport();
        if (resource != null) {
            try {
                errorReport.setUri(new VRI(resource.getURI()));
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        }
        errorReport.setMeta(new MetaInfoSpider(resource, model).parse());

        Literal httpStatus = resource.getProperty(
                OTDatatypeProperties.httpStatus().asDatatypeProperty(model)).getObject().as(Literal.class);

        if (httpStatus != null) {
            errorReport.setHttpStatus(httpStatus.getInt());
        }

        Literal errorCode = resource.getProperty(
                OTDatatypeProperties.errorCode().asDatatypeProperty(model)).getObject().as(Literal.class);
        if (errorCode != null) {
            errorReport.setErrorCode(errorCode.getString());
        }

        Literal details = resource.getProperty(
                OTDatatypeProperties.details().asDatatypeProperty(model)).getObject().as(Literal.class);

        if (details != null) {
            errorReport.setDetails(details.getString());
        }

        Literal message = resource.getProperty(
                OTDatatypeProperties.message().asDatatypeProperty(model)).getObject().as(Literal.class);

        if (message != null) {
            errorReport.setMessage(message.getString());
        }

        Literal actor = resource.getProperty(
                OTDatatypeProperties.actor().asDatatypeProperty(model)).getObject().as(Literal.class);

        if (actor != null) {
            errorReport.setActor(actor.getString());
        }

        Statement traceProp = resource.getProperty(OTObjectProperties.errorReport().asObjectProperty(model));
        if (traceProp != null) {
            Resource errorCause = traceProp.getResource();
            if (errorCause != null) {
                ErrorReportSpider causeSpider = new ErrorReportSpider(errorCause, model);
                errorReport.setErrorCause(causeSpider.parse());
            }
        }

        return errorReport;
    }
}
