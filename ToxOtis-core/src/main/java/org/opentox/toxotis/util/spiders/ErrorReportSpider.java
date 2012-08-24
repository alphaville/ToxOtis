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
package org.opentox.toxotis.util.spiders;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import java.net.URISyntaxException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.ErrorReport;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.collection.OTDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTObjectProperties;

/**
 * Parser for an ErrorReport RDF representation.
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class ErrorReportSpider extends Tarantula<ErrorReport> {

    /**
     * Create an empty error report.
     */
    public ErrorReportSpider() {
    }

    /**
     * Create an ErrorReportSpider entity using a given ontological resource and
     * ontological model.
     * 
     * @param resource
     *      The error report resource - the basis element.
     * @param model 
     *      The ontological model to contain the resource.
     */
    public ErrorReportSpider(Resource resource, OntModel model) {
        super(resource, model);
    }

    /**
     * Create an ErrorReportSpider over a given ontological model.
     * @param model 
     *      An instance of OntModel.
     */
    public ErrorReportSpider(OntModel model) {
        super();
        setOntModel(model);
        Property traceProperty = model.getProperty(OTObjectProperties.trace().getUri());
        if (traceProperty != null) {
            StmtIterator traceIter = model.listStatements(new SimpleSelector(null, traceProperty, (RDFNode) null));
            while (traceIter.hasNext()) {
                Statement traceStatement = traceIter.next();
                if (traceStatement.getObject() != null && traceStatement.getSubject() == null) {
                    setResource(traceStatement.getObject().as(Resource.class));
                    return;
                }
            }
        }
        if (getResource() == null) {
            StmtIterator allErrorReportsIter = model.listStatements(
                    new SimpleSelector(null, RDF.type, OTClasses.errorReport().inModel(model)));
            if (allErrorReportsIter.hasNext()) {
                setResource(allErrorReportsIter.next().getSubject().as(Resource.class));
            }
        }

    }

    /**
     * Create a new Error Spider that searches in a data model of an error
     * report for a node that has a specified actor. Use this constructor only if
     * you need to parse an Error Report for which you know the actor otherwise use
     * {@link ErrorReport#ErrorReport() }
     * @param actor
     *      URI of the <b>actor</b> of the Error Report; this is not the URI of
     *      the error report itself but the URI of the peer that produced the
     *      report.
     * @param model
     *      Ontological model of the error report.
     */
    ErrorReportSpider(VRI actor, OntModel model) {
        setOntModel(model);
        Property actorProperty = OTDatatypeProperties.actor().asDatatypeProperty(model);
        if (actorProperty != null) {
            StmtIterator reportIt = model.listStatements(new SimpleSelector(null, actorProperty, actor.getStringNoQuery()));
            if (reportIt.hasNext()) {
                setResource(reportIt.nextStatement().getSubject().as(Resource.class));
            }
        }
    }

    @Override
    public ErrorReport parse() {
        ErrorReport errorReport = new ErrorReport();
        if (getResource() != null) {
            try {
                errorReport.setUri(new VRI(getResource().getURI()));
            } catch (URISyntaxException ex) {
                throw new IllegalArgumentException("Bad URI Syntax",ex);
            }
        }
        if (getResource() == null){
            return null;//nothing to parse!
        }
        errorReport.setMeta(new MetaInfoSpider(getResource(), getOntModel()).parse());

        Statement httpStatusStmt = getResource().getProperty(
                OTDatatypeProperties.httpStatus().asDatatypeProperty(getOntModel()));
        if (httpStatusStmt != null) {
            Literal httpStatus = httpStatusStmt.getObject().as(Literal.class);

            if (httpStatus != null) {
                errorReport.setHttpStatus(httpStatus.getInt());
            }
        }

        Literal errorCode = getResource().getProperty(
                OTDatatypeProperties.errorCode().asDatatypeProperty(getOntModel())).getObject().as(Literal.class);
        if (errorCode != null) {
            errorReport.setErrorCode(errorCode.getString());
        }

        
        
            Statement detailsStmt = getResource().getProperty(OTDatatypeProperties.details().
                    asDatatypeProperty(getOntModel()));
            if (detailsStmt != null) {
                Literal details = detailsStmt.getObject().as(Literal.class);

                if (details != null) {
                    errorReport.setDetails(details.getString());
                }
            }
        

        Literal message = getResource().getProperty(
                OTDatatypeProperties.message().asDatatypeProperty(getOntModel())).getObject().as(Literal.class);

        if (message != null) {
            errorReport.setMessage(message.getString());
        }

        Literal actor = getResource().getProperty(
                OTDatatypeProperties.actor().asDatatypeProperty(getOntModel())).getObject().as(Literal.class);

        if (actor != null) {
            errorReport.setActor(actor.getString());
        }

        Statement traceProp = getResource().getProperty(OTObjectProperties.errorReport().
                asObjectProperty(getOntModel()));
        if (traceProp != null) {
            Resource errorCause = traceProp.getResource();
            if (errorCause != null) {
                ErrorReportSpider causeSpider = new ErrorReportSpider(errorCause, getOntModel());
                errorReport.setErrorCause(causeSpider.parse());
            }
        }

        return errorReport;
    }
}
