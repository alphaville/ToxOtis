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
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Services {

    private static final String _NTUA_SERVICES = "http://opentox.ntua.gr:4000/%s";
    private static final String _AMBIT_PLOVDIV = "https://ambit.uni-plovdiv.bg:8443/ambit2/%s";
    private static final String _IDEACONSULT = "http://apps.ideaconsult.net:8080/ambit2/%s";
    private static final String _TUM_DEV = "http://opentox.informatik.tu-muenchen.de:8080/OpenTox-dev/%s";
    public static final String SSO_HOST = "opensso.in-silico.ch";
    private static final String _SSO_SERVER = "https://" + SSO_HOST;
    private static final String _SSO_IDENTITY = "https://" + SSO_HOST + "/auth/%s";
    private static final String _SSO_POLICY = "https://" + SSO_HOST + "/pol";
    private static final String _SSO_POLICY_OLD = "https://" + SSO_HOST + "/Pol/opensso-pol";
    private static final String _OPENTOX_ORG = "http://opentox.org/%s";
    private static final String _ANONYMOUS_ONG = "http://anonymous.org/%s";

    public static VRI anonymous() {
        try {
            return new VRI(String.format(_ANONYMOUS_ONG, ""));
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static VRI opentox() {
        try {
            return new VRI(String.format(_OPENTOX_ORG, ""));
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static VRI ntua() {
        try {
            return new VRI(String.format(_NTUA_SERVICES, ""));
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static VRI ideaconsult() {
        try {
            return new VRI(String.format(_IDEACONSULT, ""));
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static VRI ambitUniPlovdiv() {
        try {
            return new VRI(String.format(_AMBIT_PLOVDIV, ""));
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static VRI tumDev() {
        try {
            return new VRI(String.format(_TUM_DEV, ""));
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static VRI sso() {
        try {
            return new VRI(_SSO_SERVER);
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Algorithms implemented by NTUA.
     */
    public static class NtuaAlgorithms {

        private static final VRI NTUA_ALGORITHM = ntua().augment("algorithm");

        public static VRI mlr() {
            return NTUA_ALGORITHM.augment("mlr");
        }

        public static VRI svm() {
            return NTUA_ALGORITHM.augment("svm");
        }

        public static VRI leverages() {
            return NTUA_ALGORITHM.augment("leverages");
        }

        public static VRI filter() {
            return NTUA_ALGORITHM.augment("filter");
        }
    }

    /**
     * Services that create a depiction of a given chemical.
     */
    public static class Depiction {

        public static VRI ambitCdkImage() {
            try {
                return new VRI(_AMBIT_PLOVDIV).augment("depict", "cdk");
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        }

        public static VRI ambitDaylightImage() {
            try {
                return new VRI(_AMBIT_PLOVDIV).augment("depict", "daylight");
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        }

        public static VRI ambitCactvsImage() {
            try {
                return new VRI(_AMBIT_PLOVDIV).augment("depict", "cactvs");
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        }

        public static VRI ideaCdkImage() {
            return ideaconsult().augment("depict", "cdk");
        }

        public static VRI ideaDaylightImage() {
            return ideaconsult().augment("depict", "daylight");
        }

        public static VRI ideaCactvsImage() {
            return ideaconsult().augment("depict", "cactvs");
        }
    }

    /**
     * SSO-related services.
     */
    public static class SingleSignOn {

        public static VRI ssoIdentity() {
            try {
                return new VRI(String.format(_SSO_IDENTITY, ""));
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        }

        public static VRI ssoAuthenticate() {
            try {
                return new VRI(String.format(_SSO_IDENTITY, "authenticate?uri=service=openldap"));
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        }

        public static VRI ssoPolicy() {
            try {
                return new VRI(String.format(_SSO_POLICY, ""));
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        }

        @Deprecated
        public static VRI ssoPolicyOld() {
            try {
                return new VRI(String.format(_SSO_POLICY_OLD, ""));
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        }

        public static VRI ssoAttributes() {
            try {
                return new VRI(String.format(_SSO_IDENTITY, "attributes"));
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        }

        public static VRI ssoValidate() {
            try {
                return new VRI(String.format(_SSO_IDENTITY, "isTokenValid"));
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        }

        public static VRI ssoInvalidate() {
            try {
                return new VRI(String.format(_SSO_IDENTITY, "logout"));
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        }

        public static VRI ssoAuthorize() {
            try {
                return new VRI(String.format(_SSO_IDENTITY, "authorize"));
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Descriptor calculation services.
     */
    public static class DescriptorCalculation {

        public static VRI joelib() {
            return Services.tumDev().augment("algorithm", "JOELIB2");
        }

        public static VRI cdkPhysChem() {
            return Services.tumDev().augment("algorithm", "CDKPhysChem");
        }
    }
}
