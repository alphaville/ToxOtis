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
package org.opentox.toxotis.persistence.db;

import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.persistence.util.HibernateUtil;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DeleteTool {

    private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DeleteTool.class);

    public static int cancelTask(VRI taskUri, ThreadLocal<Session> local) {
        Session session = local.get();
        if (session == null) {
            session = HibernateUtil.getSessionFactory().openSession();
            session.setCacheMode(CacheMode.IGNORE);
        }
        local.set(session);
        session.setFlushMode(FlushMode.COMMIT);
        try {
            String sql = "UPDATE Task SET status = 'CANCELLED' WHERE uri = ?";
            Query q = session.createSQLQuery(sql).setString(0, taskUri.toString());
            session.flush();
            return q.executeUpdate();
        } catch (HibernateException ex) {
            logger.warn("Cancellation of task with URI " + taskUri
                    + " failed. Logging the corresponding exception for details", ex);
            try {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
            } catch (Throwable rte) {
                logger.error("Cannot rollback", rte);
            }
            throw ex;
        } finally {
            try {
                session.close();
                local.set(null);
            } catch (HibernateException ex) {
                logger.error("Cannot close session", ex);
                throw ex;
            }
        }
    }

    /**
     * Deletes a task of a given URI from the database (Note that this is different
     * from cancelling the task since it completely and permanently deletes the task
     * from the database).
     * @param session
     * @param taskUri
     */
    public static int deleteTask(VRI taskUri) {

        Session session = null;        
            session = HibernateUtil.getSessionFactory().openSession();
            session.setCacheMode(CacheMode.IGNORE);
        session.setFlushMode(FlushMode.COMMIT);
        Transaction transaction = null;
        Task t = new Task(taskUri);
        t.setMeta(null);
        t.setCreatedBy(null);
        t.setResultUri(null);
        t.setStatus(null);
        t.setDuration(null);
        t.setErrorReport(null);
        t.setOntologicalClasses(null);
        try {
            transaction = session.beginTransaction();
            String sql = "DELETE FROM Task WHERE uri = ?";
            Query q = session.createSQLQuery(sql).setString(0, taskUri.toString());
            return q.executeUpdate();
        } catch (RuntimeException ex) {
            logger.warn("Deletion of task with URI " + taskUri
                    + " failed. Logging the corresponding exception for details", ex);
            try {
                if (transaction != null) {
                    transaction.rollback();
                }
            } catch (Throwable rte) {
                logger.error("Cannot rollback", rte);
            }
            throw ex;
        } finally {
            try {
                session.close();
                //local.set(null);
            } catch (HibernateException ex) {
                logger.error("Cannot close session", ex);
                throw ex;
            }
        }
    }

    public static void deleteTask(Session session, Task.Status... status) {
        try {
            session.beginTransaction();
            String hql = "DELETE from Task WHERE status = :status";
            for (Task.Status s : status) {
                session.createQuery(hql).setString("status", s.toString()).executeUpdate();
            }
            session.getTransaction().commit();
            session.clear();
        } catch (RuntimeException ex) {
            logger.warn("Deletion of tasks failed. Logging the corresponding exception for details", ex);
            try {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
            } catch (Throwable rte) {
                logger.error("Cannot rollback", rte);
            }
            throw ex;
        }


    }
}
