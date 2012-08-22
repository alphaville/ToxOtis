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
package org.opentox.toxotis.database.engine.metainfo;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.opentox.toxotis.database.DbUpdater;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.MetaInfoBlobber;

/**
 * Configurable Updater of MetaInfo entries. 
 * TODO: Write more documentation here!
 * 
 * @author Pantelis Sopasakis
 */
public class UpdateMetaInfo extends DbUpdater {

    public enum UpdateMode {

        APPEND,
        REPLACE;
    }
    /* id of the component whose meta-info should be updated*/
    private String componentId;
    private MetaInfo meta;
    private UpdateMode updateMode = UpdateMode.REPLACE;
    /*
     * Adds a new MetaInfo object.
     */
    private static final String SQL_ADD_METAINFO = "INSERT IGNORE MetaInfo (id, meta)  VALUES (?, compress(?))";
    /*
     * Updates the MetaInfo column of an OTComponet - Now pointing to a new MetaInfo
     * entry.
     */
    private static final String SQL_UPDATE_COMPONENT_META = "UPDATE OTComponent SET meta=? WHERE id=?";
    private PreparedStatement updateComponentMetaStmt = null;
    private PreparedStatement addMetaStmt = null;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UpdateMetaInfo.class);

    /**
     * ID of the component.
     * @return 
     */
    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public MetaInfo getMeta() {
        return meta;
    }

    public void setMeta(MetaInfo meta) {
        this.meta = meta;
    }

    public UpdateMode getUpdateMode() {
        return updateMode;
    }

    public void setUpdateMode(UpdateMode updateMode) {
        this.updateMode = updateMode;
    }

    @Override
    public int update() throws DbException {
        Connection connection = getConnection();
        try {
            //TODO: Create a MetaInfo Writer
            connection.setAutoCommit(false);
            addMetaStmt = connection.prepareStatement(SQL_ADD_METAINFO);
            int metaHash = getMeta().hashCode();
            addMetaStmt.setInt(1, metaHash);
            MetaInfoBlobber mib = new MetaInfoBlobber(getMeta());
            try {
                Blob blob = mib.toBlob();
                addMetaStmt.setBlob(2, blob);
            } catch (final SQLException ex) {
                logger.warn("Improper parametrization of prepared statement", ex);
                throw ex;
            } catch (final Exception ex) {
                logger.warn("MetaInfo serialization exception", ex);
            }
            int status = addMetaStmt.executeUpdate();

            updateComponentMetaStmt = connection.prepareStatement(SQL_UPDATE_COMPONENT_META);
            updateComponentMetaStmt.setLong(1, metaHash);
            updateComponentMetaStmt.setString(2, componentId);
            status += updateComponentMetaStmt.executeUpdate();
            connection.commit();
            return status;
        } catch (final SQLException ex) {
            throw new DbException(ex);
        }
    }

    @Override
    public void close() throws DbException {
        DbException exception = null;
        if (addMetaStmt != null) {
            try {
                addMetaStmt.close();
            } catch (final SQLException ex) {
                exception = new DbException(ex);
            }
        }
        if (updateComponentMetaStmt != null) {
            try {
                updateComponentMetaStmt.close();
            } catch (final SQLException ex) {
                exception = new DbException(ex);
            }
        }
        super.close();
        if (exception != null) {
            throw exception;
        }
    }
}
