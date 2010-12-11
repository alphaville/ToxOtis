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
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.IGetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.core.component.BibTeX;
import org.opentox.toxotis.core.component.ErrorReport;
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

    public BibTeXSprider(Resource resource, OntModel model) throws ToxOtisException {
        super(resource, model);
        if (resource != null) {
            try {
                this.uri = new VRI(resource.getURI());
                if (!BibTeX.class.equals(uri.getOpenToxType())) {
                    throw new ToxOtisException("Bad URI : Not a BibTeX URI (" + uri + ")");
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
        }
    }

    public BibTeXSprider(VRI uri) throws ToxOtisException {
        this(uri, (AuthenticationToken) null);
    }

    public BibTeXSprider(VRI uri, AuthenticationToken token) throws ToxOtisException {
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
                    throw new ToxOtisException(ErrorCause.AuthenticationFailed,
                            "Access denied to : '" + uri + "'", er);
                }
                if (status == 401) {
                    throw new ToxOtisException(ErrorCause.UnauthorizedUser,
                            "User is not authorized to access : '" + uri + "'", er);
                }
                if (status == 404) {
                    throw new ToxOtisException(ErrorCause.BibTexNotFoundInDatabase,
                            "The following algorithm was not found : '" + uri + "'", er);
                } else {
                    throw new ToxOtisException(ErrorCause.CommunicationError,
                            "Communication Error with : '" + uri + "'", er);
                }
            }
            model = client.getResponseOntModel();
            resource = model.getResource(uri.getStringNoQuery());
        } catch (ToxOtisException ex) {
            logger.trace("ToxOtisException caught in BibTeXSpider", ex);
            throw ex;
        } catch (IOException ex) {
            logger.debug("I/O Exception while attempting connection to "
                    + "the remote BibTeX resource at " + uri, ex);
            throw new ToxOtisException(ex);
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException ex) {
                    throw new ToxOtisException(ErrorCause.StreamCouldNotClose, ex);
                }
            }
        }
    }

    @Override
    public BibTeX parse() throws ToxOtisException {
        if (resource == null) {
            resource = getresource(model);
        }
        if (resource == null) {
            throw new ToxOtisException(ErrorCause.ImproperBibTexResource, "Could not parse the BibTex resource because no "
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

        BibTeX bibtex = new BibTeX(uri);
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
                throw new ToxOtisException(ErrorCause.NumberExpected, ex);
            }
        }

        if (volume != null) {
            try {
                bibtex.setVolume(Integer.parseInt(volume));
            } catch (NumberFormatException ex) {
                throw new ToxOtisException(ErrorCause.NumberExpected, ex);
            }
        }

        if (number != null) {
            try {
                bibtex.setNumber(Integer.parseInt(number));
            } catch (NumberFormatException ex) {
                throw new ToxOtisException(ErrorCause.NumberExpected, ex);
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
