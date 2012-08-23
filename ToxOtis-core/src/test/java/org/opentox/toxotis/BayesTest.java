package org.opentox.toxotis;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.File;
import org.junit.Test;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.aa.TokenPool;
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
        File passwordFile = new File(System.getProperty("user.home") + "/toxotisKeys/.my.key");
        AuthenticationToken at = new AuthenticationToken(passwordFile);
        User u = at.getUser();
        System.out.println(u);

    }
}
