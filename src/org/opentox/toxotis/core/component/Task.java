package org.opentox.toxotis.core.component;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.OTOnlineResource;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;
import org.opentox.toxotis.util.spiders.TaskSpider;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Task extends OTOnlineResource<Task> {

    protected Task loadFromRemote(VRI uri) throws ToxOtisException {
        TaskSpider tSpider = new TaskSpider(uri);
        Task downloadedTask = tSpider.parse();
        setMeta(downloadedTask.getMeta());
        setErrorReport(downloadedTask.getErrorReport());
        seStatus(downloadedTask.getStatus());
        setPercentageCompleted(downloadedTask.getPercentageCompleted());
        setResultUri(downloadedTask.getResultUri());
        setHttpStatus(downloadedTask.getHttpStatus());
        return this;
    }

    @Override
    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public enum Status {

        RUNNING,
        COMPLETED,
        CANCELLED,
        ERROR;
    }
    private VRI resultUri;
    private Status hasStatus;
    private float percentageCompleted = -1;
    private ErrorReport errorReport;
    private float httpStatus = -1;

    public Task() {
        super();
    }

    public Task(VRI uri) {
        super(uri);
    }

    /**
     * The status of the task as an element of the enumeration {@link Status }.
     * A task can either be <code>RUNNING</code>, <code>COMPLETED</code>, <code>
     * CANCELLED</code> and <code>ERROR</code>.
     * @return
     *      The status of the task.
     */
    public Status getStatus() {
        return hasStatus;
    }

    /**
     * Set the status of a task.
     * @param hasStatus
     *      The new value for the status of the task.
     */
    public void seStatus(Status hasStatus) {
        this.hasStatus = hasStatus;
    }

    public float getPercentageCompleted() {
        return percentageCompleted;
    }

    public void setPercentageCompleted(float percentageCompleted) {
        this.percentageCompleted = percentageCompleted;
    }

    public VRI getResultUri() {
        return resultUri;
    }

    public void setResultUri(VRI resultUri) {
        this.resultUri = resultUri;
    }

    public ErrorReport getErrorReport() {
        return errorReport;
    }

    public void setErrorReport(ErrorReport errorReport) {
        this.errorReport = errorReport;
    }

    public float getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(float httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        Individual indiv = model.createIndividual(getUri() != null ? getUri().getStringNoQuery() : null, OTClasses.Task().inModel(model));
        getMeta().attachTo(indiv, model);
        if (hasStatus != null) {
            indiv.addLiteral(OTDatatypeProperties.hasStatus().asDatatypeProperty(model),
                    model.createTypedLiteral(hasStatus, XSDDatatype.XSDstring));
        }
        if (percentageCompleted != -1) {
            indiv.addLiteral(OTDatatypeProperties.percentageCompleted().asDatatypeProperty(model),
                    model.createTypedLiteral(percentageCompleted, XSDDatatype.XSDfloat));
        }
        if (httpStatus != -1) {
            indiv.addLiteral(OTDatatypeProperties.httpStatus().asDatatypeProperty(model),
                    model.createTypedLiteral(httpStatus, XSDDatatype.XSDfloat));
        }
        if (resultUri != null) {
            indiv.addLiteral(OTDatatypeProperties.resultURI().asDatatypeProperty(model),
                    model.createTypedLiteral(resultUri.clearToken().toString(), XSDDatatype.XSDanyURI));
        }
        if (errorReport != null) {
            indiv.addProperty(OTObjectProperties.errorReport().asObjectProperty(model), errorReport.asIndividual(model));
        }
        return indiv;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Task {");
        sb.append(getUri());
        sb.append("\n");
        sb.append("HTTP Status : ");
        sb.append(getHttpStatus());
        sb.append("\n");
        sb.append("Status      : ");
        sb.append(hasStatus);
        sb.append("\n");
        if (resultUri!=null){
            sb.append("Result URI  : ");
        sb.append(resultUri);
        sb.append("\n");
        }
        return new String(sb);
    }


}
