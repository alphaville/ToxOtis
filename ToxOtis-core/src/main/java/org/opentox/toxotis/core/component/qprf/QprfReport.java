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
import java.util.ArrayList;
import java.util.HashSet;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.OTPublishable;
import org.opentox.toxotis.core.component.Compound;
import org.opentox.toxotis.core.component.Model;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class QprfReport extends OTPublishable<OTPublishable> implements Serializable {

    /**
     * Serialization UID
     */
    private static final long serialVersionUID = 8129724L;
    /**
     * Level of similarity according to which the structural analogues
     * where retrieved for the compound
     */
    private double similarityLevel;
    /**
     * The model of the QPRF report with which the prediction was generated
     */
    private Model model;
    /**
     * The URI of the Domain of Applicability Model that is used
     * along with the predictive model
     */
    private String doaUri;
    /**
     * The Name of the DoA algorithm used
     */
    private String doAName;
    /**
     * The keyword for the compound as provided by the user
     */
    private String keyword;
    /**
     * Set of authors of this QPRF report
     * Every author is identified by a URI from where it's representation in
     * RDF is available using the FOAF web ontology.
     * see http://xmlns.com/foaf/spec/
     */
    private HashSet<QprfAuthor> authors = new HashSet<QprfAuthor>();
    /**
     * The date (timestamp) of the QPRF report. Can be easily exported as
     * java.util.Date or java.sql.Date.
     */
    private Long report_date = System.currentTimeMillis();
    /**
     * Timestamp of model creation.
     */
    private Long model_date;
    /**
     * Result of the applicability domain algorithm on the
     * submitted compound. Usually a YES/NO answer.
     */
    private String applicabilityDomainResult;
    /**
     * Result of the prediction using the QSAR model
     */
    private String predictionResult;
    /**
     * The experimental value for the compound. The units can be
     * retrieved from the dependent feature of the model.
     */
    private String experimentalResult;
    /**
     * Experimental result units
     */
    private String expResultUnits;
    /**
     * Predicted result units
     */
    private String predResultUnits;
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
    private QprfReportMeta reportMeta;
    /**
     * The compound for which the QPRF report is created
     */
    private Compound compound;
    /**
     * ArrayList of structural analogues as a list of compounds
     */
    private ArrayList<Compound> structuralAnalogues;
    /*
     * Experimental values for compounds
     */
    private ArrayList<String> experimentalValues;

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

    public String getQMRFreference() {
        return QMRFreference;
    }

    public QprfReport setQMRFreference(String QMRFreference) {
        this.QMRFreference = QMRFreference;
        return this;
    }

    public String getApplicabilityDomainResult() {
        return applicabilityDomainResult;
    }

    public QprfReport setApplicabilityDomainResult(String applicabilityDomainResult) {
        this.applicabilityDomainResult = applicabilityDomainResult;
        return this;
    }

    public HashSet<QprfAuthor> getAuthors() {
        return authors;
    }

    public QprfReport setAuthors(HashSet<QprfAuthor> authors) {
        this.authors = authors;
        return this;
    }

    public String getDoaUri() {
        return doaUri;
    }

    public QprfReport setDoaUri(String doaUri) {
        this.doaUri = doaUri;
        return this;
    }

    public String getExperimentalResult() {
        return experimentalResult;
    }

    public QprfReport setExperimentalResult(String experimentalResult) {
        this.experimentalResult = experimentalResult;
        return this;
    }

    public String getKeyword() {
        return keyword;
    }

    public QprfReport setKeyword(String keywords) {
        this.keyword = keywords;
        return this;
    }

    public Long getModelDate() {
        return model_date;
    }

    public QprfReport setModelDate(long model_date) {
        this.model_date = model_date;
        return this;
    }

    public String getPredictionResult() {
        return predictionResult;
    }

    public QprfReport setPredictionResult(String predictionResult) {
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

    public Compound getCompound() {
        return compound;
    }

    public QprfReport setCompound(Compound compound) {
        this.compound = compound;
        return this;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public ArrayList<Compound> getStructuralAnalogues() {
        return structuralAnalogues;
    }

    public void setStructuralAnalogues(ArrayList<Compound> structuralAnalogues) {
        this.structuralAnalogues = structuralAnalogues;
    }

    public String getDoAName() {
        return doAName;
    }

    public void setDoAName(String doAName) {
        this.doAName = doAName;
    }

    public double getSimilarityLevel() {
        return similarityLevel;
    }

    public QprfReport setSimilarityLevel(double similarityLevel) {
        this.similarityLevel = similarityLevel;
        return this;
    }

    public String getExpResultUnits() {
        return expResultUnits;
    }

    public QprfReport setExpResultUnits(String expResultUnits) {
        this.expResultUnits = expResultUnits;
        return this;
    }

    public String getPredResultUnits() {
        return predResultUnits;
    }

    public QprfReport setPredResultUnits(String predResultUnits) {
        this.predResultUnits = predResultUnits;
        return this;
    }

    public ArrayList<String> getExperimentalValues() {
        return experimentalValues;
    }

    public void setExperimentalValues(ArrayList<String> experimentalValues) {
        this.experimentalValues = experimentalValues;
    }
    
    
}
