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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Model;
import org.opentox.toxotis.database.DbReader;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class FindModel extends DbReader<Model> {

    private final VRI baseUri;
    private boolean includeDisabled = false;
    private Statement statement = null;
    private boolean resolveUsers;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(FindModel.class);

    public FindModel(final VRI baseUri) {
        this.baseUri = baseUri;
    }

    public boolean isResolveUsers() {
        return resolveUsers;
    }

    public void setResolveUsers(boolean resolveUsers) {
        this.resolveUsers = resolveUsers;
    }


    public void setSearchById(String id) {
        String whereTemplate = "Model.id='%s'";
        setWhere(String.format(whereTemplate, id));
    }

    @Override
    public IDbIterator<Model> list() throws DbException {
        setTable("Model");
        setTableColumns("Model.id", "Model.createdBy", "Model.algorithm", "Model.localcode", "Model.dataset", "uncompress(actualModel)", "uncompress(MetaInfo.meta)");
        setInnerJoin("OTComponent ON Model.id=OTComponent.id "
                + "LEFT JOIN MetaInfo ON OTComponent.meta=MetaInfo.id");
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
            ModelIterator it = new ModelIterator(rs, baseUri);
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
