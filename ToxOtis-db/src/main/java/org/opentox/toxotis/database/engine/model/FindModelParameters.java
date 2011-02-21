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

import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Parameter;
import org.opentox.toxotis.database.DbOperation;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.ontology.LiteralValue;

/**
 *
 * @author Pantelis Sopasakis
 */
public class FindModelParameters extends DbOperation {

    private final String modelId;
    private final VRI baseUri;
    Statement statement = null;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(FindModelParameters.class);

    public FindModelParameters(final String modelId, final VRI baseUri) {
        super();
        this.modelId = modelId;
        this.baseUri = baseUri;
    }

    @Override
    public String getSqlTemplate() {
        String SQL = "SELECT id,name,scope,value,valueType FROM Parameter WHERE modelId='%s'";
        return SQL;
    }

    private String getSql() {
        return String.format(getSqlTemplate(), modelId);
    }

    private Parameter resolveParameter(final ResultSet rs) throws SQLException {
        Parameter p = new Parameter(new VRI(baseUri).augment("parameter", rs.getString(1)));
        p.setName(rs.getString(2));
        p.setScope(Parameter.ParameterScope.valueOf(rs.getString(3)));

        LiteralValue lv = new LiteralValue();
        String value = rs.getString(4);
        lv.setValue(value);

        String datatypeUri = rs.getString(5);
        if (datatypeUri != null) {
            TypeMapper mapper = TypeMapper.getInstance();
            XSDDatatype xsd = (XSDDatatype) mapper.getTypeByName(datatypeUri);
            lv.setType(xsd);
        }
        p.setTypedValue(lv);
        return p;
    }

    public Set<Parameter> listParameters() throws DbException {
        Connection connection = null;
        connection = getConnection();
        ResultSet rs = null;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(getSql());
            Set<Parameter> set = new HashSet<Parameter>();
            while (rs.next()) {
                set.add(resolveParameter(rs));
            }
            return set;
        } catch (SQLException ex) {
            final String msg = "SQL-related exception while listing model parameters retrieved from the database";
            logger.warn(msg, ex);
            throw new DbException(msg, ex);
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                final String msg = "Result set engaged with parameters failed to close";
                logger.warn(msg, ex);
            }            
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
