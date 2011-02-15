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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opentox.toxotis.core.component.Feature;
import org.opentox.toxotis.core.component.Model;
import org.opentox.toxotis.core.component.Parameter;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.database.DbWriter;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.ontology.MetaInfoBlobber;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class AddModel extends DbWriter {

    private final Model model;

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
        StringBuilder insertParametersSB = new StringBuilder("INSERT INTO Parameter (id,name,scope,value,valueType,modelId) VALUES ");
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

        String insertFeatureSql = "INSERT IGNORE Feature (uri, units) VALUES (?,?)";//write all features (if not already)
        String insertModelAsComponent = "INSERT INTO OTComponent (id,enabled,meta) VALUES ('%s',?,compress(?))";//write component stuff related to model
        String insertModelSql = "INSERT INTO Model (id, createdBy, algorithm, localCode, dataset, actualModel) VALUES ('%s', ?,?,?,?,compress(?))";//write model (blob is compressed)
        String insertModelDependentFeatures = "INSERT INTO ModelDepFeatures (modelId,featureUri,idx) VALUES ";//write model dep. feature
        String insertModelIndependentFeatures = "INSERT INTO ModelIndepFeatures (modelId,featureUri,idx) VALUES ";//write model indep. features
        String insertModelPredictedFeatures = "INSERT INTO ModelPredictedFeatures (modelId,featureUri,idx) VALUES ";//write model pred. features      

        insertModelAsComponent = String.format(insertModelAsComponent, model.getUri().getId());
        insertModelSql = String.format(insertModelSql, model.getUri().getId());

        Connection connection = getConnection();
        PreparedStatement writeFeature = null;
        PreparedStatement writeComponent = null;
        PreparedStatement writeModel = null;

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
            writeFeature = connection.prepareStatement(insertFeatureSql);
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
             * Write model in the database
             */
            // INSERT INTO OTComponent (id,enabled,meta) VALUES ('%s',?,?)
            // INSERT INTO `Model` (createdBy, algorithm, localCode, dataset, actualModel) VALUES ('%s', ?,?,?,?)
            writeComponent.setBoolean(1, model.isEnabled());
            MetaInfoBlobber mib = new MetaInfoBlobber(model.getMeta());
            Blob miBlob = null;
            try {
                miBlob = mib.toBlob();
                writeComponent.setBlob(2, miBlob);
            } catch (Exception ex) {
                Logger.getLogger(AddModel.class.getName()).log(Level.SEVERE, null, ex);
            }
            writeComponent.executeUpdate();
            String modelCreator = User.GUEST.getUid();//default user is User.GUEST
            if (model.getCreatedBy() != null) {
                modelCreator = model.getCreatedBy().getUid();
            }
            writeModel.setString(1, modelCreator);
            writeModel.setString(2, model.getAlgorithm().getUri().toString());
            writeModel.setString(3, model.getLocalCode());
            writeModel.setString(4, model.getDataset().toString());
            writeModel.setBlob(5, model.getBlob());
            writeModel.executeUpdate();

            /*
             * Dependent, Independent, Predicted features
             */
            Statement statement = connection.createStatement();
            if (model.getDependentFeatures() != null) {
                String depFeatSQL = prepareQueryForFeatureRelations(insertModelDependentFeatures, model.getDependentFeatures());
                statement.addBatch(depFeatSQL);
            }
            if (model.getIndependentFeatures() != null) {
                String indepFeatSQL = prepareQueryForFeatureRelations(insertModelIndependentFeatures, model.getIndependentFeatures());
                statement.addBatch(indepFeatSQL);
            }
            if (model.getPredictedFeatures() != null) {
                String predFeatSQL = prepareQueryForFeatureRelations(insertModelPredictedFeatures, model.getPredictedFeatures());
                statement.addBatch(predFeatSQL);
            }
            if (model.getParameters() != null && !model.getParameters().isEmpty()) {
                System.out.println(prepareQueryForParameters());
                statement.addBatch(prepareQueryForParameters());
            }
            statement.executeBatch();


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
            close();
        }

        return -1;
    }
}
