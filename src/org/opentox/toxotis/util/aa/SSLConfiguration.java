package org.opentox.toxotis.util.aa;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import org.opentox.toxotis.client.collection.Services;

/**
 * 
 * Manages your SSL connections. The method {@link #initializeSSLConnection() } in this
 * class should be invoked prior to any SSL connection in order to identify the
 * remote server's certificate during the handshake.
 * IMPORTANT NOTE: You have to add the certificate from opensso.in-silico.ch
 * to your $JAVA_HOME. To do this, run the main method in InstallCert (this
 * is provided by Sun). This will produce a file (ignore the exception messages).
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class SSLConfiguration {

    private static boolean isSslInitialized = false;
    private static final String PROTOCOL = "SSL";

    public static void initializeSSLConnection() {
        if (!isSslInitialized) {
            initSsl();
        }
    }

    private static void initSsl() {
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance(PROTOCOL);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        try {
            sc.init(null, null, new SecureRandom());
        } catch (KeyManagementException ex) {
            throw new RuntimeException(ex);
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HostnameVerifier hv = new HostnameVerifier() {

            @Override
            public boolean verify(String urlHostName, SSLSession session) {
                /* This is to avoid spoofing */
                return (urlHostName.equals(session.getPeerHost())
                        && urlHostName.equals(Services.SSO_HOST));
            }
        };

        HttpsURLConnection.setDefaultHostnameVerifier(hv);
        isSslInitialized = true;
    }
}
