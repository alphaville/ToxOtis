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
        this.model = model;
        Property traceProperty = model.getProperty(OTObjectProperties.trace().getUri());
        if (traceProperty != null) {
            StmtIterator traceIter = model.listStatements(new SimpleSelector(null, traceProperty, (RDFNode) null));
            while (traceIter.hasNext()) {
                Statement traceStatement = traceIter.next();
                if (traceStatement.getObject() != null && traceStatement.getSubject() == null) {
                    resource = traceStatement.getObject().as(Resource.class);
                    return;
                }
            }
        }
        if (resource == null) {
            StmtIterator allErrorReportsIter = model.listStatements(
                    new SimpleSelector(null, RDF.type, OTClasses.ErrorReport().inModel(model)));
            if (allErrorReportsIter.hasNext()) {
                resource = allErrorReportsIter.next().getSubject().as(Resource.class);
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
        this.model = model;
        Property actorProperty = OTDatatypeProperties.actor().asDatatypeProperty(model);
        if (actorProperty != null) {
            StmtIterator reportIt = model.listStatements(new SimpleSelector(null, actorProperty, actor.getStringNoQuery()));
            if (reportIt.hasNext()) {
                resource = reportIt.nextStatement().getSubject().as(Resource.class);
            }
        }
    }

    @Override
    public ErrorReport parse() {
        ErrorReport errorReport = new ErrorReport();
        if (resource != null) {
            try {
                errorReport.setUri(new VRI(resource.getURI()));
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (resource == null){
            return null;//nothing to parse!
        }
        errorReport.setMeta(new MetaInfoSpider(resource, model).parse());

        Statement httpStatusStmt = resource.getProperty(
                OTDatatypeProperties.httpStatus().asDatatypeProperty(model));
        if (httpStatusStmt != null) {
            Literal httpStatus = httpStatusStmt.getObject().as(Literal.class);

            if (httpStatus != null) {
                errorReport.setHttpStatus(httpStatus.getInt());
            }
        }

        Literal errorCode = resource.getProperty(
                OTDatatypeProperties.errorCode().asDatatypeProperty(model)).getObject().as(Literal.class);
        if (errorCode != null) {
            errorReport.setErrorCode(errorCode.getString());
        }

        
        
            Statement detailsStmt = resource.getProperty(OTDatatypeProperties.details().asDatatypeProperty(model));
            if (detailsStmt != null) {
                Literal details = detailsStmt.getObject().as(Literal.class);

                if (details != null) {
                    errorReport.setDetails(details.getString());
                }
            }
        

        Literal message = resource.getProperty(
                OTDatatypeProperties.message().asDatatypeProperty(model)).getObject().as(Literal.class);

        if (message != null) {
            errorReport.setMessage(message.getString());
        }

        Literal actor = resource.getProperty(
                OTDatatypeProperties.actor().asDatatypeProperty(model)).getObject().as(Literal.class);

        if (actor != null) {
            errorReport.setActor(actor.getString());
        }

        Statement traceProp = resource.getProperty(OTObjectProperties.errorReport().asObjectProperty(model));
        if (traceProp != null) {
            Resource errorCause = traceProp.getResource();
            if (errorCause != null) {
                ErrorReportSpider causeSpider = new ErrorReportSpider(errorCause, model);
                errorReport.setErrorCause(causeSpider.parse());
            }
        }

        return errorReport;
    }
}
