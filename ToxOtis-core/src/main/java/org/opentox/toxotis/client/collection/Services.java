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
import org.opentox.toxotis.client.VRI;

/**
 * A collection of URIs of services that participate in the OpenTox net.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Services {

    private static final String NTUA_SERVICES = "http://opentox.ntua.gr:8080/%s";
    private static final String AMBIT_PLOVDIV = "https://ambit.uni-plovdiv.bg:8443/ambit2/%s";
    private static final String IDEACONSULT = "http://apps.ideaconsult.net:8080/ambit2/%s";
    private static final String TUM_DEV = "http://opentox.informatik.tu-muenchen.de:8080/OpenTox-dev/%s";
    public static final String SSO_HOST = "opensso.in-silico.ch";
    private static final String SSO_SERVER = "https://" + SSO_HOST;
    private static final String SSO_IDENTITY = "https://" + SSO_HOST + "/auth/%s";
    private static final String SSO_POLICY = "https://" + SSO_HOST + "/pol";
    private static final String SSO_POLICY_OLD = "https://" + SSO_HOST + "/Pol/opensso-pol";
    private static final String OPENTOX_ORG = "http://opentox.org/%s";
    private static final String ANONYMOUS_ONG = "http://anonymous.org/%s";

    /**
     * Anonymous URI for general use. Use this base URI when you need
     * just to specify an arbitrary identified.
     * @return 
     *      Anonymous URI.
     */
    public static VRI anonymous() {
        try {
            return new VRI(String.format(ANONYMOUS_ONG, ""));
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * The OpenTox main URI.
     * @return 
     *      OpenTox URI.
     */
    public static VRI opentox() {
        try {
            return new VRI(String.format(OPENTOX_ORG, ""));
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * URI of the NTUA (National Technical University of Athens) resources.
     * @return 
     *      NTUA URI.
     */
    public static VRI ntua() {
        try {
            return new VRI(String.format(NTUA_SERVICES, ""));
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * URI of services by IDEAconsult ltd.
     * @return 
     *      IDEAconsult URI.
     */
    public static VRI ideaconsult() {
        try {
            return new VRI(String.format(IDEACONSULT, ""));
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * Secure URI of resources with AMBIT at the University Plovdiv.
     * @return 
     *      AMBIT-plovdiv URI.
     */
    public static VRI ambitUniPlovdiv() {
        try {
            return new VRI(String.format(AMBIT_PLOVDIV, ""));
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * Development version of TUM resources.
     * @return 
     *      TUM URI.
     */
    public static VRI tumDev() {
        try {
            return new VRI(String.format(TUM_DEV, ""));
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * URI of the central SSO server.
     * @return 
     *      SSO server URI.
     */
    public static VRI sso() {
        try {
            return new VRI(SSO_SERVER);
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * Algorithms implemented by NTUA.
     */
    public static class NtuaAlgorithms {

        private static final VRI NTUA_ALGORITHM = ntua().augment("algorithm");

        /**
         * Multiple Linear Regression.
         * 
         * @return
         *      MLR algorithm URI.
         */
        public static VRI mlr() {
            return NTUA_ALGORITHM.augment("mlr");
        }

        /**
         * Support Vector Machine.
         * @return 
         *      SVN algorithm URI.
         */
        public static VRI svm() {
            return NTUA_ALGORITHM.augment("svm");
        }

        /**
         * Leverage DoA.
         * 
         * @return
         *      Leverage DoA algorithm URI.
         */
        public static VRI leverage() {
            return NTUA_ALGORITHM.augment("leverages");
        }

        /**
         * Generic Dataset Filter.
         * @return
         *      Filter algorithm URI.
         */
        public static VRI filter() {
            return NTUA_ALGORITHM.augment("filter");
        }
    }

    /**
     * Services that create a depiction of a given chemical.
     */
    public static class Depiction {

        /**
         * AMBIT CKD-based depiction.
         */
        public static VRI ambitCdkImage() {
            try {
                return new VRI(AMBIT_PLOVDIV).augment("depict", "cdk");
            } catch (URISyntaxException ex) {
                throw new IllegalArgumentException(ex);
            }
        }

        /**
         * 
         * AMBIT Daylight depiction service.
         */
        public static VRI ambitDaylightImage() {
            try {
                return new VRI(AMBIT_PLOVDIV).augment("depict", "daylight");
            } catch (URISyntaxException ex) {
                throw new IllegalArgumentException(ex);
            }
        }

        /**
         * 
         * AMBIT CACTVS-based depiction service.
         */
        public static VRI ambitCactvsImage() {
            try {
                return new VRI(AMBIT_PLOVDIV).augment("depict", "cactvs");
            } catch (URISyntaxException ex) {
                throw new IllegalArgumentException(ex);
            }
        }

        /**
         * 
         * IDEAconsult CDK-based depiction service.
         */
        public static VRI ideaCdkImage() {
            return ideaconsult().augment("depict", "cdk");
        }

        /**
         * 
         * IDEAconsult DAYLIGHT-based depiction service.
         */
        public static VRI ideaDaylightImage() {
            return ideaconsult().augment("depict", "daylight");
        }

        /**
         * 
         * IDEAconsult CACTVS-based depiction service.
         */
        public static VRI ideaCactvsImage() {
            return ideaconsult().augment("depict", "cactvs");
        }
    }

    /**
     * SSO-related services.
     */
    public static class SingleSignOn {

        /**
         * SSO Identity service.
         */
        public static VRI ssoIdentity() {
            try {
                return new VRI(String.format(SSO_IDENTITY, ""));
            } catch (URISyntaxException ex) {
                throw new IllegalArgumentException(ex);
            }
        }

        /**
         * SSO Authentication service
         */
        public static VRI ssoAuthenticate() {
            try {
                return new VRI(String.format(SSO_IDENTITY, "authenticate?uri=service=openldap"));
            } catch (URISyntaxException ex) {
                throw new IllegalArgumentException(ex);
            }
        }

        /**
         * SSO policy service.
         */
        public static VRI ssoPolicy() {
            try {
                return new VRI(String.format(SSO_POLICY, ""));
            } catch (URISyntaxException ex) {
                throw new IllegalArgumentException(ex);
            }
        }

        /**
         * SSO Old Policy service.
         * @deprecated 
         */
        @Deprecated
        public static VRI ssoPolicyOld() {
            try {
                return new VRI(String.format(SSO_POLICY_OLD, ""));
            } catch (URISyntaxException ex) {
                throw new IllegalArgumentException(ex);
            }
        }

        /**
         * SSO Attributes service.
         */
        public static VRI ssoAttributes() {
            try {
                return new VRI(String.format(SSO_IDENTITY, "attributes"));
            } catch (URISyntaxException ex) {
                throw new IllegalArgumentException(ex);
            }
        }

        /**
         * SSO identity validation service.
         */
        public static VRI ssoValidate() {
            try {
                return new VRI(String.format(SSO_IDENTITY, "isTokenValid"));
            } catch (URISyntaxException ex) {
                throw new IllegalArgumentException(ex);
            }
        }

        /**
         * SSO logout/token-invalidation service.
         */
        public static VRI ssoInvalidate() {
            try {
                return new VRI(String.format(SSO_IDENTITY, "logout"));
            } catch (URISyntaxException ex) {
                throw new IllegalArgumentException(ex);
            }
        }

        /**
         * SSO authorization service.
         */
        public static VRI ssoAuthorize() {
            try {
                return new VRI(String.format(SSO_IDENTITY, "authorize"));
            } catch (URISyntaxException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
    }

    /**
     * Descriptor calculation services.
     */
    public static class DescriptorCalculation {

        /**
         * JOElib service.
         */
        public static VRI joelib() {
            return Services.tumDev().augment("algorithm", "JOELIB2");
        }

        /**
         * CDK physicochemical calculation service.
         */
        public static VRI cdkPhysChem() {
            return Services.tumDev().augment("algorithm", "CDKPhysChem");
        }
    }
}