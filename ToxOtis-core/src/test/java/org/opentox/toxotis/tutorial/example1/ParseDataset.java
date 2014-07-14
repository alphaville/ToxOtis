package org.opentox.toxotis.tutorial.example1;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Dataset;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.ontology.RDFValidator;
import weka.core.Instances;

/**
 *
 * @author Pantelis Sopasakis
 */
public class ParseDataset {

    public ParseDataset() {
    }

    @Test
    public void test1() throws ServiceInvocationException, ToxOtisException {
        Dataset ds = new Dataset(Services.ideaconsult().augment("dataset", 9).addUrlParameter("max", 10));
        System.out.println("Downloading " + ds.getUri() + "...");
        ds.loadFromRemote();

        /*
         * Convert to weka.core.Instances
         */
        System.out.println("Parsing into instances...");
        Instances weka = ds.getInstances();
        System.out.println("Instances...");
        System.out.println(weka);

        /*
         * Validate reconstructed OWL-DL RDF
         */
        System.out.println("\nValidating...");
        OntModel ontModel = ds.asOntModel();
        
        RDFValidator validator = new RDFValidator(ontModel);
        validator.validate(OntModelSpec.OWL_DL_MEM_TRANS_INF);
        validator.printIssues(System.out);   
        boolean isValid = validator.isValid();
        assertTrue("This is not an OWL-DL document!", isValid);
        System.out.println("The reconstructed RDF document is " + 
                (isValid ? "" : "**NOT**") + "OWL-DL valid");       

    }
}
