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

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.IGetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.core.component.BibTeX;
import org.opentox.toxotis.core.component.ErrorReport;
import org.opentox.toxotis.exceptions.impl.BadRequestException;
import org.opentox.toxotis.exceptions.impl.ConnectionException;
import org.opentox.toxotis.exceptions.impl.ForbiddenRequest;
import org.opentox.toxotis.exceptions.impl.NotFound;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.exceptions.impl.Unauthorized;
import org.opentox.toxotis.ontology.OTDatatypeProperty;
import org.opentox.toxotis.ontology.collection.KnoufBibTex;
import org.opentox.toxotis.ontology.collection.KnoufDatatypeProperties;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 * Downloads and parses data models of BibTeX resource represented in RDF according
 * to the Knouf BibTeX ontology.
 *
 * @see KnoufBibTex Knouf Ontology
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class BibTeXSprider extends Tarantula<BibTeX> {

    /**
     * URI of the BibTeX to be downloaded and parsed stored in
     * a private field.
     */
    private VRI uri;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BibTeXSprider.class);

    private BibTeXSprider() {
    }

    public BibTeXSprider(Resource resource, OntModel model) throws ServiceInvocationException {
        super(resource, model);
        if (resource != null) {
            try {
                this.uri = new VRI(resource.getURI());
                if (!BibTeX.class.equals(uri.getOpenToxType())) {
                    throw new BadRequestException("Bad URI : Not a BibTeX URI (" + uri + ")");
                }
            } catch (URISyntaxException ex) {
                logger.warn("URI syntax exception thrown for the malformed URI "
                        + resource.getURI() + " found in the RDF graph of the resource at " + uri, ex);
            }
        } else {
            StmtIterator it = model.listStatements(new SimpleSelector(resource, RDF.type, (RDFNode) model.getResource(KnoufBibTex.Entry().getUri())));
            if (it.hasNext()) {
                resource = it.nextStatement().getSubject();
            }
            if (resource == null) {
                it = model.listStatements(new SimpleSelector(resource, RDF.type, (RDFNode) model.getResource(KnoufBibTex.Article().getUri())));
                if (it.hasNext()) {
                    resource = it.nextStatement().getSubject();
                }
            }
            if (resource == null) {
                it = model.listStatements(new SimpleSelector(resource, RDF.type, (RDFNode) model.getResource(KnoufBibTex.Book().getUri())));
                if (it.hasNext()) {
                    resource = it.nextStatement().getSubject();
                }
            }
            if (resource == null) {
                it = model.listStatements(new SimpleSelector(resource, RDF.type, (RDFNode) model.getResource(KnoufBibTex.Conference().getUri())));
                if (it.hasNext()) {
                    resource = it.nextStatement().getSubject();
                }
            }
            if (resource == null) {
                it = model.listStatements(new SimpleSelector(resource, RDF.type, (RDFNode) model.getResource(KnoufBibTex.Phdthesis().getUri())));
                if (it.hasNext()) {
                    resource = it.nextStatement().getSubject();
                }
            }
        }
    }

    public BibTeXSprider(VRI uri) throws ServiceInvocationException {
        this(uri, (AuthenticationToken) null);
    }

    public BibTeXSprider(VRI uri, AuthenticationToken token) throws ServiceInvocationException {
        this.uri = uri;
        IGetClient client = ClientFactory.createGetClient(uri);
        client.authorize(token);
        client.setMediaType(Media.APPLICATION_RDF_XML);
        try {
            int status = client.getResponseCode();
            if (status != 200) {
                OntModel om = client.getResponseOntModel();
                ErrorReportSpider ersp = new ErrorReportSpider(om);
                ErrorReport er = ersp.parse();

                if (status == 403) {
                    ForbiddenRequest fr = new ForbiddenRequest("Access denied to : '" + uri + "'");
                    fr.setErrorReport(er);
                    throw fr;
                }
                if (status == 401) {
                    Unauthorized unauth = new Unauthorized("User is not authorized to access : '" + uri + "'");
                    unauth.setErrorReport(er);
                    throw unauth;
                }
                if (status == 404) {
                    NotFound notFound = new NotFound("The following algorithm was not found : '" + uri + "'");
                    notFound.setErrorReport(er);
                    throw notFound;
                } else {
                    ConnectionException connectionException = new ConnectionException("Communication Error with : '" + uri + "'");
                    connectionException.setErrorReport(er);
                    throw connectionException;

                }
            }
            model = client.getResponseOntModel();
            resource = model.getResource(uri.getStringNoQuery());
        } catch (ServiceInvocationException ex) {
            logger.trace("ServiceInvocationException caught in BibTeXSpider", ex);
            throw ex;
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException ex) {
                    throw new ConnectionException(ex);
                }
            }
        }
    }

    @Override
    public BibTeX parse() throws ServiceInvocationException {
        if (resource == null) {
            resource = getresource(model);
        }
        if (resource == null) {
            throw new BadRequestException("Could not parse the BibTex resource because no "
                    + "bibtex entity (Article, Book, Conference, etc) was found.");
        }

        String hasAbstract = forProperty(model, resource, KnoufDatatypeProperties.hasAbstract());
        String bookTitle = forProperty(model, resource, KnoufDatatypeProperties.hasBookTitle());
        String author = forProperty(model, resource, KnoufDatatypeProperties.hasAuthor());
        String chapter = forProperty(model, resource, KnoufDatatypeProperties.hasChapter());
        String copy = forProperty(model, resource, KnoufDatatypeProperties.hasCopyright());
        String crossref = forProperty(model, resource, KnoufDatatypeProperties.hasCrossRef());
        String isbn = forProperty(model, resource, KnoufDatatypeProperties.hasISBN());
        String issn = forProperty(model, resource, KnoufDatatypeProperties.hasISSN());
        String edition = forProperty(model, resource, KnoufDatatypeProperties.hasEdition());
        String editor = forProperty(model, resource, KnoufDatatypeProperties.hasEditor());
        String journal = forProperty(model, resource, KnoufDatatypeProperties.hasJournal());
        String series = forProperty(model, resource, KnoufDatatypeProperties.hasSeries());
        String title = forProperty(model, resource, KnoufDatatypeProperties.hasTitle());
        String url = forProperty(model, resource, KnoufDatatypeProperties.hasURL());

        String volume = forProperty(model, resource, KnoufDatatypeProperties.hasVolume());
        String number = forProperty(model, resource, KnoufDatatypeProperties.hasNumber());
        String year = forProperty(model, resource, KnoufDatatypeProperties.hasYear());

        StmtIterator typeIterator = model.listStatements(new SimpleSelector(resource, RDF.type, (RDFNode) null));
        BibTeX.BIB_TYPE bibtype = null;
        if (typeIterator.hasNext()) {
            try {
                String bbType = typeIterator.nextStatement().getObject().as(Resource.class).getURI().replaceAll(KnoufBibTex.NS, "");
                bibtype = BibTeX.BIB_TYPE.valueOf(bbType);
            } catch (final NullPointerException npe) {
            } catch (final IllegalArgumentException iae) {
            }
        }

        BibTeX bibtex = null;
        try {
            bibtex = new BibTeX(uri);
        } catch (ToxOtisException ex) {
            logger.warn("Invalid bibtex URI!!!", ex);
            throw new BadRequestException("The URI " + uri + " is not a valid bibtex URI!", ex);
        }
        bibtex.setBibType(bibtype != null ? bibtype : BibTeX.BIB_TYPE.Entry);
        bibtex.setAbstract(hasAbstract);
        bibtex.setAuthor(author);
        bibtex.setBookTitle(bookTitle);
        bibtex.setChapter(chapter);
        bibtex.setCopyright(copy);
        bibtex.setCrossref(crossref);
        bibtex.setEditor(editor);
        bibtex.setEdition(edition);
        bibtex.setIsbn(isbn);
        bibtex.setIssn(issn);
        bibtex.setJournal(journal);
        bibtex.setSeries(series);
        bibtex.setTitle(title);
        bibtex.setUrl(url);

        if (year != null) {
            try {
                bibtex.setYear(Integer.parseInt(year));
            } catch (NumberFormatException ex) {
                throw new BadRequestException("Number was expected", ex);
            }
        }

        if (volume != null) {
            try {
                bibtex.setVolume(Integer.parseInt(volume));
            } catch (NumberFormatException ex) {
                throw new BadRequestException("Number was expected", ex);
            }
        }

        if (number != null) {
            try {
                bibtex.setNumber(Integer.parseInt(number));
            } catch (NumberFormatException ex) {
                throw new BadRequestException("Number was expected", ex);
            }
        }

        return bibtex;
    }

    private Resource getresource(OntModel model) {
        Resource clazz = null;

        if ((clazz = model.getResource(KnoufBibTex.Article().getUri())) != null) {
            StmtIterator it = model.listStatements(new SimpleSelector(null, RDF.type, clazz));
            if (it.hasNext()) {
                return it.nextStatement().getSubject();
            }
        }

        if ((clazz = model.getResource(KnoufBibTex.Book().getUri())) != null) {
            StmtIterator it = model.listStatements(new SimpleSelector(null, RDF.type, clazz));
            if (it.hasNext()) {
                return it.nextStatement().getSubject();
            }
        }

        if ((clazz = model.getResource(KnoufBibTex.Conference().getUri())) != null) {
            StmtIterator it = model.listStatements(new SimpleSelector(null, RDF.type, clazz));
            if (it.hasNext()) {
                return it.nextStatement().getSubject();
            }
        }

        if ((clazz = model.getResource(KnoufBibTex.Phdthesis().getUri())) != null) {
            StmtIterator it = model.listStatements(new SimpleSelector(null, RDF.type, clazz));
            if (it.hasNext()) {
                return it.nextStatement().getSubject();
            }
        }

        if ((clazz = model.getResource(KnoufBibTex.Entry().getUri())) != null) {
            StmtIterator it = model.listStatements(new SimpleSelector(null, RDF.type, clazz));
            if (it.hasNext()) {
                return it.nextStatement().getSubject();
            }
        }
        return null;
    }

    @SuppressWarnings("empty-statement")
    private String forProperty(OntModel model, Resource resource, OTDatatypeProperty prop) {
        if (resource == null) {
            throw new NullPointerException("No target resource specified in the parser!");
        }
        DatatypeProperty dataProp = prop.asDatatypeProperty(model);
        if (dataProp != null) {
            StmtIterator it = model.listStatements(new SimpleSelector(resource, dataProp, (RDFNode) null));
            if (it.hasNext()) {
                return it.nextStatement().getLiteral().getString();
            }
        }
        return null;
    }
}
