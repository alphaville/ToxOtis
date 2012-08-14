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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class TokenPoolTest {

    static final String MASTER_KEY_LOCATION = System.getProperty("user.home") + "/toxotisKeys/master.key",
            GUEST_SECRET_KEY = System.getProperty("user.home") + "/toxotisKeys/.guest.key",
            RNG_SYS = "/dev/random";
    
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TokenPoolTest.class);

    public TokenPoolTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * If there is no master password file, create it.
     */
    public void createMasterPasswordFileIfMissing() throws URISyntaxException {
        File masterPassFile = new File(MASTER_KEY_LOCATION);

        if (!masterPassFile.exists()) {
            System.out.println("No master password file found at " + MASTER_KEY_LOCATION + "- creating...");
            Thread createPasswordFile = new Thread() {

                @Override
                public void run() {
                    try {
                        PasswordFileManager.CRYPTO.createMasterPasswordFile(RNG_SYS, MASTER_KEY_LOCATION, 500, true);
                    } catch (final IOException ex) {
                        logger.error(ex.getMessage());
                    }
                }
            };
            Executors.newFixedThreadPool(1).submit(createPasswordFile);
            while (true) {
                if (PasswordFileManager.CRYPTO.hasChanged()) {
                    System.out.println(PasswordFileManager.CRYPTO.getPasswordGenerationProgress());
                }
                if (PasswordFileManager.CRYPTO.getPasswordGenerationProgress() == 100) {
                    break;
                }
            }
        }
    }

    public void createGuestCredentialsFileIfMissing() throws Exception {
        createMasterPasswordFileIfMissing();
        PasswordFileManager.CRYPTO.setMasterPasswordFile(MASTER_KEY_LOCATION);
        PasswordFileManager.CRYPTO.createPasswordFile("guest", "guest", GUEST_SECRET_KEY);
    }

    @Test
    public void testLogin() throws Exception {
        createGuestCredentialsFileIfMissing();

        TokenPool tokenPool = TokenPool.getInstance();
        for (int i = 0; i < 10; i++) {
            tokenPool.login(System.getProperty("user.home") + "/toxotisKeys/.my.key");
            tokenPool.login("guest", "guest");
        }
        assertEquals(2, tokenPool.size());

    }
}
