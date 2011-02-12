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

import java.net.URISyntaxException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.ErrorReport;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.database.DbIterator;
import org.opentox.toxotis.database.IDbIterator;
import org.opentox.toxotis.database.engine.cache.Cache;
import org.opentox.toxotis.database.engine.cache.ICache;
import org.opentox.toxotis.database.exception.DbException;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.MetaInfoDeblobber;

/**
 *
 * @author Pantelis Sopasakis
 */
public class TaskIterator extends DbIterator<Task> {
    
    private final VRI baseVri;
    private boolean resolveErrorReport = false;
    private boolean resolveUser = false;
    private ICache<String, User> usersCache = new Cache<String, User>();
    private ICache<String, ErrorReport> errorReportCache = new Cache<String, ErrorReport>();


    public TaskIterator(final ResultSet rs, final VRI baseUri) {
        super(rs);
        this.baseVri = baseUri;
    }

    public boolean isResolveErrorReport() {
        return resolveErrorReport;
    }

    public void setResolveErrorReport(boolean resolveErrorReport) {
        this.resolveErrorReport = resolveErrorReport;
    }

    public boolean isResolveUser() {
        return resolveUser;
    }

    public void setResolveUser(boolean resolveUser) {
        this.resolveUser = resolveUser;
    }

        
    @Override
    public Task next() throws DbException {
        // "id", "resultUri", "httpStatus", "percentageCompleted", "status", "duration", "errorReport", "createdBy"
        // "OTComponent.enabled", "OTComponent.meta"
        Task t = new Task();
        try {
            t.setUri(new VRI(baseVri).augment("task", rs.getString("Task.id")));

            String resultUriString = rs.getString("Task.resultUri");
            if (resultUriString != null) {
                try {
                    VRI resultVri = new VRI(resultUriString);
                    t.setResultUri(resultVri);
                } catch (URISyntaxException ex) {
                    throw new DbException("Illegal entry was found in the database for the field `Task.resultUri`. "
                            + "Value : " + resultUriString + "is not a valid URI", ex);
                }
            } else {
                t.setResultUri(null);
            }

            t.setHttpStatus(rs.getFloat("httpStatus"));
            t.setPercentageCompleted(rs.getFloat("percentageCompleted"));
            t.setStatus(Task.Status.valueOf(rs.getString("status")));
            t.setDuration(rs.getLong("duration"));

            String errorReportString = rs.getString("errorReport");
            if (errorReportString != null) {
                ErrorReport er = new ErrorReport();
                if (resolveErrorReport) {
                    //TODO: More on Error Report!
                    //TODO: Put error reports in a CACHE!
                }
                t.setErrorReport(er);
            }


            String taskCreator = rs.getString("createdBy");

            if (taskCreator != null) {
                User user = usersCache.get(taskCreator);//try to get the user from the cache.
                if (user == null) {// if user is not found in cache, create it and put it there!
                    user = new User();
                    user.setUid(taskCreator);
                    if (resolveUser) {
                        //TODO: resolve users!
                        //TODO: Put users in a CACHE!
                    }
                    usersCache.put(taskCreator, user);
                }                
                t.setCreatedBy(user);
            }

            boolean isTaskEnabled = rs.getBoolean("OTComponent.enabled");
            t.setEnabled(isTaskEnabled);

            Blob metablob = rs.getBlob(10);
            if (metablob != null) {
                MetaInfoDeblobber deblodder = new MetaInfoDeblobber(metablob);
                MetaInfo mi = deblodder.toMetaInfo();
                t.setMeta(mi);
                metablob.free();
            }



        } catch (SQLException ex) {
            throw new DbException(ex);
        }
        return t;
    }

}
