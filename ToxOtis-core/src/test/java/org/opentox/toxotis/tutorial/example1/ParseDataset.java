package org.opentox.toxotis.tutorial.example1;

import com.hp.hpl.jena.ontology.OntModel;
import org.junit.Test;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Dataset;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.ontology.WonderWebValidator;
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
        Instances weka = ds.getInstances();
        System.out.println(weka);

        /*
         * Validate reconstructed OWL-DL RDF
         */
        System.out.println("\nValidating...");
        OntModel ontModel = ds.asOntModel();
        WonderWebValidator validator = new WonderWebValidator(ontModel);
        boolean isOwnDl = validator.validate(WonderWebValidator.OWL_SPECIFICATION.DL);        
        System.out.println("The reconstructed RDF document is " + (isOwnDl ? "" : "**NOT**") + "OWL-DL valid");       

    }
}
