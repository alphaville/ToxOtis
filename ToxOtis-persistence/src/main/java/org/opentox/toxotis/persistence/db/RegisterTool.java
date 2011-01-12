package org.opentox.toxotis.persistence.db;

import java.util.Set;
import org.hibernate.Session;
import org.hibernate.StaleObjectStateException;
import org.opentox.toxotis.core.OTComponent;
import org.opentox.toxotis.core.component.*;
import org.opentox.toxotis.ontology.OntologicalClass;
import org.opentox.toxotis.ontology.collection.OTAlgorithmTypes;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.ontology.impl.OntologicalClassImpl;
import org.opentox.toxotis.core.component.User;

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

    private static void preprocessComponent(OTComponent component) {
        component.getUri().clearToken();
    }

    public static void storeUser(User user, Session session) {
        try {
            session.beginTransaction();
            session.saveOrUpdate(user);
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

    public static void storeModel(Model model, Session session) {
        try {
            preprocessComponent(model);
            session.beginTransaction();
            if (model.getAlgorithm() != null) {
                session.saveOrUpdate(model.getAlgorithm());
            }
            session.flush();
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
                    session.saveOrUpdate(ft);
                    session.flush();
                    session.evict(ft);
                }
            }
            if (model.getCreatedBy() != null) {
                session.saveOrUpdate(model.getCreatedBy());
                session.flush();
                session.evict(model.getCreatedBy());
            }
            if (model.getDependentFeature() != null) {
                session.saveOrUpdate(model.getDependentFeature());
                session.flush();
                session.evict(model.getDependentFeature());
            }

            if (model.getPredictedFeature() != null) {
                session.saveOrUpdate(model.getPredictedFeature());
                session.flush();
                session.evict(model.getPredictedFeature());
            }

            session.saveOrUpdate(model);
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

    public static void storeTask(Session session, Task task) {
        try {
            preprocessComponent(task);
            session.beginTransaction();
            User createdBy = task.getCreatedBy();
            if (createdBy != null) {
                session.saveOrUpdate(createdBy);
            }
            ErrorReport er = task.getErrorReport();
            if (er != null) {
                session.saveOrUpdate(er);
                session.flush();
                session.evict(er);
            }
            session.saveOrUpdate(task);
            session.getTransaction().commit();
            session.clear();
        } catch (RuntimeException ex) {
            logger.warn("Storage of Task failed. Logging the corresponding exception for details", ex);
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
