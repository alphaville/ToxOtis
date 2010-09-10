package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.Task;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class TaskSpider extends Tarantula<Task>{

    public TaskSpider() {
    }

    public TaskSpider(Resource resource, OntModel model) {
        super(resource, model);
    }

    @Override
    public Task parse() throws ToxOtisException {
        Task task = new Task();

        Literal hasStatus = resource.getProperty(
                    OTDatatypeProperties.hasStatus().asDatatypeProperty(model)
                    ).getObject().as(Literal.class);

        if(hasStatus != null){
            task.setHasStatus(Task.Status.valueOf(hasStatus.getString()));
        }

        Literal resultUri = resource.getProperty(
                    OTDatatypeProperties.resultURI().asDatatypeProperty(model)
                    ).getObject().as(Literal.class);

        if(resultUri != null){
            try {
                task.setResultUri(new VRI(resultUri.getString()));
            } catch (URISyntaxException ex) {
                Logger.getLogger(TaskSpider.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Resource errorReport = resource.getProperty(
                OTObjectProperties.errorReport().asObjectProperty(model)
                ).getObject().as(Resource.class);

        if(errorReport != null){
            task.setErrorReport(new ErrorReportSpider(errorReport, model).parse());
        }

        return task;
    }

}
