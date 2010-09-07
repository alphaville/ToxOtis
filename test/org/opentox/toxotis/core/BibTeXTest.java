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
        BibTeX bib = new BibTeX(new VRI("http://localhost:3000/bibtex/3ae95773-c121-47ec-bc43-8e95e867b488"));
        bib.setAuthor("Sopasakis P.");
        bib.setTitle("This is the Title");
        bib.setVolume(100);
        bib.setBibType(BibTeX.BIB_TYPE.Article);
        System.out.println(bib);
    }

}