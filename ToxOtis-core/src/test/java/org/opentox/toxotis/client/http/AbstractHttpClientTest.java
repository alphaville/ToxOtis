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

package org.opentox.toxotis.client.http;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.ErrorReport;
import org.opentox.toxotis.core.component.Feature;
import static org.junit.Assert.*;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.util.spiders.ErrorReportSpider;

/**
 *
 * @author chung
 */
public class AbstractHttpClientTest {

    public AbstractHttpClientTest() {
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

    //@Test
    public void testSomeMethod() throws ServiceInvocationException {
        PostHttpClient poster = new PostHttpClient(Services.ntua().augment("bibtex"));
        poster.setMediaType(Media.APPLICATION_RDF_XML);
        poster.post();        
        ErrorReport er = new ErrorReportSpider(poster.getResponseOntModel()).parse();
        System.out.println(er.getMessage());
    }

    @Test
    public void testPOst() throws Exception {
        if (true) return ;
        PostHttpClient client = new PostHttpClient(new VRI("http://alphaville:4000/algorithm/mlr"));
        client.setContentType(Media.APPLICATION_RDF_XML);
        client.setPostable(new Feature().asOntModel());
        client.setMediaType("text/uri-list");
        client.post();
        ErrorReport er = new ErrorReportSpider(client.getResponseOntModel()).parse();
        System.out.println(er.getMessage());
        System.out.println(er.getHttpStatus());
        assertEquals(client.getResponseCode(), er.getHttpStatus());
    }

}