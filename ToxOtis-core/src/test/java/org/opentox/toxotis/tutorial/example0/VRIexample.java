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
package org.opentox.toxotis.tutorial.example0;

import java.net.URISyntaxException;
import java.util.ArrayList;
import org.junit.Test;
import org.opentox.toxotis.client.Pair;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Services;
import static org.junit.Assert.*;
import static java.lang.System.out;

/**
 *
 * @author Pantelis Sopasakis
 */
public class VRIexample {

    public VRIexample() {
    }

    @Test
    public void test1() throws URISyntaxException {
        out.println("Test 1 - Construct VRI, retrieve ID");
        VRI vri = new VRI("http://me.org:2001/myapp/algorithm/svm?p=a#ff&q=3");
        out.println("VRI is           : " + vri);
        out.println("VRI has ID       : " + vri.getId());
        out.println("VRI has protocol : " + vri.getProtocol());
        out.println("VRI has port     : " + vri.getPort());
        out.println("VRI query        : " + vri.getQueryAsString());
        out.println("VRI Base URI     : " + vri.getServiceBaseUri());

        ArrayList<Pair<String, String>> pairs = vri.getUrlParams();
        Pair second = pairs.get(1);
        out.println("VRI has parameter : " + second.getKey() + " has value " + second.getValue());

        Class<?> correspondingClass = vri.getOpenToxType();
        out.println("Correspondoing class is '" + correspondingClass.getSimpleName() + "'");
        out.println("");
    }

    @Test
    public void test2() {
        out.println("Test 2 - Some standard URIs");
        out.println("Ambit Secure TLS/SSL : " + Services.ambitUniPlovdiv());
        out.println("Anonymous URI        : " + Services.anonymous());
        out.println("IDEAconsult          : " + Services.ideaconsult());
        out.println("NTUA                 : " + Services.ntua());
        out.println("OpenTox              : " + Services.opentox());
        out.println("OpenSSO server of OT : " + Services.sso());
        out.println("TUM                  : " + Services.tumDev());
        out.println("");
    }

    @Test
    public void test3() {
        out.println("Test 3 - Modify URIs");
        out.println("Initial URI        : " + Services.anonymous());
        out.println("Augmented          : " + Services.anonymous().augment("algorithm",143));
        out.println("Parameter appended : " + Services.anonymous().addUrlParameter("a", 100));
    }
}
