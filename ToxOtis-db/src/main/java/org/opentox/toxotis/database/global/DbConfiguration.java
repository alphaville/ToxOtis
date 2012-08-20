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
package org.opentox.toxotis.database.global;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class DbConfiguration {

    private Properties properpties = getDefaultProperties();
    private static org.slf4j.Logger staticLogger = org.slf4j.LoggerFactory.getLogger(DbConfiguration.class);
    public static final String 
            /**
             * The default C3P0 file. Can be used for production.
             */
            DEFAULT_C3P0_FILE = "c3p0.properties",
            /**
             * The C3P0 file used only for testing purposes.
             */
            TEST_C3P0_FILE = "c3p0test.properties";
    private static String c3p0PropertiedFile = DEFAULT_C3P0_FILE;

    /**
     * Choose the c3p0 properties file.
     * @param c3p0PropertiesFile 
     *      Name of the C3P0 properties file (extension included, e.g. c3p0.properties).
     */
    public static void setC3p0PropertiedFile(String c3p0PropertiesFile) {
        DbConfiguration.c3p0PropertiedFile = c3p0PropertiesFile;
        InputStream is = DbConfiguration.class.getClassLoader().getResourceAsStream(c3p0PropertiesFile);
        try {
            DbConfiguration.getInstance().properpties = new Properties();
            DbConfiguration.getInstance().properpties.load(is);
        } catch (IOException ex) {
            final String msg = "Cannot load default properties for C3P0";
            staticLogger.error(msg, ex);
            throw new RuntimeException(msg, ex);
        }
    }

    private static class DbConfigurationHolder {

        private final static DbConfiguration instance = new DbConfiguration();
    }

    public static synchronized DbConfiguration getInstance() {
        return DbConfigurationHolder.instance;
    }

    private Properties getDefaultProperties() {
        Properties c3p0Props = new Properties();
        InputStream is = DbConfiguration.class.getClassLoader().getResourceAsStream(c3p0PropertiedFile);
        try {
            c3p0Props.load(is);
        } catch (IOException ex) {
            final String msg = "Cannot load default properties for C3P0";
            staticLogger.error(msg, ex);
            throw new RuntimeException(msg, ex);
        }
        return c3p0Props;
    }

    public void setProperties(Properties properties) {
        this.properpties = properties;
    }

    public Properties getProperpties() {
        return properpties;
    }
}
