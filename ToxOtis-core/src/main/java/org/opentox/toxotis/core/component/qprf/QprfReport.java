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

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.OTPublishable;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class QprfReport extends OTPublishable<OTPublishable> implements Serializable {

    /**
     * The URI of the model for the the QPRF report is generated
     * (i.e. the model which creates the prediction)
     */
    private VRI modelUri;
    /**
     * Uri of the compund for which the QPRF report is created
     */
    private VRI compoundUri;
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
     * Set of authors of this QPRF report
     * Every author is identified by a URI from where it's representation in
     * RDF is available using the FOAF web ontology.
     * see http://xmlns.com/foaf/spec/
     */
    private HashSet<VRI> authors = new HashSet<VRI>();
    /**
     * The date (timestamp) of the QPRF report. Can be easily exported as
     * java.util.Date or java.sql.Date.
     */
    private Long report_date = System.currentTimeMillis();
    /**
     * Timestamp of model creation
     */
    private Long model_date;
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
    /*
     * Creator - The user that holds the report which is not
     * necessary one of its authors (e.g. the user that POSTs the report
     * online and creates it as a resource; an example can be the 'guest'
     * user identified by guest@opensso.in-silico.ch)
     */
    private User createdBy;
    private QprfReportMeta reportMeta;

    public QprfReport() {
        super();
    }

    public QprfReport(VRI uri) {
        super(uri);
    }

    public QprfReportMeta getReportMeta() {
        return reportMeta;
    }

    public void setReportMeta(QprfReportMeta reportMeta) {
        this.reportMeta = reportMeta;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public String getQMRFreference() {
        return QMRFreference;
    }

    public QprfReport setQMRFreference(String QMRFreference) {
        this.QMRFreference = QMRFreference;
        return this;
    }

    public LiteralValue getApplicabilityDomainResult() {
        return applicabilityDomainResult;
    }

    public QprfReport setApplicabilityDomainResult(LiteralValue applicabilityDomainResult) {
        this.applicabilityDomainResult = applicabilityDomainResult;
        return this;
    }

    public HashSet<VRI> getAuthors() {
        return authors;
    }

    public QprfReport setAuthors(HashSet<VRI> authors) {
        this.authors = authors;
        return this;
    }

    public VRI getCompoundUri() {
        return compoundUri;
    }

    public QprfReport setCompoundUri(VRI compoundUri) {
        this.compoundUri = compoundUri;
        return this;
    }

    public VRI getDatasetStructuralAnalogues() {
        return datasetStructuralAnalogues;
    }

    public QprfReport setDatasetStructuralAnalogues(VRI datasetStructuralAnalogues) {
        this.datasetStructuralAnalogues = datasetStructuralAnalogues;
        return this;
    }

    public VRI getDoaUri() {
        return doaUri;
    }

    public QprfReport setDoaUri(VRI doaUri) {
        this.doaUri = doaUri;
        return this;
    }

    public LiteralValue getExperimentalResult() {
        return experimentalResult;
    }

    public QprfReport setExperimentalResult(LiteralValue experimentalResult) {
        this.experimentalResult = experimentalResult;
        return this;
    }

    public String getKeywords() {
        return keywords;
    }

    public QprfReport setKeywords(String keywords) {
        this.keywords = keywords;
        return this;
    }

    public VRI getModelUri() {
        return modelUri;
    }

    public QprfReport setModelUri(VRI modelUri) {
        this.modelUri = modelUri;
        return this;
    }

    public Long getModelDate() {
        return model_date;
    }

    public QprfReport setModelDate(long model_date) {
        this.model_date = model_date;
        return this;
    }

    public LiteralValue getPredictionResult() {
        return predictionResult;
    }

    public QprfReport setPredictionResult(LiteralValue predictionResult) {
        this.predictionResult = predictionResult;
        return this;
    }

    public Long getReportDate() {
        return report_date;
    }

    public QprfReport setReportDate(long report_date) {
        this.report_date = report_date;
        return this;
    }

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
