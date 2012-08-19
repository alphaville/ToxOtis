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
 *
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
    

    public String getDescriptorDomain() {
        return descriptorDomain;
    }

    public QprfReportMeta setDescriptorDomain(String descriptorDomain) {
        this.descriptorDomain = descriptorDomain;
        return this;
    }

    public String getQMRFReportDiscussion() {
        return QMRFReportDiscussion;
    }

    public QprfReportMeta setQMRFReportDiscussion(String qmrfReportDiscussion) {
        this.QMRFReportDiscussion = qmrfReportDiscussion;
        return this;
    }

    public String getSec32e() {
        return section32e;
    }

    public QprfReportMeta setSec32e(String sec32e) {
        this.section32e = sec32e;
        return this;
    }

    public String getSec33c() {
        return section33c;
    }

    public QprfReportMeta setSec33c(String sec33c) {
        this.section33c = sec33c;
        return this;
    }

    public String getSec34() {
        return section34;
    }

    public QprfReportMeta setSec34(String sec34) {
        this.section34 = sec34;
        return this;
    }

    public String getSec35() {
        return section35;
    }

    public QprfReportMeta setSec35(String sec35) {
        this.section35 = sec35;
        return this;
    }

    public String getSec41() {
        return section41;
    }

    public QprfReportMeta setSec41(String sec41) {
        this.section41 = sec41;
        return this;
    }

    public String getSec42() {
        return section42;
    }

    public QprfReportMeta setSec42(String sec42) {
        this.section42 = sec42;
        return this;
    }

    public String getSec43() {
        return section43;
    }

    public QprfReportMeta setSec43(String sec43) {
        this.section43 = sec43;
        return this;
    }

    public String getSec44() {
        return section44;
    }

    public QprfReportMeta setSec44(String sec44) {
        this.section44 = sec44;
        return this;
    }

    public String getStereoFeatures() {
        return stereoFeatures;
    }

    public QprfReportMeta setStereoFeatures(String stereoFeatures) {
        this.stereoFeatures = stereoFeatures;
        return this;
    }

    public String getStructuralDomain() {
        return structuralDomain;
    }

    public QprfReportMeta setStructuralDomain(String structuralDomain) {
        this.structuralDomain = structuralDomain;
        return this;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public QprfReportMeta setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
        return this;
    }

    public String getMechanismDomain() {
        return mechanismDomain;
    }

    public QprfReportMeta setMechanismDomain(String mechanismDomain) {
        this.mechanismDomain = mechanismDomain;
        return this;
    }

    public String getMetabolicDomain() {
        return metabolicDomain;
    }

    public QprfReportMeta setMetabolicDomain(String metabolicDomain) {
        this.metabolicDomain = metabolicDomain;
        return this;
    }

    public String getQMRFReportReference() {
        return QMRFReportReference;
    }

    public QprfReportMeta setQMRFReportReference(String QMRFReportReference) {
        this.QMRFReportReference = QMRFReportReference;
        return this;
    }
    
    

}