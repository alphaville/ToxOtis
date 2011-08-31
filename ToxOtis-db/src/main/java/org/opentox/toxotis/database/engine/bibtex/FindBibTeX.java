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


package org.opentox.toxotis.database.engine.bibtex;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.BibTeX;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.DbReader;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class FindBibTeX extends DbReader<BibTeX> {

    private final VRI baseUri;
    private Statement statement = null;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(FindBibTeX.class);
    private boolean includeDisabled = false;
    private boolean resolveUsers;

    public FindBibTeX(final VRI baseUri) {
        this.baseUri = baseUri;
    }

    public boolean isIncludeDisabled() {
        return includeDisabled;
    }

    public void setIncludeDisabled(boolean includeDisabled) {
        this.includeDisabled = includeDisabled;
    }

    public boolean isResolveUsers() {
        return resolveUsers;
    }

    public void setResolveUsers(boolean resolveUsers) {
        this.resolveUsers = resolveUsers;
    }

    public void setSearchById(String id) {
        String whereTemplate = "BibTeX.id='%s'";
        setWhere(String.format(whereTemplate, id));
    }

//    CREATE TABLE `BibTeX` (
//  `id` varchar(255) COLLATE utf8_bin NOT NULL,
//  `abstract` varchar(255) COLLATE utf8_bin DEFAULT NULL,
//  `address` varchar(255) COLLATE utf8_bin DEFAULT NULL,
//  `annotation` varchar(255) COLLATE utf8_bin DEFAULT NULL,
//  `author` varchar(255) COLLATE utf8_bin NOT NULL,
//  `bibType` varchar(255) COLLATE utf8_bin DEFAULT NULL,
//  `bookTitle` varchar(255) COLLATE utf8_bin DEFAULT NULL,
//  `chapter` varchar(255) COLLATE utf8_bin DEFAULT NULL,
//  `copyright` varchar(255) COLLATE utf8_bin DEFAULT NULL,
//  `crossref` varchar(255) COLLATE utf8_bin DEFAULT NULL,
//  `edition` varchar(255) COLLATE utf8_bin DEFAULT NULL,
//  `editor` varchar(255) COLLATE utf8_bin DEFAULT NULL,
//  `isbn` varchar(20) COLLATE utf8_bin DEFAULT NULL,
//  `issn` varchar(20) COLLATE utf8_bin DEFAULT NULL,
//  `journal` varchar(255) COLLATE utf8_bin DEFAULT NULL,
//  `bibkey` varchar(255) COLLATE utf8_bin DEFAULT NULL,
//  `keywords` varchar(255) COLLATE utf8_bin DEFAULT NULL,
//  `number` int(11) unsigned DEFAULT NULL,
//  `pages` varchar(32) COLLATE utf8_bin DEFAULT NULL,
//  `series` varchar(255) COLLATE utf8_bin DEFAULT NULL,
//  `title` varchar(255) COLLATE utf8_bin DEFAULT NULL,
//  `url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
//  `volume` int(11) unsigned DEFAULT NULL,
//  `year` int(11) unsigned DEFAULT NULL,
//  `createdBy` varchar(255) COLLATE utf8_bin DEFAULT 'guest@opensso.in-silico.ch',
//  PRIMARY KEY (`id`) USING BTREE,
//  KEY `index_bibtex_id` (`id`) USING BTREE,
//  KEY `index_bibtex_createdBy` (`createdBy`) USING BTREE,
//  KEY `index_bibtex_author` (`author`) USING BTREE,
//  KEY `index_bibtex_booktitle` (`bookTitle`) USING BTREE,
//  KEY `index_bibtex_url` (`url`) USING BTREE,
//  CONSTRAINT `bibtex_extends_component_key` FOREIGN KEY (`id`) REFERENCES `OTComponent` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
//  CONSTRAINT `bibtex_user_reference` FOREIGN KEY (`createdBy`) REFERENCES `User` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
//) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin
    @Override
    public IDbIterator<BibTeX> list() throws DbException {
        setTable("BibTeX");
        setTableColumns(
                "BibTeX.id",
                "BibTeX.createdBy",
                "BibTeX.abstract",
                "BibTeX.address",
                "BibTeX.annotation",
                "BibTeX.author",
                "BibTeX.bibType",
                "BibTeX.bookTitle",
                "BibTeX.chapter",
                "BibTeX.copyright",
                "BibTeX.crossref",
                "BibTeX.edition",
                "BibTeX.editor",
                "BibTeX.isbn",
                "BibTeX.issn",
                "BibTeX.journal",
                "BibTeX.bibkey",
                "BibTeX.keywords",
                "BibTeX.number",
                "BibTeX.pages",
                "BibTeX.series",
                "BibTeX.title",
                "BibTeX.url",
                "BibTeX.volume",
                "BibTeX.year",
                "uncompress(MetaInfo.meta)");
        setInnerJoin("OTComponent ON BibTeX.id=OTComponent.id "
                + "INNER JOIN MetaInfo ON OTComponent.meta=MetaInfo.id "
                + "OR OTComponent.meta IS NULL");
        if (!includeDisabled) {
            if (where == null) {
                setWhere("OTComponent.enabled=true");
            } else {
                setWhere(where + " AND OTComponent.enabled=true");
            }
        }

        Connection connection = null;
        connection = getConnection();

        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(getSql());
            BibTeXIterator it = new BibTeXIterator(rs, baseUri);
            it.setResolveUser(resolveUsers);
            return it;
        } catch (SQLException ex) {
            throw new DbException(ex);
        } finally {
            // Do Nothing:  The client is expected to close the statement and the connection
        }
    }

    @Override
    public void close() throws DbException {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException ex) {
            throw new DbException(ex);
        } finally {
            super.close();
        }
    }
}
