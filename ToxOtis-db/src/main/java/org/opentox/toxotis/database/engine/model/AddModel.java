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
package org.opentox.toxotis.database.engine.model;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.opentox.toxotis.core.component.Feature;
import org.opentox.toxotis.core.component.Model;
import org.opentox.toxotis.core.component.Parameter;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.database.DbWriter;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.MetaInfoBlobber;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class AddModel extends DbWriter {

    private final Model model;
    private Statement modelStatement = null;
    private PreparedStatement writeParamMeta = null;
    private PreparedStatement writeParamComponent = null;
    private PreparedStatement writeMeta = null;
    private PreparedStatement writeFeature = null;
    private PreparedStatement writeComponent = null;
    private PreparedStatement writeModel = null;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AddModel.class);
    private static final String INSERT_FEATURE =
            "INSERT IGNORE Feature (uri, units) VALUES (?,?)";//write all features (if not already)
    private static final String INSERT_META =
            "INSERT IGNORE INTO MetaInfo (id, meta) VALUES (?,compress(?))";
    private static final String INSERT_COMPONENT =
            "INSERT INTO OTComponent (id,enabled,meta) VALUES ('%s',?,?)";//write component stuff related to model
    private static final String INSERT_COMPONENT_FS =
            "INSERT IGNORE OTComponent (id,enabled,meta) VALUES (?,true,?)";//write component stuff related to param
    private static final String INSERT_MODEL =
            "INSERT INTO Model (id, createdBy, algorithm, localCode, dataset, actualModel) VALUES ('%s', ?,?,?,?,compress(?))";//write model (blob is compressed)
    private static final String INSERT_MODEL_DEPF =
            "INSERT INTO ModelDepFeatures (modelId,featureUri,idx) VALUES ";//write model dep. feature
    private static final String INSERT_MODEL_IDEPF =
            "INSERT INTO ModelIndepFeatures (modelId,featureUri,idx) VALUES ";//write model indep. features
    private static final String INSERT_MODEL_PREDF =
            "INSERT INTO ModelPredictedFeatures (modelId,featureUri,idx) VALUES ";//write model pred. features

    public AddModel(Model model) throws NullPointerException {
        if (model == null) {
            throw new NullPointerException("You can't write a NULL model in the database!");
        }
        this.model = model;
    }

    private String prepareQueryForFeatureRelations(String queryStart, List<Feature> features) {
        StringBuilder sb = new StringBuilder(queryStart);
        int length = features.size();
        String modelId = model.getUri().getId();
        for (int i = 0; i < length; i++) {
            sb.append("('");
            sb.append(modelId);
            sb.append("', '");
            sb.append(features.get(i).getUri().toString());
            sb.append("', ");
            sb.append(i);
            sb.append(")");
            if (i != length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    private String prepareQueryForParameters() {
        if (model.getParameters() == null || (model.getParameters() != null && model.getParameters().isEmpty())) {
            throw new IllegalArgumentException("This method should not have been invoked! Programatic error!");
        }
        StringBuilder insertParametersSB = new StringBuilder("INSERT IGNORE Parameter (id,name,scope,value,valueType,modelId) VALUES ");
        int length = model.getParameters().size();
        Set<Parameter> params = model.getParameters();
        Iterator<Parameter> iter = params.iterator();
        Parameter p = null;
        for (int i = 0; i < length; i++) {
            p = iter.next();
            insertParametersSB.append("('");
            insertParametersSB.append(p.getUri().getId());
            insertParametersSB.append("', '");
            insertParametersSB.append(p.getName().getValueAsString());
            insertParametersSB.append("', '");
            insertParametersSB.append(p.getScope());
            insertParametersSB.append("', '");
            insertParametersSB.append(p.getTypedValue().getValueAsString());
            insertParametersSB.append("', '");
            insertParametersSB.append(p.getTypedValue().getType().getURI());
            insertParametersSB.append("', '");
            insertParametersSB.append(model.getUri().getId());
            insertParametersSB.append("' )");
            if (i != length - 1) {
                insertParametersSB.append(", ");
            }
        }
        return insertParametersSB.toString();
    }

    @Override
    public int write() throws DbException {

        String insertModelAsComponent = String.format(INSERT_COMPONENT, model.getUri().getId());
        String insertModelSql = String.format(INSERT_MODEL, model.getUri().getId());

        Connection connection = getConnection();

        Set<Feature> featuresToBeWritten = new HashSet<Feature>();
        if (model.getDependentFeatures() != null) {
            featuresToBeWritten.addAll(model.getDependentFeatures());
        }
        if (model.getIndependentFeatures() != null) {
            featuresToBeWritten.addAll(model.getIndependentFeatures());
        }
        if (model.getPredictedFeatures() != null) {
            featuresToBeWritten.addAll(model.getPredictedFeatures());
        }


        try {
            /*
             * Prepare statements
             */
            if (model.getMeta() != null) {
                writeMeta = connection.prepareStatement(INSERT_META);
            }
            writeFeature = connection.prepareStatement(INSERT_FEATURE);
            writeComponent = connection.prepareStatement(insertModelAsComponent);
            writeModel = connection.prepareStatement(insertModelSql);


            /*
             * Write all features in the database!
             */
            connection.setAutoCommit(false);
            for (Feature f : featuresToBeWritten) {
                writeFeature.setString(1, f.getUri().toString());
                if (f.getUnits() != null) {
                    writeFeature.setString(2, f.getUnits());
                } else {
                    writeFeature.setNull(2, Types.VARCHAR);
                }
                writeFeature.executeUpdate();
                writeFeature.clearParameters();
            }

            /*
             * Write MetaInfo
             */
            if (writeMeta != null) {
                writeMeta.setInt(1, model.getMeta().hashCode());
                MetaInfoBlobber mib = new MetaInfoBlobber(model.getMeta());
                Blob miBlob = null;
                try {
                    miBlob = mib.toBlob();
                    writeMeta.setBlob(2, miBlob);
                } catch (final Exception ex) {
                    logger.error("Not Serializable?!", ex);
                }
                writeMeta.executeUpdate();
            }

            /*
             * Write model in the database
             */
            writeComponent.setBoolean(1, model.isEnabled());
            if (model.getMeta() != null) {
                writeComponent.setInt(2, model.getMeta().hashCode());
            } else {
                writeComponent.setNull(2, Types.INTEGER);
            }
            writeComponent.executeUpdate();
            String modelCreator = User.GUEST.getUid();//default user is User.GUEST
            if (model.getCreatedBy() != null) {
                modelCreator = model.getCreatedBy().getUid();
            }
            writeModel.setString(1, modelCreator);
            writeModel.setString(2, model.getAlgorithm().getUri().toString());
            writeModel.setString(3, model.getLocalCode());
            writeModel.setString(4, model.getDataset() != null ? model.getDataset().toString() : null);
            writeModel.setBlob(5, model.getBlob());
            writeModel.executeUpdate();

            /*
             * Before proceeding write Parameters as OTComponents
             *  1. Write their metadata (if any)
             *  2. Add params as components
             */
            if (model.getParameters() != null && !model.getParameters().isEmpty()) {
                writeParamMeta = connection.prepareStatement(INSERT_META);
                writeParamComponent = connection.prepareStatement(INSERT_COMPONENT_FS);
                for (Parameter p : model.getParameters()) {
                    MetaInfo prmMeta = p.getMeta();
                    int prmHash = prmMeta.hashCode();
                    writeParamMeta.setInt(1, prmHash);
                    MetaInfoBlobber blobber = new MetaInfoBlobber(prmMeta);
                    try {
                        Blob blob = blobber.toBlob();
                        writeParamMeta.setBlob(2, blob);
                    } catch (final Exception ex) {
                        logger.error("MetaInfo cannot be serialized!", ex);
                        throw new DbException(ex);
                    }
                    writeParamMeta.addBatch();

                    writeParamComponent.setString(1, p.getUri().getId());
                    writeParamComponent.setInt(2, prmHash);
                    writeParamComponent.addBatch();
                }
            }
            writeParamMeta.executeBatch();
            writeParamComponent.executeBatch();


            /*
             * Dependent, Independent, Predicted features
             */
            modelStatement = connection.createStatement();
            if (model.getDependentFeatures() != null) {
                String depFeatSQL = prepareQueryForFeatureRelations(INSERT_MODEL_DEPF, model.getDependentFeatures());
                modelStatement.addBatch(depFeatSQL);
            }
            if (model.getIndependentFeatures() != null) {
                String indepFeatSQL = prepareQueryForFeatureRelations(INSERT_MODEL_IDEPF, model.getIndependentFeatures());
                modelStatement.addBatch(indepFeatSQL);
            }
            if (model.getPredictedFeatures() != null) {
                String predFeatSQL = prepareQueryForFeatureRelations(INSERT_MODEL_PREDF, model.getPredictedFeatures());
                modelStatement.addBatch(predFeatSQL);
            }
            if (model.getParameters() != null && !model.getParameters().isEmpty()) {
                modelStatement.addBatch(prepareQueryForParameters());
            }
            modelStatement.executeBatch();
            connection.commit();
        } catch (final SQLException ex) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (final SQLException ex1) {
                    throw new DbException(ex1);
                }
            }
            throw new DbException(ex);
        } finally {
            SQLException sqlOnClose = null;

            if (modelStatement != null) {
                try {
                    modelStatement.close();
                } catch (SQLException ex) {
                    final String msg = "SQL exception occured while closing the (main) SQL statement for "
                            + "adding a model in the database consisting of particular batched statements";
                    logger.warn(msg, ex);
                    sqlOnClose = ex;
                }
            }
            if (writeComponent != null) {
                try {
                    writeComponent.close();
                } catch (SQLException ex) {
                    final String msg = "SQL exception occured while closing the SQL statement for "
                            + "adding a model in the database : ".concat(insertModelAsComponent != null ? insertModelAsComponent : "N/A");
                    logger.warn(msg, ex);
                    sqlOnClose = ex;
                }
            }
            if (writeFeature != null) {
                try {
                    writeFeature.close();
                } catch (SQLException ex) {
                    final String msg = "SQL exception occured while closing the SQL statement for "
                            + "adding a model in the database : ".concat(INSERT_FEATURE);
                    logger.warn(msg, ex);
                    sqlOnClose = ex;
                }
            }
            if (writeMeta != null) {
                try {
                    writeMeta.close();
                } catch (SQLException ex) {
                    final String msg = "SQL exception occured while closing the SQL statement for "
                            + "adding a model in the database : ".concat(INSERT_META != null ? INSERT_META : "N/A");
                    logger.warn(msg, ex);
                    sqlOnClose = ex;
                }
            }
            if (writeModel != null) {
                try {
                    writeModel.close();
                } catch (SQLException ex) {
                    final String msg = "SQL exception occured while closing the SQL statement for "
                            + "adding a model in the database : ".concat(insertModelSql != null ? insertModelSql : "N/A");
                    logger.warn(msg, ex);
                    sqlOnClose = ex;
                }
            }
            if (writeParamMeta != null) {
                try {
                    writeParamMeta.close();
                } catch (SQLException ex) {
                    final String msg = "SQL exception occured while closing the SQL statement for "
                            + "adding a model in the database : ".concat(INSERT_META != null ? INSERT_META : "N/A");
                    logger.warn(msg, ex);
                    sqlOnClose = ex;
                }
            }
            if (writeParamComponent != null) {
                try {
                    writeParamComponent.close();
                } catch (SQLException ex) {
                    final String msg = "SQL exception occured while closing the SQL statement for "
                            + "adding a model in the database : ".concat(INSERT_COMPONENT_FS != null ? INSERT_COMPONENT_FS : "N/A");
                    logger.warn(msg, ex);
                    sqlOnClose = ex;
                }
            }
            close();
            if (sqlOnClose != null) {
                logger.warn(null, sqlOnClose);
                throw new DbException(sqlOnClose);
            }
        }

        return -1;
    }
}
