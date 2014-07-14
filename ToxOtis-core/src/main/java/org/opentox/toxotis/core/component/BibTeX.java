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
import java.util.regex.Pattern;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.HttpStatusCodes;
import org.opentox.toxotis.client.IPostClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.core.html.HTMLContainer;
import org.opentox.toxotis.core.html.HTMLDivBuilder;
import org.opentox.toxotis.core.html.HTMLTable;
import org.opentox.toxotis.core.html.impl.HTMLTagImpl;
import org.opentox.toxotis.exceptions.impl.ForbiddenRequest;
import org.opentox.toxotis.exceptions.impl.RemoteServiceException;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.KnoufBibTex;
import org.opentox.toxotis.ontology.collection.KnoufDatatypeProperties;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.spiders.BibTeXSprider;

/**
 * Bibliographic reference designed according to the BibTeX specifications.
 * OpenTox resource are pointing to bibliographic entries (articles, conferences
 * etc) using the Knouf ontology. Part of the JavaDoc documentation in this
 * class has been fetched from the manual: <em>BibTeXing The original manual
 * (1988) by the co-author of BibTeX, Oren Patashnik.</em>. You can find more
 * information about BibTeX at <a
 * href="http://en.wikipedia.org/wiki/BibTeX">en.wikipedia.org/wiki/BibTeX</a>.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 *
 * @see KnoufBibTex Knouf Ontology
 */
public class BibTeX extends OTPublishable<BibTeX>
        implements IHTMLSupport {

    /**
     * Enumeration for bibliographic types supported by the Knouf ontology.
     */
    public enum BibTYPE {

        /**
         * An article from a journal or magazine. Required fields: author,
         * title, journal, year. Optional fields: volume, number, pages, month,
         * note.
         */
        Article,
        /**
         * A book with an explicit publisher. Required fields: author or editor,
         * title, publisher, year. Optional fields: volume or number, series,
         * address, edition, month, note.
         */
        Book,
        /**
         * An oral presentation or a poster presented in a conference or
         * document that was presented in a conference by any means. Symposium,
         * workshops and other similar presentations are also reported here.
         */
        Conference,
        /**
         * A PhD dissertation. Required fields: author, title, school, year.
         * Optional fields: type, address, month, note.
         */
        Phdthesis,
        /**
         * A work that is printed and bound, but without a named publisher or
         * sponsoring institution. Required field: title. Optional fields:
         * author, howpublished, address, month, year, note.
         */
        Booklet,
        /**
         * A part of a book, which may be a chapter (or section or whatever)
         * and/or a range of pages. Required fields: author or editor, title,
         * chapter and/or pages, publisher, year. Optional fields: volume or
         * number, series, type, address, edition, month, note.
         */
        Inbook,
        /**
         * A part of a book having its own title. Required fields: author,
         * title, booktitle, publisher, year. Optional fields: editor, volume or
         * number, series, type, chapter, pages, address, edition, month, note.
         */
        Incollection,
        /**
         * An article in a conference proceedings.
         */
        Inproceedings,
        /**
         * Technical documentation
         */
        Manual,
        /**
         * A Master's thesis.
         */
        Mastersthesis,
        /**
         * Misc, Use this type when nothing else fits.
         */
        Misc,
        /**
         * The proceedings of a conference.
         */
        Proceedings,
        /**
         * A report published by a school or other institution, usually numbered
         * within a series.
         */
        TechReport,
        /**
         * A document having an author and title, but not formally published.
         */
        Unpublished,
        /**
         * A generic BibTeX entry which encapsulates all cases.
         */
        Entry;
    }
    /*
     * WARNING: DO NOT MODIFY THE NAMES OF THE FOLLOWING FIELDS
     * BECAUSE SOME METHODS IN BIBTEX USE REFLECTIVE LOOKUPS AND COMPARISONS
     * BASED ON THE NAME OF THE FIELD.
     */
    private String mAbstract;
    private String mAuthor;
    private String mTitle;
    private String mBookTitle;
    private String mChapter;
    private String mCopyright;
    private String mEdition;
    private String mEditor;
    private String mCrossref;
    private String mAddress;
    private String mYear;
    private String mPages;
    private String mVolume;
    private String mNumber;
    private String mJournal;
    private String mIsbn;
    private String mIssn;
    private String mKeywords;
    private String mKey;
    private String mAnnotation;
    private String mSeries;
    private String mUrl;
    private BibTYPE mBibType;
    private User mCreatedBy;
    private final org.slf4j.Logger logger;

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    /**
     * The user that created the BibTeX object.
     *
     * @return Instance of {@link User}
     */
    public User getCreatedBy() {
        return mCreatedBy;
    }

    /**
     * Sets the user who created the current BibTeX object.
     *
     * @param createdBy Instance of {@link User}
     */
    public void setCreatedBy(User createdBy) {
        this.mCreatedBy = createdBy;
    }

    /**
     * The abstract of the BibTeX object.
     *
     * @return
     */
    public String getAbstract() {
        return mAbstract;
    }

    /**
     * Setter method for the abstract.
     *
     * @param theAbstract The abstract as a String.
     * @return The current modifiable instance of BibTeX with the updated
     * abstract.
     */
    public BibTeX setAbstract(String theAbstract) {
        this.mAbstract = theAbstract;
        return this;
    }

    /**
     * The name(s) of the author(s), in the format described in the LaTeX book.
     *
     * @return Name(s) of authors as String.
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * Setter method for the author(s).
     *
     * @param author The list of authors or the single author as a String.
     * @return The current modifiable instance of BibTeX with the updated
     * author.
     * @see #getAuthor() #getAuthor
     */
    public BibTeX setAuthor(String author) {
        this.mAuthor = author;
        return this;
    }

    /**
     * The bibliographic type.
     *
     * @return The bibliographic type of the current BibTeX object as an
     * instance of {@link BibTYPE }.
     */
    public BibTYPE getBibType() {
        return mBibType;
    }

    /**
     * Setter for the bibliographic type.
     *
     * @param bibType The bibliographic type you need to specify.
     * @return The current modifiable instance of BibTeX with the updated value
     * of bibtype.
     * @see #getBibType() getBibType()
     */
    public BibTeX setBibType(BibTYPE bibType) {
        this.mBibType = bibType;
        return this;
    }

    /**
     * Title of a book, part of which is being cited. See the LaTeX book for how
     * to type titles. For book entries, use the <code>title</code> field
     * instead.
     *
     * @return The <code>booktitle</code> as String.
     */
    public String getBookTitle() {
        return mBookTitle;
    }

    /**
     * Setter method for the bookTitle.
     *
     * @param bookTitle The bookTitle as a String.
     * @return The current modifiable instance of BibTeX with the updated
     * bookTitle.
     */
    public BibTeX setBookTitle(String bookTitle) {
        this.mBookTitle = bookTitle;
        return this;
    }

    /**
     * A chapter (or section or whatever) number.
     *
     * @return The chapter title/number as a String.
     */
    public String getChapter() {
        return mChapter;
    }

    /**
     * Setter method for the chapter of the BibTeX resource.
     *
     * @param chapter The chapter as a String.
     * @return The current modifiable instance of BibTeX with the updated
     * chapter parameter.
     */
    public BibTeX setChapter(String chapter) {
        this.mChapter = chapter;
        return this;
    }

    public String getCopyright() {
        return mCopyright;
    }

    /**
     * Setter method for the copyright notice of the BibTeX resource.
     *
     * @param copyright The copyright notice as a String.
     * @return The current modifiable instance of BibTeX with the updated
     * copyright parameter.
     */
    public BibTeX setCopyright(String copyright) {
        this.mCopyright = copyright;
        return this;
    }

    /**
     * The database key of the entry being cross referenced. A URI of some other
     * BibTeX resource should be ideal for a cross-reference.
     *
     * @return Cross-reference to some other BibTeX resource or piece of work.
     */
    public String getCrossref() {
        return mCrossref;
    }

    /**
     * Setter method for the cross-ref of this BibTeX resource. A URI of some
     * other BibTeX entity should be ideally provided.
     *
     * @param crossref The chapter as a String.
     * @return The current modifiable instance of BibTeX with the updated
     * cross-ref.
     */
    public BibTeX setCrossref(String crossref) {
        this.mCrossref = crossref;
        return this;
    }

    /**
     * The edition of a book—for example, "Second". This should be an ordinal,
     * and should have the first letter capitalized, as shown here; the standard
     * styles convert to lower case when necessary.
     *
     * @return The edition of the BibTeX object.
     */
    public String getEdition() {
        return mEdition;
    }

    /**
     * Setter for the edition.
     *
     * @param edition
     * @return The current modifiable BibTeX object updated with the value of
     * edition.
     * @see #getEdition()
     */
    public BibTeX setEdition(String edition) {
        this.mEdition = edition;
        return this;
    }

    /**
     * Name(s) of editor(s), typed as indicated in the LaTeX book. If there is
     * also an author field, then the editor field gives the editor of the book
     * or collection in which the reference appears.
     *
     * @return Editor(s) as String.
     */
    public String getEditor() {
        return mEditor;
    }

    /**
     * Setter for the editor(s).
     *
     * @param editor
     * 
     * @return The current modifiable BibTeX object updated with the value of
     * editor(s).
     * 
     * @see #getEditor()
     */
    public BibTeX setEditor(String editor) {
        this.mEditor = editor;
        return this;
    }

    /**
     * The ISBN of the resource. ISBNs now come in two styles, containing 10
     * digits or 13 digits, respectively (corresponding to the above "ISBN-10:"
     * and "ISBN-13:" numbers). Please use the 13-digit one if available (if
     * nowhere else, it is written under the barcode: the hyphenation will be
     * 978-, or in the future 979-, then the same as in the 10-digit ISBN, but
     * the last digit is different for ISBN-10 and ISBN-13, as they use
     * different checksum algorithms).
     *
     * @return The ISBN of the current BibTeX object as a String.
     */
    public String getIsbn() {
        return mIsbn;
    }

    /**
     * Setter method for the ISBN.
     *
     * @param isbn The ISBN.
     * @return The current BibTeX instance with updated ISBN.
     * @see #getIsbn() #getIsbn()
     */
    public BibTeX setIsbn(String isbn) {
        this.mIsbn = isbn;
        return this;
    }

    /**
     * The ISSN of the resource. An International Standard Serial Number (ISSN)
     * is a unique eight-digit number used to identify a print or electronic
     * periodical publication. Periodicals published in both print and
     * electronic form may have two ISSNs, a print ISSN (p-ISSN) and an
     * electronic ISSN (e-ISSN or eISSN). The ISSN system was first drafted as
     * an ISO international standard in 1971 and published as ISO 3297 in 1975.
     * The ISO subcommittee TC 46/SC 9 is responsible for the standard.
     *
     * @return The ISSN of the current BibTeX object as a String.
     */
    public String getIssn() {
        return mIssn;
    }

    /**
     * Setter method for the ISSN.
     *
     * @param issn The ISSN.
     * @return The current BibTeX instance with updated ISSN.
     * @see #getIssn() #getIssn()
     */
    public BibTeX setIssn(String issn) {
        this.mIssn = issn;
        return this;
    }

    /**
     * A journal name. Abbreviations are provided for many journals; see the
     * Local Guide.
     *
     * @return The journal name as a String.
     */
    public String getJournal() {
        return mJournal;
    }

    /**
     * Setter for the journal name.
     *
     * @param journal Name of the journal.
     * @return The current BibTeX instance with updated <code>journal</code>.
     * @see #getJournal() getJournal()
     */
    public BibTeX setJournal(String journal) {
        this.mJournal = journal;
        return this;
    }

    /**
     * Used for alphabetizing, cross referencing, and creating a label when the
     * "author" information (described in Section 4) is missing. This field
     * should not be confused with the key that appears in the
     * <code>\cite</code> command and at the beginning of the database entry.
     *
     * @return
     */
    public String getKey() {
        return mKey;
    }

    /**
     * Setter for the variable key.
     *
     * @param key The key.
     * @return The current object with updated value of key.
     * @see #getKey() getKey()
     */
    public BibTeX setKey(String key) {
        this.mKey = key;
        return this;
    }

    /**
     * A list of keywords separated by any custom delimiter.
     *
     * @return List of keywords as a single string.
     */
    public String getKeywords() {
        return mKeywords;
    }

    /**
     * Set a list of keywords separated by any custom delimiter.
     *
     * @param keywords Keywords.
     * @return The current object with updated value of keywords.
     */
    public BibTeX setKeywords(String keywords) {
        this.mKeywords = keywords;
        return this;
    }

    /**
     * The number of a journal, magazine, technical report, or of a work in a
     * series. An issue of a journal or magazine is usually identified by its
     * volume and number; the organization that issues a technical report
     * usually gives it a number; and sometimes books are given numbers in a
     * named series.
     *
     * @return The number of the current BibTeX object.
     */
    public Integer getNumber() {
        if (mNumber == null) {
            return null;
        }
        return Integer.parseInt(mNumber);
    }

    /**
     * Setter method for the number of the current BibTeX object.
     *
     * @param number The <code>number</code> as Integer.
     * @return The current modifiable BibTeX object updated number.
     * @see #getNumber() getNumber()
     */
    public BibTeX setNumber(Integer number) {
        if (number == null || number < 0) {
            this.mNumber = null;
        } else {
            this.mNumber = Integer.toString(number);
        }
        return this;
    }

    /**
     * One or more page numbers or range of numbers, such as 42--111 or
     * 7,41,73--97 or 43+ (the ‘+’ in this last example indicates pages
     * following that don’t form a simple range). To make it easier to maintain
     * Scribe- compatible databases, the standard styles convert a single dash
     * (as in 7-33) to the double dash used in TEX to denote number ranges (as
     * in 7--33).
     *
     * @return Pages as String.
     */
    public String getPages() {
        return mPages;
    }

    /**
     * Setter method for the pages of the current BibTeX object.
     *
     * @param pages Pages as String
     * @return The current modifiable BibTeX object updated pages.
     * @see #getPages() getPages()
     */
    public BibTeX setPages(String pages) {
        this.mPages = pages;
        return this;
    }

    /**
     * The volume of a journal or multivolume book.
     *
     * @return The volume as Integer.
     */
    public Integer getVolume() {
        if (mVolume == null) {
            return null;
        }
        return Integer.parseInt(mVolume);
    }

    /**
     * Setter method for the volume of a journal or multivolume book.
     *
     * @param volume The volume as Integer.
     * @return The current modifiable BibTeX object updated volume.
     */
    public BibTeX setVolume(Integer volume) {
        if (volume == null || volume < 0) {
            this.mVolume = null;
        } else {
            this.mVolume = Integer.toString(volume);
        }
        return this;
    }

    /**
     * The year of publication or, for an unpublished work, the year it was
     * written. Generally it should consist of four numerals, such as
     * <code>1984</code>. This method returns the year as Integer.
     *
     * @return The year of publication/inception as Integer.
     */
    public Integer getYear() {
        if (mYear == null) {
            return null;
        }
        return Integer.parseInt(mYear);
    }

    /**
     * Set the year of publication. The year is provided as an instance of
     * Integer. If the supplied value is <code>null</code>, or not-null but
     * non-positive, then no value is stored in the corresponding field.
     *
     * @param year The year of publication for the BibTeX provided as an Integer
     * object.
     * @return The current updated BibTeX object.
     */
    public BibTeX setYear(Integer year) {
        if (year == null || (year != null && year < 0)) {
            this.mYear = null;
        } else {
            this.mYear = Integer.toString(year);
        }
        return this;
    }

    /**
     * Usually the address of the publisher or other type of institution. For
     * major publishing houses, van Leunen recommends omitting the information
     * entirely. For small publishers, on the other hand, you can help the
     * reader by giving the complete address.
     *
     * @return The address as String.
     */
    public String getAddress() {
        return mAddress;
    }

    /**
     * Setter method for the address of the publisher or other type of
     * institution. For major publishing houses, it is preferable to omit the
     * information entirely. For other smaller publishers, you can help the
     * reader by giving the complete address.
     *
     * @param address The address as a simple String.
     * @return The current modifiable BibTeX object updated address.
     */
    public BibTeX setAddress(String address) {
        this.mAddress = address;
        return this;
    }

    /**
     * An annotation. It is not used by the standard bibliography styles, but
     * may be used by others that produce an annotated bibliography.
     *
     * @return Annotation as String.
     */
    public String getAnnotation() {
        return mAnnotation;
    }

    /**
     * Setter method for the annotation.
     *
     * @param annotation The annotation as String.
     * @return The current modifiable BibTeX object updated annotation.
     * @see #getAnnotation() getAnnotation()
     */
    public BibTeX setAnnotation(String annotation) {
        this.mAnnotation = annotation;
        return this;
    }

    /**
     * The name of a series or set of books. When citing an entire book, the the
     * title field gives its title and an optional series field gives the name
     * of a series or multi-volume set in which the book is published.
     *
     * @return The <code>series</code> as String.
     */
    public String getSeries() {
        return mSeries;
    }

    /**
     * Setter method for the <code>series</code> of the BibTeX entry.
     *
     * @param series Series as String.
     * @return The current modifiable BibTeX object updated series.
     */
    public BibTeX setSeries(String series) {
        this.mSeries = series;
        return this;
    }

    /**
     * The work’s title, typed as explained in the LaTeX book.
     *
     * @return The title as String.
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Setter method for the title.
     *
     * @param title The title as String.
     * @return The current modifiable BibTeX object updated title.
     */
    public BibTeX setTitle(String title) {
        this.mTitle = title;
        return this;
    }

    /**
     * A URL where more information about the work described by this BibTeX can
     * be found. Usually, it is the URL where someone may find the actual
     * resource.
     *
     * @return URL as String.
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     *
     * @param url The URL of the BibTeX actual document.
     * @return The current modifiable BibTeX object updated annotation.
     */
    public BibTeX setUrl(String url) {
        this.mUrl = url;
        return this;
    }// </editor-fold>

    //TODO: We could use this: http://www.bibtex.org/Convert/ to create HTML representations of BibTeXs!!! ;-)
    /**
     * Empty constructor for BibTeX objects.
     */
    public BibTeX() {
        super();
        this.logger = org.slf4j.LoggerFactory.getLogger(BibTeX.class);
        addOntologicalClasses(KnoufBibTex.entry());
    }

    /**
     * Create a new BibTeX object with specified URI.
     *
     * @param uri URI of the BibTeX.
     * @throws ToxOtisException If the provided URI is not a valid BibTeX URI.
     */
    public BibTeX(VRI uri) throws ToxOtisException {
        super(uri);
        this.logger = org.slf4j.LoggerFactory.getLogger(BibTeX.class);
        addOntologicalClasses(KnoufBibTex.entry());
        if (uri != null) {
            if (!BibTeX.class.equals(uri.getOpenToxType())) {
                throw new ToxOtisException("The provided URI : '" + uri.getStringNoQuery()
                        + "' is not a valid BibTeX uri according to the OpenTox specifications.");
            }
        }
    }

    /**
     * The bibtex service.
     *
     * @return The BibTeX service as a {@link VRI URI}.
     */
    public VRI getBibTexService() {
        if (getUri() == null) {
            return null;
        }
        Pattern pattern = Pattern.compile("/bibtex/");
        String[] splitted = pattern.split(getUri().toString());
        String bibtexUri = splitted[0] + "/bibtex";
        try {
            return new VRI(bibtexUri);
        } catch (final URISyntaxException ex) {
            throw new IllegalArgumentException("Bad URI", ex);
        }
    }

    @Override
    protected BibTeX loadFromRemote(VRI uri, AuthenticationToken token) throws ServiceInvocationException {
        if (token != null && !AuthenticationToken.TokenStatus.ACTIVE.equals(token.getStatus())) {
            throw new ForbiddenRequest("The Provided token is inactive");
        }
        BibTeXSprider spider = new BibTeXSprider(uri, token);
        BibTeX o = spider.parse();
        setUri(o.getUri());
        setAbstract(o.getAbstract());
        setAddress(o.getAddress());
        setAnnotation(o.getAnnotation());
        setAuthor(o.getAuthor());
        setBibType(o.getBibType());
        setBookTitle(o.getBookTitle());
        setChapter(o.getChapter());
        setCopyright(o.getCopyright());
        setCreatedBy(o.getCreatedBy());
        setCrossref(o.getCrossref());
        setEdition(o.getEdition());
        setEditor(o.getEditor());
        setIsbn(o.getIsbn());
        setIssn(o.getIssn());
        setJournal(o.getJournal());
        setKey(o.getKey());
        setKeywords(o.getKeywords());
        setPages(o.getPages());
        setSeries(o.getSeries());
        setTitle(o.getTitle());
        setUrl(o.getUrl());
        setNumber(o.getNumber());
        setYear(o.getYear());
        setVolume(o.getVolume());
        setMeta(o.getMeta());
        return this;
    }

    @Override
    public Task publishOnline(VRI vri, AuthenticationToken token) throws ServiceInvocationException {
        IPostClient pc = ClientFactory.createPostClient(vri);
        pc.setMediaType(Media.TEXT_URI_LIST);
        pc.setContentType(Media.APPLICATION_RDF_XML);
        pc.setPostable(asOntModel());
        pc.authorize(token);
        pc.post();
        Task task = new Task();

        int status = pc.getResponseCode();
        if (status == HttpStatusCodes.Success.getStatus()) {// BibTeX returned
            try {
                // BibTeX returned
                task.setResultUri(new VRI(pc.getResponseText()));
                task.setStatus(Task.Status.COMPLETED);
                task.setPercentageCompleted(100);
                return task;
            } catch (URISyntaxException ex) {
                throw new RemoteServiceException("Invalid URI returned from remote service", ex);
            }
        } else if (status == HttpStatusCodes.Accepted.getStatus()) {// Task
            String taskUriString = pc.getResponseText().trim();
            try {
                VRI taskUri = new VRI(taskUriString);
                task.setUri(taskUri);
                return task;
            } catch (URISyntaxException ex) {
                String message = "Task URI expected as a response from the remote service at " + vri
                        + " which responded with status code 202 (Task Created) but the response body is not "
                        + "a valid URI: " + taskUriString;
                logger.debug(message, ex);
                throw new RemoteServiceException(message, ex);
            }

        } else {
            throw new RemoteServiceException("Status code : '" + status + "' returned from remote service! Remote server says : " + pc.getResponseText());
        }

    }

    /**
     * Publishes the BibTeX object to the bibtex service that corresponds to the
     * URI that is returned by the method {@link BibTeX#getUri() getUri()}.
     *
     * @return A Task for monitoring the progress of your request. If the
     * service returns the URI of the resource right away and does not return a
     * task, then the object you will receive from this method will now have an
     * identifier, its status will be set to {@link Task.Status#COMPLETED }, its
     * progress will be set to <code>100%</code> and the URI of the created
     * resource will be available applying the method {@link Task#getResultUri()
     * } on the returned task. In any case, the service's response will be
     * wrapped in a {@link Task }
     * object.
     *
     * @throws ServiceInvocationException In case of invalid credentials, if the
     * POSTed resource is not acceptable by the remote service (returns a status
     * code 400), communication error occur with the remote server or other
     * connection problems or the access to the service was denied (401 or 403).
     *
     * @see BibTeX#publishOnline(org.opentox.toxotis.client.VRI,
     * org.opentox.toxotis.util.aa.AuthenticationToken) alternative method
     */
    @Override
    public Task publishOnline(AuthenticationToken token) throws ServiceInvocationException {
        VRI bibTexService = getBibTexService();
//        if (token != null) {// Append tokenid to the list of URL parameters
//            bibTexService.appendToken(token);
//        }
        return publishOnline(bibTexService, token);
    }

    @Override
    public void writeRdf(XMLStreamWriter writer) throws XMLStreamException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public HTMLContainer inHtml() {
        HTMLDivBuilder builder = new HTMLDivBuilder();
        builder.addComment("BibTeX Representation automatically generated by JAQPOT");
        builder.addSubHeading("BibTeX Entry").horizontalSeparator().breakLine();

        builder.addSubSubSubHeading("BibTeX Information");
        HTMLTable table = builder.addTable(2).
                setTextAtCursor("BibTeX URI").setTextAtCursor(new HTMLTagImpl("a", getUri().toString()).addTagAttribute("href", getUri().toString()).toString()).
                setTextAtCursor("Author(s)").setTextAtCursor(getAuthor()).
                setTextAtCursor("Entry Type").setTextAtCursor(getBibType().toString()).
                setColWidth(1, 150).
                setColWidth(2, 550).
                setTableBorder(1).
                setCellPadding(10).
                setCellSpacing(1);
        if (mAbstract != null) {
            table.setTextAtCursor("Abstract").setTextAtCursor(mAbstract);
        }
        if (mEdition != null) {
            table.setTextAtCursor("Edition").setTextAtCursor(mEdition);
        }
        if (mEditor != null) {
            table.setTextAtCursor("Editor").setTextAtCursor(mEditor);
        }
        if (mPages != null) {
            table.setTextAtCursor("Pages").setTextAtCursor(mPages);
        }
        if (mAddress != null) {
            table.setTextAtCursor("Address").setTextAtCursor(mAddress);
        }
        if (mAnnotation != null) {
            table.setTextAtCursor("Annotation").setTextAtCursor(mAnnotation);
        }
        if (mBookTitle != null) {
            table.setTextAtCursor("Book Title").setTextAtCursor(mBookTitle);
        }
        if (mTitle != null) {
            table.setTextAtCursor("Title").setTextAtCursor(mTitle);
        }
        if (mChapter != null) {
            table.setTextAtCursor("Chapter").setTextAtCursor(mChapter);
        }
        if (mCopyright != null) {
            table.setTextAtCursor("Copyright Note").setTextAtCursor(mCopyright);
        }
        if (mCrossref != null) {
            String cr = mCrossref;
            if (mCrossref.contains("bibtex/") && mCrossref.contains("http://")) {
                cr = "<a href=\"" + mCrossref + "\">" + mCrossref + "</a>";
            }
            table.setTextAtCursor("Cross-reference").setTextAtCursor(cr);
        }
        if (mIsbn != null) {
            table.setTextAtCursor("ISBN").setTextAtCursor(mIsbn);
        }
        if (mIssn != null) {
            table.setTextAtCursor("ISSN").setTextAtCursor(mIssn);
        }
        if (mJournal != null) {
            table.setTextAtCursor("Journal").setTextAtCursor(mJournal);
        }
        if (mKey != null) {
            table.setTextAtCursor("Key").setTextAtCursor(mKey);
        }
        if (mKeywords != null) {
            table.setTextAtCursor("Keywords").setTextAtCursor(mKeywords);
        }
        if (mVolume != null) {
            table.setTextAtCursor("Volume").setTextAtCursor(mVolume);
        }
        if (mNumber != null) {
            table.setTextAtCursor("Number").setTextAtCursor(mNumber);
        }
        if (mSeries != null) {
            table.setTextAtCursor("Series").setTextAtCursor(mSeries);
        }
        if (mUrl != null) {
            table.setTextAtCursor("URL").setTextAtCursor("<a href=\"" + mUrl + "\">" + mUrl + "</a>");
        }
        if (mYear != null) {
            table.setTextAtCursor("Year").setTextAtCursor(mYear);
        }

        builder.getDiv().breakLine();
        builder.addSubSubSubHeading("BibTeX in Plain Text Format");
        builder.addComponent(
                new HTMLTagImpl("pre", toString()));
        builder.getDiv().breakLine();
        builder.addComponent(createLinksFooter());

        builder.getDiv().breakLine().horizontalSeparator();
        return builder.getDiv();
    }

    /**
     * Retained for backward compatibility with third party systems. Will be
     * removed in some future distribution. Use {@link BibTeX#toString() }
     * instead.
     *
     * @return Plain text representation of the BibTeX resource.
     * @deprecated
     */
    @Deprecated
    public String getPlainText() {
        return toString();
    }

    @Override
    public Individual asIndividual(OntModel model) {
        String bibtexUri = getUri() != null ? getUri().toString() : null;
        Individual indiv = null;

        OntologicalClass bibTypeClass = KnoufBibTex.entry();
        if (mBibType != null) {
            try {
                bibTypeClass = KnoufBibTex.forName(mBibType.toString());
            } catch (ToxOtisException ex) {
                throw new IllegalArgumentException("Serialization to Individual is not possible - Severe BUG!", ex);
            }
        }
        indiv = model.createIndividual(bibtexUri, bibTypeClass.inModel(model));

        indiv.addComment(model.createTypedLiteral("Individual automatically generated by ToxOtis",
                XSDDatatype.XSDstring));
        if (bibtexUri != null) {
            indiv.addLiteral(model.createAnnotationProperty(DC.identifier.getURI()),
                    model.createTypedLiteral(bibtexUri, XSDDatatype.XSDstring));
        }

        if (mAuthor != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasAuthor().asDatatypeProperty(model),
                    model.createTypedLiteral(mAuthor, XSDDatatype.XSDstring));
        }
        if (mAbstract != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasAbstract().asDatatypeProperty(model),
                    model.createTypedLiteral(mAbstract, XSDDatatype.XSDstring));
        }
        if (mBookTitle != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasBookTitle().asDatatypeProperty(model),
                    model.createTypedLiteral(mBookTitle, XSDDatatype.XSDstring));
        }
        if (mChapter != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasChapter().asDatatypeProperty(model),
                    model.createTypedLiteral(mChapter, XSDDatatype.XSDstring));
        }
        if (mCopyright != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasCopyright().asDatatypeProperty(model),
                    model.createTypedLiteral(mCopyright, XSDDatatype.XSDstring));
        }
        if (mCrossref != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasCrossRef().asDatatypeProperty(model),
                    model.createTypedLiteral(mCrossref, XSDDatatype.XSDanyURI));
        }
        if (mEdition != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasEdition().asDatatypeProperty(model),
                    model.createTypedLiteral(mEdition, XSDDatatype.XSDstring));
        }
        if (mEditor != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasEditor().asDatatypeProperty(model),
                    model.createTypedLiteral(mEditor, XSDDatatype.XSDstring));
        }
        if (mAddress != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasAddress().asDatatypeProperty(model),
                    model.createTypedLiteral(mAddress, XSDDatatype.XSDstring));
        }
        if (mAnnotation != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasAnnotation().asDatatypeProperty(model),
                    model.createTypedLiteral(mAnnotation, XSDDatatype.XSDstring));
        }
        if (mYear != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasYear().asDatatypeProperty(model),
                    model.createTypedLiteral(mYear, XSDDatatype.XSDint));
        }
        if (mVolume != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasVolume().asDatatypeProperty(model),
                    model.createTypedLiteral(mVolume, XSDDatatype.XSDint));
        }
        if (mNumber != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasNumber().asDatatypeProperty(model),
                    model.createTypedLiteral(mNumber, XSDDatatype.XSDstring));
        }
        if (mIsbn != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasISBN().asDatatypeProperty(model),
                    model.createTypedLiteral(mIsbn, XSDDatatype.XSDstring));
        }
        if (mIssn != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasISSN().asDatatypeProperty(model),
                    model.createTypedLiteral(mIssn, XSDDatatype.XSDstring));
        }
        if (mKey != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasKey().asDatatypeProperty(model),
                    model.createTypedLiteral(mKey, XSDDatatype.XSDstring));
        }
        if (mKeywords != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasKeywords().asDatatypeProperty(model),
                    model.createTypedLiteral(mKeywords, XSDDatatype.XSDstring));
        }
        if (mJournal != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasJournal().asDatatypeProperty(model),
                    model.createTypedLiteral(mJournal, XSDDatatype.XSDstring));
        }
        if (mPages != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasPages().asDatatypeProperty(model),
                    model.createTypedLiteral(mPages, XSDDatatype.XSDstring));
        }
        if (mSeries != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasSeries().asDatatypeProperty(model),
                    model.createTypedLiteral(mSeries, XSDDatatype.XSDstring));
        }
        if (mTitle != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasTitle().asDatatypeProperty(model),
                    model.createTypedLiteral(mTitle, XSDDatatype.XSDstring));
        }
        if (mUrl != null) {
            indiv.addLiteral(KnoufDatatypeProperties.hasURL().asDatatypeProperty(model),
                    model.createTypedLiteral(mUrl, XSDDatatype.XSDstring));
        }
        return indiv;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("@");
        result.append(mBibType.toString());
        result.append("{");
        result.append(getUri());
        result.append(",\n");
        result.append("author = \"");
        result.append(mAuthor);
        result.append("\"");

        for (Field f : this.getClass().getDeclaredFields()) {
            try {
                if (!f.getName().equals("mCreatedBy")
                        && !f.getName().equals("mAuthor")
                        && !f.getName().equals("mBibType")
                        && !f.getName().equals("logger")
                        && f.get(this) != null) {
                    result.append(",\n");
                    result.append(f.getName().substring(1));
                    result.append(" = \"");
                    result.append(f.get(this));
                    result.append("\"");
                }
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException(ex);
            } catch (IllegalAccessException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
        result.append("\n}\n");
        return result.toString();
    }

    /**
     * Create a new BibTeX resource from its String representation. Parses the
     * String representation of a BibTeX into an instance of BibTeX. Then you
     * can use the methods defined in {@link BibTeX } to publish the BibTeX in
     * some BibTeX service or create an RDF representation of it (using the
     * Knouf ontology).
     *
     * @param string String representation of the BibTeX entity. The provided
     * string is not expected to be URL encoded. In case it is, you should
     * consider using a decoder (e.g. <code>java.net.URLDecoder</code>.
     * @return Updated instance of BibTeX
     * @throws ToxOtisException In case the provided string representation is
     * not valid.
     */
    public BibTeX readString(String string) throws ToxOtisException {
        StringReader sr = new StringReader(string);
        this.readString(sr);
        sr.close();
        return this;

    }

    /**
     * Create a new BibTeX resource from its String representation. Parses the
     * String representation of a BibTeX into an instance of BibTeX. Then you
     * can use the methods defined in {@link BibTeX } to publish the BibTeX in
     * some BibTeX service or create an RDF rerpesentation of it (using the
     * Knouf ontology).
     *
     * @param iStream Input stream from which the string is read. Such an input
     * stream might be pointing to a file or some remote locate (i.e. to a URL).
     * Be warned that this method will not close the provided input stream which
     * has to be closed from the outside.
     * @return Updated instance of BibTeX
     * @throws ToxOtisException In case the provided string representation is
     * not valid.
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
     * String representation of a BibTeX into an instance of BibTeX. Then you
     * can use the methods defined in {@link BibTeX } to publish the BibTeX in
     * some BibTeX service or create an RDF representation of it (using the
     * Knouf ontology).
     *
     * @param reader Reader used to acquire the String representation of the
     * BibTeX. The method will not close the reader so users have to close is
     * when needed.
     * @return Updated instance of BibTeX
     * @throws ToxOtisException In case the provided string representation is
     * not valid.
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
                        setBibType(BibTYPE.valueOf(type));
                        String identifier = firstLineFragments[1].trim().replaceAll(",", "");
                        try {
                            setUri(new VRI(identifier));
                        } catch (URISyntaxException ex) {
                            try {
                                setUri(new VRI("example.org/bibtex/" + UUID.randomUUID().toString()));
                            } catch (URISyntaxException ex1) {
                                throw new IllegalArgumentException(ex1);
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
                setOfFields.remove(this.getClass().getDeclaredField("mBibType")); // ..__  Already parsed!
            } catch (final NoSuchFieldException ex) {
                throw new IllegalArgumentException(ex);
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
                        if (f.getName().equalsIgnoreCase("m" + paramName)) {
                            foundField = f;
                            try {
                                f.set(this, paramVal);
                            } catch (final IllegalAccessException ex) {
                                throw new IllegalArgumentException(ex);
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

    /**
     * Reads from a file and parses a BibTeX document into an instance of
     * {@link BibTeX}.
     *
     * @param bibFile A file pointing to a <code>.bib</code> file.
     * @return The parsed BibTeX object.
     * @throws NullPointerException If the specified bibfile is
     * <code>null</code>.
     * @throws ToxOtisException If the file is not found, the exception is
     * wrapped in a ToxOtisException and thrown as such. Also any other
     * exceptional event is wrapped in a ToxOtisException.
     */
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
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    logger.warn(null, ex);
                    throw new ToxOtisException(ex);
                }
            } else {
                logger.debug("BibTeX: FIS is null inside the finally statement. It "
                        + "seems that no FIS could be opened to the specified File.");
            }
        }
    }
}
