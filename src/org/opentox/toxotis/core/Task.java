package org.opentox.toxotis.core;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.Date;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.MetaInfo;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Task extends OTComponent<Task>{

    public enum Status{
        RUNNING,
        COMPLETED,
        CANCELLED;
    }

    private VRI resultUri;
    private Status hasStatus;
    private float percentageCompleted;

    private ErrorReport errorReport;

    public Task() {
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
    public Task createFrom(OntModel model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Individual asIndividual(OntModel model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}