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
    public static final String SSO_HOST = "opensso.in-silico.ch";
    private static final String _SSO_SERVER = "https://" + SSO_HOST;
    private static final String _SSO_IDENTITY = "https://" + SSO_HOST + "/auth/%s";
    private static final String _SSO_POLICY = "https://" + SSO_HOST + "/pol";

    /*
     * General Services
     */
    public static final VRI NTUA;
    public static final VRI AMBIT_UNI_PLOVDIV;
    public static final VRI IDEACONSULT;
    public static final VRI TUM_DEV;
    public static final VRI SSO;
    /*
     * Authentication Services
     */
    public static final VRI SSO_IDENTITY;
    public static final VRI SSO_AUTHENTICATE;
    public static final VRI SSO_POLICY;
    public static final VRI SSO_TOKEN_VALIDATE;
    public static final VRI SSO_TOKEN_INVALIDATE;
    public static final VRI SSO_ATTRIBUTES;
    /*
     * Image Services
     */
    public static final VRI AMBIT_CDK_IMAGE;
    public static final VRI AMBIT_DAYLIGHT_IMAGE;
    public static final VRI AMBIT_CACTVS_IMAGE;
    public static final VRI IDEACONSULT_CDK_IMAGE;
    public static final VRI IDEACONSULT_DAYLIGHT_IMAGE;
    public static final VRI IDEACONSULT_CACTVS_IMAGE;

    static {
        try {
            NTUA = new VRI(String.format(_NTUA_SERVICES, ""));
            AMBIT_UNI_PLOVDIV = new VRI(String.format(_AMBIT_PLOVDIV, ""));
            IDEACONSULT = new VRI(String.format(_IDEACONSULT, ""));
            TUM_DEV = new VRI("http://opentox.informatik.tu-muenchen.de:8080/OpenTox-dev");
            SSO = new VRI(_SSO_SERVER);
            SSO_IDENTITY = new VRI(String.format(_SSO_IDENTITY, ""));
            SSO_AUTHENTICATE = new VRI(String.format(_SSO_IDENTITY, "authenticate?uri=service=openldap"));
            SSO_TOKEN_VALIDATE = new VRI(String.format(_SSO_IDENTITY, "isTokenValid"));
            SSO_TOKEN_INVALIDATE = new VRI(String.format(_SSO_IDENTITY, "logout"));
            SSO_ATTRIBUTES = new VRI(String.format(_SSO_IDENTITY, "attributes"));
            SSO_POLICY = new VRI(String.format(_SSO_POLICY, ""));

            AMBIT_CDK_IMAGE = new VRI(AMBIT_UNI_PLOVDIV).augment("depict","cdk");
            AMBIT_DAYLIGHT_IMAGE = new VRI(AMBIT_UNI_PLOVDIV).augment("depict","daylight");
            AMBIT_CACTVS_IMAGE = new VRI(AMBIT_UNI_PLOVDIV).augment("depict","cactvs");

            IDEACONSULT_CDK_IMAGE = new VRI(IDEACONSULT).augment("depict","cdk");
            IDEACONSULT_DAYLIGHT_IMAGE = new VRI(IDEACONSULT).augment("depict","daylight");
            IDEACONSULT_CACTVS_IMAGE = new VRI(IDEACONSULT).augment("depict","cactvs");

        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }
}
