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

import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.Set;
import javax.swing.ImageIcon;
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
import static org.junit.Assert.*;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.spiders.CompoundSpider;

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
    public void listConformers() throws Exception {
        CompoundSpider spider = new CompoundSpider("ethene", null);
        Compound compound = spider.parse();
        assertTrue(compound.listConformers(null).size()>0);
    }

    @Test
    public void testDepict() throws ToxOtisException, ServiceInvocationException {
        CompoundSpider spider = new CompoundSpider("eugenol", null);
        Compound compound = spider.parse();
        ImageIcon icon = compound.getDepiction(null);
        int height = icon.getIconHeight();
        assertTrue(height>0);
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
    public void testDownloadCompound_noSuchURL() throws ServiceInvocationException, URISyntaxException, ToxOtisException {
        VRI vri = new VRI("http://asdf.wqret.fd:8765").augment("compound", "xyz");
        Compound c = new Compound(vri);
        try {
            c.download(new StringWriter(), Media.CHEMICAL_MDLMOL, (AuthenticationToken) null);
        } catch (ServiceInvocationException ex) {
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

    @Test
    public void testGetFeatValue() throws Exception {
        Compound c = new Compound(new VRI("http://apps.ideaconsult.net:8080/ambit2/compound/287/conformer/5418684"));
        Feature f = new Feature(new VRI("http://apps.ideaconsult.net:8080/ambit2/feature/7885337"));
        LiteralValue lv = c.getProperty(f, null);
        assertNotNull(lv);
        assertNotNull(lv.getHash());
        assertNotNull(lv.getType());
        assertNotNull(lv.getValue());
        assertNotNull(lv.getValueAsString());

    }

    @Test
    public void testSimilar() throws Exception{
        AuthenticationToken at = new AuthenticationToken("guest", "guest");
        Compound c = new Compound(new VRI("http://apps.ideaconsult.net:8080/ambit2/compound/144317/conformer/413851"));
        Set<VRI> similar = c.getSimilar(0.95, at);
        assertNotNull(similar);
        assertFalse(similar.isEmpty());
        at.invalidate();
    }
}
