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
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.HttpStatusCodes;
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
            StmtIterator it = model.listStatements(new SimpleSelector(resource, RDF.type, (RDFNode) model.getResource(KnoufBibTex.entry().getUri())));
            if (it.hasNext()) {
                resource = it.nextStatement().getSubject();
            }
            if (resource == null) {
                it = model.listStatements(new SimpleSelector(resource, RDF.type, (RDFNode) model.getResource(KnoufBibTex.article().getUri())));
                if (it.hasNext()) {
                    resource = it.nextStatement().getSubject();
                }
            }
            if (resource == null) {
                it = model.listStatements(new SimpleSelector(resource, RDF.type, (RDFNode) model.getResource(KnoufBibTex.book().getUri())));
                if (it.hasNext()) {
                    resource = it.nextStatement().getSubject();
                }
            }
            if (resource == null) {
                it = model.listStatements(new SimpleSelector(resource, RDF.type, (RDFNode) model.getResource(KnoufBibTex.conference().getUri())));
                if (it.hasNext()) {
                    resource = it.nextStatement().getSubject();
                }
            }
            if (resource == null) {
                it = model.listStatements(new SimpleSelector(resource, RDF.type, (RDFNode) model.getResource(KnoufBibTex.phdThesis().getUri())));
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
            if (status != HttpStatusCodes.Success.getStatus()) {
                OntModel om = client.getResponseOntModel();
                ErrorReportSpider ersp = new ErrorReportSpider(om);
                ErrorReport er = ersp.parse();

                if (status == HttpStatusCodes.Forbidden.getStatus()) {
                    ForbiddenRequest fr = new ForbiddenRequest("Access denied to : '" + uri + "'");
                    fr.setErrorReport(er);
                    throw fr;
                }
                if (status == HttpStatusCodes.Unauthorized.getStatus()) {
                    Unauthorized unauth = new Unauthorized("User is not authorized to access : '" + uri + "'");
                    unauth.setErrorReport(er);
                    throw unauth;
                }
                if (status == HttpStatusCodes.NotFound.getStatus()) {
                    NotFound notFound = new NotFound("The following algorithm was not found : '" + uri + "'");
                    notFound.setErrorReport(er);
                    throw notFound;
                } else {
                    ConnectionException connectionException = new ConnectionException("Communication Error with : '" + uri + "'");
                    connectionException.setErrorReport(er);
                    throw connectionException;

                }
            }
            setOntModel(client.getResponseOntModel());
            setResource(getOntModel().getResource(uri.getStringNoQuery()));
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
        if (getResource() == null) {
            setResource(getresource(getOntModel()));
        }
        if (getResource() == null) {
            throw new BadRequestException("Could not parse the BibTex resource because no "
                    + "bibtex entity (Article, Book, Conference, etc) was found.");
        }

        String hasAbstract = forProperty(getOntModel(), getResource(), KnoufDatatypeProperties.hasAbstract());
        String bookTitle = forProperty(getOntModel(), getResource(), KnoufDatatypeProperties.hasBookTitle());
        String author = forProperty(getOntModel(), getResource(), KnoufDatatypeProperties.hasAuthor());
        String chapter = forProperty(getOntModel(), getResource(), KnoufDatatypeProperties.hasChapter());
        String copy = forProperty(getOntModel(), getResource(), KnoufDatatypeProperties.hasCopyright());
        String crossref = forProperty(getOntModel(), getResource(), KnoufDatatypeProperties.hasCrossRef());
        String isbn = forProperty(getOntModel(), getResource(), KnoufDatatypeProperties.hasISBN());
        String issn = forProperty(getOntModel(), getResource(), KnoufDatatypeProperties.hasISSN());
        String edition = forProperty(getOntModel(), getResource(), KnoufDatatypeProperties.hasEdition());
        String editor = forProperty(getOntModel(), getResource(), KnoufDatatypeProperties.hasEditor());
        String journal = forProperty(getOntModel(), getResource(), KnoufDatatypeProperties.hasJournal());
        String series = forProperty(getOntModel(), getResource(), KnoufDatatypeProperties.hasSeries());
        String title = forProperty(getOntModel(), getResource(), KnoufDatatypeProperties.hasTitle());
        String url = forProperty(getOntModel(), getResource(), KnoufDatatypeProperties.hasURL());

        String volume = forProperty(getOntModel(), getResource(), KnoufDatatypeProperties.hasVolume());
        String number = forProperty(getOntModel(), getResource(), KnoufDatatypeProperties.hasNumber());
        String year = forProperty(getOntModel(), getResource(), KnoufDatatypeProperties.hasYear());

        StmtIterator typeIterator = getOntModel().listStatements(new SimpleSelector(getResource(), RDF.type, (RDFNode) null));
        BibTeX.BibTYPE bibtype = null;
        if (typeIterator.hasNext()) {
            try {
                String bbType = typeIterator.nextStatement().getObject().as(Resource.class).getURI().replaceAll(KnoufBibTex.NS, "");
                bibtype = BibTeX.BibTYPE.valueOf(bbType);
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
        bibtex.setBibType(bibtype != null ? bibtype : BibTeX.BibTYPE.Entry);
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

        if ((clazz = model.getResource(KnoufBibTex.article().getUri())) != null) {
            StmtIterator it = model.listStatements(new SimpleSelector(null, RDF.type, clazz));
            if (it.hasNext()) {
                return it.nextStatement().getSubject();
            }
        }

        if ((clazz = model.getResource(KnoufBibTex.book().getUri())) != null) {
            StmtIterator it = model.listStatements(new SimpleSelector(null, RDF.type, clazz));
            if (it.hasNext()) {
                return it.nextStatement().getSubject();
            }
        }

        if ((clazz = model.getResource(KnoufBibTex.conference().getUri())) != null) {
            StmtIterator it = model.listStatements(new SimpleSelector(null, RDF.type, clazz));
            if (it.hasNext()) {
                return it.nextStatement().getSubject();
            }
        }

        if ((clazz = model.getResource(KnoufBibTex.phdThesis().getUri())) != null) {
            StmtIterator it = model.listStatements(new SimpleSelector(null, RDF.type, clazz));
            if (it.hasNext()) {
                return it.nextStatement().getSubject();
            }
        }

        if ((clazz = model.getResource(KnoufBibTex.entry().getUri())) != null) {
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
