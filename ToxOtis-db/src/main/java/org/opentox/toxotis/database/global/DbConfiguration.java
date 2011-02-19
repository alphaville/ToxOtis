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

    private static class DbConfigurationHolder {

        private final static DbConfiguration instance = new DbConfiguration();
    }

    public static synchronized DbConfiguration getInstance() {
        return DbConfigurationHolder.instance;
    }

    private Properties getDefaultProperties() {
        Properties c3p0Props = new Properties();
        InputStream is = DbConfiguration.class.getClassLoader().getResourceAsStream("c3p0.properties");
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
