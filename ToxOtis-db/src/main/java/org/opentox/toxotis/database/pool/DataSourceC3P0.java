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
package org.opentox.toxotis.database.pool;

import javax.sql.DataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.util.Properties;
import java.util.UUID;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.database.global.DbConfiguration;

class DataSourceC3P0 implements IDataSourceC3P0 {

    protected volatile ComboPooledDataSource datasource;
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DataSourceC3P0.class);
    private static final String ticket = UUID.randomUUID().toString();

    /**
     * Default configuration
     * @throws DbException
     */
    public DataSourceC3P0() throws DbException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (final ClassNotFoundException ex) {
            final String msg = "Driver com.mysql.jdbc.Driver not found";
            logger.error(msg, ex);
            throw new DbException(msg, ex);
        }
        datasource = new ComboPooledDataSource();  // create a new datasource object
        /*
         * Setup the JDBC URL manually to make sure that things 
         * work as expected. Also some other properties are not configured 
         * correctly when using just datasource.setProperties(stdProperties);
         */
        Properties stdProperties = DbConfiguration.getInstance().getProperpties();
        Properties prescribedProps = stdProperties != null ? stdProperties : new Properties();
        datasource.setJdbcUrl(prescribedProps.getProperty("c3p0.jdbcUrl"));
        datasource.setMaxPoolSize(Integer.valueOf(prescribedProps.getProperty("c3p0.maxPoolSize", "1000")));
        datasource.setMinPoolSize(Integer.valueOf(prescribedProps.getProperty("c3p0.minPoolSize", "50")));
        datasource.setInitialPoolSize(Integer.valueOf(prescribedProps.getProperty("c3p0.initialPoolSize", "100")));
        datasource.setAcquireIncrement(Integer.valueOf(prescribedProps.getProperty("c3p0.acquireIncrement", "3")));
        datasource.setAcquireRetryAttempts(Integer.valueOf(prescribedProps.getProperty("c3p0.acquireRetryAttempts", "50")));
        datasource.setAcquireRetryDelay(Integer.valueOf(prescribedProps.getProperty("c3p0.acquireRetryDelay", "1000")));
        datasource.setNumHelperThreads(Integer.valueOf(prescribedProps.getProperty("c3p0.numHelperThreads", "110")));
        datasource.setTestConnectionOnCheckin(Boolean.valueOf(prescribedProps.getProperty("c3p0.testConnectionOnCheckin", "true")));
        datasource.setTestConnectionOnCheckout(Boolean.valueOf(prescribedProps.getProperty("c3p0.testConnectionOnCheckout", "true")));
        datasource.setUser(prescribedProps.getProperty("c3p0.user"));
        datasource.setPassword(prescribedProps.getProperty("c3p0.password"));               
        datasource.setProperties(stdProperties);
        logger.info("Acquired datasource [".concat(ticket).concat("]".concat(" with properties... ".concat(datasource.getProperties().toString()))));

    }

    public DataSourceC3P0(String connectURI) throws DbException {
        this();
        datasource.setJdbcUrl(connectURI);
    }

    @Override
    public String getTicket() {
        return ticket;
    }

    @Override
    public void close() throws DbException {
        if (datasource != null) {
            try {
                datasource.close();
            } catch (final Exception ex) {
                final String msg = "Unexpected exception while closing datasource";
                logger.warn(msg, ex);
                throw new DbException(msg, ex);
            } catch (final Error ex) {
                final String msg = "Unexpected error while closing datasource";
                logger.warn(msg, ex);
                throw new DbException(msg, ex);
            }
        }

    }

    @Override
    public DataSource getDatasource() {
        return datasource;
    }
}
