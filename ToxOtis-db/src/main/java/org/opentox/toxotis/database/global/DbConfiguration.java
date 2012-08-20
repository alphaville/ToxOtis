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
    public static final String DEFAULT_C3O0_FILE = "c3p0.properties", TEST_C3O0_FILE = "c3p0test.properties";
    private static String c3p0PropertiedFile = DEFAULT_C3O0_FILE;

    /**
     * Choose the c3p0 properties file.
     * @param c3p0PropertiedFile 
     *      Name of the C3P0 properties file (extension included, e.g. c3p0.properties).
     */
    public static void setC3p0PropertiedFile(String c3p0PropertiedFile) {
        DbConfiguration.c3p0PropertiedFile = c3p0PropertiedFile;
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
