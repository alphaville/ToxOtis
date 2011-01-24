package org.opentox.toxotis.persistence.db;

import java.io.NotSerializableException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Algorithm;
import org.opentox.toxotis.core.component.Feature;
import org.opentox.toxotis.core.component.Model;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.persistence.util.HibernateUtil;
import org.opentox.toxotis.util.LoggingConfiguration;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class RegisterToolTest {

    public RegisterToolTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {        
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSomeMethod() throws ToxOtisException, NotSerializableException {
        LoggingConfiguration.configureLog4jDefault();
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        final boolean doAlter = true;


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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        RegisterTool.storeAllOntClasses(session);
        
        Model model = new Model(Services.ntua().augment("model", "1"));
        model.addPredictedFeatures(
                new Feature(Services.ideaconsult().augment("feature", "X1")),
                new Feature(Services.ideaconsult().augment("feature", "X2")),
                new Feature(Services.ideaconsult().augment("feature", "X3")));
        model.addIndependentFeatures(
                new Feature(Services.ideaconsult().augment("feature", "100")),
                new Feature(Services.ideaconsult().augment("feature", "101")),
                new Feature(Services.ideaconsult().augment("feature", "102")));
        model.setAlgorithm(new Algorithm(Services.ntua().augment("algorithm", "xyz")));
        model.setDataset(Services.ideaconsult().augment("dataset", "10"));
        model.setActualModel(new HashSet());
        model.addOntologicalClasses(OTClasses.Algorithm());

        RegisterTool.storeModel(model, session);
        

    }
}
