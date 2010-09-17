package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.ontology.OntModel;
import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.GetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.ErrorReport;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class ErrorReportSpiderTest {

    public ErrorReportSpiderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    //@Test
    public void testParseErrorReport() throws URISyntaxException, ToxOtisException {
        /**
         * Ask for an algorithm from NTUA without providing an authentication 
         * token or credentials. This will throw an error!
         */
        VRI uri = new VRI(Services.NTUA.augment("algorithm", "mlr"));
        GetClient client = new GetClient();
        client.setUri(uri);
        OntModel model = client.getResponseOntModel();
        ErrorReportSpider spider = new ErrorReportSpider(uri, model);
        ErrorReport er = spider.parse();
        System.out.println(er.getErrorCode());
    }

    @Test
    public void testUnauthorizedAlgorithm() throws URISyntaxException, ToxOtisException {
        VRI uri = new VRI(Services.NTUA.augment("algorithm", "mlr"));
        try {
            new AlgorithmSpider(uri);
        } catch (ToxOtisException tox) {
            assertNotNull(tox.getRemoteErrorReport());
            System.out.println(tox.getRemoteErrorReport());
        }
    }
}
