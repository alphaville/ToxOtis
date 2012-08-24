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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.util.PrintStateObserver;
import static org.junit.Assert.*;

/**
 *
 * @author chung
 */
public class PasswordFileManagerTest {

    public PasswordFileManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testCreateMasterPWDFile() throws IOException {
        PasswordFileManager.CRYPTO.createMasterPasswordFile("/dev/urandom",
                "/home/chung/toxotisKeys/test-master.key", 500, false);
    }

    @Test
    public void testPasswordManager() throws InterruptedException {
        final int size = 10000;
        final String testMasterKey = "/home/chung/toxotisKeys/someMasterKey.txt";
        File testMasterFile = new File(testMasterKey);
        if (testMasterFile.exists()) {
            testMasterFile.delete();
        }
        assertFalse(testMasterFile.exists());
        Thread createPasswordFile = new Thread() {

            @Override
            public void run() {
                try {
                    PasswordFileManager.CRYPTO.createMasterPasswordFile("/dev/urandom",
                            testMasterKey, size, false);
                } catch (IOException ex) {
                    org.slf4j.LoggerFactory.getLogger(PasswordFileManager.class).warn(null, ex);
                }
            }
        };

        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(createPasswordFile);
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.DAYS);
        assertTrue(testMasterFile.exists());
        testMasterFile.delete();

    }
}
