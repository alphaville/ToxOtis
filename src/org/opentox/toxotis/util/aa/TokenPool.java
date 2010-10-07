package org.opentox.toxotis.util.aa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ToxOtisException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class TokenPool {

    private static TokenPool instanceOfThis;
    private Map<String, AuthenticationToken> pool = new HashMap<String, AuthenticationToken>();

    public static TokenPool getInstance() {
        if (instanceOfThis == null) {
            instanceOfThis = new TokenPool();
        }
        return instanceOfThis;
    }

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
     * token is found, <code>null</code> is returned.
     * @param username
     *      Username for which the token is to be lookep up for.
     * @return
     *      Authentication token for the specified user and <code>null</code> if
     *      no such user is found in the pool.
     */
    public AuthenticationToken getToken(String username) {
        if (username!=null){
        return this.pool.get(username);
        } return null;
    }

    public AuthenticationToken login(String username, String password) throws ToxOtisException {
        if (pool.containsKey(username)) {
            AuthenticationToken tokenInPool = pool.get(username);
            if (tokenInPool.getStatus() != null && AuthenticationToken.TokenStatus.ACTIVE.equals(tokenInPool.getStatus())) {
                return tokenInPool;
            }
        }
        AuthenticationToken token = new AuthenticationToken(username, password);
        pool.put(PasswordFileManager.CRYPTO.encrypt(username), token);
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
     * @throws IOException
     * @throws ToxOtisException
     */
    public AuthenticationToken login(File credentialsFile) throws IOException, ToxOtisException {
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
            }
        }
        AuthenticationToken token = PasswordFileManager.CRYPTO.authFromFile(credentialsFile);
        pool.put(username, token);
        return token;

    }

    public AuthenticationToken login(String credentialsFile) throws IOException, ToxOtisException {
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
    public int logout(String username) {
        String encryptedUserName = PasswordFileManager.CRYPTO.encrypt(username);
        AuthenticationToken tokn = pool.get(encryptedUserName);
        if (tokn != null) {
            try {
                tokn.invalidate();
                pool.remove(encryptedUserName);
                return 1;
            } catch (ToxOtisException ex) {
                Logger.getLogger(TokenPool.class.getName()).log(Level.SEVERE,
                        "Exception caught while invalidating a token", ex);
                return -1;
            }
        } else {
            return 0;
        }
    }

    public void logoutAll() {
        for (Map.Entry<String, AuthenticationToken> e : pool.entrySet()) {
            try {
                e.getValue().invalidate();
            } catch (ToxOtisException ex) {
                Logger.getLogger(TokenPool.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // Empty pool...
        pool = new HashMap<String, AuthenticationToken>();
    }

    public static void main(String... args) throws ToxOtisException, IOException {
        TokenPool pool = TokenPool.getInstance();
    }
}
