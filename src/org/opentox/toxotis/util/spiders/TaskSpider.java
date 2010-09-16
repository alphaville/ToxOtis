package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.GetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.Task;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class TaskSpider extends Tarantula<Task> {

    private VRI vri;

    public TaskSpider() {
    }

    public TaskSpider(VRI vri) throws ToxOtisException {
        this.vri = vri;
        GetClient client = new GetClient();
        client.setMediaType("application/rdf+xml");
        client.setUri(vri);
        try {
            final int status = client.getResponseCode();
            if (status == 403) {
                throw new ToxOtisException(ErrorCause.AuthenticationFailed, "Access denied to : '" + vri + "'");
            }
            if (status == 401) {
                throw new ToxOtisException(ErrorCause.UnauthorizedUser, "User is not authorized to access : '" + vri + "'");
            }
            if (status == 404) {
                throw new ToxOtisException(ErrorCause.TaskNotFoundError, "The following task was not found : '" + vri + "'");
            }
            if (status != 200 && status != 202) {
                throw new ToxOtisException(ErrorCause.CommunicationError, "Communication Error with : '" + vri + "'. status = " + status);
            }
        } catch (IOException ex) {
            throw new ToxOtisException("Communication Error with the remote service at :" + vri, ex);
        }
        model = client.getResponseOntModel();
        resource = model.getResource(vri.getStringNoQuery());
    }

    public TaskSpider(Resource resource, OntModel model) {
        super(resource, model);
    }

    @Override
    public Task parse() throws ToxOtisException {
        Task task = new Task();

        task.setMeta(new MetaInfoSpider(resource, model).parse());

        Literal hasStatus = resource.getProperty(
                OTDatatypeProperties.hasStatus().asDatatypeProperty(model)).getObject().as(Literal.class);

        if (hasStatus != null) {
            task.setHasStatus(Task.Status.valueOf(hasStatus.getString().toUpperCase()));
        }


        Statement resultUriStmt = resource.getProperty(
                OTDatatypeProperties.resultURI().asDatatypeProperty(model));
        Literal resultUri = null;
        if (resultUriStmt != null) {
            resultUri = resultUriStmt.getObject().as(Literal.class);
        }

        if (resultUri != null) {
            try {
                task.setResultUri(new VRI(resultUri.getString()));
            } catch (URISyntaxException ex) {
                Logger.getLogger(TaskSpider.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


        Statement percentageStmt = resource.getProperty(
                OTDatatypeProperties.percentageCompleted().asDatatypeProperty(model));
        Literal percentageCompleted = percentageStmt != null ? percentageStmt.getObject().as(Literal.class) : null;

        if (percentageCompleted != null) {
            task.setPercentageCompleted(percentageCompleted.getFloat());
        }

        Statement errorReportStmt = resource.getProperty(
                OTObjectProperties.errorReport().asObjectProperty(model));
        Resource errorReport = errorReportStmt != null ? errorReportStmt.getObject().as(Resource.class) : null;

        if (errorReport != null) {
            task.setErrorReport(new ErrorReportSpider(errorReport, model).parse());
        }



        return task;
    }
}
