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
package org.opentox.toxotis.util.aa;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
    
    private SSLConfiguration(){
        // Hidden Constructor - SSLConfiguration is a utility class.
    }

    private static boolean isSslInitialized = false;
    private static final String PROTOCOL = "SSL";
    private static boolean acceptAllCerts = true;

    public static boolean isAcceptAllCerts() {
        return acceptAllCerts;
    }

    public static void setAcceptAllCerts(boolean acceptAllCerts) {
        SSLConfiguration.acceptAllCerts = acceptAllCerts;
    }

    public static void initializeSSLConnection() {
        if (!isSslInitialized) {
            if (acceptAllCerts) {
                initInsecure();
            } else {
                initSsl();
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="INSECURE INITIALIZATION">
    private static void initInsecure() {
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance(PROTOCOL);
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }
        HttpsURLConnection.setDefaultHostnameVerifier(
                new HostnameVerifier() {

                    @Override
                    public boolean verify(String string, SSLSession ssls) {
                        return true;
                    }
                });
        isSslInitialized = true;
    }
    //</editor-fold>

    private static void initSsl() {
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance(PROTOCOL);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalArgumentException(ex);
        }
        try {
            sc.init(null, null, new SecureRandom());
        } catch (KeyManagementException ex) {
            throw new IllegalArgumentException(ex);
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HostnameVerifier hv = new HostnameVerifier() {

            @Override
            public boolean verify(String urlHostName, SSLSession session) {
                /* This is to avoid spoofing */
                return (urlHostName.equals(session.getPeerHost()));
            }
        };

        HttpsURLConnection.setDefaultHostnameVerifier(hv);
        isSslInitialized = true;
    }
}
