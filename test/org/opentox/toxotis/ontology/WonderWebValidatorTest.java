package org.opentox.toxotis.ontology;

import com.hp.hpl.jena.ontology.OntModel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.core.component.BibTeX;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class WonderWebValidatorTest {

    public WonderWebValidatorTest() {
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
    public void testSomeMethod() throws ToxOtisException {
        BibTeX bib = new BibTeX(); // ...Create anonymous bibtex
        bib.setAuthor("Sopasakis P.");
        bib.setTitle("This is the Title");
        bib.setVolume(100);
        bib.setCrossref("http://localhost:3000/bibtex/549a9f40-9758-44b3-90fe-db31fe1a1a01");
        bib.setBibType(BibTeX.BIB_TYPE.Article);

        /**
         * Test the ont model using the WonderWed validator
         */
        OntModel om = bib.asOntModel();
        WonderWebValidator vld = new WonderWebValidator(om);
        assertTrue(vld.validate(WonderWebValidator.OWL_SPECIFICATION.DL));

    }
}
