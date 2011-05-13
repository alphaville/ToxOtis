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
     * Discussion on the QMRF report.
     */
    private String QMRFReportDiscussion;

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

    public QprfReportMeta setQMRFReportDiscussion(String QMRFReportDiscussion) {
        this.QMRFReportDiscussion = QMRFReportDiscussion;
        return this;
    }

    public String getSec_3_2_e() {
        return sec_3_2_e;
    }

    public QprfReportMeta setSec_3_2_e(String sec_3_2_e) {
        this.sec_3_2_e = sec_3_2_e;
        return this;
    }

    public String getSec_3_3_c() {
        return sec_3_3_c;
    }

    public QprfReportMeta setSec_3_3_c(String sec_3_3_c) {
        this.sec_3_3_c = sec_3_3_c;
        return this;
    }

    public String getSec_3_4() {
        return sec_3_4;
    }

    public QprfReportMeta setSec_3_4(String sec_3_4) {
        this.sec_3_4 = sec_3_4;
        return this;
    }

    public String getSec_3_5() {
        return sec_3_5;
    }

    public QprfReportMeta setSec_3_5(String sec_3_5) {
        this.sec_3_5 = sec_3_5;
        return this;
    }

    public String getSec_4_1() {
        return sec_4_1;
    }

    public QprfReportMeta setSec_4_1(String sec_4_1) {
        this.sec_4_1 = sec_4_1;
        return this;
    }

    public String getSec_4_2() {
        return sec_4_2;
    }

    public QprfReportMeta setSec_4_2(String sec_4_2) {
        this.sec_4_2 = sec_4_2;
        return this;
    }

    public String getSec_4_3() {
        return sec_4_3;
    }

    public QprfReportMeta setSec_4_3(String sec_4_3) {
        this.sec_4_3 = sec_4_3;
        return this;
    }

    public String getSec_4_4() {
        return sec_4_4;
    }

    public QprfReportMeta setSec_4_4(String sec_4_4) {
        this.sec_4_4 = sec_4_4;
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

}