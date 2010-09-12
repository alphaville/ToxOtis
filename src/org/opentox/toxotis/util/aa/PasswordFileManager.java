package org.opentox.toxotis.util.aa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.crypto.BadPaddingException;
import org.opentox.toxotis.ToxOtisException;

/**
 * <p align=justify>
 * Create highly secure password files to store your credentials. Your username and
 * password are stored in an encrypted format using a master key that you have to
 * provide. The master key is used when you need to create a new username+password file
 * or when you need to use this password file to authenticate your self. This way,
 * you will never need to provide your credentials within your application. Firstly,
 * you have to create a password file that has the following structure:<br/><br/></p>
 * <pre>--- START MASTER KEY ---
 * m2gWQ1FRUVdSXXxhOVBnazQyKy8vUzRcPWFfJ2tmKpE
 * tZm1rcm5rZ2MNCaA/RPQtLjJfFjt/bHQ0S0Q/MzkxbU
 * qg|lztU51cRDRmCcczbmMjZ3ZiZlFRbGRlYWtmbnIoL
 * jAaYWZlLW5lbWbwT0IERXgHtUtubWQ+ZjJtQEBtZmlh
 * ZG5hamVuZmdyb18xOTQVIWD3A:FJMQYzN5c2hjM1vTk
 * ... [ More random characters go here ] ....
 * eq25nDVg4sfmaFp4g5taFghtSD2g5l09Ufn*adegHlo
 * --- END MASTER KEY ---</pre>
 * <br/>
 * <p align=justify>
 * Store this file somewhere on your machine (We suggest that you made it hidden
 * and specify its permissions properly). Then you can use this file to create an
 * encrypted password file or authenticate yourself against the SSO server providing
 * the path of your password file and not your credentials! Here are two examples:
 * </p><br/>
 * <pre> // Example 1: Create a password file
 * PasswordFileManager.CRYPTO.createPasswordFile("john", "s3cret", "./secret/my.key");
 * </pre>
 * <pre> // Example 2: Authentication using the password file
 * // No credentials are provided
 * AuthenticationToken at = PasswordFileManager.CRYPTO.
 *    authFromFile("./secret/my.key");
 * </pre>
 * <p align=justify>
 * <b>It does not work:</b> The most common reasons for exceptional events are the
 * following:
 * <ol>
 * <li>The master password file is not found: Make sure you have created a master
 * password file and have stored it in a directory where the application has access
 * and read priviledges. You can set the path of the master key file using the method
 * {@link PasswordFileManager#setMasterPasswordFile(java.lang.String)
 * setMasterPasswordFile(String)}. The default destination for this file is
 * './secret/secret.key' on linux and MacOSX machines and '.\secret\secret.key'
 * on Windows.</li>
 * <li>The master key file has bad syntax: Make sure your master key file has the
 * header <code>--- START MASTER KEY ---</code> as the first line and the footer
 * <code>--- END MASTER KEY ---</code> at the end. Leave no empty lines.</li>
 * <li>The master key is found and is valid but you cannot authenticate yourself:
 * This might happen if your password file was created with less or more <code>salt</code>.
 * Check out the salting iterations using the method {@link PasswordFileManager#getCryptoIterations()
 * getCryptoIterations()} (The default value is 23).</li>
 * </ol>
 * </p>
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 *
 * @see PasswordFileManager#authFromFile(java.lang.String)
 * @see PasswordFileManager#createPasswordFile(java.lang.String, java.lang.String, java.lang.String)
 */
public class PasswordFileManager {

    private int cryptoIterations = 23;
    private String masterPasswordFile;
    private javax.crypto.Cipher eCipher;
    private javax.crypto.Cipher dCipher;
    private static char[] masterPassword;
    /** This class is a singleton and this is the access point for it. Being final users
    don't need to worry about synchronization*/
    public static final PasswordFileManager CRYPTO = getInstance();
    /** A dummy variable holding the instance of the singleton. */
    private static PasswordFileManager instanceOfThis = null;

    private static PasswordFileManager getInstance() {
        if (instanceOfThis == null) {
            instanceOfThis = new PasswordFileManager();
        }
        return instanceOfThis;
    }

    private PasswordFileManager() {
        super();
        String PATH_SEP = System.getProperty("file.separator");
        masterPasswordFile = "." + PATH_SEP + "secret" + PATH_SEP + "secret.key";
        FileReader fr = null;
        BufferedReader br = null;
        try {
            File secretFile = new File(masterPasswordFile);
            if (!secretFile.exists()) {
                throw new RuntimeException("File containing the master password was not found!");
            }
            fr = new FileReader(secretFile);
            br = new BufferedReader(fr);
            String line = null;
            StringBuilder buffer = new StringBuilder();
            while ((line = br.readLine()) != null && !line.equals("--- START MASTER KEY ---")) {
                // skip all those lines
            }
            while ((line = br.readLine()) != null && !line.equals("--- END MASTER KEY ---")) {
                line = line.trim();
                buffer.append(line);
            }

            masterPassword = new String(Base64.decode(buffer.toString())).toCharArray();
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException ex) {
                    String message = "Unexpected error while trying to "
                            + "close a File Reader used to read the master password of YAQP.";
                    throw new RuntimeException(message, ex);
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    String message = "Unexpected error while trying to "
                            + "close a Buffered Reader used to read the master password of YAQP.";
                    throw new RuntimeException(message, ex);
                }
            }
        }
        java.security.Security.addProvider(new com.sun.crypto.provider.SunJCE());
        byte[] salt = {
            (byte) 0xaf, (byte) 0x35, (byte) 0x11, (byte) 0x0c,
            (byte) 0xd6, (byte) 0xdd, (byte) 0x02, (byte) 0x1a};
        int iterations = cryptoIterations;
        createChipher(masterPassword, salt, iterations);
    }

    private void createChipher(char[] pass, byte[] salt, int iterations) throws SecurityException {
        try {
            javax.crypto.spec.PBEParameterSpec paramSpec =
                    new javax.crypto.spec.PBEParameterSpec(salt, iterations);
            javax.crypto.SecretKeyFactory secretKeyFactory =
                    javax.crypto.SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            javax.crypto.SecretKey secretKey =
                    secretKeyFactory.generateSecret(new javax.crypto.spec.PBEKeySpec(pass));
            String chipherTransformation = "PBEWithMD5AndDES/CBC/PKCS5Padding";
            eCipher = javax.crypto.Cipher.getInstance(chipherTransformation);
            eCipher.init(javax.crypto.Cipher.ENCRYPT_MODE, secretKey, paramSpec);
            dCipher = javax.crypto.Cipher.getInstance(chipherTransformation);
            dCipher.init(javax.crypto.Cipher.DECRYPT_MODE, secretKey, paramSpec);
        } catch (Exception exc) {
            throw new SecurityException(exc);
        }
    }

    protected synchronized String encrypt(final String message) throws SecurityException {
        try {
            byte[] stringBytes = message.getBytes("UTF-8");
            byte[] enc = eCipher.doFinal(stringBytes);
            return new String(Base64.encode(enc));
        } catch (Exception exc) {
            throw new SecurityException(exc);
        }
    }

    protected synchronized String decrypt(final String encrypted) throws SecurityException {
        try {
            byte[] dec = Base64.decode(encrypted);
            byte[] utf8 = dCipher.doFinal(dec);
            return new String(utf8, "UTF-8");
        } catch (BadPaddingException ex) {
            throw new SecurityException("The master key might has been corrupted!", ex);
        } catch (Exception exc) {
            throw new SecurityException(exc);
        }
    }

    /**
     * Create an encrypted file containing the credentials provided in this method.
     * You need the master password file that was used to encrypt these credentials
     * to decrypt them and use them to acquire an authentication token.
     * @param username
     *      You username
     * @param password
     *      Your password
     * @param filePath
     *      The file where the credentials should be stored.
     * @throws IOException
     *      In case the destination is not reachable or you do not have access and
     *      write priviledges to that file or other I/O event inhibits the data
     *      transaction.
     */
    public synchronized void createPasswordFile(String username, String password, String filePath) throws IOException {
        FileWriter fstream = null;
        BufferedWriter out = null;
        try {
            fstream = new FileWriter(filePath);
            out = new BufferedWriter(fstream);
            out.write("--- START PRIVATE KEY ---\n");
            out.write(encrypt(username));
            out.write("\n");
            out.write(encrypt(password));
            out.write("\n--- END PRIVATE KEY ---");
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
            if (fstream != null) {
                fstream.close();
            }
        }
    }

    /**
     * Acquire an authentication token using the credentials in an encrypted file.
     * These are decrypted using the master key (the destination to the master
     * key file should have been set properly) and POSTed to the SSO server which
     * returns a token for that user or an error response if the credentials are
     * not valid.
     *
     * @param filePath
     *      Path to the file containing the credentials
     * @return
     *      An Authentication Token upon successful authentication against the
     *      SSO server.
     * @throws IOException
     *      In case the file path you provided cannot be found (an instance of
     *      <code>java.io.FileNotFoundException</code> or if you do not have
     *      sufficient priviledges to read from that file.
     * @throws ToxOtisException
     *      In case the remote SSO server responds with an error code (e.g. 401 -
     *      Unauthorized or 403 - Forbidden), other HTTP related events inhibit the
     *      creation of am Authentication Token or if the provided password file
     *      is not valid.
     * @see PasswordFileManager#authFromFile(java.io.File) authFromFile(File)
     */
    public synchronized AuthenticationToken authFromFile(String filePath) throws IOException, ToxOtisException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("No file found at : '" + filePath + "'");
        }
        return authFromFile(file);
    }

    /**
     * Acquire an authentication token using the credentials in an encrypted file.
     * These are decrypted using the master key (the destination to the master
     * key file should have been set properly) and POSTed to the SSO server which
     * returns a token for that user or an error response if the credentials are
     * not valid.
     *
     * @param file
     *      File containing the credentials (an instance of <code>java.io.File</code>)
     * @return
     *      An Authentication Token upon successful authentication against the
     *      SSO server.
     * @throws IOException
     *      In case the file path you provided cannot be found (an instance of
     *      <code>java.io.FileNotFoundException</code> or if you do not have
     *      sufficient priviledges to read from that file.
     * @throws ToxOtisException
     *      In case the remote SSO server responds with an error code (e.g. 401 -
     *      Unauthorized or 403 - Forbidden), other HTTP related events inhibit the
     *      creation of am Authentication Token or if the provided password file
     *      is not valid.
     * @see PasswordFileManager#authFromFile(java.lang.String) authFromFile(String)
     */
    public synchronized AuthenticationToken authFromFile(File file) throws IOException, ToxOtisException {
        FileReader fr = new FileReader(file);
        BufferedReader br = null;
        String username = null;
        String password = null;
        try {
            br = new BufferedReader(fr);
            String line;
            line = br.readLine();
            if (line == null || (line != null && !line.equals("--- START PRIVATE KEY ---"))) {
                throw new ToxOtisException("Invalid password file: Header not found!");
            }
            line = br.readLine();
            if (line != null) {
                username = decrypt(line);
            }
            line = br.readLine();
            if (line != null) {
                password = decrypt(line);
            }
            line = br.readLine();
            if (line == null || (line != null && !line.equals("--- END PRIVATE KEY ---"))) {
                throw new ToxOtisException("Invalid password file: Footer not found!");
            }
            AuthenticationToken at = new AuthenticationToken(username, password);
            return at;
        } catch (ToxOtisException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            username = null;
            password = null;
            if (fr != null) {
                fr.close();
            }
            if (br != null) {
                br.close();
            }
        }
    }

    /**
     * Set the location of the master password file. By default this is set
     * to './secret/secret.key'
     * @param masterPasswordFile
     *      Path to the master password file
     * @see PasswordFileManager Documentation
     */
    public void setMasterPasswordFile(String masterPasswordFile) {
        this.masterPasswordFile = masterPasswordFile;
    }

    public int getCryptoIterations() {
        return cryptoIterations;
    }

    /**
     * Set the salting iterations to be used in all encryption/decryption
     * operations of the Password Manager. A large number of salting iterations
     * offers greater security but might cause slower responses.
     * @param cryptoIterations
     *      Number of salting iteration.
     */
    public void setCryptoIterations(int cryptoIterations) {
        this.cryptoIterations = cryptoIterations;
    }
}
