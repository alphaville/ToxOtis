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

import org.opentox.toxotis.core.component.User;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;

/**
 * Serves as a pool of tokens you can use in your project to best manage your tokens
 * and not create multiple tokens for the same user.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public final class TokenPool {

    private static TokenPool instanceOfThis;
    private Map<String, AuthenticationToken> pool = new HashMap<String, AuthenticationToken>();
    private Map<String, User> pool2 = new HashMap<String, User>();

    public static TokenPool getInstance() {
        if (instanceOfThis == null) {
            instanceOfThis = new TokenPool();
        }
        return instanceOfThis;
    }
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TokenPool.class);

    private TokenPool() {
        SSLConfiguration.initializeSSLConnection();
    }

    public int size() {
        return pool.size();
    }

    public static void setMasterPasswordFile(File masterPasswordFile) {
        PasswordFileManager.CRYPTO.setMasterPasswordFile(masterPasswordFile.getAbsolutePath());
    }

    public static void setMasterPasswordFile(String masterPasswordFile) {
        PasswordFileManager.CRYPTO.setMasterPasswordFile(masterPasswordFile);
    }

    /**
     * Returns the token for a certain user by searching in the pool. If no such
     * token is found, <code>null</code> is returned. Note that the token you get,
     * if not null, might have expired. Always check the status of the token you get
     * from this method using {@link AuthenticationToken#getStatus() }.
     * @param username
     *      Username for which the token is to be lookep up for.
     * @return
     *      Authentication token for the specified user and <code>null</code> if
     *      no such user is found in the pool.
     */
    public AuthenticationToken getToken(String username) {
        if (username != null) {
            String hashName = PasswordFileManager.CRYPTO.encrypt(username);
            AuthenticationToken token = this.pool.get(hashName);            
            return token;
        }
        return null;
    }

    public AuthenticationToken login(String username, String password) throws ToxOtisException, ServiceInvocationException {
        String hashName = PasswordFileManager.CRYPTO.encrypt(username);
        if (pool.containsKey(hashName)) {
            AuthenticationToken tokenInPool = pool.get(hashName);
            if (tokenInPool.getStatus() != null && AuthenticationToken.TokenStatus.ACTIVE.equals(tokenInPool.getStatus())) {
                return tokenInPool;
            } else {
                if (tokenInPool.validate()) {
                    tokenInPool.invalidate();
                }
            }
        }
        AuthenticationToken token = new AuthenticationToken(username, password);
        pool.put(PasswordFileManager.CRYPTO.encrypt(username), token);
        pool2.put(hashName, token.getUser());
        username = null;
        password = null;
        return token;
    }

    /**
     * Login and acquire an authentication token that you can use to access OpenTox
     * services
     * @param credentialsFile
     *      Encrypted file with your credentials (username and password) that have
     * @return
     *      The authentication token that results from the login/authentication operation.
     * @throws IOException
     * @throws ToxOtisException
     */
    public AuthenticationToken login(File credentialsFile) throws IOException, ToxOtisException, ServiceInvocationException {
        FileReader fr = new FileReader(credentialsFile);
        BufferedReader br = new BufferedReader(fr);
        String username = null;
        String line;
        line = br.readLine();
        if (line == null || (line != null && !line.equals("--- START PRIVATE KEY ---"))) {
            throw new ToxOtisException("Invalid password file: Header not found!");
        }
        line = br.readLine();
        if (line != null) {
            username = line;
        }
        if (pool.containsKey(username)) {
            AuthenticationToken tokenInPool = pool.get(username);
            if (tokenInPool.getStatus() != null && AuthenticationToken.TokenStatus.ACTIVE.equals(tokenInPool.getStatus())) {
                return tokenInPool;
            }else {
                if (tokenInPool.validate()) {
                    tokenInPool.invalidate();
                }
            }
        }
        AuthenticationToken token = PasswordFileManager.CRYPTO.authFromFile(credentialsFile);
        pool.put(username, token);
        pool2.put(username, token.getUser());
        return token;
    }

    public User getUser(String username) {
        if (username != null) {
            String hashName = PasswordFileManager.CRYPTO.encrypt(username);
            return this.pool2.get(hashName);
        }
        return null;
    }

    public Collection<User> getLoggedIn() {
        return pool2 != null ? pool2.values() : null;
    }

    public AuthenticationToken login(String credentialsFile) throws IOException, ToxOtisException, ServiceInvocationException {
        return login(new File(credentialsFile));
    }

    /**
     * Logs out a user identified by its username. An invalidation request is sent
     * to the remote SSO service and the token of that user becomes invalid.
     * @param username
     *      Name of the user which is to be logged out.
     * @return
     *      Returns an integer flag that identifies whether the token was invalidated.
     *      In particular returns <code>1</code> if the user has a token in the pool and
     *      it is successfully invalidated, <code>-1</code>
     */
    public int logout(String username) throws ServiceInvocationException {
        String encryptedUserName = PasswordFileManager.CRYPTO.encrypt(username);
        AuthenticationToken tokn = pool.get(encryptedUserName);
        if (tokn != null) {
            try {
                if (tokn.validate()) {
                    tokn.invalidate();
                }
                pool.remove(encryptedUserName);
                return 1;
            } catch (Exception ex) {
                logger.warn("Exception caught while invalidating a token", ex);
                return -1;
            }
        } else {
            return 0;
        }
    }

    public void logoutAll() throws ServiceInvocationException {
        for (Map.Entry<String, AuthenticationToken> e : pool.entrySet()) {
            try {
                if (e.getValue().validate()) {
                    e.getValue().invalidate();
                }
            } catch (ServiceInvocationException ex) {
                logger.debug("Exception caught on token invalidation", ex);
            }
        }
        // Empty pool...
        pool = new HashMap<String, AuthenticationToken>();
    }
}
