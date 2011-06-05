package org.opentox.toxotis.profiler;

import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Dataset;
import org.opentox.toxotis.util.spiders.DatasetSpider;
import weka.core.Instances;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ProfileMe {

    private static void downloadAndParseRDF() throws Exception {
        long mem0 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Memory used: " + mem0 / 1024 + " K bytes");
        System.out.println("Downloading and parsing dataset");


        VRI dataset = Services.ideaconsult().augment("dataset", "585036").addUrlParameter("max", "500");
//        OntModel model = ModelFactory.createOntologyModel();
//        model.read(dataset.toString());
//        model.close();

        DatasetSpider dss = new DatasetSpider(dataset);
        Dataset ds = dss.parse();
        System.out.println("Done");
        long mem1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Memory used for Jena object " + (mem1 - mem0) / (1024) + " K bytes");
        Instances inst = ds.getInstances();
    }

    public static void main(String... args) throws Exception {
        downloadAndParseRDF();
    }
}
