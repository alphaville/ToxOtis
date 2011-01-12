package org.opentox.toxotis.persistence.util;

import java.io.File;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure(new File("src/main/resources/hibernate.cfg.xml")).buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            for (StackTraceElement ste : ex.getStackTrace()){
                System.out.println(ste);
            }
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Returns a session factory used to
     * @return
     *      Session Factory that connects to the database specified in the configuration
     *      file <code>hibernate.cfg.xml</code> found in package
     *      <code>org.opentox.toxotis.persistence.config</code>.
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
