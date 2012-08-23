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

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import java.io.Closeable;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.opentox.toxotis.client.HttpStatusCodes;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.exceptions.impl.ConnectionException;
import org.opentox.toxotis.exceptions.impl.ForbiddenRequest;
import org.opentox.toxotis.exceptions.impl.NotFound;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.Unauthorized;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.ResourceValue;
import org.opentox.toxotis.ontology.collection.OTAlgorithmTypes;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.impl.OntologicalClassImpl;

/**
 * A generic parser for OpenTox components.
 * @param <Result>
 *      The expected result or parsed object from the method {@link Tarantula#parse() parse()} in this class.
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public abstract class Tarantula<Result> implements Closeable {

    private static final boolean verbose = false;
    /**
     * The core resource that is to be parsed out of the
     * datamodel into an OpenTox component object.
     */
    protected Resource resource;
    /**
     * The data model which is used in the parsing procedure
     * and also hold the {@link Tarantula#resource base resource}
     */
    protected OntModel model;
    /**
     * Time in ms that was needed for the reading the data model from
     * the remote location identified by the URI of the parsed entity.
     */
    protected long readRemoteTime = -1;
    /**
     * Computational time needed for the parsing of the data model. Does not include the
     * time needed for the entity to be downloaded and/or converted into a
     * datamodel (OntModel of Jena).
     */
    protected long parseTime = -1;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Tarantula.class);

    /**
     * Constructor of a generic parsing processor with a given data model and
     * a base resource which is the target of the parsing processor.
     * @param resource
     *      The base resource which is to the parsed by the method
     *      {@link Tarantula#parse() }
     * @param model
     */
    public Tarantula(Resource resource, OntModel model) {
        this.resource = resource;
        this.model = model;
    }

    public Tarantula() {
    }

    /**
     * Parse the data model of an OpenTox entity and create an ToxOtis object
     * according to the data content of the data model.
     *
     * @return
     *      The parsed object.
     * @throws ServiceInvocationException
     */
    public abstract Result parse() throws ServiceInvocationException;    
    
    protected Set<LiteralValue> retrievePropertyLiterals(Property prop) {
        Set<LiteralValue> results = new HashSet<LiteralValue>();
        StmtIterator it = model.listStatements(new SimpleSelector(resource, prop, (RDFNode) null));
        try {
            while (it.hasNext()) {
                RDFNode node = it.nextStatement().getObject();
                if (node.isLiteral()) {
                    XSDDatatype datatype = (XSDDatatype) node.as(Literal.class).getDatatype();
                    String stringVal = node.as(Literal.class).getString();
                    if (datatype != null && datatype.equals(XSDDatatype.XSDdateTime)) {
                        // For the following bug fix, credits go to FABIAN BUCHWALD!!!!!!
                        DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy",
                                Locale.ENGLISH);
                        try {
                            try {
                                Date date = (Date) formatter.parse(stringVal);
                                results.add(new LiteralValue<Date>(date, datatype));
                            } catch (ParseException ex) {

                                long longDate = Long.parseLong(stringVal);
                                results.add(new LiteralValue<Date>(new Date(longDate), datatype));
                            }
                        } catch (NumberFormatException nfe) {
                            logger.warn("Date format not supported.");
                        }
                    } else {
                        results.add(new LiteralValue(stringVal, datatype));
                    }
                } else if (node.isResource()) {
                    logger.info("Non-literal value found for property : " + prop.getURI());
                    logger.info("URI of that value is : " + node.as(Resource.class).getURI());
                }
            }
        } finally {
            it.close();
        }
        return results;
    }

    protected Set<ResourceValue> retrievePropertyNodes(Property prop) {
        Set<ResourceValue> results = new HashSet<ResourceValue>();
        StmtIterator it = model.listStatements(new SimpleSelector(resource, prop, (RDFNode) null));
        while (it.hasNext()) {
            try {
                RDFNode node = it.nextStatement().getObject();
                if (node.isResource()) {
                    Resource value = node.as(Resource.class);
                    try {
                        results.add(
                                new ResourceValue(value.getURI() != null ? new VRI(value.getURI()) : null,
                                OTClasses.forName(value.getLocalName())));
                    } catch (URISyntaxException ex) {
                        logger.debug(null, ex);
                        throw new IllegalArgumentException(ex);
                    }
                } else if (node.isLiteral()) {
                    if (verbose) {
                        logger.warn("Parsing warning (no exception is thrown). Timestamp : " + new Date(System.currentTimeMillis()) + ".");
                        logger.warn("Details: Found literal value while expecting a resource for the property :" + prop.getURI());
                        logger.warn("Property value : " + node.as(Literal.class).getString());
                    }
                }
            } finally {
                it.close();
            }
        }
        return results;
    }

    protected Set<OntologicalClass> getOTATypes(Resource currentResource) {
        Set<OntologicalClass> ontClasses = new HashSet<OntologicalClass>();

        StmtIterator classIt = model.listStatements(
                new SimpleSelector(currentResource, RDF.type,
                (RDFNode) null));
        Set<OntClass> ontClassSet = new HashSet<OntClass>();
        while (classIt.hasNext()) {
            OntClass tempClass = classIt.nextStatement().getObject().as(OntClass.class);
            if (tempClass.getNameSpace().equals(OTAlgorithmTypes.NS)) {
                ontClassSet.add(tempClass);
                ontClassSet = getSuperTypes(ontClassSet);
            }
        }
        for (OntClass oc : ontClassSet) {
            ontClasses.add(OTAlgorithmTypes.forName(oc.getLocalName()));
        }
        return ontClasses;
    }

    protected Set<OntologicalClass> getOntologicalTypes(Resource currentResource) {
        Set<OntologicalClass> ontClasses = new HashSet<OntologicalClass>();
        StmtIterator classIt = model.listStatements(new SimpleSelector(currentResource, RDF.type, (RDFNode) null));
        Set<OntClass> ontClassSet = new HashSet<OntClass>();
        while (classIt.hasNext()) {
            OntClass tempClass = null;
            tempClass = classIt.nextStatement().getObject().as(OntClass.class);
            if (tempClass != null && OTClasses.NS.equals(tempClass.getNameSpace())) {
                ontClassSet.add(tempClass);
                ontClassSet = getSuperTypes(ontClassSet);
            }
        }
        for (OntClass oc : ontClassSet) {
            OntologicalClass tempOC = OTClasses.forName(oc.getLocalName());
            if (tempOC != null) {
                ontClasses.add(tempOC);
            } else {
                ontClasses.add(new OntologicalClassImpl(oc.getLocalName(), oc.getNameSpace()));
            }

        }
        return ontClasses;
    }

    private Set<OntClass> getSuperTypes(Set<OntClass> ontClasses) {
        Set<OntClass> newSet = new HashSet<OntClass>();
        newSet.addAll(ontClasses);
        for (OntClass oc : ontClasses) {
            newSet.addAll(oc.listSuperClasses().toSet());
        }
        if (!ontClasses.equals(newSet)) {
            newSet.addAll(getSuperTypes(newSet));
        }
        return newSet;
    }

    /**
     * Closes the ontological model used by the Spider (if any) and releases all
     * resources it holds. Closing the Spider is considered good practise and prefered
     * comparing to leaving it to the finalizer.
     */
    @Override
    public void close() {
        if (model != null) {
            model.close();
        }
    }

    /**
     * Returns the ontological model held by this parser.
     * 
     * @return
     *      Ontological Model of the processed resource.
     */
    public OntModel getOntModel() {
        return model;
    }

    /**
     * Get the time needed to parse the Ontological Model into
     * an in-house component of ToxOtis. This time does not include the time
     * needed to download the Ontological Model from the remote location.
     *
     * @return
     *      Parsing time. The method will return <code>-1</code> in case the method
     *      {@link Tarantula#parse() parse} has not been invoked.
     */
    public long getParseTime() {
        return parseTime;
    }

    /**
     * Get the time needed to download the Ontological Model and cast it as an
     * Ontological model (instance of <code>com.hp.hpl.jena.ontology.OntModel</code>).
     *
     * @return
     *      Download time plus the time needed by the method <code>read(InputStream, String)</code>
     *      in <code>com.hp.hpl.jena.ontology.OntModel</code> to convert the representation
     *      into an Ontological Model. The method returns <code>-1</code> if the underlying
     *      Spider (subclass of Tarantula) was not properly initialized or the model was not
     *      loaded from a remote location.
     */
    public long getReadRemoteTime() {
        return readRemoteTime;
    }

    protected void assessHttpStatus(int status, VRI actionUri) throws ServiceInvocationException {
        if (status == HttpStatusCodes.Forbidden.getStatus()) {
            throw new ForbiddenRequest("Access denied to : '" + actionUri + "' (status 403)");
        }
        if (status == HttpStatusCodes.Unauthorized.getStatus()) {
            throw new Unauthorized("User is not authorized to access : '" + actionUri + "' (status 401)");
        }
        if (status == HttpStatusCodes.NotFound.getStatus()) {
            throw new NotFound("The following task was not found : '" + actionUri + "' (status 404)");
        }
        if (status != HttpStatusCodes.Success.getStatus() && 
                status != HttpStatusCodes.Accepted.getStatus() && 
                status != HttpStatusCodes.Created.getStatus()) {
            throw new ConnectionException("Communication Error with : '" + actionUri + "'. status = " + status);
        }
    }
}
