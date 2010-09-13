package org.opentox.toxotis.core;

import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;

/**
 *
 * @author chung
 */
public class BibTeXTest {

    public BibTeXTest() {
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
    public void testSomeMethod() throws URISyntaxException, ToxOtisException {
        BibTeX bib = new BibTeX();
        bib.setAuthor("Sopasakis P.");
        bib.setTitle("This is the Title");
        bib.setVolume(100);
        bib.setCrossref("http://localhost:3000/bibtex/549a9f40-9758-44b3-90fe-db31fe1a1a01");
        bib.setBibType(BibTeX.BIB_TYPE.Article);
        Task t = bib.publishOnline(new VRI("http://localhost:3000/bibtex"), null);
        System.out.println(t.getResultUri().getOpenToxType());

    }

}