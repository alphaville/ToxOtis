/*
 *
 * ToxOtis
 *
 * ToxOtis is the Greek word for Sagittarius, that actually means ‘archer’. ToxOtis
 * is a Java interface to the predictive toxicology services of OpenTox. ToxOtis is
 * being developed to help both those who need a painless way to consume OpenTox
 * services and for ambitious service providers that don’t want to spend half of
 * their time in RDF parsing and creation.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 *
 */
package org.opentox.toxotis.ontology;

import com.hp.hpl.jena.ontology.OntModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import org.opentox.toxotis.client.http.PostHttpClient;
import org.opentox.toxotis.client.RequestHeaders;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.IStAXWritable;
import org.opentox.toxotis.exceptions.impl.ConnectionException;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;

/**
 * <p align=justify width=80%>
 * Validate an ontological model against the online OWL validator at
 * <code>http://www.mygrid.org.uk/OWL/Validator</code>. A method returns either
 * <code>true</code> or <code>false</code> depending on whether the submitted OntModel object is OWL-*
 * compliant or not. Users can configure the validator to use a certain specification such
 * as <code>OWL-DL</code>, <code>OWL-Full</code> or <code>OWL-Lite</code>.
 * </p>
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class WonderWebValidator {

    private static final String m_WONDERWEB_URL = "http://www.mygrid.org.uk/OWL/Validator";
    /**
     * The URI that identifies the online OWL-DL validation service.
     */
    public static final VRI WONDERWEB_VLD;
    private static final String responseTemplate = "<p><strong>%s</strong>: <span class=\"yes\">YES</span>";
    private IStAXWritable stax;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(WonderWebValidator.class);

    /**
     * An enumeration of supported OWL specifications.
     */
    public enum OWL_SPECIFICATION {

        DL,
        Lite,
        Full;
    }

    static {
        try {
            WONDERWEB_VLD = new VRI(m_WONDERWEB_URL);
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    ;
    private OntModel model;

    /**
     * Dummy constructor for an WonderWebValidator object.
     */
    public WonderWebValidator() {
    }

    /**
     * Construct a WonderWebValidator object providing an OntModel which is
     * to be checked for compliance against some OWL specification.
     * @param model
     *      Ontological Data Model.
     */
    public WonderWebValidator(OntModel model) {
        this.model = model;
    }

    public WonderWebValidator(IStAXWritable stax) {
        this.stax = stax;
    }

    public void setValidatable(OntModel model) {
        this.model = model;
    }

    public void setValidatable(IStAXWritable stax) {
        this.stax = stax;
    }

    public boolean validate(OWL_SPECIFICATION specification) throws ServiceInvocationException {
        PostHttpClient pc = new PostHttpClient(WONDERWEB_VLD);
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        if (model != null) {
            model.write(printWriter);
        } else if (stax != null) {
            stax.writeRdf(printWriter);
        }
        pc.addPostParameter("rdf", result.toString());
        pc.addPostParameter("url", "");
        pc.addPostParameter("level", specification.toString());
        pc.setContentType("application/x-www-form-urlencoded");
        pc.addHeaderParameter(RequestHeaders.REFERER, WONDERWEB_VLD.toString());
        pc.post();
        try {
            InputStream is = pc.getRemoteStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String line;
            String found = String.format(responseTemplate, specification.toString());
            int linesLimit = 100; //
            int i = 0;
            while (i < linesLimit && (line = br.readLine()) != null) {
                i++;
                if (line.equals(found)) {
                    return true;
                }
            }

        } catch (IOException ex) {
            logger.warn("Unexpected IO Exception", ex);
            throw new ConnectionException(ex);
        } finally {
            if (pc != null) {
                try {
                    pc.close();
                } catch (IOException ex) {
                    String message = "Unexpected IO Exception caught while closing connection after POST";
                    logger.warn(message, ex);
                    throw new ConnectionException(message, ex);
                }
            }
            if (printWriter != null) {
                printWriter.flush();
                printWriter.close();
            }
        }

        return false;
    }
}
