package org.opentox.toxotis.ontology.impl;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Resource;
import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Dataset;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.ResourceValue;
import org.opentox.toxotis.ontology.collection.OTClasses;

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

    //@Test
    public void testAttach() throws URISyntaxException, ToxOtisException {
        MetaInfo mi = new MetaInfoImpl();
        mi.addContributor(new LiteralValue("CCC", XSDDatatype.XSDNCName));        
        SimpleOntModelImpl model = new SimpleOntModelImpl();
        Resource base = model.createResource("http://base.com/base", OTClasses.Compound().inModel(model));
        mi.attachTo(base, model);
        model.write(System.out);
    }

    @Test
    public void testStaXWriterSerialization() throws Exception{
        MetaInfo mi = new MetaInfoImpl().addComment("this is a comment").addAudience("anyone interested").addDescription("description").
                addContributor("me","you").addPublisher("pub").addSeeAlso(new ResourceValue(Services.ntua(), OTClasses.Algorithm())).
                addIdentifier("haha");
        Dataset ds = new Dataset();
        ds.setMeta(mi);
        ds.writeRdf(System.out);
    }
}
