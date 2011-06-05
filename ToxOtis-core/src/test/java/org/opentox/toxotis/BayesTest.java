package org.opentox.toxotis;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.junit.Test;
import weka.classifiers.bayes.net.EditableBayesNet;
import weka.classifiers.bayes.net.estimate.MultiNomialBMAEstimator;
import weka.classifiers.bayes.net.search.local.K2;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class BayesTest {

    @Test
    public void test1() throws Exception {
        try {
//            Instances ins = DataSource.read("/home/chung/Desktop/test.arff");
//            System.out.println(ins);
//            ins.setClassIndex(0);
//            K2 learner = new K2();
//            MultiNomialBMAEstimator estimator = new MultiNomialBMAEstimator();
//            estimator.setUseK2Prior(true);
//            EditableBayesNet bn = new EditableBayesNet(ins);
//            bn.initStructure();
//            learner.buildStructure(bn, ins);
//            estimator.estimateCPTs(bn);

            Model jenaModel = ModelFactory.createOntologyModel();
            long mem0 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            System.out.println("Memory used: " + mem0 / 1024 + " K bytes");
            jenaModel.read("http://apps.ideaconsult.net:8080/ambit2/dataset/585036", null);
            long mem1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            System.out.println("Memory used for Jena object " + (mem1 - mem0) / (1024) + " K bytes");

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }
}
