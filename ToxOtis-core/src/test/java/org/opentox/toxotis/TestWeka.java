package org.opentox.toxotis;

import java.io.File;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.FCBFSearch;
import weka.attributeSelection.GreedyStepwise;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class TestWeka {

    public static void main(String... args) throws Exception {
        CSVLoader loader = new CSVLoader();
        loader.setFile(new File("/home/chung/Desktop/Kloster-Seeon.csv"));
        Instances in = loader.getDataSet();
        in.setClassIndex(in.numAttributes()-1);
        System.out.println(in);

        GreedyStepwise search = new GreedyStepwise();
        CfsSubsetEval eval = new CfsSubsetEval();
        eval.buildEvaluator(in);
        int[] selection = search.search(eval, in);
        
    }
}
