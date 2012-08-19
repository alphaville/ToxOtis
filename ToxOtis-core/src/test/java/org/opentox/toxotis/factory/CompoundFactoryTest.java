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

package org.opentox.toxotis.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.aa.PasswordFileManager;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class CompoundFactoryTest {

    public CompoundFactoryTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test(timeout = 10000)
    public void testPublishFromFile() throws ServiceInvocationException, URISyntaxException {
        AuthenticationToken at = new AuthenticationToken("guest", "guest");
        File f = new File(CompoundFactory.class.getClassLoader().getResource("samples/sample.sdf").toURI());
        if (!f.exists()) {
            fail("File not found!");
        }
        Task t = CompoundFactory.getInstance().publishFromFile(f, Media.CHEMICAL_MDLSDF, at);
        t = t.loadFromRemote(at);
        assertNotNull(t);
        assertNotNull(t.getUri());
        assertEquals(OTClasses.task(), t.getUri().getOntologicalClass());
        while (!Task.Status.COMPLETED.equals(t.getStatus())) {
            t = t.loadFromRemote(at);
            System.out.println(t.getUri());
        }
        VRI resultUri = t.getResultUri();
        assertNotNull(resultUri);
        assertEquals(OTClasses.conformer(), resultUri.getOntologicalClass());
        at.invalidate();
    }

    @Test(timeout = 10000)
    public void testPublishFromStream() throws ServiceInvocationException,
            URISyntaxException, FileNotFoundException, IOException {
        AuthenticationToken at = new AuthenticationToken("guest", "guest");
        File f = new File(CompoundFactory.class.getClassLoader().getResource("samples/sample.sdf").toURI());
        if (!f.exists()) {
            fail("File not found!");
        }
        InputStream is = new FileInputStream(f);
        try {
            Task t = CompoundFactory.getInstance().publishFromStream(is, Media.CHEMICAL_MDLSDF, at);
            assertNotNull(t);
            t = t.loadFromRemote(at);
            assertNotNull(t);
            assertNotNull(t.getUri());
            while (!Task.Status.COMPLETED.equals(t.getStatus())) {
                t = t.loadFromRemote(at);
                System.out.println(t.getUri());
            }
            VRI resultUri = t.getResultUri();
            assertNotNull(resultUri);
            assertEquals(OTClasses.conformer(), resultUri.getOntologicalClass());
        } finally {
            at.invalidate();
            is.close();
        }

    }
}
