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

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.MetaInfo;
import org.opentox.toxotis.ontology.impl.MetaInfoImpl;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class ParameterTest {

    public ParameterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of endowUri method, of class Parameter.
     */
    @Test
    public void testEndowUri() {
        Parameter p = new Parameter();
        p.setName("myParam");
        p.setScope(Parameter.ParameterScope.OPTIONAL);
        p.setMeta(new MetaInfoImpl().addDescription("asdf"));
        p.setTypedValue(new LiteralValue((double) 100));
        p.endowUri(Services.ntua());
        assertNotNull(p.getUri());
    }

    /**
     * Test of equals method, of class Parameter.
     */
    @Test
    public void testEquals() {
        Parameter p1 = new Parameter();
        Parameter p2 = new Parameter();
        assertEquals(p1, p2);
    }

    /**
     * Test of hashCode method, of class Parameter.
     */
    @Test
    public void testHashCode1() {
        Parameter p1 = new Parameter();
        Parameter p2 = new Parameter();
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testHashCode2() {
        Parameter p1 = new Parameter();
        p1.setName("a");
        Parameter p2 = new Parameter();
        p2.setName("b");
        assertNotSame(p1.hashCode(), p2.hashCode());
        assertNotSame(p1, p2);
        assertFalse(p1.equals(p2));
        assertFalse(p1.hashCode()==p2.hashCode());
    }
    
    @Test
    public void testHashCode3() {
        Parameter p1 = new Parameter();
        p1.setTypedValue(new LiteralValue("a"));
        Parameter p2 = new Parameter();
        p2.setTypedValue(new LiteralValue("a", XSDDatatype.XSDanyURI));
        assertNotSame(p1.hashCode(), p2.hashCode());
        assertNotSame(p1, p2);
    }
    
    @Test
    public void testHashCode4() {
        Parameter p1 = new Parameter();
        p1.setMeta(new MetaInfoImpl().addDescription("x"));
        Parameter p2 = new Parameter();        
        assertNotSame(p1.hashCode(), p2.hashCode());
        assertNotSame(p1, p2);
    }
    
    
    
}
