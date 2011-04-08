package org.opentox.toxotis.database.engine.bibtex;

import java.sql.ResultSet;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.BibTeX;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.database.DbIterator;
import org.opentox.toxotis.database.engine.cache.Cache;
import org.opentox.toxotis.database.engine.cache.ICache;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class BibTeXIterator extends DbIterator<BibTeX>{

    private final VRI baseUri;
    private ICache<String, User> usersCache = new Cache<String, User>();
    private boolean resolveUser = false;

    public BibTeXIterator(final ResultSet rs, final VRI baseUri) {
        super(rs);
        this.baseUri= baseUri;
    }

    public boolean isResolveUser() {
        return resolveUser;
    }

    public void setResolveUser(boolean resolveUser) {
        this.resolveUser = resolveUser;
    }


//                "BibTeX.id",
//                "BibTeX.createdBy",
//                "BibTeX.abstract",
//                "BibTeX.address",
//                "BibTeX.annotation",
//                "BibTeX.author",
//                "BibTeX.bibType",
//                "BibTeX.bookTitle",
//                "BibTeX.chapter",
//                "BibTeX.copyright",
//                "BibTeX.crossref",
//                "BibTeX.edition",
//                "BibTeX.editor",
//                "BibTeX.isbn",
//                "BibTeX.issn",
//                "BibTeX.journal",
//                "BibTeX.bibkey",
//                "BibTeX.keywords",
//                "BibTeX.number",
//                "BibTeX.pages",
//                "BibTeX.series",
//                "BibTeX.title",
//                "BibTeX.url",
//                "BibTeX.volume",
//                "BibTeX.year",
//                "uncompress(meta)"
    

    @Override
    public BibTeX next() throws DbException {
        BibTeX nextBibTeX = new BibTeX();
        throw new UnsupportedOperationException("Not supported yet.");
    }

}