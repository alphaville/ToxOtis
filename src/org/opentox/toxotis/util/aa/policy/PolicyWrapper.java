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
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.IPostClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.aa.InactiveTokenException;
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
        if (this.pols == null){
            this.pols = new HashSet<Policy>();
        }
        for (Policy p : pols ){
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

    public Document getDocument() {
        return document;
    }

    public int publish(VRI policyServer, AuthenticationToken token) throws ToxOtisException {
        if (!token.getStatus().equals(AuthenticationToken.TokenStatus.ACTIVE)) {
            throw new InactiveTokenException("This token is not active: " + token.getStatus());
        }

        if (policyServer == null) {
            policyServer = Services.SingleSignOn.ssoPolicyOld();
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
                throw new ToxOtisException(ErrorCause.PolicyCreationError, policyErrorMsg);
            }
            return spc.getResponseCode();
        } catch (IOException ex) {
            throw new ToxOtisException("Communication error with the SSO policy server at " + policyServer, ex);
        } finally {
            if (spc != null) {
                try {
                    spc.close();
                } catch (IOException ex) {
                    logger.error("IO Exception while closing an HTTPS client", ex);
                    throw new ToxOtisException(ex);
                }
            }
        }
    }

}
