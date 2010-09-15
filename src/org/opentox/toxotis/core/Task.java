package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.util.spiders.TaskSpider;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Task extends OTOnlineResource<Task> {

    protected  Task loadFromRemote(VRI uri) throws ToxOtisException {
        TaskSpider tSpider = new TaskSpider(uri);
        Task downloadedTask = tSpider.parse();
        setMeta(downloadedTask.getMeta());
        setErrorReport(downloadedTask.getErrorReport());
        setHasStatus(downloadedTask.getHasStatus());
        setPercentageCompleted(downloadedTask.getPercentageCompleted());
        setResultUri(downloadedTask.getResultUri());
        return this;
    }

    public enum Status {

        RUNNING,
        COMPLETED,
        CANCELLED;
    }
    private VRI resultUri;
    private Status hasStatus;
    private float percentageCompleted;
    private ErrorReport errorReport;

    public Task() {
        super();
    }

    public Task(VRI uri) {
        super(uri);
    }



    public Status getHasStatus() {
        return hasStatus;
    }

    public void setHasStatus(Status hasStatus) {
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

    @Override
    public Individual asIndividual(OntModel model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    


}
