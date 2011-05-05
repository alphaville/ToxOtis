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

import java.net.URISyntaxException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Algorithm;
import org.opentox.toxotis.core.component.Model;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.database.DbIterator;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.engine.cache.Cache;
import org.opentox.toxotis.database.engine.cache.ICache;
import org.opentox.toxotis.database.engine.user.FindUser;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.MetaInfoDeblobber;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ModelIterator extends DbIterator<Model> {

    private final VRI baseUri;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ModelIterator.class);
    private boolean resolveUser = false;
    private ICache<String, User> usersCache = new Cache<String, User>();

    public ModelIterator(final ResultSet rs, final VRI baseUri) {
        super(rs);
        this.baseUri = baseUri;
    }

    public boolean isResolveUser() {
        return resolveUser;
    }

    public void setResolveUser(boolean resolveUser) {
        this.resolveUser = resolveUser;
    }

    @Override
    public Model next() throws DbException {
        Model nextModel = new Model();
        try {
            String modelId = rs.getString(1);
            nextModel.setUri(new VRI(baseUri).augment("model", modelId));

            String modelCreator = rs.getString(2);
            if (modelCreator != null) {
                User user = usersCache.get(modelCreator);//try to get the user from the cache.
                if (user == null) {// if user is not found in cache, create it and put it there!
                    user = new User();
                    user.setUid(modelCreator);
                    if (resolveUser) {
                        FindUser fu = new FindUser();
                        fu.setWhere("uid='" + modelCreator + "'");
                        IDbIterator<User> users = fu.list();
                        if (users.hasNext()) {
                            user = users.next();
                        }
                        users.close();
                        fu.close();
                    }
                    usersCache.put(modelCreator, user);
                }
                nextModel.setCreatedBy(user);
            }

            try {
                nextModel.setAlgorithm(new Algorithm(new VRI(rs.getString(3))));
            } catch (final ToxOtisException ex) {
                logger.warn("Invalid algorithm URI pointed by the model".concat(modelId), ex);
                throw new RuntimeException(ex);
            } catch (final URISyntaxException ex) {
                logger.error("Illegal URI found for training algorithm for a model", ex);
                throw new RuntimeException(ex);
            }
            nextModel.setLocalCode(rs.getString(4));
            try {
                nextModel.setDataset(new VRI(rs.getString(5)));
            } catch (URISyntaxException ex) {
                final String msg = "Illegal URI found for training dataset for a model";
                logger.error(msg, ex);
                throw new RuntimeException(msg, ex);
            }
            Blob actualModelBlob = rs.getBlob(6);
            if (actualModelBlob != null) {
                nextModel.setBlob(actualModelBlob);
                try {
                    actualModelBlob.free();
                } catch (final SQLException ex) {
                    logger.warn("An error occured releasing the (actual model) Blob's resources", ex);
                    throw ex;
                }
            }

            Blob metaInfoBlob = rs.getBlob(7);
            if (metaInfoBlob != null) {
                MetaInfoDeblobber mid = new MetaInfoDeblobber(metaInfoBlob);
                MetaInfo mi = mid.toMetaInfo();
                nextModel.setMeta(mi);
                metaInfoBlob.free();
            }

            FindModelParameters paramFinder = new FindModelParameters(modelId, baseUri);
            nextModel.setParameters(paramFinder.listParameters());
            paramFinder.close();

            FindModelFeatures depFeaturesFinder = new FindModelFeatures(FindModelFeatures.SEARCH_MODE.DEPENDENT, modelId);
            nextModel.setDependentFeatures(depFeaturesFinder.list());
            depFeaturesFinder.close();

            FindModelFeatures indepFeaturesFinder = new FindModelFeatures(FindModelFeatures.SEARCH_MODE.INDEPENDENT, modelId);
            nextModel.setIndependentFeatures(indepFeaturesFinder.list());
            indepFeaturesFinder.close();

            FindModelFeatures predictedFeaturesFinder = new FindModelFeatures(FindModelFeatures.SEARCH_MODE.PREDICTED, modelId);
            nextModel.setPredictedFeatures(predictedFeaturesFinder.list());
            predictedFeaturesFinder.close();

            ModelReferencesFinder modelReferences = new ModelReferencesFinder(modelId);
            Set<String> references = modelReferences.getReferences();
            for (String ref : references) {
                nextModel.addBibTeXReferences(new VRI(baseUri).augment("bibtex", ref));
            }

            return nextModel;
        } catch (final SQLException ex) {
            final String msg = "SQL-related exception thrown while reading model data from the database";
            logger.warn(msg, ex);
            throw new DbException(msg, ex);
        }
    }
}
