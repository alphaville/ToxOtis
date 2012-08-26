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
package org.opentox.toxotis.core.component.qprf;

import java.io.Serializable;

/**
 * MetaInfo about a QPRF report. 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class QprfReportMeta implements Serializable {

    private static final long serialVersionUID = 1445812487948234L;
    /**
     * Stereochemical features that may affect the reliability of the
     * prediction
     */
    private String stereoFeatures;
    /**
     * Information that help identify the model version
     * (free text)
     */
    private String modelVersion;
    /**
     * Comment on the predicted value
     */
    private String section32e;
    /**
     * Comment on the uncertainty of the prediction
     */
    private String section34;
    /**
     * The chemical and biological mechanisms according to the model
     * underpinning the predicted result (OECD principle 5)
     */
    private String section35;
    /**
     * Considerations on structural analogues
     */
    private String section33c;
    /**
     * Regulatory purpose
     */
    private String section41;
    /**
     * Regulatory interpretation
     */
    private String section42;
    /**
     * Outcome
     */
    private String section43;
    /**
     * Conclusion
     */
    private String section44;
    private String metabolicDomain;
    private String structuralDomain;
    private String descriptorDomain;
    private String mechanismDomain;
    /**
     * Discussion on the QMRF report.
     */
    private String QMRFReportDiscussion;
    /**
     * A reference/link to a QMRF report
     * Can be the URI of an OpenTox QMRF report
     */
    private String QMRFReportReference;

    /**
     * The descriptor domain.
     * 
     * <p><b>General Info about domains:</b>Discuss whether the query chemical 
     * falls in the applicability domain of the model as defined in the corresponding 
     * QMRF (section 5 of QMRF, Defining the applicability domain – OECD Principle 3). 
     * If additional software/methods were used to assess the applicability domain 
     * then they should also be documented in this section.</p>
     * 
     * @return 
     *      Descriptor domain.
     */
    public String getDescriptorDomain() {
        return descriptorDomain;
    }

    /**
     * Setter method for the descriptor domain.
     * 
     * @param descriptorDomain
     *      The descriptor domain as String.
     * 
     * @return 
     *      The current instance of QprfReportMeta updated with the
     *      descriptor domain information provided by this setter.
     */
    public QprfReportMeta setDescriptorDomain(String descriptorDomain) {
        this.descriptorDomain = descriptorDomain;
        return this;
    }

    /**
     * QMRF-report discussion.
     * 
     * @return 
     *      QMRF-report related information and discussion.
     * 
     * @see #getQMRFReportReference() 
     */
    public String getQMRFReportDiscussion() {
        return QMRFReportDiscussion;
    }

    /**
     * Setter method for the discussion about the corresponding QMRP report.
     * 
     * @param qmrfReportDiscussion 
     *      Discussion about the corresponding QMRP report.
     * 
     * @return 
     *      The current instance of QprfReportMeta updated with the
     *      discussion about the corresponding QMRP report.
     */
    public QprfReportMeta setQMRFReportDiscussion(String qmrfReportDiscussion) {
        this.QMRFReportDiscussion = qmrfReportDiscussion;
        return this;
    }

    /**
     * Predicted value (comments).
     * 
     * <p>If the result is qualitative (e.g. yes/no) or semi-quantitative 
     * (e.g. low/medium/high), explain the cut-off values that were used as the 
     * basis for classification. In reporting the predicted value, pay attention 
     * to the transformations (e.g. if the prediction is made in log units, 
     * apply anti-logarithm function).</p>
     * 
     * @return 
     *      Comments on the predicted value.
     */
    public String getSec32e() {
        return section32e;
    }

    /**
     * Setter method for comments on the predicted value.
     * 
     * @param sec32e 
     *      Comments on the predicted value.
     * 
     * @return 
     *      The current instance of QprfReportMeta updated with comments
     *      on the predicted value.
     *      
     */
    public QprfReportMeta setSec32e(String sec32e) {
        this.section32e = sec32e;
        return this;
    }

    /**
     * Considerations on structural analogues.
     * 
     * <p>Discuss how predicted and experimental
     * data for analogues support the prediction of the chemical
     * under consideration.</p>
     * 
     * @return 
     *      Content of Section 3.3.c: Considerations on structural analogues
     */
    public String getSec33c() {
        return section33c;
    }

    /**
     * Setter method for the discussion on structural analogies.
     * 
     * @param sec33c 
     *      The discussion on structural analogies as String.
     * 
     * @return 
     *      The current instance of QprfReportMeta updated with a
     *      discussion on structural analogies.
     */
    public QprfReportMeta setSec33c(String sec33c) {
        this.section33c = sec33c;
        return this;
    }

    /**
     * The uncertainty of the prediction (OECD principle 4).
     * <p>If possible, comment on the uncertainty of the prediction for 
     * this chemical, taking into account relevant information (e.g. variability 
     * of the experimental results). </p>
     * 
     * @return 
     *      Content of section 3.4.
     */
    public String getSec34() {
        return section34;
    }

    /**
     * Setter method for the uncertainty of the prediction (OECD principle 4).
     * 
     * @param sec34 
     *      The uncertainty of the prediction
     * 
     * @return 
     *      The current instance of QprfReportMeta updated with the
     *      uncertainty of the prediction.
     */
    public QprfReportMeta setSec34(String sec34) {
        this.section34 = sec34;
        return this;
    }

    /**
     * The chemical and biological mechanisms according to the model underpinning 
     * the predicted result (OECD principle 5).  
     * 
     * <p>Discuss the mechanistic interpretation of the model prediction 
     * for this specific chemical. For an expert system based on structural 
     * alerts (e.g. Derek for Windows, OncologicTM) the rationale for the 
     * structural alert fired should be provided.</p>
     * 
     * @return 
     *      Content of section 3.5.
     */
    public String getSec35() {
        return section35;
    }

    /**
     * Setter method for the chemical and biological mechanisms according 
     * to the model underpinning the predicted result (OECD principle 5).
     * 
     * @param sec35  
     *      The content of section 3.4.
     * 
     * @return 
     *      The current instance of QprfReportMeta updated with the
     *      chemical and biological mechanisms according to the model 
     *      underpinning the predicted result.
     */
    public QprfReportMeta setSec35(String sec35) {
        this.section35 = sec35;
        return this;
    }

    /**
     * The regulatory purpose.
     * 
     * <p><b>Generally:</b> The information provided in this section might be useful, depending on 
     * the reporting needs and formats of the regulatory framework of interest. 
     * This information aims to facilitate considerations about the adequacy 
     * of the (Q)SAR prediction (result) estimate. A (Q)SAR prediction may or 
     * may not be considered adequate (“fit-for-purpose”), depending on whether 
     * the prediction is sufficiently reliable and relevant in relation to the 
     * particular regulatory purpose. The adequacy of the prediction also depends 
     * on the availability of other information, and is determined in a 
     * weight-of-evidence assessment.</p>
     * 
     * <p><b>Regulatory Purpose:</b> Explain the regulatory purpose for which the 
     * prediction described in Section 3 is being used.</p>
     * 
     */
    public String getSec41() {
        return section41;
    }

    /**
     * Setter method for the regulatory purpose of the prediction.
     * 
     * @param sec41 
     *      The regulatory purpose.
     * 
     * @return 
     *      The updated current instance of QprfReportMeta.
     */
    public QprfReportMeta setSec41(String sec41) {
        this.section41 = sec41;
        return this;
    }

    /**
     * Approach for regulatory interpretation of the model result.
     * 
     * <p>Describe how the predicted result is going to be interpreted in light 
     * of the specific regulatory purpose (e.g. by applying an algorithm or 
     * regulatory criteria). This may involve the need to convert the units of 
     * the dependent variable (e.g. from log molar units to mg/l). It may also 
     * involve the application of another algorithm, an assessment factor, or 
     * regulatory criteria, and the use or consideration of additional 
     * information in a weight-of-evidence assessment.</p>
     * 
     * @return 
     *      Content of the section 4.2. on the approach for regulatory 
     *      interpretation of the model result.
     */
    public String getSec42() {
        return section42;
    }

    /**
     * Setter method for the approach for regulatory interpretation of 
     * the model result
     * 
     * @param sec42 
     *      The approach for regulatory interpretation of the model result
     * 
     * @return 
     *      The updated current instance of QprfReportMeta.
     */
    public QprfReportMeta setSec42(String sec42) {
        this.section42 = sec42;
        return this;
    }

    /**
     * Outcome.
     * 
     * <p>Report the interpretation of the model result in relation to the 
     * defined regulatory purpose.</p>
     * 
     * @return 
     *      Content of section 4.3. of the QPRF report on the outcome of the 
     *      prediction.
     */
    public String getSec43() {
        return section43;
    }

    /**
     * Setter method for the outcome of the prediction.
     * 
     * @param sec43  
     *      The outcome of the prediction
     * 
     * @return 
     *      The current instance of QprfReportMeta updated with the outcome of 
     *      the prediction.
     */
    public QprfReportMeta setSec43(String sec43) {
        this.section43 = sec43;
        return this;
    }

    /**
     * Conclusion.
     * 
     * <p>Provide  an assessment of whether the final result is considered 
     * adequate for a regulatory conclusion, or whether additional information 
     * is required (and, if so, what this additional information should be).</p>
     * 
     * @return
     *      Content of section 4.4.: Conclusion.
     */
    public String getSec44() {
        return section44;
    }

    
    /**
     * Setter method for the conclusion of the prediction.
     * 
     * @param sec44 
     *      The conclusion of the prediction
     * 
     * @return 
     *      The current instance of QprfReportMeta updated with
     *      the conclusion of the prediction.
     */
    public QprfReportMeta setSec44(String sec44) {
        this.section44 = sec44;
        return this;
    }

    /**
     * Stereochemical features. 
     * 
     * <p>Indicate whether the substance is a stereo-isomer and consequently may 
     * have properties that depend on the orientation of its atoms in space. 
     * Identify the stereochemical features that may affect the reliability of 
     * predictions for the substance, e.g. cis/trans isomerism, chiral centres. 
     * Are these features encoded in the structural representations mentioned above?</p>
     * 
     * @return 
     *      The stereo-chemical features.
     */
    public String getStereoFeatures() {
        return stereoFeatures;
    }

    /**
     * Setter method for the stereo-chemical features of the compound that may
     * affect the reliability of the prediction.
     * 
     * @param stereoFeatures 
     *      The stereo-chemical features
     * 
     * @return 
     *      The current instance of QprfReportMeta updated with a
     *      discussion on the stereo-chemical features.
     */
    public QprfReportMeta setStereoFeatures(String stereoFeatures) {
        this.stereoFeatures = stereoFeatures;
        return this;
    }

    /**
     * Structural domain information.
     * @return 
     *      Structural domain.
     */
    public String getStructuralDomain() {
        return structuralDomain;
    }

    /**
     * Setter method for the structural domain of the prediction.
     * 
     * @param structuralDomain   
     *      The mechanism domain of the prediction.
     * 
     * @return 
     *      The current instance of QprfReportMeta updated with info on
     *      the structural domain of the prediction.
     */
    public QprfReportMeta setStructuralDomain(String structuralDomain) {
        this.structuralDomain = structuralDomain;
        return this;
    }

    /**
     * Model version. 
     * 
     * <p>Identify, where relevant, the version number and/or date of 
     * the model and sub-model.</p>
     * 
     * @return 
     *      Version of the model as String.
     */
    public String getModelVersion() {
        return modelVersion;
    }

    /**
     * Setter method for the version of the model that was used to derive
     * the prediction.
     * 
     * @param modelVersion 
     *      The mechanism domain of the prediction.
     * 
     * @return 
     *      The current instance of QprfReportMeta updated with info on
     *      the model version.
     */
    public QprfReportMeta setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
        return this;
    }

    /**
     * Mechanistic domain.
     * @return 
     *      The mechanistic domain as String.
     */
    public String getMechanismDomain() {
        return mechanismDomain;
    }

    
    /**
     * Setter method for the mechanism domain of the prediction.
     * 
     * @param mechanismDomain  
     *      The mechanism domain of the prediction.
     * 
     * @return 
     *      The current instance of QprfReportMeta updated with info on
     *      the mechanism domain of the prediction.
     */
    public QprfReportMeta setMechanismDomain(String mechanismDomain) {
        this.mechanismDomain = mechanismDomain;
        return this;
    }

    /**
     * Metabolic domain.
     * @return 
     *      The metabolic domain as String.
     */
    public String getMetabolicDomain() {
        return metabolicDomain;
    }

    /**
     * Setter method for the metabolic domain of the prediction.
     * 
     * @param metabolicDomain 
     *      The metabolic domain of the prediction.
     * 
     * @return 
     *      The current instance of QprfReportMeta updated with info on
     *      the metabolic domain of the prediction.
     */
    public QprfReportMeta setMetabolicDomain(String metabolicDomain) {
        this.metabolicDomain = metabolicDomain;
        return this;
    }

    /**
     *QMRF report: Section 3,2(c) of the QPRF report specification.
     * 
     * <p>Provide relevant information about the QMRF that stores information 
     * about the model used to make the prediction. Possible useful pieces of 
     * information are: availability, source, reference number (if any) of the 
     * QMRF. Examples: “The corresponding QMRF named ‘BIOWIN for Biodegradation’ 
     * has been downloaded from the JRC QSAR Model Database”; 
     * “The corresponding QMRF named ‘TOPKAT Skin Irritation Acyclics 
     * (Acids, Amines, Esters) MOD v SEV Model’ has been newly compiled”.</p>
     * 
     * @return
     *       Reference to a relevant QMRF report.
     */
    public String getQMRFReportReference() {
        return QMRFReportReference;
    }

    /**
     * Setter method for a QMRF-report reference.
     * 
     * @param qmrfReportReference 
     *      A QMRF-report reference.
     * 
     * @return 
     *      The current instance of QprfReportMeta updated with a
     *      QMRF-report reference if any.
     */
    public QprfReportMeta setQMRFReportReference(String qmrfReportReference) {
        this.QMRFReportReference = qmrfReportReference;
        return this;
    }
}