package org.opentox.toxotis.database.engine.bibtex;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import org.opentox.toxotis.core.component.BibTeX;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.database.DbWriter;
import org.opentox.toxotis.database.exception.DbException;

/**
 * Assumes that the user is already there. No need to perform a batch update for
 * every time a bibtex is written and also update the user as user updates are
 * expected to be more rare.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class AddBibTeX extends DbWriter {

    private final BibTeX bibtex;

    public AddBibTeX(final BibTeX bibtex) {
        super();
        this.bibtex = bibtex;
    }

    @Override
    public int write() throws DbException {
        if (this.bibtex == null) {
            throw new DbException("Nothing to write. BibTeX is null");
        }
        if (this.bibtex.getUri() == null) {
            throw new DbException("Cannot register a bibtex without an identifier");
        }
        setTable("BibTeX");
        setTableColumns("id", "abstract", "address", "annotation", "author", "bibtype",
                "bookTitle", "chapter", "copyright", "crossref", "edition", "editor", "isbn",
                "issn", "journal", "bibkey", "keywords", "number", "pages", "series", "title", "url",
                "volume", "year", "createdBy");
        try {

            

            PreparedStatement ps = getConnection().prepareStatement(getSql());
            ps.setString(1, bibtex.getUri().getId());
            ps.setString(2, bibtex.getAbstract());
            ps.setString(3, bibtex.getAddress());
            ps.setString(4, bibtex.getAnnotation());
            ps.setString(5, bibtex.getAuthor());
            BibTeX.BIB_TYPE bibType = bibtex.getBibType();
            String bibTypeString = null;
            if (bibType != null) {
                bibTypeString = bibType.toString();
            }
            ps.setString(6, bibTypeString);

            ps.setString(7, bibtex.getBookTitle());
            ps.setString(8, bibtex.getChapter());
            ps.setString(9, bibtex.getCopyright());
            ps.setString(10, bibtex.getCrossref());
            ps.setString(11, bibtex.getEdition());
            ps.setString(12, bibtex.getEditor());
            ps.setString(13, bibtex.getIsbn());

            ps.setString(14, bibtex.getIssn());
            ps.setString(15, bibtex.getJournal());
            ps.setString(16, bibtex.getKey());
            ps.setString(17, bibtex.getKeywords());
            if (bibtex.getNumber() != null) {
                ps.setInt(18, bibtex.getNumber());
            } else {
                ps.setNull(18, Types.INTEGER);
            }
            ps.setString(19, bibtex.getPages());
            ps.setString(20, bibtex.getSeries());
            ps.setString(21, bibtex.getTitle());
            ps.setString(22, bibtex.getUrl());
            if (bibtex.getVolume() != null) {
                ps.setInt(23, bibtex.getVolume());
            } else {
                ps.setNull(23, Types.INTEGER);
            }
            if (bibtex.getYear()!= null) {
                ps.setInt(24, bibtex.getYear());
            } else {
                ps.setNull(24, Types.INTEGER);
            }            
            ps.setString(25, bibtex.getCreatedBy().getUid());

            int update = ps.executeUpdate();
            ps.close();
            return update;
        } catch (SQLException ex) {
            throw new DbException(ex);
        } finally {
            close();
        }

    }
}
