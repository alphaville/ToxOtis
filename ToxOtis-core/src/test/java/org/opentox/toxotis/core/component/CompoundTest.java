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

package org.opentox.toxotis.core.component;

import java.io.File;
import java.io.StringWriter;
import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.exceptions.IConnectionException;
import org.opentox.toxotis.exceptions.INotFound;
import org.opentox.toxotis.exceptions.IUnauthorized;
import static org.junit.Assert.*;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.factory.CompoundFactory;
import org.opentox.toxotis.util.aa.AuthenticationToken;

/**
 *
 * @author chung
 */
public class CompoundTest {

    public CompoundTest() {
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
    public void testDownloadCompound() throws ToxOtisException, ServiceInvocationException {
        Compound comp = new Compound(new VRI(Services.ideaconsult()).augment("compound", "10"));
        StringWriter sw = new StringWriter();
        comp.download(sw, Media.CHEMICAL_MDLMOL, (AuthenticationToken) null);
        assertTrue(sw.getBuffer().length() > 10);
    }

    @Test
    public void testDownloadCompoundNotFound() throws ToxOtisException, ServiceInvocationException {
        try {
            Compound comp = new Compound(new VRI(Services.ideaconsult()).augment("compound", "xyz"));
            StringWriter sw = new StringWriter();
            comp.download(sw, Media.CHEMICAL_MDLMOL, (AuthenticationToken) null);
        } catch (ServiceInvocationException ex) {
            assertTrue(ex instanceof INotFound);
            return;
        }
        fail("Should have failed");
    }

    @Test
    public void testDownloadCompound_noSuchURL() throws ServiceInvocationException, URISyntaxException, ToxOtisException{
        VRI vri = new VRI("http://asdf.wqret.fd:8765").augment("compound", "xyz");
        Compound c = new Compound(vri);
        try{
        c.download(new StringWriter(), Media.CHEMICAL_MDLMOL, (AuthenticationToken) null);
        } catch (ServiceInvocationException ex){
            assertTrue(ex instanceof IConnectionException);
            return;
        }
        fail("Should have failed");
    }

    //@Test
    public void testDownloadCompound_overSSL()
            throws ToxOtisException, ServiceInvocationException {
        VRI vri = new VRI(Services.ambitUniPlovdiv()).augment("compound", "100");
        AuthenticationToken at = new AuthenticationToken("guest", "guest");
        Compound comp = new Compound(vri);
        StringWriter sw = new StringWriter();
        comp.download(sw, Media.CHEMICAL_MDLMOL, at);
        assertTrue(sw.getBuffer().length() > 10);
    }


}
