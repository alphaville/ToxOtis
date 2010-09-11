package org.opentox.toxotis.core;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class AuthenticationToken {

    private long tokenCreationTimestamp = System.currentTimeMillis();
    private String tokenId;
    private static final long tokenLocalLifeTime = 23 * 3600 * 1000L; // 23hrs

    public AuthenticationToken() {
        SSLConfiguration.initializeSSLConnection();
    }


}