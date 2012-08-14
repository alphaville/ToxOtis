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
package org.opentox.toxotis.util.aa.policy;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.IPostClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.exceptions.impl.ConnectionException;
import org.opentox.toxotis.exceptions.impl.ForbiddenRequest;
import org.opentox.toxotis.exceptions.impl.RemoteServiceException;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class PolicyWrapper implements IPolicyWrapper {

    private Set<Policy> pols;
    private Document document;
    private static final String SUBJECT_ID = "subjectid";
    private static final String _DocTypePublic =
            "-//Sun Java System Access Manager7.1 2006Q3 Admin CLI DTD//EN";
    private static final String _DocTypeSystem =
            "jar://com/sun/identity/policy/policyAdmin.dtd";
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PolicyWrapper.class);

    public PolicyWrapper() {
        this.pols = new HashSet<Policy>();
    }

    public PolicyWrapper(Policy... pols) {
        this();
        for (Policy p : pols) {
            this.pols.add(p);
        }
    }

    public Set<Policy> getPolicies() {
        return pols;
    }

    public IPolicyWrapper setPolicies(Set<Policy> pols) {
        this.pols = pols;
        return this;
    }

    public IPolicyWrapper addPolicies(Policy... pols) {
        if (this.pols == null) {
            this.pols = new HashSet<Policy>();
        }
        for (Policy p : pols) {
            this.pols.add(p);
        }
        return this;
    }

    private void createDocument() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();

            Element policies = (Element) document.createElement("Policies");
            document.appendChild(policies);
            for (Policy pol : this.pols) {
                pol.xmlElement(document, policies);
            }

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(PolicyWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public String getText() {
        try {
            createDocument();
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            trans.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, _DocTypePublic);
            trans.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, _DocTypeSystem);
            //create string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(document);
            trans.transform(source, result);
            return sw.toString();
        } catch (TransformerException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Document getDocument() {
        return document;
    }

    @Override
    public int publish(VRI policyServer, AuthenticationToken token) throws ServiceInvocationException {
        if (!token.getStatus().equals(AuthenticationToken.TokenStatus.ACTIVE)) {
            throw new ForbiddenRequest("This token is not active: " + token.getStatus());
        }

        if (policyServer == null) {
            policyServer = Services.SingleSignOn.ssoPolicy();
        }
        IPostClient spc = ClientFactory.createPostClient(policyServer);
        spc.setContentType(Media.APPLICATION_XML);
        spc.addHeaderParameter(SUBJECT_ID, token.stringValue());
        spc.setPostable(this.getText().trim(), true);
        spc.post();
        try {
            int httpStatus = spc.getResponseCode();
            if (httpStatus != 200) {
                String policyErrorMsg = "Policy server at " + policyServer
                        + " responded with a status code " + httpStatus + " with message \n" + spc.getResponseText();
                logger.debug(policyErrorMsg);
                throw new RemoteServiceException(policyErrorMsg);
            }
            return spc.getResponseCode();
        } finally {
            if (spc != null) {
                try {
                    spc.close();
                } catch (IOException ex) {
                    String message = "IO Exception while closing an HTTPS client";
                    logger.error(message, ex);
                    throw new ConnectionException(message, ex);
                }
            }
        }
    }
}
