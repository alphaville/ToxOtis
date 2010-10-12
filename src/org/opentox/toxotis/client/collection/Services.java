package org.opentox.toxotis.client.collection;

import java.net.URISyntaxException;
import org.opentox.toxotis.client.VRI;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Services {

    private static final String _NTUA_SERVICES = "http://opentox.ntua.gr:3000/%s";
    private static final String _AMBIT_PLOVDIV = "http://ambit.uni-plovdiv.bg:8080/ambit2/%s";
    private static final String _IDEACONSULT = "http://apps.ideaconsult.net:8080/ambit2/%s";
    private static final String _TUM_DEV = "http://opentox.informatik.tu-muenchen.de:8080/OpenTox-dev/%s";
    public static final String SSO_HOST = "opensso.in-silico.ch";
    private static final String _SSO_SERVER = "https://" + SSO_HOST;
    private static final String _SSO_IDENTITY = "https://" + SSO_HOST + "/auth/%s";
    private static final String _SSO_POLICY = "https://" + SSO_HOST + "/pol";

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
    }

    public static class DescriptorCalculation {

        public static VRI joelib() {
            return Services.tumDev().augment("algorithm", "JOELIB2");
        }

        public static VRI cdkPhysChem() {
            return Services.tumDev().augment("algorithm", "CDKPhysChem");
        }
    }
}
