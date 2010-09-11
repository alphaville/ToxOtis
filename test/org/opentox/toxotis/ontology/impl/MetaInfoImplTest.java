package org.opentox.toxotis.ontology.impl;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.Feature;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.util.spiders.FeatureSpider;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class MetaInfoImplTest {

    public MetaInfoImplTest() {
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

    @Test
    public void testAttach() throws URISyntaxException, ToxOtisException {
        MetaInfo mi = new MetaInfoImpl();
        mi.addContributor("Hampos Chomenides");
        mi.addContributor("Pantelis Sopasakis");
        mi.setComment("Just a comment");
        mi.setCreator("YAQP web services");
        mi.setDescription("Brief Description");
        mi.setHasSource("http://someserver.com/service/1/model/24875");
        mi.setIdentifier("http://somserver.net/service/134");
        mi.setPublisher("http://opentox.ntua.gr:3000");
        mi.setSameAs("http://opentox.ntua.gr:3000/clone/24875");
        mi.setSeeAlso("http://opentox.org");
        mi.setTitle("My Resource");
        mi.setVersionInfo("1.1");
        String featureUri = "http://apps.ideaconsult.net:8080/ambit2/feature/22204";
        FeatureSpider fSpider = new FeatureSpider(new VRI(featureUri));
        fSpider.parse();
        OntModel model = fSpider.getOntModel();
        Resource resouce = model.getResource(featureUri);
        resouce = mi.attachTo(resouce, model);
        FeatureSpider fS = new FeatureSpider(model, featureUri);
        Feature parsedFeature = fS.parse();        
    }
}
