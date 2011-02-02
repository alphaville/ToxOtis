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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.ITable;
import org.hibernate.CacheMode;
import org.hibernate.HibernateException;
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
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.persistence.util.HibernateUtil;
import org.opentox.toxotis.util.LoggingConfiguration;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class RegisterToolTest {

    private static Throwable failure = null;
    private static final ThreadLocal<Session> local = new ThreadLocal<Session>();

    private static Session getCurrentSession() {
        Session s = local.get();
        if (s == null) {
            s = HibernateUtil.getSessionFactory().openSession();
            s.setCacheMode(CacheMode.IGNORE);
        }
        local.set(s);
        return s;
    }

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

    public static void closeSession() throws HibernateException {
        Session s = (Session) local.get();
        if (s != null) {
            s.close();
        }
        local.set(null);
    }

    @Test
    public void testTaskWriteRead() throws Exception {
        System.out.println("Single-threaded write/read in the database");
        doTaskWriteRead();
    }

    @Test
    public void multiThreadedTest() throws Exception {
        System.out.println("Multi-threaded write/read in the database");
        int poolSize = 50;
        int folds = poolSize+10;
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

    public void doTaskWriteRead() throws Exception {
        String sql = "SELECT percentageCompleted FROM Task WHERE uri=\"%s\"";
        IDatabaseConnection c = getConnection();
        String uid = UUID.randomUUID().toString();
        Task task = new Task(new VRI("http://alphaville:4000/task/" + uid));
        task.setStatus(Task.Status.QUEUED);
        task.setPercentageCompleted(0.0f);
        task.setHttpStatus(202);
        new RegisterTool().storeTask(task, local);

        ITable table = c.createQueryTable("TASK", String.format(sql, task.getUri()));
        assertEquals(1, table.getRowCount());
        assertEquals(new Float(0f), table.getValue(0, "percentageCompleted"));

        task.setStatus(Task.Status.RUNNING);
        for (float i = 1; i <= 99; i++) {
            task.setPercentageCompleted(i);
            new RegisterTool().storeTask(task, local);

            table = c.createQueryTable("TASK", String.format(sql, task.getUri()));
            assertEquals(1, table.getRowCount());
            assertEquals(new Float(i), table.getValue(0, "percentageCompleted"));


            Query q = getCurrentSession().createQuery("FROM Task where uri = :uri");
            q.setParameter("uri", task.getUri());
            Task foundTask = (Task) q.uniqueResult();
            assertEquals(i, foundTask.getPercentageCompleted());
            closeSession();
        }

        task.setStatus(Task.Status.COMPLETED);
        task.setPercentageCompleted(100);
        task.setResultUri(new VRI("http://someserver.com/dataset/1432"));

        new RegisterTool().storeTask(task, local);

        table = c.createQueryTable("TASK", String.format(sql, task.getUri()));
        assertEquals(1, table.getRowCount());
        assertEquals(new Float(100), table.getValue(0, "percentageCompleted"));


        Task foundTask = (Task) getCurrentSession().createCriteria(Task.class).
                add(Restrictions.eq("uri", task.getUri())).uniqueResult();
        assertNotNull(foundTask);
        assertEquals(foundTask.getUri(), task.getUri());
        assertEquals(foundTask.getPercentageCompleted(), 100.0f);
        closeSession();
        

        c.close();

    }
}
