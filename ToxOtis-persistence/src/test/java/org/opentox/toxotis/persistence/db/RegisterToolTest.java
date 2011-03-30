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

import java.io.NotSerializableException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Algorithm;
import org.opentox.toxotis.core.component.Model;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.persistence.util.HibernateUtil;
import org.opentox.toxotis.util.LoggingConfiguration;
import static org.junit.Assert.*;

/**
 *
 * @author Pantelis Sopasakis
 */
public class RegisterToolTest {

    private static final ThreadLocal<Session> LOCAL = new ThreadLocal<Session>();
    private static Throwable failure = null;

    public RegisterToolTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        LoggingConfiguration.configureLog4j(RegisterTool.class.getClassLoader().getResource("log4j.properties"));
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    protected IDatabaseConnection getConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection jdbcConnection = DriverManager.getConnection(
                "jdbc:mysql://localhost/toxotis?useUnicode=true&characterEncoding=UTF8&characterSetResults=UTF-8", "root", "opensess@me");
        return new DatabaseConnection(jdbcConnection);
    }

    @After
    public void tearDown() {
    }

    /**
     * Tasks are created and registered in the database. Then they are looked up
     * using DbUnit (just for testing) and Hibernate queries.
     * @throws Exception
     *      Test fails!
     *  @see {@link #doTaskWriteRead() }
     */
    @Test
    public void testTaskWriteRead() throws Exception {
        if (true) return;
        System.out.println("Single-threaded write/read in the database");
        doTaskWriteRead();
    }

    /**
     * The test {@link #testTaskWriteRead() } is run multithreadedly to check whether
     * the implementation is thread-safe.
     * @throws Exception
     * @see {@link #doTaskWriteRead() }
     */
    //@Test
    public void multiThreadedTaskWriteRead() throws Exception {
        System.out.println("Multi-threaded write/read in the database");
        int poolSize = 40;
        int folds = 4 * poolSize + 10;// just to make sure!!!
        final ExecutorService es = Executors.newFixedThreadPool(poolSize);
        for (int i = 1; i <= folds; i++) {
            es.submit(new Runnable() {

                @Override
                public void run() {
                    try {
                        new RegisterToolTest().doTaskWriteRead();
                    } catch (final Throwable ex) {
                        failure = ex;
                        ex.printStackTrace();
                    }
                }
            });
        }

        es.shutdown();
        while (!es.isTerminated()) {
            Thread.sleep(100);
        }

        if (failure != null) {
            fail();
        }
    }

    public void doTaskWriteRead() {
        Session session = null;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        String sql = "SELECT percentageCompleted FROM Task WHERE uri=\"%s\"";
        IDatabaseConnection c = null;
        RegisterTool registerer = new RegisterTool();
        try {
            c = getConnection();

            String uid = UUID.randomUUID().toString();
            Task task = null;
            try {
                task = new Task(new VRI("http://alphaville:4000/task/" + uid));
            } catch (URISyntaxException ex) {
                fail("Invalid URI exception");
            }
            task.setStatus(Task.Status.QUEUED);
            task.setPercentageCompleted(0.0f);
            task.setHttpStatus(202);
            registerer.storeTask(task);

            ITable table = null;
            try {
                table = c.createQueryTable("TASK", String.format(sql, task.getUri()));
            } catch (DataSetException ex) {
                fail();
            } catch (SQLException ex) {
                fail();
            }
            assertEquals(1, table.getRowCount());
            try {
                assertEquals(new Float(0f), table.getValue(0, "percentageCompleted"));
            } catch (DataSetException ex) {
                fail("Table TASK cannot be accessed");
            }


            task.setStatus(Task.Status.RUNNING);
            for (float i = 1; i <= 99; i++) {
                task.setPercentageCompleted(i);
                registerer.storeTask(task);
                try {
                    table = c.createQueryTable("TASK", String.format(sql, task.getUri()));
                    assertEquals(1, table.getRowCount());
                    assertEquals(new Float(i), table.getValue(0, "percentageCompleted"));
                } catch (DataSetException ex) {
                    fail();
                } catch (SQLException ex) {
                    fail();
                }

                session = HibernateUtil.getSessionFactory().openSession();
                Query q = session.createQuery("FROM Task where uri = :uri");
                q.setParameter("uri", task.getUri());
                Task foundTask = (Task) q.uniqueResult();
                assertEquals(i, foundTask.getPercentageCompleted());
                session.close();
            }

            task.setStatus(Task.Status.COMPLETED);
            task.setPercentageCompleted(100);
            String resultUri = "http://someserver.com/dataset/1432";
            try {
                task.setResultUri(new VRI(resultUri));
            } catch (URISyntaxException ex) {
                fail("Invalid URI :" + resultUri);
            }

            registerer.storeTask(task);
            try {
                table = c.createQueryTable("TASK", String.format(sql, task.getUri()));
                assertEquals(1, table.getRowCount());
                assertEquals(new Float(100), table.getValue(0, "percentageCompleted"));
            } catch (DataSetException ex) {
                fail();
            } catch (SQLException ex) {
                fail();
            }


            session = sessionFactory.openSession();
            Task foundTask = (Task) session.createCriteria(Task.class).
                    add(Restrictions.eq("uri", task.getUri())).uniqueResult();
            assertNotNull(foundTask);
            assertEquals(foundTask.getUri(), task.getUri());
            assertEquals(foundTask.getPercentageCompleted(), 100.0f);
            session.close();

        } catch (Exception ex) {
            fail("Connection to the database cannot be established");
        } finally {
            try {
                c.close();
            } catch (SQLException ex) {
                fail("DbUnit Connection cannot close");
            }
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

    }

    //@Test
    public void testStoreModel() throws Exception {
        System.out.println("Store Model in DB");
        doStoreModel();
    }

    //@Test
    public void multiThreadedModelWriteRead() throws Exception {
        System.out.println("Multi-threaded write/read of MODELS in the database");
        int poolSize = 50;
        int folds = 4 * poolSize + 10;// just to make sure!!!
        final ExecutorService es = Executors.newFixedThreadPool(poolSize);
        for (int i = 1; i <= folds; i++) {
            es.submit(new Runnable() {

                @Override
                public void run() {
                    try {
                        System.out.println("submitted");
                        new RegisterToolTest().doStoreModel();
                        System.out.println("done");
                    } catch (final Throwable ex) {
                        failure = ex;
                        ex.printStackTrace();
                    }
                }
            });
        }

        es.shutdown();
        while (!es.isTerminated()) {
            Thread.sleep(100);
        }

        if (failure != null) {
            fail();
        }
    }

    private void doStoreModel() throws Exception {
        Session session = null;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        String sql = "SELECT dataset, algorithm FROM Model WHERE uri=\"%s\"";
        IDatabaseConnection c = null;
        VRI trainingDsUri = new VRI("http://someservice.com:8081/dataset/243");
        VRI trainingAlgorithmUri = new VRI("http://alphaville:4000/algorithm/mlr");
        VRI modelUri = new VRI("http://alphaville:7001/model" + UUID.randomUUID());
        try {
            RegisterTool registerer = new RegisterTool(LOCAL);
            c = getConnection();

            Model model = new Model(modelUri);
            model.setAlgorithm(new Algorithm(trainingAlgorithmUri));
            model.setDataset(trainingDsUri);
            try {
                model.setActualModel(new weka.classifiers.functions.SMOreg());
            } catch (final NotSerializableException ex) {
                ex.printStackTrace();
                fail(ex.getMessage());
            }
            registerer.storeModel(model);

            /* Check using hibernate */
            session = sessionFactory.openSession();
            Model foundModel = (Model) session.createCriteria(Model.class).
                    add(Restrictions.eq("uri", model.getUri())).uniqueResult();
            assertNotNull(foundModel);
            assertNotNull(foundModel.getDataset());
            assertNotNull(foundModel.getAlgorithm());
            assertNotNull(foundModel.getActualModel());
            assertEquals(trainingDsUri.toString(), foundModel.getDataset().toString());
            assertEquals(trainingAlgorithmUri.toString(), foundModel.getAlgorithm().getUri().toString());
            assertNotNull(foundModel.getDependentFeatures());
            assertTrue(foundModel.getDependentFeatures().isEmpty());

            /* Check again what's in the DB using DBUnit tests */
            ITable table = null;
            try {
                table = c.createQueryTable("TASK", String.format(sql, model.getUri()));
            } catch (final DataSetException ex) {
                ex.printStackTrace();
                fail();
            } catch (final SQLException ex) {
                ex.printStackTrace();
                fail(ex.getMessage());
            }
            assertNotNull(table);
            assertEquals(1, table.getRowCount());
            assertEquals(trainingDsUri.toString(), table.getValue(0, "dataset"));
            assertEquals(trainingAlgorithmUri.toString(), table.getValue(0, "algorithm"));

            /* The test succeeds! */
        } catch (final Exception ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            try {
                c.close();
            } catch (final SQLException ex) {
                fail("DbUnit Connection cannot close");
            }
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
