package org.opentox.toxotis.core.component;

import java.io.IOException;
import org.opentox.toxotis.core.component.Task;
import java.io.File;
import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.util.aa.AuthenticationToken;

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
    public void testRDF() throws URISyntaxException, ToxOtisException, IOException {
        BibTeX bib = new BibTeX(); // ...Create anonymous bibtex
        bib.setAuthor("Sopasakis P.");
        bib.setTitle("This is the Title");
        bib.setVolume(100);
        bib.setCrossref("http://otherserver.com:5000/bibtex/549a9f40-9758-44b3-90fe-db31fe1a1a01");
        bib.setBibType(BibTeX.BIB_TYPE.Article);
        AuthenticationToken at = new AuthenticationToken(new File("/home/chung/toxotisKeys/my.key")); // << Provide your credentials here
        bib.publishOnline(Services.ntua().augment("bibtex"), at);
    }

    //@Test
    public void testSomeMethod() throws URISyntaxException, ToxOtisException {
        /**
         * This is my BibTeX object.....
         */
        BibTeX bib = new BibTeX(); // ...Create anonymous bibtex
        bib.setAuthor("Sopasakis P.");
        bib.setTitle("This is the Title");
        bib.setVolume(100);
        bib.setCrossref("http://localhost:3000/bibtex/549a9f40-9758-44b3-90fe-db31fe1a1a01");
        bib.setBibType(BibTeX.BIB_TYPE.Article);

        /**
         * Now I post it to a remote server:
         */
        Task t = bib.publishOnline(new VRI("http://localhost:3000/bibtex"), null);
        System.out.println(t.getResultUri());
        //assertEquals(BibTeX.class, t.getResultUri().getOpenToxType());
    }

    @Test
    public void testParseString() throws ToxOtisException {
        BibTeX b = new BibTeX();
        b.readString("@Article{http://bibtex/xx,\n"
                + "author=\"me\",\n"
                + "edition = \"1\",\n"
                + "year=\"2008\",\n"
                + "copyright=\"NTUA, 2010\"\n"
                + "\"}\"");
        System.out.println(b);
    }

    @Test
    public void testParseFile() throws ToxOtisException {
        BibTeX b = new BibTeX();
        b.readString(new File("/home/chung/Desktop/my.bib"));
        System.out.println(b);
    }
}
