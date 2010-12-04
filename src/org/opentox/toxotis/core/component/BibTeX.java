package org.opentox.toxotis.core.component;

import org.opentox.toxotis.core.*;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.DC;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.ErrorCause;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.http.PostHttpClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.html.Alignment;
import org.opentox.toxotis.core.html.HTMLContainer;
import org.opentox.toxotis.core.html.HTMLDivBuilder;
import org.opentox.toxotis.core.html.HTMLTable;
import org.opentox.toxotis.core.html.impl.HTMLTagImpl;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.KnoufBibTex;
import org.opentox.toxotis.ontology.collection.KnoufDatatypeProperties;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.aa.User;

/**
 * Bibliographic reference designed according to the BibTeX specifications. OpenTox
 * resource are pointing to bibliographic entries (articles, conferences etc) using the
 * Knouf ontology.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 *
 * @see KnoufBibTex Knouf Ontology
 */
public class BibTeX extends OTPublishable<BibTeX>
        implements IHTMLSupport {

    //TODO: We could use this: http://www.bibtex.org/Convert/ to create HTML representations of BibTeXs!!! ;-)
    public BibTeX() {
        super();
    }

    public BibTeX(VRI uri) throws ToxOtisException {
        super(uri);
        if (uri != null) {
            if (!BibTeX.class.equals(uri.getOpenToxType())) {
                throw new ToxOtisException("The provided URI : '" + uri.getStringNoQuery()
                        + "' is not a valid BibTeX uri according to the OpenTox specifications.");
            }
        }
    }

    public VRI getBibTexService() {
        if (uri == null) {
            return null;
        }
        Pattern pattern = Pattern.compile("/bibtex/");
        String[] splitted = pattern.split(uri.toString());
        String bibtexUri = splitted[0] + "/bibtex";
        try {
            return new VRI(bibtexUri);
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected BibTeX loadFromRemote(VRI uri, AuthenticationToken token) throws ToxOtisException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Task publishOnline(VRI vri, AuthenticationToken token) throws ToxOtisException {
        if (token != null) {
            // Replace existing token with the new ones
            vri.clearToken().appendToken(token);
        }
        PostHttpClient pc = new PostHttpClient(vri);
        pc.setMediaType("text/uri-list");
        pc.setContentType("application/rdf+xml");
        pc.setPostable(asOntModel());
        pc.post();
        Task task = new Task();
        try {
            int status = pc.getResponseCode();
            if (status == 200) {// BibTeX returned
                try {
                    // BibTeX returned
                    task.setResultUri(new VRI(pc.getResponseText()));
                    task.setStatus(Task.Status.COMPLETED);
                    task.setPercentageCompleted(100);
                    return task;
                } catch (URISyntaxException ex) {
                    throw new ToxOtisException("Invalid URI returned from remote service", ex);
                }
            } else if (status == 202) {// Task
                //TODO: Handle tasks
                return null;
            } else {
                throw new ToxOtisException("Status code : '" + status + "' returned from remote service!");
            }
        } catch (IOException ex) {
            throw new ToxOtisException(ErrorCause.CommunicationError, ex);
        }


    }

    /**
     * Publishes the BibTeX object to the bibtex service that corresponds to the
     * URI that is returned by the method {@link BibTeX#getUri() getUri()}.
     * 
     * @return
     *      A Task for monitoring the progress of your request. If the service
     *      returns the URI of the resource right away and does not return a task,
     *      then the object you will receive from this method will now have an identifier,
     *      its status will be set to {@link Task.Status#COMPLETED }, its progress
     *      will be set to <code>100%</code> and the URI of the created resource will
     *      be available applying the method {@link Task#getResultUri() } on the returned
     *      task. In any case, the service's response will be wrapped in a {@link Task }
     *      object.
     *
     * @throws ToxOtisException
     *      In case of invalid credentials, if the POSTed resource is not acceptable
     *      by the remote service (returns a status code 400), communication error
     *      occur with the remote server or other connection problems or the access
     *      to the service was denied (401 or 403).
     *
     * @see BibTeX#publishOnline(org.opentox.toxotis.client.VRI, org.opentox.toxotis.util.aa.AuthenticationToken)  alternative method
     */
    @Override
    public Task publishOnline(AuthenticationToken token) throws ToxOtisException {
        VRI bibTexService = getBibTexService();
        if (token != null) {// Append tokenid to the list of URL parameters
            bibTexService.appendToken(token);
        }
        return publishOnline(bibTexService, token);
    }

    @Override
    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public HTMLContainer inHtml() {
        HTMLDivBuilder builder = new HTMLDivBuilder();
        builder.addComment("BibTeX Representation automatically generated by YAQP");
        builder.addSubHeading("BibTeX Entry").horizontalSeparator().breakLine();

        builder.addSubSubSubHeading("BibTeX Information");
        HTMLTable table = builder.addTable(2).
                setTextAtCursor("BibTeX URI").setTextAtCursor(new HTMLTagImpl("a", getUri().toString()).addTagAttribute("href", getUri().toString()).toString()).
                setTextAtCursor("Title").setTextAtCursor(getTitle()).
                setTextAtCursor("Author(s)").setTextAtCursor(getAuthor()).
                setTextAtCursor("Entry Type").setTextAtCursor(getBibType().toString()).
                setColWidth(1, 150).
                setColWidth(2, 550).
                setTableBorder(1).
                setCellPadding(10).
                setCellSpacing(1);
        if (m_abstract != null) {
            table.setTextAtCursor("Abstract").setTextAtCursor(m_abstract);
        }
        if (m_edition != null) {
            table.setTextAtCursor("Edition").setTextAtCursor(m_edition);
        }
        if (m_editor != null) {
            table.setTextAtCursor("Editor").setTextAtCursor(m_editor);
        }
        if (m_pages != null) {
            table.setTextAtCursor("Pages").setTextAtCursor(m_pages);
        }
        if (m_address != null) {
            table.setTextAtCursor("Address").setTextAtCursor(m_address);
        }
        if (m_annotation != null) {
            table.setTextAtCursor("Annotation").setTextAtCursor(m_annotation);
        }
        if (m_bookTitle != null) {
            table.setTextAtCursor("Book Title").setTextAtCursor(m_bookTitle);
        }
        if (m_title != null) {
            table.setTextAtCursor("Title").setTextAtCursor(m_title);
        }
        if (m_chapter != null) {
            table.setTextAtCursor("Chapter").setTextAtCursor(m_chapter);
        }
        if (m_copyright != null) {
            table.setTextAtCursor("Copyright Note").setTextAtCursor(m_copyright);
        }
        if (m_crossref != null) {
            String cr = m_crossref;
            if (m_crossref.contains("bibtex/") && m_crossref.contains("http://")) {
                cr = "<a href=\"" + m_crossref + "\">" + m_crossref + "</a>";
            }
            table.setTextAtCursor("Cross-reference").setTextAtCursor(cr);
        }
        if (m_isbn != null) {
            table.setTextAtCursor("ISBN").setTextAtCursor(m_isbn);
        }
        if (m_issn != null) {
            table.setTextAtCursor("ISSN").setTextAtCursor(m_issn);
        }
        if (m_journal != null) {
            table.setTextAtCursor("Journal").setTextAtCursor(m_journal);
        }
        if (m_key != null) {
            table.setTextAtCursor("Key").setTextAtCursor(m_key);
        }
        if (m_keywords != null) {
            table.setTextAtCursor("Keywords").setTextAtCursor(m_keywords);
        }
        if (m_volume != null) {
            table.setTextAtCursor("Volume").setTextAtCursor(m_volume);
        }
        if (m_number != null) {
            table.setTextAtCursor("Number").setTextAtCursor(m_number);
        }
        if (m_series != null) {
            table.setTextAtCursor("Series").setTextAtCursor(m_series);
        }
        if (m_url != null) {
            table.setTextAtCursor("URL").setTextAtCursor("<a href=\"" + m_url + "\">" + m_url + "</a>");
        }
        if (m_year != null) {
            table.setTextAtCursor("Year").setTextAtCursor(m_year);
        }

        builder.getDiv().breakLine();
        builder.addSubSubSubHeading("BibTeX in Plain Text Format");
        builder.addComponent(
                new HTMLTagImpl("pre", toString()));
        builder.getDiv().breakLine();
        builder.addParagraph("<small>Other Formats: "
                + "<a href=\"" + getUri() + "?accept=application/rdf%2Bxml" + "\">RDF/XML</a>,"
                + "<a href=\"" + getUri() + "?accept=application/x-turtle" + "\">Turtle</a>,"
                + "<a href=\"" + getUri() + "?accept=text/plain" + "\">Plain Text</a>,"
                + "<a href=\"" + getUri() + "?accept=text/uri-list" + "\">URI-list</a>,"
                + "</small>", Alignment.left);

        builder.getDiv().breakLine().horizontalSeparator();
        return builder.getDiv();
    }

    /**
     * Retained for backward compatibility with third party systems. Will be
     * removed in some future distribution. Use {@link BibTeX#toString() } instead.
     * @return
     *      Plain text representation of the BibTeX resource.
     */
    @Deprecated
    public String getPlainText() {        
        return toString();
    }

    /**
     * Enumeration for bibliographic types supported by the
     * Knouf ontology.
     */
    public enum BIB_TYPE {

        Article,
        Book,
        Conference,
        Phdthesis,
        Entry;
    }
    /*
     * WARNING: DO NOT MODIFY THE NAMES OF THE FOLLOWING FIELDS
     * BECAUSE SOME METHODS IN BIBTEX USE REFLECTIVE LOOKUPS AND COMPARISONS
     * BASED ON THE NAME OF THE FIELD.
     */
    private String m_abstract;
    private String m_author;
    private String m_title;
    private String m_bookTitle;
    private String m_chapter;
    private String m_copyright;
    private String m_edition;
    private String m_editor;
    private String m_crossref;
    private String m_address;
    private String m_year;
    private String m_pages;
    private String m_volume;
    private String m_number;
    private String m_journal;
    private String m_isbn;
    private String m_issn;
    private String m_keywords;
    private String m_key;
    private String m_annotation;
    private String m_series;
    private String m_url;
    private BIB_TYPE m_bib_type;
    private User m_createdBy;

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public User getCreatedBy() {
        return m_createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.m_createdBy = createdBy;
    }

    public String getAbstract() {
        return m_abstract;
    }

    public BibTeX setAbstract(String m_abstract) {
        this.m_abstract = m_abstract;
        return this;
    }

    public String getAuthor() {
        return m_author;
    }

    public BibTeX setAuthor(String m_author) {
        this.m_author = m_author;
        return this;
    }

    public BIB_TYPE getBibType() {
        return m_bib_type;
    }

    public BibTeX setBibType(BIB_TYPE m_bib_type) {
        this.m_bib_type = m_bib_type;
        return this;
    }

    public String getBookTitle() {
        return m_bookTitle;
    }

    public BibTeX setBookTitle(String m_bookTitle) {
        this.m_bookTitle = m_bookTitle;
        return this;
    }

    public String getChapter() {
        return m_chapter;
    }

    public BibTeX setChapter(String m_chapter) {
        this.m_chapter = m_chapter;
        return this;
    }

    public String getCopyright() {
        return m_copyright;
    }

    public BibTeX setCopyright(String m_copyright) {
        this.m_copyright = m_copyright;
        return this;
    }

    public String getCrossref() {
        return m_crossref;
    }

    public BibTeX setCrossref(String m_crossref) {
        this.m_crossref = m_crossref;
        return this;
    }

    public String getEdition() {
        return m_edition;
    }

    public BibTeX setEdition(String m_edition) {
        this.m_edition = m_edition;
        return this;
    }

    public String getEditor() {
        return m_editor;
    }

    public BibTeX setEditor(String m_editor) {
        this.m_editor = m_editor;
        return this;
    }

    public String getIsbn() {
        return m_isbn;
    }

    public BibTeX setIsbn(String isbn) {
        this.m_isbn = isbn;
        return this;
    }

    public String getIssn() {
        return m_issn;
    }

    public BibTeX setIssn(String issn) {
        this.m_issn = issn;
        return this;
    }

    public String getJournal() {
        return m_journal;
    }

    public BibTeX setJournal(String journal) {
        this.m_journal = journal;
        return this;
    }

    public String getKey() {
        return m_key;
    }

    public BibTeX setKey(String key) {
        this.m_key = key;
        return this;
    }

    public String getKeywords() {
        return m_keywords;
    }

    public BibTeX setKeywords(String keywords) {
        this.m_keywords = keywords;
        return this;
    }

    public Integer getNumber() {
        if (m_number == null) {
            return null;
        }
        return Integer.parseInt(m_number);
    }

    public BibTeX setNumber(Integer number) {
        if (number == null || (number != null && number < 0)) {
            this.m_number = null;
        } else {
            this.m_number = Integer.toString(number);
        }
        return this;
    }

    public String getPages() {
        return m_pages;
    }

    public BibTeX setPages(String pages) {
        this.m_pages = pages;
        return this;
    }

    public Integer getVolume() {
        if (m_volume == null) {
            return null;
        }
        return Integer.parseInt(m_volume);
    }

    public BibTeX setVolume(Integer volume) {
        if (volume == null || (volume != null && volume < 0)) {
            this.m_volume = null;
        } else {
            this.m_volume = Integer.toString(volume);
        }
        return this;
    }

    public Integer getYear() {
        if (m_year == null) {
            return null;
        }
        return Integer.parseInt(m_year);
    }

    public BibTeX setYear(Integer year) {
        if (year == null || (year != null && year < 0)) {
            this.m_year = null;
        } else {
            this.m_year = Integer.toString(year);
        }
        return this;
    }

    public String getAddress() {
        return m_address;
    }

    public BibTeX setAddress(String address) {
        this.m_address = address;
        return this;
    }

    public String getAnnotation() {
        return m_annotation;
    }

    public BibTeX setAnnotation(String annotation) {
        this.m_annotation = annotation;
        return this;
    }

    public String getSeries() {
        return m_series;
    }

    public BibTeX setSeries(String m_series) {
        this.m_series = m_series;
        return this;
    }

    public String getTitle() {
        return m_title;
    }

    public BibTeX setTitle(String title) {
        this.m_title = title;
        return this;
    }

    public String getUrl() {
        return m_url;
    }

    public BibTeX setUrl(String Url) {
        this.m_url = Url;
        return this;
    }// </editor-fold>

    public Individual asIndividual(OntModel model) {
        String bibtexUri = uri != null ? uri.toString() : null;
        Individual indiv = null;
        try {
            OntologicalClass bibTypeClass = KnoufBibTex.Entry();
            if (m_bib_type != null) {
                bibTypeClass = KnoufBibTex.forName(m_bib_type.toString());
            }
            indiv = model.createIndividual(bibtexUri, bibTypeClass.inModel(model));
        } catch (ToxOtisException ex) {
            throw new RuntimeException("");
        }

        indiv.addComment(model.createTypedLiteral("Individual automatically generated by ToxOtis",
                XSDDatatype.XSDstring));
        if (bibtexUri != null) {
            indiv.addLiteral(model.createAnnotationProperty(DC.identifier.getURI()),
                    model.createTypedLiteral(bibtexUri, XSDDatatype.XSDstring));
        }

        if (m_author != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasAuthor().asDatatypeProperty(model),
                    model.createTypedLiteral(m_author, XSDDatatype.XSDstring));
        }
        if (m_abstract != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasAbstract().asDatatypeProperty(model),
                    model.createTypedLiteral(m_abstract, XSDDatatype.XSDstring));
        }
        if (m_bookTitle != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasBookTitle().asDatatypeProperty(model),
                    model.createTypedLiteral(m_bookTitle, XSDDatatype.XSDstring));
        }
        if (m_chapter != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasChapter().asDatatypeProperty(model),
                    model.createTypedLiteral(m_chapter, XSDDatatype.XSDstring));
        }
        if (m_copyright != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasCopyright().asDatatypeProperty(model),
                    model.createTypedLiteral(m_copyright, XSDDatatype.XSDstring));
        }
        if (m_crossref != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasCrossRef().asDatatypeProperty(model),
                    model.createTypedLiteral(m_crossref, XSDDatatype.XSDstring));
        }
        if (m_edition != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasEdition().asDatatypeProperty(model),
                    model.createTypedLiteral(m_edition, XSDDatatype.XSDstring));
        }
        if (m_editor != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasEditor().asDatatypeProperty(model),
                    model.createTypedLiteral(m_editor, XSDDatatype.XSDstring));
        }
        if (m_address != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasAddress().asDatatypeProperty(model),
                    model.createTypedLiteral(m_address, XSDDatatype.XSDstring));
        }
        if (m_annotation != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasAnnotation().asDatatypeProperty(model),
                    model.createTypedLiteral(m_annotation, XSDDatatype.XSDstring));
        }
        if (m_year != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasYear().asDatatypeProperty(model),
                    model.createTypedLiteral(m_year, XSDDatatype.XSDint));
        }
        if (m_volume != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasVolume().asDatatypeProperty(model),
                    model.createTypedLiteral(m_volume, XSDDatatype.XSDint));
        }
        if (m_number != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasNumber().asDatatypeProperty(model),
                    model.createTypedLiteral(m_number, XSDDatatype.XSDint));
        }
        if (m_isbn != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasISBN().asDatatypeProperty(model),
                    model.createTypedLiteral(m_isbn, XSDDatatype.XSDstring));
        }
        if (m_issn != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasISSN().asDatatypeProperty(model),
                    model.createTypedLiteral(m_issn, XSDDatatype.XSDstring));
        }
        if (m_key != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasKey().asDatatypeProperty(model),
                    model.createTypedLiteral(m_key, XSDDatatype.XSDstring));
        }
        if (m_keywords != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasKeywords().asDatatypeProperty(model),
                    model.createTypedLiteral(m_keywords, XSDDatatype.XSDstring));
        }
        if (m_journal != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasJournal().asDatatypeProperty(model),
                    model.createTypedLiteral(m_journal, XSDDatatype.XSDstring));
        }
        if (m_pages != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasPages().asDatatypeProperty(model),
                    model.createTypedLiteral(m_pages, XSDDatatype.XSDstring));
        }
        if (m_series != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasSeries().asDatatypeProperty(model),
                    model.createTypedLiteral(m_series, XSDDatatype.XSDstring));
        }
        if (m_title != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasTitle().asDatatypeProperty(model),
                    model.createTypedLiteral(m_title, XSDDatatype.XSDstring));
        }
        if (m_url != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasURL().asDatatypeProperty(model),
                    model.createTypedLiteral(m_url, XSDDatatype.XSDanyURI));
        }
        return indiv;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("@");
        result.append(m_bib_type.toString());
        result.append("{");
        result.append(getUri());
        result.append(",\n");
        result.append("author = \"");
        result.append(m_author);
        result.append("\"");

        for (Field f : this.getClass().getDeclaredFields()) {
            try {
                if (f.getName().equals("m_createdBy")) {
                    // skip it
                } else if (!f.getName().equals("m_author")
                        && !f.getName().equals("m_bib_type")
                        && f.get(this) != null) {
                    result.append(",\n");
                    result.append(f.getName().substring(2));
                    result.append(" = \"");
                    result.append(f.get(this));
                    result.append("\"");
                }
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
        result.append("\n}\n");
        return result.toString();
    }

    /**
     * Create a new BibTeX resource from its String representation. Parses the
     * String representation of a BibTeX into an instance of BibTeX. Then you can
     * use the methods defined in {@link BibTeX } to publish the BibTeX in some
     * BibTeX service or create an RDF rerpesentation of it (using the Knouf ontology).
     * @param string
     *      String representation of the BibTeX entity. The provided string is not
     *      expected to be URL encoded. In case it is, you should consider using a
     *      decoder (e.g. <code>java.net.URLDecoder</code>.
     * @return
     *      Updated instance of BibTeX
     * @throws ToxOtisException
     *      In case the provided string representation is not valid.
     */
    public BibTeX readString(String string) throws ToxOtisException {
        StringReader sr = new StringReader(string);
        this.readString(sr);
        sr.close();
        return this;

    }

    /**
     * Create a new BibTeX resource from its String representation. Parses the
     * String representation of a BibTeX into an instance of BibTeX. Then you can
     * use the methods defined in {@link BibTeX } to publish the BibTeX in some
     * BibTeX service or create an RDF rerpesentation of it (using the Knouf ontology).
     * @param iStream
     *      Input stream from which the string is read. Such an input stream might
     *      be pointing to a file or some remote locate (i.e. to a URL). Be warned
     *      that this method will not close the provided input stream which has to
     *      be closed from the outside.
     * @return
     *      Updated instance of BibTeX
     * @throws ToxOtisException
     *      In case the provided string representation is not valid.
     */
    public BibTeX readString(InputStream iStream) throws ToxOtisException {
        InputStreamReader isr = new InputStreamReader(iStream);
        readString(isr);
        try {
            isr.close();
        } catch (final IOException ex) {
            throw new ToxOtisException("Error while trying to close InputStreamReader", ex);
        }
        return this;
    }

    /**
     * Create a new BibTeX resource from its String representation. Parses the
     * String representation of a BibTeX into an instance of BibTeX. Then you can
     * use the methods defined in {@link BibTeX } to publish the BibTeX in some
     * BibTeX service or create an RDF rerpesentation of it (using the Knouf ontology).
     * 
     * @param reader
     *     Reader used to acquire the String representation of the BibTeX. The method
     *     will not close the reader so users have to close is when needed.
     * @return
     *      Updated instance of BibTeX
     * @throws ToxOtisException
     *      In case the provided string representation is not valid.
     */
    public BibTeX readString(Reader reader) throws ToxOtisException {
        //TODO: Would be better if the procedure was based on splitting the string on commas instead of newlines!
        BufferedReader br = new BufferedReader(reader);
        String line;
        try {
            /*
             * Iterate through all emtpy lines that might be present in the begining
             * of the representation till the first line is found. Check if it has the
             * expected format. retrieve the bib type and its identifier
             */
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("@")) {
                    String[] firstLineFragments = line.split(Pattern.quote("{"));
                    if (firstLineFragments.length == 2) {
                        String type = firstLineFragments[0].trim().replaceAll("@", "");
                        setBibType(BIB_TYPE.valueOf(type));
                        String identifier = firstLineFragments[1].trim().replaceAll(",", "");
                        try {
                            setUri(new VRI(identifier));
                        } catch (URISyntaxException ex) {
                            try {
                                setUri(new VRI("example.org/bibtex/" + UUID.randomUUID().toString()));
                            } catch (URISyntaxException ex1) {
                                throw new RuntimeException(ex1);
                            }
                        }
                    } else {
                        throw new ToxOtisException("Invalid BibTeX representation; for line : \n" + line);
                    }
                    break;
                }
            }
            /*
             * Proceed with the parsing of other lines:
             */
            Field[] fields = this.getClass().getDeclaredFields();
            Set<Field> setOfFields = new HashSet<Field>();
            java.util.Collections.addAll(setOfFields, fields);
            try {
                setOfFields.remove(this.getClass().getDeclaredField("m_bib_type")); // ..__  Already parsed!
            } catch (final NoSuchFieldException ex) {
                throw new RuntimeException(ex);
            } catch (final SecurityException ex) {
                throw new RuntimeException(ex);
            } catch (final IllegalArgumentException ex) {
                throw new RuntimeException(ex);
            }

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equals("}") || line.endsWith("}")) {
                    break;
                }
                String[] nameValueFragments = line.split(Pattern.quote("="));
                if (nameValueFragments.length == 2) {
                    String paramName = nameValueFragments[0].trim();
                    String paramVal = nameValueFragments[1].trim().replaceAll("\",", "").replaceAll("\"", "");
                    Field foundField = null;
                    for (Field f : setOfFields) {
                        if (f.getName().equalsIgnoreCase("m_" + paramName)) {
                            foundField = f;
                            try {
                                f.set(this, paramVal);
                            } catch (final IllegalArgumentException ex) {
                                throw new RuntimeException(ex);
                            } catch (final IllegalAccessException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                    if (foundField != null) {
                        setOfFields.remove(foundField);
                    }
                }
            }

        } catch (final IOException ex) {
            throw new RuntimeException("Error while reading String! Utterly unexpected!", ex);
        } finally {

            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    throw new RuntimeException("IOException caught while trying to close a buffered reader", ex);
                }
            }
        }
        return this;
    }

    public BibTeX readString(File bibFile) throws ToxOtisException {
        if (bibFile == null) {
            throw new NullPointerException("BibFile cannot be null!");
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(bibFile);
            this.readString(fis);
            return this;
        } catch (FileNotFoundException ex) {
            throw new ToxOtisException("File not found at " + bibFile.getName(), ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(BibTeX.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
