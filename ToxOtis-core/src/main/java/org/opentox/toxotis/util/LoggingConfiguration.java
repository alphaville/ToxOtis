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
package org.opentox.toxotis.util;

import java.net.URL;

/**
 * Helper for LOG4J global configuration.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class LoggingConfiguration {

    /**
     * Default configuration for LOG4J.
     */
    public static void configureLog4jDefault() {
        configureLog4j(LoggingConfiguration.class, "log4j.properties");
    }

    /**
     * Configure the apache log4j logger providing the name of the configuration resource.
     * Prior to the configuration the previous configurations are reset applying a
     * <code>org.apache.log4j.LogManager.resetConfiguration();</code> and afterwards the
     * method <code>configure</code> in <code>PropertyConfigurator</code>
     * is called using the provided configuration resource.
     *
     * @param loggingConfiguration
     *      URL of the configuring resource (locan file-like URL or remote location
     *      pointing to a log4j properties file.
     *
     */
    public static void configureLog4j(URL loggingConfiguration) {
        org.apache.log4j.LogManager.resetConfiguration();
        org.apache.log4j.PropertyConfigurator.configure(loggingConfiguration);
    }

    public static void configureLog4j(Class invokingClass, String loggingConfigResource) {
        configureLog4j(invokingClass.getClassLoader().getResource(loggingConfigResource));
    }

    /**
     * Configure the apache log4j logger providing the name of the configuration resource.
     * Edit a configuration file and store it on your local resources folder. This will
     * be retrieved using the classloader of the invoking class. Prior to the configuration
     * the previous configurations are reset applying a <code>org.apache.log4j.LogManager.resetConfiguration();</code>
     * and afterwards the method <code>configure</code> in <code>PropertyConfigurator</code>
     * is called using the provided configuration resource. The local resource is usually assumed
     * to be found under <code>src/main/resources</code> - unless otherwise specified or
     * overriden by the user.
     * 
     * @param loggingConfigResource
     *      Name of the configuring resource.
     *
     * @exception RuntimeException
     *      In case the stack trace elements don't suffice to identify the invoking
     *      class (Has never happened on testing and is considered quite impossible)
     *      or if the class cannot be loaded because is not in the classpath (which
     *      is also unlikely to happen)
     */
    public static void configureLog4j(String loggingConfigResource) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length < 2) {
            String message = "Configuration of SLF4J failed: cannot detect the invoking class of the method "
                    + "LoggingConfiguration#configureSLF4JLogger(String):void - Unexpected error!";
            throw new RuntimeException(message);
        }
        String invokingClassName = stackTrace[2].getClassName();
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        Class invokingClass = null;
        try {
            invokingClass = cl.loadClass(invokingClassName);
        } catch (ClassNotFoundException ex) {
            String message = "Configuration of SLF4J failed: The invoking class (" + invokingClassName + ") cannot be "
                    + "loaded by means of the System class loader.";
            throw new RuntimeException(message, ex);
        }
        LoggingConfiguration.configureLog4j(invokingClass.getClassLoader().getResource(loggingConfigResource));
    }
}
