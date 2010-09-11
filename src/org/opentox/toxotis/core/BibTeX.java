package org.opentox.toxotis.core;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.DC;
import java.lang.reflect.Field;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.KnoufBibTex;
import org.opentox.toxotis.ontology.collection.KnoufDatatypeProperties;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class BibTeX extends OTOnlineResource<BibTeX> {

    @Override
    public BibTeX loadFromRemote() throws ToxOtisException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public enum BIB_TYPE {

        Article,
        Book,
        Conference,
        Phdthesis,
        Entry;
    }
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

    public BibTeX() {
        super();
    }

    public BibTeX(VRI uri) {
        super(uri);
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public String getAbstract() {
        return m_abstract;
    }

    public void setAbstract(String m_abstract) {
        this.m_abstract = m_abstract;
    }

    public String getAuthor() {
        return m_author;
    }

    public void setAuthor(String m_author) {
        this.m_author = m_author;
    }

    public BIB_TYPE getBibType() {
        return m_bib_type;
    }

    public void setBibType(BIB_TYPE m_bib_type) {
        this.m_bib_type = m_bib_type;
    }

    public String getBookTitle() {
        return m_bookTitle;
    }

    public void setBookTitle(String m_bookTitle) {
        this.m_bookTitle = m_bookTitle;
    }

    public String getChapter() {
        return m_chapter;
    }

    public void setChapter(String m_chapter) {
        this.m_chapter = m_chapter;
    }

    public String getCopyright() {
        return m_copyright;
    }

    public void setCopyright(String m_copyright) {
        this.m_copyright = m_copyright;
    }

    public String getCrossref() {
        return m_crossref;
    }

    public void setCrossref(String m_crossref) {
        this.m_crossref = m_crossref;
    }

    public String getEdition() {
        return m_edition;
    }

    public void setEdition(String m_edition) {
        this.m_edition = m_edition;
    }

    public String getEditor() {
        return m_editor;
    }

    public void setEditor(String m_editor) {
        this.m_editor = m_editor;
    }

    public String getIsbn() {
        return m_isbn;
    }

    public void setIsbn(String isbn) {
        this.m_isbn = isbn;
    }

    public String getIssn() {
        return m_issn;
    }

    public void setIssn(String issn) {
        this.m_issn = issn;
    }

    public String getJournal() {
        return m_journal;
    }

    public void setJournal(String journal) {
        this.m_journal = journal;
    }

    public String getKey() {
        return m_key;
    }

    public void setKey(String key) {
        this.m_key = key;
    }

    public String getKeywords() {
        return m_keywords;
    }

    public void setKeywords(String keywords) {
        this.m_keywords = keywords;
    }

    public int getNumber() {
        return Integer.parseInt(m_number);
    }

    public void setNumber(int number) {
        this.m_number = Integer.toString(number);
    }

    public String getPages() {
        return m_pages;
    }

    public void setPages(String pages) {
        this.m_pages = pages;
    }

    public int getVolume() {
        return Integer.parseInt(m_volume);
    }

    public void setVolume(int volume) {
        this.m_volume = Integer.toString(volume);
    }

    public int getYear() {
        return Integer.parseInt(m_year);
    }

    public void setYear(int year) {
        this.m_year = Integer.toString(year);
    }

    public String getAddress() {
        return m_address;
    }

    public void setAddress(String address) {
        this.m_address = address;
    }

    public String getAnnotation() {
        return m_annotation;
    }

    public void setAnnotation(String annotation) {
        this.m_annotation = annotation;
    }

    public String getSeries() {
        return m_series;
    }

    public void setSeries(String m_series) {
        this.m_series = m_series;
    }

    public String getTitle() {
        return m_title;
    }

    public void setTitle(String title) {
        this.m_title = title;
    }

    public String getUrl() {
        return m_url;
    }

    public void setUrl(String Url) {
        this.m_url = Url;
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

        indiv.addComment(model.createTypedLiteral("Individual automatically generated by YAQP",
                XSDDatatype.XSDstring));
        indiv.addLiteral(model.createAnnotationProperty(DC.identifier.getURI()),
                model.createTypedLiteral(bibtexUri, XSDDatatype.XSDstring));

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
                if (!f.getName().equals("m_id")
                        && !f.getName().equals("m_author")
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

    public BibTeX createFromString(String string) {
        return this;
    }
}
