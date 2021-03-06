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
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.IHTMLSupport;
import org.opentox.toxotis.core.OTOnlineResource;
import org.opentox.toxotis.core.html.Alignment;
import org.opentox.toxotis.core.html.HTMLContainer;
import org.opentox.toxotis.core.html.HTMLDivBuilder;
import org.opentox.toxotis.core.html.HTMLTable;
import org.opentox.toxotis.core.html.HTMLUtils;
import org.opentox.toxotis.core.html.impl.HTMLTextImpl;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.spiders.TaskSpider;

/**
 * An asynchronous job running on a remote service. Tasks are resources provided to
 * the client to be able to monitor the progress of a job running on a server.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Task extends OTOnlineResource<Task> implements IHTMLSupport {

    @Override
    protected Task loadFromRemote(VRI uri, AuthenticationToken token) throws ServiceInvocationException {
        TaskSpider tSpider = new TaskSpider(uri, token);
        Task downloadedTask = tSpider.parse();
        setMeta(downloadedTask.getMeta());
        setErrorReport(downloadedTask.getErrorReport());
        setStatus(downloadedTask.getStatus());
        setPercentageCompleted(downloadedTask.getPercentageCompleted());
        setResultUri(downloadedTask.getResultUri());
        setHttpStatus(downloadedTask.getHttpStatus());
        setCreatedBy(downloadedTask.getCreatedBy());
        return this;
    }

    @Override
    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public HTMLContainer inHtml() {
        HTMLDivBuilder builder = new HTMLDivBuilder("jaqpot_algorithm");
        builder.addComment("Task Representation automatically generated by Jaqpot");
        builder.addSubHeading("Task Report");
        builder.addSubSubHeading(getUri().toString(),"margin-left:20px;");
        builder.getDiv().setAlignment(Alignment.justify).breakLine().horizontalSeparator();
        builder.addSubSubHeading("Information about the Task");
        builder.getDiv().breakLine();

        builder.addSubSubSubHeading("Summary");
        HTMLTable summaryTable = builder.addTable(2).setStyle("margin-left:20px;");
        summaryTable.setAtCursor(new HTMLTextImpl("Task Status").formatBold(true)).setTextAtCursor(getStatus() != null ? getStatus().toString() : "Undefined").
                setAtCursor(new HTMLTextImpl("HTTP Status").formatBold(true)).setTextAtCursor("" + getHttpStatus());
        if (Task.Status.RUNNING.equals(getStatus()) || Task.Status.QUEUED.equals(getStatus())) {
            summaryTable.setAtCursor(new HTMLTextImpl("% Completed").formatBold(true)).setTextAtCursor("" + getPercentageCompleted());
            
            builder.addJS("setTimeout(  function(){window.location.reload(1)}, 1000  );");
        }
        if (Task.Status.COMPLETED.equals(getStatus())) {
            summaryTable.setAtCursor(new HTMLTextImpl("Result URI").formatBold(true)).setTextAtCursor("<a id='modelURI' href=\"" + getResultUri() + "\">" + getResultUri() + "</a>");
        }
        if (duration != 0) {
            summaryTable.setAtCursor(new HTMLTextImpl("Duration").formatBold(true)).setTextAtCursor(duration + "ms");
        }
        if (getCreatedBy() != null) {
            summaryTable.setAtCursor(new HTMLTextImpl("Created by").formatBold(true)).setTextAtCursor("" + getCreatedBy().getUid());
        }

        summaryTable.setCellPadding(5).
                setCellSpacing(2).
                setTableBorder(1).
                setColWidth(1, 150).
                setColWidth(2, 400);
        builder.getDiv().breakLine();

        if (getErrorReport() != null) {
            builder.addSubSubSubHeading("Error Report");
            HTMLTable errorReportTable = builder.addTable(2).setStyle("margin-left:20px;");
            errorReportTable.setAtCursor(new HTMLTextImpl("URI").formatBold(true)).setTextAtCursor(getErrorReport().getUri() != null ? HTMLUtils.linkUrlsInText(getErrorReport().getUri().toString()) : "Undefined").
                    setAtCursor(new HTMLTextImpl("Actor").formatBold(true)).setTextAtCursor(getErrorReport().getActor() != null ? getErrorReport().getActor() : "Undefined").
                    setAtCursor(new HTMLTextImpl("Error Code").formatBold(true)).setTextAtCursor(getErrorReport().getErrorCode() != null ? getErrorReport().getErrorCode() : "-").
                    setAtCursor(new HTMLTextImpl("Message").formatBold(true)).setTextAtCursor(getErrorReport().getMessage() != null ? getErrorReport().getMessage() : "-").
                    setAtCursor(new HTMLTextImpl("HTTP Code").formatBold(true)).setTextAtCursor(getErrorReport().getHttpStatus() > 0 ? "" + getErrorReport().getHttpStatus() : "-").
                    setAtCursor(new HTMLTextImpl("Details").formatBold(true)).setAtCursor(new HTMLTextImpl(htmlNormalize(getErrorReport().getDetails() != null ? getErrorReport().getDetails() : "-")).formatPRE(true));
            errorReportTable.setCellPadding(5).
                    setCellSpacing(2).
                    setTableBorder(1).
                    setColWidth(1, 150).
                    setColWidth(2, 400);
            builder.getDiv().breakLine();
        }


        if (getMeta() != null) {
            builder.addSubSubSubHeading("Meta Information");
            MetaInfo mtTemp = getMeta();
            mtTemp.setStyle("margin-left:20px;");
            builder.addComponent(mtTemp.inHtml());
        }

        builder.addComponent(createLinksFooter());

        return builder.getDiv();
    }

    private String htmlNormalize(String in) {
        if (in == null) {
            return null;
        }
        return in.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }

    /**
     * Status of the task revealing information about the asynchronous job with
     * which the task is related and runs on a server.
     */
    public enum Status {

        /**
         * The task is still Running in the background as an asynchronous job. This
         * status means that the task has been submitted to the Execution Pool but has
         * not completed yet.
         */
        RUNNING,
        /**
         * The task has completed execution successfully. The result can be found under
         * #resultUri and is accessible via {@link Task#getResultUri() #getResultUri()}.
         * The corresponding status is either 200 if the result is the URI of a created
         * resource or 201 if it redirects to some other task (most probably on some
         * other server)
         */
        COMPLETED,
        /**
         * The task is cancelled by the User.
         */
        CANCELLED,
        /**
         * Task execution was interrupted due to some error related with the asynchronous
         * job, either due to a bad request by the client, an internal server error or an
         * error related to a third remote service. In such a case, the task is accompanied
         * by an error report that provides access to details about the exceptional event.
         */
        ERROR,
        /**
         * Due to large load on the server or issues related to A&amp;A or user quota,
         * the task was rejected for execution.
         */
        REJECTED,
        /**
         * The task is created and put in an execution queue but is not running yet.
         * HTTP status codes of queued tasks is 202.
         */
        QUEUED;
    }
    private VRI resultUri;
    private Status hasStatus;
    private float percentageCompleted = -1;
    private ErrorReport errorReport;
    private float httpStatus = -1;
    private User createdBy;
    private long duration = 0L;

    public Task() {
        super();
        addOntologicalClasses(OTClasses.task());
    }

    public Task(VRI uri) {
        super(uri);
        addOntologicalClasses(OTClasses.task());
    }

    /**
     * The duration of the task, that is the overall time since its execution started,
     * not including the time it was queued.
     * @return
     *      The duration of the task in millisenconds or <code>0</code> if no duration
     *      is assigned.
     */
    public Long getDuration() {
        return duration;
    }

    /**
     * Setter method for the duration of the task in milliseconds. The time during
     * which the task was in a queue should not be included in the execution
     * duration.
     * @param duration
     *      The duration of the task in milliseconds
     * @return
     *      The current updated modifiable Task object.
     */
    public Task setDuration(Long duration) {
        if (duration == null) {
            this.duration = 0;
            return this;
        }
        this.duration = duration;
        return this;
    }

    /**
     * Retrieve the user which created the task.
     * @return
     *      Creator of the task or <code>null</code> if not available.
     */
    public User getCreatedBy() {
        return createdBy;
    }

    public Task setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
        return this;
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
     * ParameterValue the status of a task.
     * 
     * @param status
     *      The new value for the status of the task.
     * 
     * @return 
     *      The current modifiable instance of Task.
     */
    public Task setStatus(Status status) {
        this.hasStatus = status;
        return this;
    }

    /**
     *
     * Get the percentage of completion of a running task.
     * 
     * @return
     *      Percentage of completion as a number in the range <code>[0, 100]</code>.
     */
    public float getPercentageCompleted() {
        return percentageCompleted;
    }

    public Task setPercentageCompleted(float percentageCompleted) {
        this.percentageCompleted = percentageCompleted;
        return this;
    }

    public VRI getResultUri() {
        return resultUri;
    }

    public Task setResultUri(VRI resultUri) {
        this.resultUri = resultUri;
        return this;
    }

    public ErrorReport getErrorReport() {
        return errorReport;
    }

    public Task setErrorReport(ErrorReport errorReport) {
        this.errorReport = errorReport;
        return this;
    }

    public float getHttpStatus() {
        return httpStatus;
    }

    public Task setHttpStatus(float httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    private String normalizeStatusString(String status) {
        String newStatus = status;
        newStatus = newStatus.toLowerCase();
        return (newStatus.length() > 0) ? Character.toUpperCase(newStatus.charAt(0)) + newStatus.substring(1) : newStatus;
    }

    @Override
    public Individual asIndividual(OntModel model) {
        Individual indiv = model.createIndividual(getUri() != null ? getUri().getStringNoQuery() : null, OTClasses.task().inModel(model));
        getMeta().attachTo(indiv, model);
        if (hasStatus != null) {
            indiv.addLiteral(OTDatatypeProperties.hasStatus().asDatatypeProperty(model),
                    model.createTypedLiteral(normalizeStatusString(hasStatus.toString()), XSDDatatype.XSDstring));
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
                    model.createTypedLiteral(resultUri.toString(), XSDDatatype.XSDanyURI));
        }
        if (errorReport != null) {
            indiv.addProperty(OTObjectProperties.errorReport().asObjectProperty(model), errorReport.asIndividual(model));
        }
        if (duration != -1) {
            indiv.addProperty(OTDatatypeProperties.duration().asDatatypeProperty(model), model.createTypedLiteral(duration, XSDDatatype.XSDlong));
        }
        return indiv;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Task {");
        sb.append(getUri());
        sb.append("}\n");
        sb.append("HTTP Status : ");
        sb.append(getHttpStatus());
        sb.append("\n");
        sb.append("Status      : ");
        sb.append(hasStatus);
        sb.append("\n");
        if (resultUri != null) {
            sb.append("Result URI  : ");
            sb.append(resultUri);
            sb.append("\n");
        }
        return new String(sb);
    }
}
