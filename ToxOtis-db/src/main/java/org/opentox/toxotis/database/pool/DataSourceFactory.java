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
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;
import org.opentox.toxotis.database.LoginInfo;
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
    /*
     * Maps connection URIs to datasources
     */
    protected ConcurrentHashMap<String, IDataSourceC3P0> datasources;

    private DataSourceFactory() {
        datasources = new ConcurrentHashMap<String, IDataSourceC3P0>();
    }

    private static class DatasourceFactoryHolder {

        private final static DataSourceFactory instance = new DataSourceFactory();
    }

    public static synchronized DataSourceFactory getInstance() {
        return DatasourceFactoryHolder.instance;
    }

    
    public synchronized DataSource getDataSource(String connectURI) throws DbException {
        if (connectURI == null) {
            throw new DbException("Connection URI not specified!");
        }
        IDataSourceC3P0 ds = getInstance().datasources.get(connectURI);
        if (ds == null) {
            ds = setupDataSource(connectURI);
            IDataSourceC3P0 oldds = getInstance().datasources.putIfAbsent(connectURI, ds);
            if (oldds != null) {
                ds = oldds;
            }
        }
        if (ds != null) {
            return ds.getDatasource();
        } else {
            return null;
        }

    }

    /**
     * Does nothing, the pool will be still active
     * @param connectURI
     * @throws AmbitException
     */
    public void logout(String connectURI) throws DbException {
    }



    
    public Connection getConnection(String connectURI) throws DbException {
        try {
            Connection connection = getDataSource(connectURI).getConnection();
            if (connection.isClosed()) {
                return getDataSource(connectURI).getConnection();
            } else {
                return connection;
            }
        } catch (SQLException x) {
            throw new DbException(x);
        }
    }

    public synchronized IDataSourceC3P0 setupDataSource(String connectURI) throws DbException {
        try {
            IDataSourceC3P0 dataSource = new DataSourceC3P0(connectURI);
            return dataSource;
        } catch (Exception x) {
            throw new DbException(x);
        }
    }

    public String getConnectionURI(LoginInfo li) {
        return getConnectionURI(li.getScheme(), li.getHostname(), li.getPort(), li.getDatabase(), li.getUser(), li.getPassword());
    }

    /**
     * Assembles connection URI
     * @param scheme
     * @param hostname
     * @param port
     * @param database
     * @param user
     * @param password
     * @return    scheme://{Hostname}:{Port}/{Database}?user={user}&password={password}
     */
    public String getConnectionURI(String scheme, String hostname, String port,
            String database, String user, String password) {

        StringBuilder b = new StringBuilder();
        b.append(scheme).append(colon).
                append(slash).append(slash);

        if (hostname == null) {
            b.append("localhost");
        } else {
            b.append(hostname);
        }
        if (port != null) {
            b.append(colon).append(port);
        }
        b.append(slash).
                append(database);
        String q = qmark;
        if (user != null) {
            b.append(q);
            q = amark;
            b.append("user").append(eqmark).append(user);
        }
        if (password != null) {
            b.append(q);
            q = amark;
            b.append("password").append(eqmark).append(password);
        }
        b.append(amark);
        b.append("useUnicode=true&characterEncoding=UTF8&characterSetResults=UTF-8");
        return b.toString();
    }

    /**
     *
     * @param scheme
     * @param hostname
     * @param port
     * @param database
     * @return    scheme://{Hostname}:{Port}/{Database}
     */
    public String getConnectionURI(String scheme, String hostname, String port,
            String database) {
        return getConnectionURI(scheme, hostname, port, database, null, null);
    }

    public String getConnectionURI(String hostname, String port,
            String database) {
        return getConnectionURI("jdbc:mysql", hostname, port, database, null, null);
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
    public boolean ping(LoginInfo li, int nTimes) {
        Connection connection = null;
        Statement st = null;
        try {
            String dburi = getConnectionURI(
                    li.getScheme(), li.getHostname(), li.getPort(),
                    li.getDatabase(), li.getUser(), li.getPassword());

            connection = getConnection(dburi.toString());
            st = connection.createStatement();

            int index = 0;

            ResultSet rs = null;
            while (index < nTimes) {
                int randomInt = pingRandom.nextInt();
                rs = st.executeQuery(String.format(pingQuery, randomInt));
                rs.first();
                int received = rs.getInt(1);
                if (randomInt != rs.getInt(1)) {
                    logger.error("DB Ping failed >> Sent: " + pingRandom + ", received :" + received);
                    return false;
                }
                rs.close();
                index++;
            }
            return true;
        } catch (final Exception x) {
            logger.error("Database server ping failed for URI "+getConnectionURI(li),x);
            return false;
        } finally {
            /*   Close SQL statement   */
            if (st != null) {
                try {
                    st.close();
                } catch (Exception x) {
                    logger.error("SQL statement for ping cannot close",x);
                }
            }
            /*  Close the DB connection */
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception x) {
                    logger.error("Database connection to "+getConnectionURI(li)+" cannot close",x);
                }
            }
        }
    }
}
