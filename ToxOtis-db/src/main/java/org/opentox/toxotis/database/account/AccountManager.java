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
package org.opentox.toxotis.database.account;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.database.engine.model.CountModel;
import org.opentox.toxotis.database.engine.task.CountTasks;
import org.opentox.toxotis.database.engine.user.FindUser;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.database.pool.DataSourceFactory;
import org.opentox.toxotis.factory.DatasetFactory;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class AccountManager {

    private User user;
    private ThreadLocal<Connection> LOCAL = new ThreadLocal<Connection>();
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AccountManager.class);

    private Connection getConnection() throws DbException {
        Connection connection = LOCAL.get();// take the connection in the LOCAL
        if (connection == null) {// If LOCAL has no connections, create a new one
            DataSourceFactory factory = DataSourceFactory.getInstance();
            try {
                connection = factory.getConnection();
            } catch (DbException ex) {
                final String msg = "Cannot get connection from the connection pool";
                logger.warn(msg, ex);
                throw new DbException(msg, ex);
            }
            LOCAL.set(connection); // Update the connection in the LOCAL
        }
        return connection;
    }

    private void closeConnection() throws DbException {
        Connection connection = LOCAL.get();
        if (connection != null) {// if any connection in the LOCAL, close it!
            try {
                connection.close();
            } catch (SQLException ex) {
                String message = "Cannot close connection";
                logger.warn(message, ex);
                throw new DbException(message, ex);
            }
        }
        LOCAL.set(null);
    }

    private AccountManager() {
        LOCAL.set(null);
    }

    public AccountManager(User user) {
        this();
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public boolean userExists() throws DbException {
        String uid = user.getUid();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(uid) FROM User WHERE uid='");
        sql.append(uid);
        sql.append("'");
        ResultSet rs = null;
        Statement statement = null;

        try {
            statement = getConnection().createStatement();
            rs = statement.executeQuery(sql.toString());
            rs.first();
            int count = rs.getInt(1);
            if (count > 0) {
                return true;
            }
        } catch (SQLException ex) {
            String msg = "SQLException caught while counting models in the database";
            logger.debug(msg, ex);
            throw new DbException(msg, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    logger.error("Exception while closing result set :(", ex);
                }
                try {
                    statement.close();
                } catch (SQLException ex) {
                    logger.error("statement is uncloseable :(", ex);
                }
                try {
                    closeConnection();
                } catch (DbException ex) {
                    logger.error("connection cannot uncloseable - Please run in circles (around "
                            + "yourself) while screaming, pulling your hair, and accusing other people "
                            + "for the situation!", ex);
                }
            }
        }
        return false;
    }

    public int countModels() throws DbException {
        CountModel counter = new CountModel();
        counter.setWhere("createdBy='" + user.getUid() + "'");
        int count = -1;
        try {
            count = counter.count();
        } catch (DbException ex) {
            logger.error("Database exception while counting models for the user "+user.getUid());
            throw ex;
        } finally {
            counter.close();
        }
        return count;
    }

    public int countActiveTasks() throws DbException {
        CountTasks taskCounter = new CountTasks();
        taskCounter.setWhere("Task.`createdBy`='" + user.getUid() + "' AND Task.`status` IN ('QUEUED','RUNNING')");
        int count = -1;
        try {
            count = taskCounter.count();
        } catch (DbException ex) {
            logger.error("Database exception while counting models for the user "+user.getUid());
            throw ex;
        } finally {
            taskCounter.close();
        }
        return count;
    }

    public int countBibTeX() {
        throw new UnsupportedOperationException();
    }
}
