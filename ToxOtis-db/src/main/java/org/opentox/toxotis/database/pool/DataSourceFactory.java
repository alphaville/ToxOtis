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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import javax.sql.DataSource;
import org.opentox.toxotis.database.exception.DbException;

public class DataSourceFactory {

    protected static final String slash = "/";
    protected static final String qmark = "?";
    protected static final String colon = ":";
    protected static final String eqmark = "=";
    protected static final String amark = "&";
    private final Random pingRandom = new Random();
    private final String pingQuery = "SELECT %s";
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DataSourceFactory.class);
    private static final long serialVersionUID = -5768L;
    protected volatile IDataSourceC3P0 datasource;

    private DataSourceFactory() {
    }

    private static class DatasourceFactoryHolder {

        private final static DataSourceFactory instance = new DataSourceFactory();
    }

    public static synchronized DataSourceFactory getInstance() {
        return DatasourceFactoryHolder.instance;
    }

    public synchronized IDataSourceC3P0 getDataSourceC3P0() throws DbException {
        IDataSourceC3P0 dataSource = getInstance().datasource;
        return dataSource;
    }

    public synchronized DataSource getDataSource() throws DbException {
        IDataSourceC3P0 ds = getInstance().datasource;
        if (ds == null) {
            ds = setupDataSource();
            this.datasource = ds;
        }
        if (ds != null) {
            return ds.getDatasource();
        } else {
            return null;
        }
    }

    public void close() throws DbException {
        System.out.println("[Shuting Down the Connection Pool]");
        if (datasource != null) {
            try {
                /*
                 * Should otherwise wait for com.mchange.v2.resourcepool.BasicResourcePool$AcquireTask
                 * which has package-access! Waiting for 5 seconds before closing the pool...
                 */
                logger.trace("closing datasource [".concat(datasource.getTicket()).concat("]"));
                synchronized (this) {// Absolutely necessary to be synchronized!
                    Thread.sleep(1000);
                    datasource.close();
                    datasource = null;
                }
            } catch (final Exception ex) {
                final String msg = "DataSource cannot close";
                logger.error(msg, ex);
                throw new DbException(msg, ex);
            }
        }
    }

    /**
     * Does nothing, the pool will be still active
     * @param connectURI
     * @throws AmbitException
     */
    public void logout(String connectURI) throws DbException {
    }

    public Connection getConnection() throws DbException {
        try {
            Connection connection = getDataSource().getConnection();
            if (connection.isClosed()) {
                return getDataSource().getConnection();
            } else {
                return connection;
            }
        } catch (final SQLException sqlEx) {
            final String msg = "Connection could not be retrieved from the pool";
            logger.warn(msg, sqlEx);
            throw new DbException(msg, sqlEx);
        }
    }

    public synchronized IDataSourceC3P0 setupDataSource() throws DbException {
        try {
            IDataSourceC3P0 dataSource = new DataSourceC3P0();
            return dataSource;
        } catch (final Exception x) {
            final String msg = "Connection could not be retrieved from the pool";
            logger.warn(msg, x);
            throw new DbException(x);
        }
    }

    /**
     * Pings the database server
     * @param li
     *      Login information
     * @param nTimes
     *      How many times the ping should be repeated
     * @return
     *      <code>true</code> if the ping was successful!
     */
    public boolean ping(int nTimes) {
        Connection connection = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            st = connection.createStatement();

            int index = 0;
            while (index < nTimes) {
                int randomInt = pingRandom.nextInt();
                rs = st.executeQuery(String.format(pingQuery, randomInt));
                if (rs.next()) {
                    int received = rs.getInt(1);

                    if (randomInt != rs.getInt(1)) {
                        logger.error("DB Ping failed >> Sent: " + pingRandom + ", received :" + received);
                        return false;
                    }
                } else {
                    logger.error("DB Ping failed >> Sent: " + pingRandom + ", received : NOTHING");
                    return false;
                }
                rs.close();
                index++;
            }
            return true;
        } catch (final Exception x) {
            logger.error("Database server ping failed", x);
            return false;
        } finally {
            /* Close result set */
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    logger.warn("result set could not be closed", ex);
                }
            }
            /*   Close SQL statement   */
            if (st != null) {
                try {
                    st.close();
                } catch (Exception x) {
                    logger.warn("SQL statement for ping cannot close", x);
                }
            }
            /*  Close the DB connection */
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception x) {
                    logger.warn("Database connection cannot close", x);
                }
            }
        }
    }

}
