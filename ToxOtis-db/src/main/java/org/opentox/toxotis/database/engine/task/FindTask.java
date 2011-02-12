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
package org.opentox.toxotis.database.engine.task;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.DbReader;
import org.opentox.toxotis.database.exception.DbException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class FindTask extends DbReader<Task> {

    private final VRI baseUri;
    private final boolean resolveErrorReport;
    private final boolean resolveUser;

    public FindTask(VRI baseUri, boolean resolveErrorReport, boolean resolveUser) {
        super();
        this.baseUri = baseUri;
        this.resolveErrorReport = resolveErrorReport;
        this.resolveUser = resolveUser;
    }

    @Override
    public IDbIterator<Task> list() throws DbException {
        setTable("Task");
        setTableColumns("Task.id", "Task.resultUri", "Task.httpStatus", "Task.percentageCompleted",
                "Task.status", "Task.duration", "Task.errorReport", "Task.createdBy", "OTComponent.enabled", "uncompress(meta)");
        setInnerJoin("OTComponent ON Task.id=OTComponent.id");
        Statement statement = null;
        Connection connection = null;
        connection = getConnection();
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(getSql());
            TaskIterator it = new TaskIterator(rs, baseUri);
            it.setResolveUser(resolveUser);
            it.setResolveErrorReport(resolveErrorReport);
            return it;
        } catch (SQLException ex) {
            Logger.getLogger(FindTask.class.getName()).log(Level.SEVERE, null, ex);
            throw new DbException(ex);
        } finally {
            // Do Nothing:  The client is expected to close the statement and the connection
        }


    }
}
