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
package org.opentox.toxotis.client.collection;

import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import org.opentox.toxotis.client.VRI;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
@Deprecated
public enum OpenToxAlgorithms {

    NTUA_MLR(Services.ntua().augment("algorithm","mlr")),
    NTUA_LEVERAGES(Services.ntua().augment("algorithm","leverages")),
    NTUA_SVM(Services.ntua().augment("algorithm","svm")),
    NTUA_FILTER(Services.ntua().augment("algorithm","filter")),
    AMBIT_LR(Services.ambitUniPlovdiv().augment("algorithm","LR")),
    TUM_KNN_CLASSIFICATION(Services.tumDev().augment("algorithm","kNNclassification"));


    private static final Date LAST_UPDATE;

    static {
        Calendar cal = Calendar.getInstance();
        cal.set(2010, Calendar.SEPTEMBER, 10, 18, 00);
        LAST_UPDATE = cal.getTime();
    }
    private String serviceURI;

    private OpenToxAlgorithms(VRI serviceURI) {
        this.serviceURI = serviceURI.getStringNoQuery();
    }

    private OpenToxAlgorithms(String serviceURI) {
        this.serviceURI = serviceURI;
    }

    public static final Date getLastUpdated() {
        return LAST_UPDATE;
    }

    public String getServiceUri() {
        return serviceURI;
    }

    public VRI getServiceVri() {
        try {
            return new VRI(serviceURI);
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }
}
