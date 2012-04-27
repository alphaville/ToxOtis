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
package org.opentox.toxotis.database.engine.parameter;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Parameter;
import org.opentox.toxotis.database.DbWriter;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.ontology.MetaInfoBlobber;

/**
 *
 * @author chung
 */
public class AddParameter extends DbWriter {

    private final Parameter parameter;
    private final VRI modelUri;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AddParameter.class);
    PreparedStatement ps = null;
    private final String insertMeta = "INSERT IGNORE INTO MetaInfo (id, meta) VALUES (?,compress(?))";
    private final String insertComponent = "INSERT INTO OTComponent (id,enabled,meta) VALUES (?,?,?)";
    private final String insertParameter = "INSERT INTO Parameter (id,name,scope,value,valueType,modelId) "
            + "VALUES (?,?,?,?,?,?)";
    private PreparedStatement writeComponent = null;
    private PreparedStatement writeMeta = null;

    public AddParameter(final Parameter prm, final VRI modelUri) {
        super();
        this.parameter = prm;
        this.modelUri = modelUri;
    }

    @Override
    public int write() throws DbException {
        if (this.parameter == null) {
            throw new DbException("Nothing to write. Parameter is null");
        }
        if (this.parameter.getUri() == null) {
            throw new DbException("Cannot register a parameter without an identifier");
        }
        Connection connection = getConnection();
        try {
            connection.setAutoCommit(false);

            if (parameter.getMeta() != null) {
                writeMeta = connection.prepareStatement(insertMeta);
                writeMeta.setInt(1, parameter.getMeta().hashCode());
                MetaInfoBlobber mib = new MetaInfoBlobber(parameter.getMeta());
                Blob miBlob = null;
                try {
                    miBlob = mib.toBlob();
                    writeMeta.setBlob(2, miBlob);
                } catch (Exception ex) {
                    logger.error("Exception while creating and setting meta-info BLOB", ex);
                }
                writeMeta.executeUpdate();
            }

            writeComponent = connection.prepareStatement(insertComponent);
            writeComponent.setString(1, parameter.getUri().getId());
            writeComponent.setBoolean(2, parameter.isEnabled());
            if (parameter.getMeta() != null) {
                writeComponent.setInt(3, parameter.getMeta().hashCode());
            } else {
                writeComponent.setNull(3, Types.INTEGER);
            }
            writeComponent.executeUpdate();

        } catch (SQLException ex) {
            logger.error("", ex);
            throw new DbException("", ex);
        } finally {
            if (writeComponent != null) {
                try {
                    writeComponent.close();
                } catch (SQLException ex) {
                    final String msg = "SQL exception occured while closing the SQL statement for "
                            + "adding a parameter in the database : ".concat(insertParameter != null
                            ? insertParameter : "N/A");
                    logger.warn(msg, ex);

                }
            }
        }

        try {
            ps = getConnection().prepareStatement(insertParameter);
            ps.setString(1, parameter.getUri().getId());
            ps.setString(2, parameter.getName() != null ? parameter.getName().getValueAsString() : null);
            ps.setString(3, parameter.getScope()!=null ? parameter.getScope().toString() : "OPTIONAL");            
            ps.setString(4, parameter.getTypedValue()!=null ? 
                    parameter.getValue().toString() : null);
            ps.setString(5, parameter.getType()!=null ? parameter.getType().getURI() : null);
            ps.setString(6, modelUri.getId());
            
            int update = ps.executeUpdate();

            /*
             * COMMIT :)
             */
            connection.commit();
            return update;
        } catch (SQLException ex) {
            final String msg = "Parameter could not be added in the database";
            logger.warn(msg, ex);
            throw new DbException(msg, ex);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    final String msg = "Prepared statement for adding a Parameter "
                            + "in the database cannot be closed";
                    logger.warn(msg, ex);
                }
            }
            close();
        }

    }
}
