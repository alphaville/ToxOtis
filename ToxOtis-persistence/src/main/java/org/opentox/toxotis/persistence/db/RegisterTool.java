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

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StaleObjectStateException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.core.component.*;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTAlgorithmTypes;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.impl.OntologicalClassImpl;
import org.opentox.toxotis.core.component.User;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.persistence.util.HibernateUtil;

/**
 * Class with static methods useful for storing objects in the database. Note that
 * none of the methods closes the session but all do clear it after use, so it's up
 * to you to invoke close() whenever you consider it is necessary.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class RegisterTool {

    private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RegisterTool.class);
    private final ThreadLocal local = new ThreadLocal();

    private static void preprocessComponent(OTComponent component) {
        component.getUri().clearToken();
    }

    public static void storeUser(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        session.setFlushMode(FlushMode.COMMIT);
        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(user);
            transaction.commit();

            session.clear();
        } catch (RuntimeException ex) {
            logger.warn("Storage of User failed. Logging the corresponding exception for details", ex);
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
            } catch (HibernateException ex) {
                logger.error("Cannot close session", ex);
                throw ex;
            }
        }
    }

    public static void storeAlgorithm(Algorithm algorithm, Session session) {
        try {
            session.beginTransaction();
            preprocessComponent(algorithm);
            for (Parameter p : algorithm.getParameters()) {
                session.saveOrUpdate(p);
                session.flush();
                session.evict(p);
            }
            session.saveOrUpdate(algorithm);
            session.flush();
            session.evict(algorithm);
            session.getTransaction().commit();
            session.clear();
        } catch (RuntimeException ex) {
            logger.warn("Storage of User failed. Logging the corresponding exception for details", ex);
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

    public static void storeFeature(Feature feature) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        session.setFlushMode(FlushMode.COMMIT);
        try {
            preprocessComponent(feature);
            transaction = session.beginTransaction();
            session.saveOrUpdate(feature);
            transaction.commit();
        } catch (RuntimeException ex) {
            logger.warn("Storage of Model failed. Logging the corresponding exception for details", ex);
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
            } catch (HibernateException ex) {
                logger.error("Cannot close session", ex);
                throw ex;
            }
        }
    }

    public static void storeModel(Model model) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        session.setFlushMode(FlushMode.COMMIT);
        try {
            preprocessComponent(model);
            transaction = session.beginTransaction();

            session.evict(model.getAlgorithm());

            if (model.getParameters() != null) {
                for (Parameter p : model.getParameters()) {
                    session.saveOrUpdate(p);
                    session.flush();
                    session.evict(p);
                }
            }

            if (model.getIndependentFeatures() != null) {
                for (Feature ft : model.getIndependentFeatures()) {
                    storeFeature(ft);
                    session.evict(ft);
                }
            }

            if (model.getCreatedBy() != null) {
                session.saveOrUpdate(model.getCreatedBy());
                session.flush();
                session.evict(model.getCreatedBy());
            }
            if (model.getDependentFeatures() != null) {
                for (Feature depFeature : model.getDependentFeatures()) {
                    session.saveOrUpdate(depFeature);
                    session.flush();
                    session.evict(depFeature);
                }
            }

            if (model.getPredictedFeatures() != null) {
                for (Feature predictedFeature : model.getPredictedFeatures()) {
                    session.saveOrUpdate(predictedFeature);
                    session.flush();
                    session.evict(predictedFeature);
                }
            }

            session.saveOrUpdate(model);
            transaction.commit();
            session.clear();
        } catch (StaleObjectStateException ex) {
            logger.error("Serious exception that cannot be recovered! Stale Object!!!!");
            try {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
            } catch (Throwable rte) {
                logger.error("Cannot rollback", rte);
            }
            throw ex;
        } catch (RuntimeException ex) {
            logger.warn("Storage of Model failed. Logging the corresponding exception for details", ex);
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
            } catch (HibernateException ex) {
                logger.error("Cannot close session", ex);
                throw ex;
            }
        }
    }

    public static void storeDataset(Dataset dataset, Session session) {
        try {
            preprocessComponent(dataset);
            session.beginTransaction();
            for (Feature f : dataset.getContainedFeatures()) {
                session.saveOrUpdate(f);
                session.flush();
                session.evict(f);
            }
            session.getTransaction().commit();
            session.clear();

            session.beginTransaction();
            Set<FeatureValue> ff = dataset.getFeatureValues();
            for (FeatureValue p : ff) {
                session.saveOrUpdate(p);
                session.flush();
                session.evict(p);
            }
            session.saveOrUpdate(dataset);
            session.getTransaction().commit();
            session.clear();
        } catch (StaleObjectStateException ex) {
            logger.error("Serious exception that cannot be recovered! Stale Object!!!!");
            try {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
            } catch (Throwable rte) {
                logger.error("Cannot rollback", rte);
            }
            throw ex;
        } catch (RuntimeException ex) {
            logger.warn("Storage of User failed. Logging the corresponding exception for details", ex);
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

    public static void storeErrorReport(ErrorReport er) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        session.setFlushMode(FlushMode.COMMIT);
        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(er);
            transaction.commit();
        } catch (RuntimeException ex) {
            logger.warn("Storage of Error Report failed. Logging the corresponding exception for details", ex);
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
            } catch (HibernateException ex) {
                logger.error("Cannot close session", ex);
                throw ex;
            }
        }
    }

    public static void storeMetaData(MetaInfo meta) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        session.setFlushMode(FlushMode.COMMIT);
        try {
            //TODO: Code for storing meta data!
        } catch (RuntimeException ex) {
            logger.warn("Storage of MetaInfo failed. Logging the corresponding exception for details", ex);
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
            } catch (HibernateException ex) {
                logger.error("Cannot close session", ex);
                throw ex;
            }
        }

    }

    public synchronized void storeTask(Task task, ThreadLocal<Session> local) {
        Session session = local.get();
        if (session == null) {
            session = HibernateUtil.getSessionFactory().openSession();
            session.setCacheMode(CacheMode.IGNORE);
        }
        local.set(session);
        Transaction transaction = null;
        session.setFlushMode(FlushMode.COMMIT);

        try {
            transaction = session.beginTransaction();
            User createdBy = task.getCreatedBy();
            if (createdBy != null) {
                session.saveOrUpdate(createdBy);
                session.evict(createdBy);
            }
            ErrorReport er = task.getErrorReport();
            if (er != null) {
                session.saveOrUpdate(er);
                session.evict(er);
            }
            session.saveOrUpdate(task);
            transaction.commit();

        } catch (RuntimeException ex) {
            logger.warn("Storage of Task failed. Logging the corresponding exception for details.", ex);
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
                local.set(null);
            } catch (HibernateException ex) {
                logger.error("Cannot close session", ex);
                throw ex;
            }
        }
    }

    public static void storeAllOntClasses(Session session) {
        try {
            session.beginTransaction();
            for (OntologicalClass oc : OTClasses.getAll()) {
                OntologicalClassImpl occ = new OntologicalClassImpl();
                occ.setMetaInfo(oc.getMetaInfo());
                occ.setName(oc.getName());
                occ.setNameSpace(oc.getNameSpace());
                session.saveOrUpdate(occ);
            }
            for (OntologicalClass oc : OTAlgorithmTypes.getAll()) {
                if (!oc.getName().equals("Thing")) {
                    OntologicalClassImpl occ = new OntologicalClassImpl();
                    occ.setMetaInfo(oc.getMetaInfo());
                    occ.setName(oc.getName());
                    occ.setNameSpace(oc.getNameSpace());
                    session.saveOrUpdate(occ);
                }
            }
            session.getTransaction().commit();
            session.clear();

            session.beginTransaction();
            for (OntologicalClass oc : OTClasses.getAll()) {
                session.saveOrUpdate(oc);
            }
            for (OntologicalClass oc : OTAlgorithmTypes.getAll()) {
                if (!oc.getName().equals("Thing")) {
                    session.saveOrUpdate(oc);
                }
            }
            session.getTransaction().commit();
            session.clear();
        } catch (RuntimeException ex) {
            logger.warn("Storage of User failed. Logging the corresponding exception for details", ex);
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

    public static void storeBibTeX(Session session, BibTeX bibtex) {
        try {
            preprocessComponent(bibtex);

            session.beginTransaction();
            User createdBy = bibtex.getCreatedBy();
            if (createdBy != null) {
                session.saveOrUpdate(createdBy);
            }
            session.saveOrUpdate(bibtex);
            session.getTransaction().commit();
            session.clear();
        } catch (RuntimeException ex) {
            logger.warn("Storage of User failed. Logging the corresponding exception for details", ex);
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
