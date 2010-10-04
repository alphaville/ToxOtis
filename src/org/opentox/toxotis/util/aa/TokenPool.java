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

    public static void setMasterPasswordFile(File masterPasswordFile) {
        PasswordFileManager.CRYPTO.setMasterPasswordFile(masterPasswordFile.getAbsolutePath());
    }

    public static void setMasterPasswordFile(String masterPasswordFile) {
        PasswordFileManager.CRYPTO.setMasterPasswordFile(masterPasswordFile);
    }

    public AuthenticationToken login(String username, String password) throws ToxOtisException {
        if (pool.containsKey(username)) {
            return pool.get(username);
        }
        AuthenticationToken token = new AuthenticationToken(username, password);
        pool.put(PasswordFileManager.CRYPTO.encrypt(username), token);
        username = null;
        password = null;
        return token;
    }

    public AuthenticationToken login(File credentialsFile) throws IOException, ToxOtisException {        
        BufferedReader br = new BufferedReader(new FileReader(credentialsFile));
        String username;
        while ((username=br.readLine())!=null){
            username = PasswordFileManager.CRYPTO.encrypt(username);
            break;
        }
        if (pool.containsKey(username)){
            return pool.get(username);
        }
        AuthenticationToken token = PasswordFileManager.CRYPTO.authFromFile(credentialsFile);
        pool.put(username, token);
        return token;
        
    }

    public AuthenticationToken login(String credentialsFile) throws IOException, ToxOtisException {
        return login(new File(credentialsFile));
    }

    public int logout(String username) {
        AuthenticationToken tokn = pool.get(username);
        if (tokn != null) {
            try {
                tokn.invalidate();
                return 1;
            } catch (ToxOtisException ex) {
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
        pool.login("/home/chung/toxotisKeys/my.key");
        pool.login("/home/chung/toxotisKeys/my.key");
        System.out.println(pool.pool.values().size());
        pool.logoutAll();
    }
}
