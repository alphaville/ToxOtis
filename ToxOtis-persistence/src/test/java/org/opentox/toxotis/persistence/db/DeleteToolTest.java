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

import java.net.URISyntaxException;
import java.util.UUID;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.persistence.util.HibernateUtil;
import static org.junit.Assert.*;

/**
 *
 * @author Pantelis Sopasakis
 */
public class DeleteToolTest {

    public DeleteToolTest() {
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
    public void testCreateDeleteTask() {
        Session session = null;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        DeleteTool deleter = new DeleteTool();
        RegisterTool registerer = new RegisterTool();
        try {

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

            /* The task is now stored in the DB */
            registerer.storeTask(task);

            /* The task is removed from the DB */
            deleter.deleteTask(task.getUri());

        } catch (final Exception ex) {
            fail(ex.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

    }
}
