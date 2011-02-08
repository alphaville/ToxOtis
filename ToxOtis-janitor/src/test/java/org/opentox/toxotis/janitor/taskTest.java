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
package org.opentox.toxotis.janitor;

import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.IGetClient;
import org.opentox.toxotis.client.IPostClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.ErrorReport;
import org.opentox.toxotis.core.component.Feature;
import static org.junit.Assert.*;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.spiders.ErrorReportSpider;

/**
 *
 * @author chung
 */
public class taskTest {

    public taskTest() {
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
    public void testSomeMethod() throws ServiceInvocationException, URISyntaxException, IOException, InterruptedException {

        AuthenticationToken at = new AuthenticationToken("AQIC5wM2LY4Sfcz0tdLTeYJFErGcbnbSFLqqs5SkVEzx13Q=@AAJTSQACMDE=#");

        IPostClient client = ClientFactory.createPostClient(new VRI("http://alphaville:4000/model/af0e1b36-70dd-4e09-8710-41343dbe598e"));
        client.addPostParameter("dataset_uri", "http://nina:8080/ambit2/dataset/R545");
        client.setMediaType(Media.TEXT_URI_LIST);
        client.authorize(at);
        VRI taskUri = new VRI(client.getResponseText());
        client.close();

        Thread.sleep(100);

        System.out.println(taskUri);


        
        IGetClient get = ClientFactory.createGetClient(taskUri);
        int code = get.getResponseCode();
        System.out.println(code);
        while (code != 200) {
            if (code==500){
                System.out.println(get.getResponseText()+"\n\n\n");
            }
            Thread.sleep(200);
            System.out.println(code);
            get = ClientFactory.createGetClient(taskUri);
            code = client.getResponseCode();
        }
    }
}
