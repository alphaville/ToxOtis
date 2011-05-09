package org.opentox.toxotis.core.component.qprf;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import java.util.HashSet;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.OTPublishable;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class QprfReport extends OTPublishable<OTPublishable> {

    /**
     * The URI of the model for the the QPRF report is generated
     * (i.e. the model which creates the prediction)
     */
    private VRI modelUri;
    /**
     * Uri of the compund for which the QPRF report is created
     */
    private VRI compundUri;
    /**
     * The URI of the Domain of Applicability Model that is used
     * along with the predictive model
     */
    private VRI doaUri;
    /**
     * Keywords that will facilitate the search
     */
    private String keywords;
    /**
     * Stereochemical features that may affect the reliability of the
     * prediction
     */
    private String stereoFeatures;
    /**
     * Set of authors of this QPRF report
     */
    private HashSet<QprfAuthor> authors = new HashSet<QprfAuthor>();
    /**
     * The date (timestamp) of the QPRF report. Can be easily exported as
     * java.util.Date or java.sql.Date.
     */
    private long report_date;
    /**
     * Timestamp of model creation
     */
    private long model_date;
    /**
     * Information that help identify the model version
     * (free text)
     */
    private String modelVersion;
    /**
     * Comment on the predicted value
     */
    private String sec_3_2_e;
    /**
     * Comment on the uncertainty of the prediction
     */
    private String sec_3_4;
    /**
     * The chemical and biological mechanisms according to the model
     * underpinning the predicted result (OECD principle 5)
     */
    private String sec_3_5;
    /**
     * Considerations on structural analogues
     */
    private String sec_3_3_c;
    /**
     * Regulatory purpose
     */
    private String sec_4_1;
    /**
     * Regulatory interpretation
     */
    private String sec_4_2;
    /**
     * Outcome
     */
    private String sec_4_3;
    /**
     * Conclusion
     */
    private String sec_4_4;
    private String metabolicDomain;
    private String structuralDomain;
    private String descriptorDomain;
    private String mechanismDomain;
    /**
     * The dataset containing the structural analogues of the submitted
     * compound and their experimental values.
     */
    private VRI datasetStructuralAnalogues;
    /**
     * Result of the applicability domain algorithm on the
     * submitted compound. Usually a YES/NO answer.
     */
    private LiteralValue applicabilityDomainResult;
    private LiteralValue predictionResult;
    private LiteralValue experimentalResult;

    /**
     * Reference to QMRF report. Can be a URI or other identifier of the
     * corresponding QMRF report.
     */
    private String QMRFreference;

    /**
     * Discussion on the QMRF report.
     */
    private String QMRFReportDiscussion;



    @Override
    public Task publishOnline(VRI vri, AuthenticationToken token) throws ServiceInvocationException {
        /*
         * Publish the QPRF report to a reporting service online
         */
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Task publishOnline(AuthenticationToken token) throws ServiceInvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected OTPublishable loadFromRemote(VRI vri, AuthenticationToken token) throws ServiceInvocationException {
        /*
         * Downloads QPRF report in RDF and parses it
         */
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Individual asIndividual(OntModel model) {
        /*
         * Serialization in RDF
         */
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        /*
         * Serialization in RDF
         */
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
