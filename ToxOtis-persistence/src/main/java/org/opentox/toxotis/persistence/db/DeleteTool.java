package org.opentox.toxotis.persistence.db;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Task;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DeleteTool {

    private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DeleteTool.class);

    public static int cancelTask(Session session, VRI taskUri) {
        try {
            String sql = "UPDATE Task SET status = 'CANCELLED' WHERE uri = ?";
            Query q = session.createSQLQuery(sql).setString(0, taskUri.toString());
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
        }
    }

    /**
     * Deletes a task of a given URI from the database (Note that this is different
     * from cancelling the task since it completely and permanently deletes the task
     * from the database).
     * @param session
     * @param taskUri
     */
    public static void deleteTask(Session session, VRI taskUri) {
        Task t = new Task(taskUri);
        t.setMeta(null);
        t.setCreatedBy(null);
        t.setResultUri(null);
        t.setStatus(null);
        try {
            session.beginTransaction();
            session.delete(t);
            session.getTransaction().commit();
        } catch (RuntimeException ex) {
            logger.warn("Deletion of task with URI " + taskUri
                    + " failed. Logging the corresponding exception for details", ex);
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
