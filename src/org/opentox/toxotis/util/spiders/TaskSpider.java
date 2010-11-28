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
import org.opentox.toxotis.client.http.GetHttpClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class TaskSpider extends Tarantula<Task> {

    private VRI vri;
    private int httpStatus = -1;

    public TaskSpider() {
    }

    public TaskSpider(VRI vri) throws ToxOtisException {
        this(vri, (AuthenticationToken) null);
    }

    public TaskSpider(VRI vri, AuthenticationToken token) throws ToxOtisException {
        this.vri = vri;
        GetHttpClient client = new GetHttpClient(vri);
        client.authorize(token);
        client.setMediaType(Media.APPLICATION_RDF_XML);
        try {
            final int status = client.getResponseCode();
            assessHttpStatus(status, vri);
            httpStatus = status;
            model = client.getResponseOntModel();
            resource = model.getResource(vri.getStringNoQuery());
        } catch (IOException ex) {
            throw new ToxOtisException("Communication Error with the remote service at :" + vri, ex);
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException ex) {
                    throw new ToxOtisException(ErrorCause.StreamCouldNotClose,
                            "Error while trying to close the stream "
                            + "with the remote location at :'" + ((vri != null)
                            ? vri.clearToken().toString() : null) + "'", ex);
                }
            }
        }
    }

    public TaskSpider(Resource resource, OntModel model) {
        super(resource, model);
    }

    @Override
    public Task parse() throws ToxOtisException {
        Task task = new Task(vri);
        task.setMeta(new MetaInfoSpider(resource, model).parse());
        task.setHttpStatus(httpStatus);

        Statement hasStatusProp = resource.getProperty(
                OTDatatypeProperties.hasStatus().asProperty(model));
        if (hasStatusProp != null) {
            Literal hasStatus = hasStatusProp.getObject().as(Literal.class);
            if (hasStatus != null) {
                task.setStatus(Task.Status.valueOf(hasStatus.getString().toUpperCase()));
            }
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
