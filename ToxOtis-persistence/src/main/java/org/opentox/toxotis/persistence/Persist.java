package org.opentox.toxotis.persistence;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Algorithm;
import org.opentox.toxotis.core.component.BibTeX;
import org.opentox.toxotis.core.component.Dataset;
import org.opentox.toxotis.core.component.Model;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.persistence.db.RegisterTool;
import org.opentox.toxotis.persistence.util.HibernateUtil;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Persist {

    public static void main(String[] args) throws Exception {

//        org.apache.log4j.LogManager.resetConfiguration();
//        org.apache.log4j.PropertyConfigurator.configure("config/log4j.properties");


        int x = 100;
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();        

        // Question: How can we know if the database is newly created?
        // (In order to know whether we have to execute the following lines...)
        final boolean doAlter = true;
        System.out.println(x);

        if (doAlter) {
            try {
                Connection c = session.connection();
                Statement stmt = c.createStatement();
                stmt.addBatch("ALTER TABLE FeatOntol DROP PRIMARY KEY");
                stmt.addBatch("ALTER TABLE FeatOntol ADD `ID_W` INT NOT NULL AUTO_INCREMENT PRIMARY KEY");
                stmt.addBatch("ALTER TABLE OTComponent ADD `created` TIMESTAMP NOT NULL DEFAULT NOW()");
                stmt.addBatch("ALTER TABLE User ADD `created` TIMESTAMP NOT NULL DEFAULT NOW()");
                stmt.executeBatch();
            } catch (HibernateException hbe) {
                hbe.printStackTrace();
            } catch (SQLException sqle) {
                System.err.println("Info: Alter failed (Probably not an error!)");
                Logger.getLogger(Persist.class).debug("Alter failed (Probably not an error!)", sqle);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*
         * OPEN SESSION
         */
        session = sf.openSession();

//        DeleteTool.deleteTask(session, Task.Status.RUNNING, Task.Status.QUEUED);

        System.out.println("Storing Ontological Classes");
        RegisterTool.storeAllOntClasses(session);
        System.out.println("Ontological Classes stored successfully!\n");
//
        System.out.println("Acquiring Token...");
        AuthenticationToken at = new AuthenticationToken(new File("/home/chung/toxotisKeys/my.key"));
        System.out.println("Done!");
        System.out.println("Authentication Token : \n" + at);
        System.out.println("User:\n" + at.getUser());

        System.out.println("Loading Algorithm");
        Algorithm algorithm = new Algorithm(Services.ntua().augment("algorithm", "svm")).loadFromRemote(at);
        System.out.println("Algorithm Loaded");
        System.out.println("Storing Algorithm");
        RegisterTool.storeAlgorithm(algorithm, session);
        System.out.println("Algorithm registered successfully!\n");
//
        System.out.println("Loading Dataset");
        Dataset d = new Dataset(Services.ideaconsult().augment("dataset", "9").addUrlParameter("max", "5")).loadFromRemote();
        System.out.println("Dataset Loaded");
        System.out.println("Storing Dataset");
        RegisterTool.storeDataset(d, session);
        System.out.println("Dataset registered successfully!\n");
//
        System.out.println("Loading Model");
        Model model = new Model(Services.ntua().augment("model", "934ef1d0-2080-48eb-9f65-f61b830b5783")).loadFromRemote();
        model.setActualModel(new java.net.URI("http://in.gr/#asdf"));
        System.out.println("Model Loaded");
        System.out.println("Storing Model");
        RegisterTool.storeModel(model, session);
        System.out.println("Model registered successfully!\n");

//
        System.out.println("Loading Tasks");
        Task task_complete = new Task(Services.ntua().augment("task", "68d471ad-0287-4f6e-a200-244d0234e8a1")).loadFromRemote(at);
        System.out.println("Task #1 Loaded");
        Task task_error = new Task(Services.ntua().augment("task", "0980cbb3-a4a8-4a89-8538-51992d2fc67f")).loadFromRemote(at);
        System.out.println("Task #2 Loaded");
        System.out.println("Storing Tasks");
        RegisterTool.storeTask(session, task_complete);
        System.out.println("Task #1 registered successfully!");
        RegisterTool.storeTask(session, task_error);
        System.out.println("Task #2 registered successfully!");
        System.out.println();

        BibTeX b = new BibTeX(Services.ntua().augment("bibtex", "1"));
        b.setAnnotation("asdf");
        b.setAuthor("gdfgfdg");
        RegisterTool.storeBibTeX(session, b);
        /*
         * For more info about criteria read:
         * http://docs.jboss.org/hibernate/core/3.3/reference/en/html/querycriteria.html
         */
        System.out.println(OTClasses.Algorithm());
        List resultsFoundInDB = session.createCriteria(Algorithm.class).list();
//                add(Restrictions.like("uri", "%svm")).list();
        System.out.println("found " + resultsFoundInDB.size());
        for (Object o : resultsFoundInDB) {
            Algorithm a = (Algorithm) o;
            VRI c = a.getUri();
            System.out.println(c);
        }
        session.close();

    }
}
//        Όταν μεγαλώσω θέλω,
//        θέλω να γίνω 82 χρονών
//        τσατσά σ'ένα μπουρδέλο
//        χωρίς δόντια να μασάω τα κρουτόν
//        και να διαβάζω Οθέλο
//
//        Όταν μεγαλώσω θέλω
//        θελώ να γίνω διαστημικός σταθμός
//        και να παίζω μπουγέλο
//        κι από μένανε να βρέχει κι ο ουρανός
//        τα ρούχα να σας πλένω
//
//        Η ομορφιά του θέλω,
//        Μάρω Μαρκέλου
//

