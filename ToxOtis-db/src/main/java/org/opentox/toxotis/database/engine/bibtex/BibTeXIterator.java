package org.opentox.toxotis.database.engine.bibtex;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.BibTeX;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.database.DbIterator;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.engine.cache.Cache;
import org.opentox.toxotis.database.engine.cache.ICache;
import org.opentox.toxotis.database.engine.user.FindUser;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.MetaInfoDeblobber;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class BibTeXIterator extends DbIterator<BibTeX> {

    private final VRI baseUri;
    private ICache<String, User> usersCache = new Cache<String, User>();
    private boolean resolveUser = false;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BibTeXIterator.class);

    public BibTeXIterator(final ResultSet rs, final VRI baseUri) {
        super(rs);
        if (baseUri == null) {
            throw new NullPointerException("The baseUri you provided to the constructor of BibTeXIterator was null! You can "
                    + "use an anonymous/fake URI instead like http://void.anonymous.io instead of a null URI.");
        }
        this.baseUri = baseUri;
    }

    public boolean isResolveUser() {
        return resolveUser;
    }

    public void setResolveUser(boolean resolveUser) {
        this.resolveUser = resolveUser;
    }

//               1 "BibTeX.id",
//               2 "BibTeX.createdBy",
//               3 "BibTeX.abstract",
//               4 "BibTeX.address",
//               5 "BibTeX.annotation",
//               6 "BibTeX.author",
//               7 "BibTeX.bibType",
//               8 "BibTeX.bookTitle",
//               9 "BibTeX.chapter",
//               10 "BibTeX.copyright",
//               11 "BibTeX.crossref",
//               12 "BibTeX.edition",
//               13 "BibTeX.editor",
//               14 "BibTeX.isbn",
//               15 "BibTeX.issn",
//               16 "BibTeX.journal",
//               17 "BibTeX.bibkey",
//               18 "BibTeX.keywords",
//               19 "BibTeX.number",
//               20 "BibTeX.pages",
//               21 "BibTeX.series",
//               22 "BibTeX.title",
//               23 "BibTeX.url",
//               24 "BibTeX.volume",
//               25 "BibTeX.year",
//               26 "uncompress(meta)"
    @Override
    public BibTeX next() throws DbException {
        BibTeX nextBibTeX = new BibTeX();
        try {
            String bibtexId = rs.getString("id");
            String createdBy = rs.getString("createdBy");
            String abstractBib = rs.getString("abstract");
            String address = rs.getString("address");
            String annotation = rs.getString("annotation");
            String author = rs.getString("author");
            String bibType = rs.getString("bibType");
            String bookTitle = rs.getString("bookTitle");
            String chapter = rs.getString("chapter");
            String copyright = rs.getString("copyright");
            String crossref = rs.getString("crossref");
            String edition = rs.getString("edition");
            String editor = rs.getString("editor");
            String isbn = rs.getString("isbn");
            String issn = rs.getString("issn");
            String journal = rs.getString("journal");
            String bibkey = rs.getString("bibkey");
            String keywords = rs.getString("keywords");
            String pages = rs.getString("pages");
            String series = rs.getString("series");
            String title = rs.getString("title");
            String url = rs.getString("url");

            String number = rs.getString("number");
            String volume = rs.getString("volume");
            String year = rs.getString("year");

            Blob metaInfoBlob = rs.getBlob(26);
            if (metaInfoBlob != null) {
                MetaInfoDeblobber mid = new MetaInfoDeblobber(metaInfoBlob);
                MetaInfo mi = mid.toMetaInfo();
                nextBibTeX.setMeta(mi);
                metaInfoBlob.free();
            } 

            nextBibTeX.setUri(new VRI(baseUri).augment(bibtexId));
            nextBibTeX.setAbstract(abstractBib);
            nextBibTeX.setAddress(address);
            nextBibTeX.setAnnotation(annotation);
            nextBibTeX.setAuthor(author);

            BibTeX.BibTYPE bibTYPE = BibTeX.BibTYPE.Entry;
            if (bibType != null) {
                bibTYPE = BibTeX.BibTYPE.valueOf(bibType);
            }
            nextBibTeX.setBibType(bibTYPE);

            nextBibTeX.setBookTitle(bookTitle);
            nextBibTeX.setChapter(chapter);
            nextBibTeX.setCopyright(copyright);


            if (createdBy != null) {
                User user = usersCache.get(createdBy);//try to get the user from the cache.
                if (user == null) {// if user is not found in cache, create it and put it there!
                    user = new User();
                    user.setUid(createdBy);
                    if (resolveUser) {
                        FindUser fu = new FindUser();
                        fu.setWhere("uid='" + createdBy + "'");
                        IDbIterator<User> users = fu.list();
                        if (users.hasNext()) {
                            user = users.next();
                        }
                        users.close();
                        fu.close();
                    }
                    usersCache.put(createdBy, user);
                }
                nextBibTeX.setCreatedBy(user);
            }

            nextBibTeX.setCrossref(crossref);
            nextBibTeX.setEdition(edition);
            nextBibTeX.setEditor(editor);
            nextBibTeX.setIsbn(isbn);
            nextBibTeX.setIssn(issn);
            nextBibTeX.setJournal(journal);
            nextBibTeX.setKey(bibkey);
            nextBibTeX.setKeywords(keywords);



            if (number == null) {
                nextBibTeX.setNumber(null);
            } else {
                nextBibTeX.setNumber(Integer.parseInt(number));
            }
            if (year == null) {
                nextBibTeX.setYear(null);
            } else {
                nextBibTeX.setYear(Integer.parseInt(year));
            }
            if (volume == null) {
                nextBibTeX.setVolume(null);
            } else {
                nextBibTeX.setVolume(Integer.parseInt(volume));
            }

            nextBibTeX.setPages(pages);
            nextBibTeX.setSeries(series);
            nextBibTeX.setTitle(title);
            nextBibTeX.setUrl(url);


        } catch (SQLException ex) {
            final String msg = "SQL-related exception thrown while reading bibtex data from the database";
            logger.warn(msg, ex);
            throw new DbException(msg, ex);
        }
        return nextBibTeX;
    }
}
