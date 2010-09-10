package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.core.ErrorReport;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class ErrorReportSpider extends Tarantula<ErrorReport>{

    public ErrorReportSpider() {
    }

    public ErrorReportSpider(Resource resource, OntModel model) {
        super(resource, model);
    }

    @Override
    public ErrorReport parse() throws ToxOtisException {
        ErrorReport errorReport = new ErrorReport();

        Literal errorCode = resource.getProperty(
                    OTDatatypeProperties.errorCode().asDatatypeProperty(model)
                    ).getObject().as(Literal.class);

        if(errorCode != null){
            errorReport.setErrorCode(errorCode.getInt());
        }

        Literal details = resource.getProperty(
                    OTDatatypeProperties.details().asDatatypeProperty(model)
                    ).getObject().as(Literal.class);

        if(details != null){
            errorReport.setDetails(details.getString());
        }

        Literal message = resource.getProperty(
                    OTDatatypeProperties.message().asDatatypeProperty(model)
                    ).getObject().as(Literal.class);

        if(message != null){
            errorReport.setMessage(message.getString());
        }

        Literal actor = resource.getProperty(
                    OTDatatypeProperties.actor().asDatatypeProperty(model)
                    ).getObject().as(Literal.class);

        if(actor != null){
            errorReport.setActor(actor.getString());
        }
        
        Resource errorCause = resource.getProperty(OTObjectProperties.errorReport().asObjectProperty(model)).getResource();
        if(errorCause != null){
            ErrorReportSpider causeSpider = new ErrorReportSpider(errorCause, model);
            errorReport.setErrorCause(causeSpider.parse());
        }

        return errorReport;
    }

}
