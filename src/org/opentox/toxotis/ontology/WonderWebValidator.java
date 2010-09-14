package org.opentox.toxotis.ontology;

import com.hp.hpl.jena.ontology.OntModel;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.PostClient;
import org.opentox.toxotis.client.RequestHeaders;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.BibTeX;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class WonderWebValidator {

    private static final String m_WONDERWEB_URL = "http://www.mygrid.org.uk/OWL/Validator";
    public static final VRI WONDERWEB_VLD;

    static {
        try {
            WONDERWEB_VLD = new VRI(m_WONDERWEB_URL);
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    ;
    private OntModel model;

    public WonderWebValidator() {
    }

    public WonderWebValidator(OntModel model) {
        this.model = model;
    }

    public OntModel getModel() {
        return model;
    }

    public void setModel(OntModel model) {
        this.model = model;
    }

    public boolean post() throws ToxOtisException {
        PostClient pc = new PostClient(WONDERWEB_VLD);
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        model.write(printWriter);
        pc.addPostParameter("rdf", result.toString());
        pc.addPostParameter("url", "");
        pc.addPostParameter("level", "DL");
        pc.setContentType("application/x-form-urlencoded");
        pc.addHeaderParameter(RequestHeaders.REFERER, "http://www.mygrid.org.uk/OWL/Validator");
        pc.post();
        try {
            InputStream is = pc.getRemoteStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String line;
            String found = "<p><strong>DL</strong>: <span class=\"yes\">YES</span>";
            int linesLimit = 100; //
            int i = 0;
            while (i < linesLimit && (line = br.readLine()) != null) {
                i++;
                if (line.equals(found)) {
                    return true;
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(WonderWebValidator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    public static void main(String... art) throws ToxOtisException {

        BibTeX bib = new BibTeX(); // ...Create anonymous bibtex
        bib.setAuthor("Sopasakis P.");
        bib.setTitle("This is the Title");
        bib.setVolume(100);
        bib.setCrossref("http://localhost:3000/bibtex/549a9f40-9758-44b3-90fe-db31fe1a1a01");
        bib.setBibType(BibTeX.BIB_TYPE.Article);
        OntModel om = bib.asOntModel();
        WonderWebValidator vld = new WonderWebValidator(om);
        System.out.println(vld.post());

    }
}
